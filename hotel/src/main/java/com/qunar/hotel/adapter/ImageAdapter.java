package com.qunar.hotel.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qunar.hotel.R;

import java.io.IOException;
import java.io.InputStream;
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

    private LayoutInflater inflater;
    private Resources resource;

    private List<String> imageUrlList;

    public ImageAdapter(Context context, ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
        inflater = LayoutInflater.from(context);
        resource = context.getResources();
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
        View itemView;
        //复用convertView
        if(convertView == null){
            Log.i(TAG,"converview == null,new convertview.");
            itemView = inflater.inflate(R.layout.gridview_item, parent,false);
        }else{
            Log.i(TAG,"converview != null,reuse convertview.");
            itemView = convertView;
        }

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview1);
        String url = imageUrlList.get(position);
        DownLoadBitMapThread thread = new DownLoadBitMapThread(url, imageView);
        thread.start();

        return itemView;
    }


    /**
     * 下载位图子线程
     */
    private class DownLoadBitMapThread extends Thread {
        private String url;
        private ImageView imageView;

        public DownLoadBitMapThread(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            super.run();
            try {
                final Bitmap bitmap = downloadBitmapFromUrl(url);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
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
