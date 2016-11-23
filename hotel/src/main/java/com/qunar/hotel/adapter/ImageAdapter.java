package com.qunar.hotel.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qunar.hotel.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxiang.peng on 2016/11/20.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";
    private final Context context;
    private List<String> imageUrlList;

    public ImageAdapter(Context context, ArrayList<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
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
        View item = View.inflate(context, R.layout.gridview_item, null);
        ImageView imageView = (ImageView) item.findViewById(R.id.imageview1);

        String url = imageUrlList.get(position);
        DownLoadBitmapTask downLoadBitmapTask = new DownLoadBitmapTask(imageView);
        downLoadBitmapTask.execute(url);

        return item;
    }

    /**
     * 下载位图异步任务，处理线程池
     */
    private class DownLoadBitmapTask extends AsyncTask<String, Void, Bitmap> {
        //使用弱引用防止DownLoadBitmapTask阻止垃圾回收器回收imageView
        private final WeakReference<ImageView> imageViewWeakReference;

        private DownLoadBitmapTask(ImageView imageView) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = downloadBitmapFromUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //如果离开展示activity，或者配合发生改变时，imageView不一定存在，故需要检测
            if (imageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
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
}
