package com.qproject.main.render.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.qproject.common.QBaseActivity;
import com.qproject.main.R;
import com.qproject.main.render.model.RenderResult;
import com.qproject.main.render.presenter.RenderContact;
import com.qproject.main.render.presenter.RenderPresenter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_render)
public class RenderActivity extends QBaseActivity implements RenderContact.ViewQ {
    @ViewInject(R.id.listview_render)
    private ListView renderListView;

    private RenderContact.PresenterQ renderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderPresenter = new RenderPresenter(this);
        renderPresenter.showRenderList(this);
    }

    @Override
    public void setPresenter(RenderContact.PresenterQ presenter) {
        renderPresenter = presenter;
    }

    @Override
    public void initRenderListShow(RenderResult renderResult) {
        RenderListAdapter renderListAdapter = new RenderListAdapter(this, renderResult.getRenderListItemList());
        renderListView.setAdapter(renderListAdapter);
    }

    @Event(value = R.id.listview_render, type = AdapterView.OnItemClickListener.class)
    private void onImageItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Title " + position + " is click", Toast.LENGTH_SHORT).show();
    }
}
