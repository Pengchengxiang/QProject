package com.qunar.hotel.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qunar.hotel.R;
import com.qunar.hotel.model.LoginModel;
import com.qunar.hotel.model.LoginModelImp;
import com.qunar.hotel.model.reponse.LoginParam;
import com.qunar.hotel.model.reponse.LoginResult;
import com.qunar.hotel.view.LoginInputView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //View层渲染用户登录页面
    private LoginInputView userNameInput;
    private LoginInputView passWordInput;
    private Button loginButton;
    private TextView responseTextView;

    //Modle层提封装了登录请求数据和行为
    private LoginModel loginModel;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //Controller层获取Modle更新变化，选择到合适的视图更新显示
                    Bundle bundle = msg.getData();
                    LoginResult loginResult = (LoginResult) bundle.getSerializable("result");
                    responseTextView.setText(loginResult.getMessage());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameInput = (LoginInputView) findViewById(R.id.login_intput_username);
        passWordInput = (LoginInputView) findViewById(R.id.login_intput_password);
        loginButton = (Button) findViewById(R.id.login_login_button);
        responseTextView = (TextView) findViewById(R.id.login_result_text);
        loginButton.setOnClickListener(this);
        userNameInput.setTitle("UserName:");
        passWordInput.setTitle("PassWord:");

        loginModel = new LoginModelImp();
    }

    @Override
    public void onClick(View v) {
        //接受从View层获取的用户点击，分发到Controller处理
        responseTextView.setText("");

        //Controller层从View层选择视图，获取用户输入
        final String userName = userNameInput.getContent();
        final String passWorld = passWordInput.getContent();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Controller层将用户输入登录信息，发送到Model层执行登录相关逻辑
                LoginParam loginParam = new LoginParam(userName,passWorld);
                LoginResult loginResult = loginModel.loginByUserNameAndPassword(LoginActivity.this,loginParam);

                //Model层获取登录信息后，通知Controller层更新UI
                Message message = handler.obtainMessage();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putSerializable("result", loginResult);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }
}