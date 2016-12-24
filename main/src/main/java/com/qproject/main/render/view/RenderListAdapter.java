package com.qproject.main.render.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qproject.common.utils.HttpsTools;
import com.qproject.main.R;
import com.qproject.main.render.model.RenderResult;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Render列表适配器
 * Created by chengxiang.peng on 2016/12/9.
 */
public class RenderListAdapter extends BaseAdapter {
    private List<RenderResult.RenderListItem> renderListItems;
    private final LayoutInflater layoutInflater;
    private ImageOptions imageOptions;

    public RenderListAdapter(final Context context, List<RenderResult.RenderListItem> renderListItems) {
        this.renderListItems = renderListItems;
        this.layoutInflater = LayoutInflater.from(context);

        this.imageOptions = new ImageOptions.Builder().setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5)).setCrop(true).setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher).setFailureDrawableId(R.mipmap.ic_launcher)
                .setParamsBuilder(new ImageOptions.ParamsBuilder() {
                    @Override
                    public RequestParams buildParams(RequestParams params, ImageOptions options) {
                        params.setSslSocketFactory(HttpsTools.getSSLContext(context).getSocketFactory());
                        return params;
                    }
                }).build();
    }

    @Override
    public int getCount() {
        return renderListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return renderListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_render, parent, false);
            holder = new ItemHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        RenderResult.RenderListItem renderListItem = renderListItems.get(position);
        x.image().bind(holder.imageView, renderListItem.getImageUrl(), imageOptions);
        holder.title.setText(renderListItem.getItemTitle());
        holder.description.setText(renderListItem.getItemDescription());

        return convertView;
    }

    private class ItemHolder {
        @ViewInject(R.id.listitem_render_imageview)
        private ImageView imageView;
        @ViewInject(R.id.listitem_render_title)
        private TextView title;
        @ViewInject(R.id.listitem_render_description)
        private TextView description;
    }
}
