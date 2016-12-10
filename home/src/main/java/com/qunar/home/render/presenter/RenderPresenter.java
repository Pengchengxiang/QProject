package com.qunar.home.render.presenter;

import android.content.Context;
import android.widget.Toast;

import com.qunar.common.CommonConfig;
import com.qunar.common.utils.HttpsTools;
import com.qunar.home.render.model.RenderResult;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Render Presenter层类
 * Created by chengxiang.peng on 2016/12/9.
 */

public class RenderPresenter implements RenderContact.Presenter {
    private RenderContact.View renderView;

    /**
     * 构造方法
     *
     * @param renderView render View层对象
     */
    public RenderPresenter(RenderContact.View renderView) {
        this.renderView = renderView;
    }

    @Override
    public void start() {

    }

    @Override
    public void showRenderList(Context context) {
        RequestParams renderParams = new RequestParams(CommonConfig.SERVER_URL + "RenderServlet");
        renderParams.setSslSocketFactory(HttpsTools.getSSLContext(context).getSocketFactory());
        x.http().post(renderParams, new Callback.CommonCallback<RenderResult>() {
            @Override
            public void onSuccess(RenderResult result) {
                renderView.initRenderListShow(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
