package com.qunar.hotel.login.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qunar.hotel.R;
import com.qunar.hotel.login.presenter.LoginContract;
import com.qunar.hotel.login.model.LoginModelImp;
import com.qunar.hotel.login.model.LoginParamQ;
import com.qunar.hotel.login.presenter.LoginPresenterQ;
import com.qunar.hotel.login.model.LoginResultQ;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.ViewQ {
    private LoginInputView userNameInput;
    private LoginInputView passWordInput;
    private Button loginButton;
    private TextView responseTextView;

    private Handler handler = new LoginHander();
    private LoginContract.PresenterQ loginPesenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameInput = (LoginInputView) findViewById(R.id.login_intput_username);
        passWordInput = (LoginInputView) findViewById(R.id.login_intput_password);
        loginButton = (Button) findViewById(R.id.login_login_button);
        responseTextView = (TextView) findViewById(R.id.login_result_text);

        loginPesenter = new LoginPresenterQ(new LoginModelImp(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPesenter.start();
    }

    @Override
    public void onClick(View v) {
        loginPesenter.doLoginRequest(LoginActivity.this);
    }

    public void setPresenter(LoginContract.PresenterQ presenter) {
        loginPesenter = presenter;
    }

    @Override
    public void initLoginShow() {
        userNameInput.setTitle("UserName:");
        passWordInput.setTitle("PassWord:");
        loginButton.setOnClickListener(this);
    }

    @Override
    public LoginParamQ getInputLoginParam() {
        final String userName = userNameInput.getContent();
        final String passWorld = passWordInput.getContent();
        LoginParamQ loginParam = new LoginParamQ(userName, passWorld);
        return loginParam;
    }

    @Override
    public void sendShowLoginMessage(LoginResultQ loginResult) {
        Message message = handler.obtainMessage();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", loginResult);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void updateLoginResultByMessage(Message message) {
        Bundle bundle = message.getData();
        LoginResultQ loginResult = (LoginResultQ) bundle.getSerializable("result");
        updateLoginResultByString(loginResult.getMessage());
    }

    @Override
    public void updateLoginResultByString(String result) {
        responseTextView.setText(result);
    }

    /**
     * 登录Handler，处理来自子线程更新登录页面的消息
     */
    private class LoginHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updateLoginResultByMessage(msg);
                    break;
            }
        }
    }
}