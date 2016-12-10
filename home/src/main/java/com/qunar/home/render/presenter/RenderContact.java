package com.qunar.home.render.presenter;

import android.content.Context;

import com.qunar.common.BasePresenter;
import com.qunar.common.BaseView;
import com.qunar.home.render.model.RenderResult;

/**
 * Render页面View和Present层沟通接口定义
 * Created by chengxiang.peng on 2016/12/9.
 */

public interface RenderContact {
    interface View extends BaseView<Presenter> {
        /**
         * 初始化Render列表的显示
         * @param renderResult
         */
        void initRenderListShow(RenderResult renderResult);
    }

    interface Presenter extends BasePresenter {
        /**
         * 显示Render列表
         */
        void showRenderList(Context context);
    }
}
