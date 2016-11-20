package com.qunar.hotel.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qunar.hotel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxiang.peng on 2016/11/20.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private Resources resource;

    private boolean isGridViewIdle;

    private List<String> imageUrlList;

    public ImageAdapter(Context context, ArrayList<String> imageUrlList, boolean isGridViewIdle) {
        this.context = context;
        this.imageUrlList = imageUrlList;
        this.isGridViewIdle = isGridViewIdle;

        inflater = LayoutInflater.from(context);
        resource = context.getResources();

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(displayImageOptions).build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageView imageView = viewHolder.imageView;
        String tag = (String) imageView.getTag();
        String url = imageUrlList.get(position);

        if (!url.equals(tag)) {
            imageView.setImageDrawable(resource.getDrawable(R.drawable.default_img));
        }

        if (isGridViewIdle) {
            imageView.setTag(url);
            ImageLoader.getInstance().displayImage(url, imageView);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
