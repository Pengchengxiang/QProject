package com.qunar.home.render.view;

import android.os.Bundle;
import android.widget.ListView;

import com.qunar.common.QBaseActivity;
import com.qunar.home.R;
import com.qunar.home.render.model.RenderResultQ;
import com.qunar.home.render.presenter.RenderContact;
import com.qunar.home.render.presenter.RenderPresenterQ;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_render)
public class RenderActivity extends QBaseActivity implements RenderContact.ViewQ {
    @ViewInject(R.id.listview_render)
    private ListView renderListView;

    private RenderContact.PresenterQ renderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderPresenter = new RenderPresenterQ(this);
        renderPresenter.showRenderList(this);
    }

    @Override
    public void setPresenter(RenderContact.PresenterQ presenter) {
        renderPresenter = presenter;
    }

    @Override
    public void initRenderListShow(RenderResultQ renderResult) {
        RenderListAdapter renderListAdapter = new RenderListAdapter(this, renderResult.getRenderListItemList());
        renderListView.setAdapter(renderListAdapter);
    }
}
