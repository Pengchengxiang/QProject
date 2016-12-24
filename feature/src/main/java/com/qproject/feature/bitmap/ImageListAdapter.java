package com.qproject.feature.bitmap;

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

import com.qproject.common.cache.DiskLruCache;
import com.qproject.feature.R;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * 图片列表适配器
 * Created by chengxiang.peng on 2016/11/20.
 */
public class ImageListAdapter extends BaseAdapter {
    private static final String TAG = "ImageListAdapter";
    private final Context context;
    //显示图片url集合
    private List<String> imageUrlList;

    //内存缓存
    private LruCache<String, Bitmap> memoryCache;

    //硬盘缓存
    private DiskLruCache diskLruCache;
    //硬盘缓存锁，应为硬盘缓存涉及到文件的操作，用来控制线程安全
    private final Object diskCacheLock = new Object();
    //硬盘缓存是否正在初始化，未初始化完成，其它缓存Task必须等待
    private boolean diskCacheStarting = true;
    //硬盘缓存大小
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    //硬盘缓存目录名称
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    public ImageListAdapter(Context context, ArrayList<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;

        //初始化内存缓存，指定内存缓存大小，并实现sizeof方法计算每个缓存实体的大小
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        //异步任务初始化硬盘缓存
        File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheDir);
    }

    /**
     * 获取硬盘缓存目录，优先使用SD卡或者内置外存
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();
        Log.i(TAG,"cachePath = " + cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 初始化硬盘缓存异步任务
     */
    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (diskCacheLock) {
                try {
                    //调用open()方法，指定缓存目录，初始化硬盘缓存。完毕后释放锁让其它线程执行
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

        //先从内存缓存中获取，没有执行异步任务从硬盘缓存或者网络获取
        final Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap != null) {
            Log.i(TAG,"get bitmap " + url + "from memeory cache.");
            imageView.setImageBitmap(bitmap);
        } else {
            //检查复用的imageView当前是否相关的异步任务正在获取图片，如果获取的不是同一张则取消
            if (cancelPotentialAsyncTask(url, imageView)) {
                //创建并执行异步任务，并将任务关联到默认图的Drawble，设置给imageView
                final DownLoadBitmapTask downLoadBitmapTask = new DownLoadBitmapTask(imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), BitmapFactory.
                        decodeResource(context.getResources(), R.drawable.default_img), downLoadBitmapTask);
                imageView.setImageDrawable(asyncDrawable);
                downLoadBitmapTask.execute(url);
            }
        }
        return viewItem;
    }

    static class ViewHolder {
        ImageView imageView;
    }

    /**
     * 检查复用的imageView当前是否相关的异步任务正在获取图片，如果获取的不是同一张则取消，创建新的task获取你想要的图片；如果是同一张，则继续执行任务获取
     *
     * @param url       当前获取图片url
     * @param imageView 当前使用imageView
     * @return 是否取消了重复任务
     */
    public static boolean cancelPotentialAsyncTask(String url, ImageView imageView) {
        final DownLoadBitmapTask bitmapWorkerTask = getBitmapTaskByImageView(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapUrl = bitmapWorkerTask.url;
            //不是同一张图片，则取消原来的任务，创建新的任务获取
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

    /**
     * 获取imageview当前对应的异步下载任务
     *
     * @param imageView image对象
     * @return 关联的异步任务
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
     * 异步Drawable，关联下载该图片的异步任务
     */
    static class AsyncDrawable extends BitmapDrawable {
        //下载位图对应的异步任务
        private final WeakReference<DownLoadBitmapTask> downLoadBitmapTaskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, DownLoadBitmapTask downLoadBitmapTask) {
            super(res, bitmap);
            downLoadBitmapTaskWeakReference = new WeakReference<>(downLoadBitmapTask);
        }

        public DownLoadBitmapTask getBitmapWorkerTask() {
            return downLoadBitmapTaskWeakReference.get();
        }
    }

    /**
     * 下载位图异步任务
     */
    private class DownLoadBitmapTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private Bitmap bitmap;
        private String url;

        private DownLoadBitmapTask(ImageView imageView) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //先从硬盘缓存获取，否则从服务器获取，成功后添加到内存和硬盘缓存
                url = params[0];
                bitmap = getBitmapFromDiskCache(url);
                if (bitmap == null) {
                    bitmap = downloadBitmapFromUrl(url);
                    Log.i(TAG,"download bitmap " + url + "from server.");
                }else{
                    Log.i(TAG,"get bitmap " + url + "from disk cache.");
                }
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

            //如果activity退出，或者配合发生改变重建时，imageView不一定存在，故需要检测
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
     * 向内存缓存和硬盘缓存缓存位图
     *
     * @param url    url
     * @param bitmap 位图
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        //保存到内存缓存
        if (getBitmapFromMemCache(url) == null) {
            memoryCache.put(url, bitmap);
            Log.i(TAG,"add bitmap " + url + "to memory cache.");
        }

        //保存到硬盘缓存
        synchronized (diskCacheLock) {
            if (diskLruCache != null) {
                final String key = hashKeyForDisk(url);
                OutputStream outputStream = null;
                try {
                    DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                    //不存在则获取Editor获取输出流，写入缓存
                    if (snapshot == null) {
                        DiskLruCache.Editor editor = diskLruCache.edit(key);
                        if (editor != null) {
                            outputStream = editor.newOutputStream(0);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                            editor.commit();
                            Log.i(TAG,"add bitmap " + url + "to disk cache.");
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

    /**
     * 从内存缓存中获取指定url的图片
     *
     * @param url url
     * @return 位图
     */
    public Bitmap getBitmapFromMemCache(String url) {
        return memoryCache.get(url);
    }

    /**
     * 从磁盘缓存中获取url的位图
     *
     * @param url url
     * @return 位图
     */
    public Bitmap getBitmapFromDiskCache(String url) {
        synchronized (diskCacheLock) {
            //如果磁盘缓存正在初始化，则等待初始化完成
            while (diskCacheStarting) {
                try {
                    diskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }

            Bitmap bitmap = null;
            if (diskLruCache != null) {
                InputStream inputStream = null;
                //将图片的url生成md5哈希值作为硬盘缓存的key
                final String key = hashKeyForDisk(url);
                try {
                    //通过Snapshot获取输出流，读取key对应的缓存文件
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
     * 从服务端下载位图
     *
     * @param url url
     * @return 位图
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
     * 生成图片url对应的mds值作为key
     */
    public static String hashKeyForDisk(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
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
