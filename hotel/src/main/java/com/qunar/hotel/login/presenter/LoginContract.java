package com.qunar.hotel.login.presenter;

import android.content.Context;
import android.os.Message;

import com.qunar.common.BasePresenter;
import com.qunar.common.BaseView;
import com.qunar.hotel.login.model.LoginParam;
import com.qunar.hotel.login.model.LoginResult;

/**
 * 登录View和Modle沟通接口
 * Created by chengxiang.peng on 2016/12/1.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        /**
         * 初始化登录页面显示
         */
        void initLoginShow();

        /**
         * 获取输入的登录参数
         */
        LoginParam getInputLoginParam();

        /**
         * 发送显示登录结果消息
         */
        void sendShowLoginMessage(LoginResult loginResult);

        /**
         * 通过消息更新登录结果
         */
        void updateLoginResultByMessage(Message message);

        /**
         * 更新登录结果信息
         */
        void updateLoginResultByString(String s);
    }

    interface Presenter extends BasePresenter {
        /**
         * 执行登录请求
         */
        void doLoginRequest(Context context);
    }
}
