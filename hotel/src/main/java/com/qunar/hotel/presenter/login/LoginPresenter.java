package com.qunar.hotel.presenter.login;

import android.content.Context;

import com.qunar.hotel.model.login.LoginModel;
import com.qunar.hotel.model.reponse.login.LoginParam;
import com.qunar.hotel.model.reponse.login.LoginResult;

/**
 * 登录Preserent类，处理从Model层获取数据，在View层更新
 * Created by chengxiang.peng on 2016/12/1.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginModel loginModel;
    private final LoginContract.View loginView;

    public LoginPresenter(LoginModel loginModel, LoginContract.View loginView) {
        this.loginModel = loginModel;
        this.loginView = loginView;

        loginView.setPresenter(this);
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
                LoginParam loginParam = loginView.getInputLoginParam();
                LoginResult loginResult = loginModel.loginByUserNameAndPassword(context, loginParam);
                loginView.sendShowLoginMessage(loginResult);
            }
        }).start();

    }
}
