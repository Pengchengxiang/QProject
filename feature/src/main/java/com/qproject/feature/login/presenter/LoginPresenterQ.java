package com.qproject.feature.login.presenter;

import android.content.Context;

import com.qproject.feature.login.model.LoginModel;
import com.qproject.feature.login.model.LoginParamQ;
import com.qproject.feature.login.model.LoginResultQ;

/**
 * 登录Preserent类，处理从Model层获取数据，在View层更新
 * Created by chengxiang.peng on 2016/12/1.
 */

public class LoginPresenterQ implements LoginContract.PresenterQ {
    private final LoginModel loginModel;
    private final LoginContract.ViewQ loginView;

    public LoginPresenterQ(LoginModel loginModel, LoginContract.ViewQ loginView) {
        this.loginModel = loginModel;
        this.loginView = loginView;
    }

    @Override
    public void start() {
        loginView.initLoginShow();
    }

    @Override
    public void doLoginRequest(final Context context) {
        loginView.updateLoginResultByString("");

        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginParamQ loginParam = loginView.getInputLoginParam();
                LoginResultQ loginResult = loginModel.loginByUserNameAndPassword(context, loginParam);
                loginView.sendShowLoginMessage(loginResult);
            }
        }).start();

    }
}
