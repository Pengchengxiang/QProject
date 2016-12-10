package com.qunar.home.render.view;

import android.os.Bundle;
import android.widget.ListView;

import com.qunar.common.BaseActivity;
import com.qunar.home.R;
import com.qunar.home.render.model.RenderResult;
import com.qunar.home.render.presenter.RenderContact;
import com.qunar.home.render.presenter.RenderPresenter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_render)
public class RenderActivity extends BaseActivity implements RenderContact.View {
    @ViewInject(R.id.listview_render)
    private ListView renderListView;

    private RenderContact.Presenter renderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderPresenter = new RenderPresenter(this);
        renderPresenter.showRenderList(this);
    }

    @Override
    public void setPresenter(RenderContact.Presenter presenter) {
        renderPresenter = presenter;
    }

    @Override
    public void initRenderListShow(RenderResult renderResult) {
        RenderListAdapter renderListAdapter = new RenderListAdapter(this, renderResult.getRenderListItemList());
        renderListView.setAdapter(renderListAdapter);
    }
}
