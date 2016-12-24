package com.qproject.feature.login.presenter;

import android.content.Context;
import android.os.Message;

import com.qproject.common.QBasePresenter;
import com.qproject.common.QBaseView;
import com.qproject.feature.login.model.LoginParamQ;
import com.qproject.feature.login.model.LoginResultQ;

/**
 * 登录View和Modle沟通接口
 * Created by chengxiang.peng on 2016/12/1.
 */

public interface LoginContract {
    interface ViewQ extends QBaseView<PresenterQ> {
        /**
         * 初始化登录页面显示
         */
        void initLoginShow();

        /**
         * 获取输入的登录参数
         */
        LoginParamQ getInputLoginParam();

        /**
         * 发送显示登录结果消息
         */
        void sendShowLoginMessage(LoginResultQ loginResult);

        /**
         * 通过消息更新登录结果
         */
        void updateLoginResultByMessage(Message message);

        /**
         * 更新登录结果信息
         */
        void updateLoginResultByString(String s);
    }

    interface PresenterQ extends QBasePresenter {
        /**
         * 执行登录请求
         */
        void doLoginRequest(Context context);
    }
}
