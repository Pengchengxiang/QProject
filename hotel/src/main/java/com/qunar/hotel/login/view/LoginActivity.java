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
import com.qunar.hotel.login.model.LoginParam;
import com.qunar.hotel.login.presenter.LoginPresenter;
import com.qunar.hotel.login.model.LoginResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.View {
    private LoginInputView userNameInput;
    private LoginInputView passWordInput;
    private Button loginButton;
    private TextView responseTextView;

    private Handler handler = new LoginHander();
    private LoginContract.Presenter loginPesenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameInput = (LoginInputView) findViewById(R.id.login_intput_username);
        passWordInput = (LoginInputView) findViewById(R.id.login_intput_password);
        loginButton = (Button) findViewById(R.id.login_login_button);
        responseTextView = (TextView) findViewById(R.id.login_result_text);

        loginPesenter = new LoginPresenter(new LoginModelImp(), this);
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

    public void setPresenter(LoginContract.Presenter presenter) {
        loginPesenter = presenter;
    }

    @Override
    public void initLoginShow() {
        userNameInput.setTitle("UserName:");
        passWordInput.setTitle("PassWord:");
        loginButton.setOnClickListener(this);
    }

    @Override
    public LoginParam getInputLoginParam() {
        final String userName = userNameInput.getContent();
        final String passWorld = passWordInput.getContent();
        LoginParam loginParam = new LoginParam(userName, passWorld);
        return loginParam;
    }

    @Override
    public void sendShowLoginMessage(LoginResult loginResult) {
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
        LoginResult loginResult = (LoginResult) bundle.getSerializable("result");
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