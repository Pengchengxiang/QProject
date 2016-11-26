package com.qunar.hotel.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qunar.hotel.R;
import com.qunar.hotel.cache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by chengxiang.peng on 2016/11/20.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";
    private final Context context;
    private List<String> imageUrlList;

    private LruCache<String, Bitmap> memoryCache;

    private DiskLruCache diskLruCache;
    private final Object diskCacheLock = new Object();
    private boolean diskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    public ImageAdapter(Context context, ArrayList<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheDir);
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItem;
        ViewHolder viewHolder;

        //如果convertView可以复用，则复用避免inflate()解析布局
        if (convertView == null) {
            viewItem = View.inflate(context, R.layout.gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) viewItem.findViewById(R.id.imageview1);
            viewItem.setTag(viewHolder);
        } else {
            viewItem = convertView;
            viewHolder = (ViewHolder) viewItem.getTag();
        }

        String url = imageUrlList.get(position);
        ImageView imageView = viewHolder.imageView;

        //先从内存缓存中获取，没有再从网络请求
        final Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap != null) {
            Log.i(TAG, "get bitmap from memory cache:" + url);
            imageView.setImageBitmap(bitmap);
        } else {
            //检查ImageView是否有异步任务获取图片，如果有且为同一场则继续异步任务请求，如果有不为同一张则取消前面的任务，新建任务获取新的图片
            if (cancelPotentialAsyncTask(url, imageView)) {
                final DownLoadBitmapTask downLoadBitmapTask = new DownLoadBitmapTask(imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), BitmapFactory.
                        decodeResource(context.getResources(), R.drawable.default_img), downLoadBitmapTask);
                imageView.setImageDrawable(asyncDrawable);
                downLoadBitmapTask.execute(url);
            }
        }

        return viewItem;
    }

    /**
     * 取消指定ImageView已存在的异步获取图片任务，如果有且为同一场则继续异步任务请求，如果有不为同一张则取消前面的任务，新建任务获取新的图片
     *
     * @param url       当前获取图片url
     * @param imageView 当前使用imageView
     * @return
     */
    public static boolean cancelPotentialAsyncTask(String url, ImageView imageView) {
        final DownLoadBitmapTask bitmapWorkerTask = getBitmapTaskByImageView(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapUrl = bitmapWorkerTask.url;
            //是相同的任务，则取消原来的任务，创建新的任务获取
            if (bitmapUrl == null || bitmapUrl != url) {
                bitmapWorkerTask.cancel(true);
            }
            //如果加载的是同一个图片，则继续异步获取，不创建新的任务下载
            else {
                return false;
            }
        }
        return true;
    }

    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromMemCache(url) == null) {
            memoryCache.put(url, bitmap);
        }

        synchronized (diskCacheLock) {
            if (diskLruCache != null) {
                final String key = hashKeyForDisk(url);
                OutputStream outputStream = null;
                try {
                    DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                    if (snapshot == null) {
                        DiskLruCache.Editor editor = diskLruCache.edit(key);
                        if (editor != null) {
                            outputStream = editor.newOutputStream(0);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                            editor.commit();
                        }
                    } else {
                        snapshot.getInputStream(0).close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String url) {
        return memoryCache.get(url);
    }

    public Bitmap getBitmapFromDiskCache(String url) {
        synchronized (diskCacheLock) {
            while (diskCacheStarting) {
                try {
                    diskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }

            Bitmap bitmap = null;
            if (diskLruCache != null) {
                InputStream inputStream = null;
                final String key = hashKeyForDisk(url);
                try {
                    DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                    if (snapshot != null) {
                        inputStream = snapshot.getInputStream(0);
                        if (inputStream != null) {
                            FileDescriptor fileDescriptor = ((FileInputStream) inputStream).getFD();
                            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }
    }

    /**
     * 获取指定Imageview当前的异步下载任务
     *
     * @param imageView
     * @return
     */
    private static DownLoadBitmapTask getBitmapTaskByImageView(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }


    /**
     * ViewHolder类，保存视图引用，避免复用视图后重复的findViewById()操作
     */
    static class ViewHolder {
        ImageView imageView;
    }

    /**
     * 下载位图异步任务，处理线程池
     */
    private class DownLoadBitmapTask extends AsyncTask<String, Void, Bitmap> {
        //使用弱引用防止DownLoadBitmapTask阻止垃圾回收器回收imageView
        private final WeakReference<ImageView> imageViewWeakReference;
        private Bitmap bitmap;
        private String url;

        private DownLoadBitmapTask(ImageView imageView) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                url = params[0];
                bitmap = getBitmapFromDiskCache(url);
                if (bitmap == null) {
                    bitmap = downloadBitmapFromUrl(url);
                    Log.i(TAG, "get bitmap from url:" + url);
                }else{
                    Log.i(TAG, "get bitmap from disk cache:" + url);
                }
                //获取位图后，添加内存和硬盘缓存中
                addBitmapToCache(url, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            //如果离开展示activity，或者配合发生改变时，imageView不一定存在，故需要检测
            if (imageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = imageViewWeakReference.get();
                final DownLoadBitmapTask downLoadBitmapTask = getBitmapTaskByImageView(imageView);
                if (this == downLoadBitmapTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * 从指定url下载位图对象
     *
     * @param url 位图的url
     * @return 位图对象
     * @throws IOException
     */
    private Bitmap downloadBitmapFromUrl(String url) throws IOException {
        Bitmap bitmap = null;
        InputStream is = null;

        try {
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setReadTimeout(1000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            if (response == 200) {
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return bitmap;
    }

    /**
     * 异步Drawable，保存下载该图片的异步任务
     */
    static class AsyncDrawable extends BitmapDrawable {
        //下载太位图的异步任务
        private final WeakReference<DownLoadBitmapTask> downLoadBitmapTaskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, DownLoadBitmapTask downLoadBitmapTask) {
            super(res, bitmap);
            downLoadBitmapTaskWeakReference = new WeakReference<>(downLoadBitmapTask);
        }

        public DownLoadBitmapTask getBitmapWorkerTask() {
            return downLoadBitmapTaskWeakReference.get();
        }
    }

    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (diskCacheLock) {
                try {
                    File cacheDir = params[0];
                    diskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
                    diskCacheStarting = false;
                    diskCacheLock.notifyAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
