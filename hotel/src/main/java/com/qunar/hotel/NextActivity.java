package com.qunar.hotel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qunar.hotel.response.HttpsResponse;
import com.qunar.hotel.ssl.HttpsHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.net.ssl.HttpsURLConnection;

public class NextActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText passWorldEditText;
    private Button loginButton;
    private TextView responseTextView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    HttpsResponse httpsResponse = (HttpsResponse) bundle.getSerializable("result");
                    responseTextView.setText(httpsResponse.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        userNameEditText = (EditText) findViewById(R.id.next_username_edittext);
        passWorldEditText = (EditText) findViewById(R.id.next_password_password);
        loginButton = (Button) findViewById(R.id.next_login_button);
        responseTextView = (TextView) findViewById(R.id.next_response_text);

        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseTextView.setText("");
                final String userName = userNameEditText.getText().toString();
                final String passWorld = passWorldEditText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        doLoginGet(userName, passWorld);
                        doLoginPost(userName,passWorld);
                    }
                }).start();
            }
        });
    }

    /**
     * 执行登录Get请求
     * @param userName 用户名
     * @param passWorld 用户密码
     */
    private void doLoginGet(String userName, String passWorld) {
        try {
            String url = "https://192.168.1.103:8443/qserver/HttpsServlet?userName=" + userName + "&passWord=" + passWorld;
            HttpsURLConnection httpsURLConnection = HttpsHelper.getHttpsURLConnection(this,url,"GET");
            if (httpsURLConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String result = new String();
                String readLine;
                if ((readLine = bufferedReader.readLine()) != null) {
                    result += readLine;
                }
                bufferedReader.close();
                httpsURLConnection.disconnect();

                Message message = handler.obtainMessage();
                message.what = 1;
                Bundle bundle = new Bundle();
                HttpsResponse httpsResponse = JSON.parseObject(result, HttpsResponse.class);
                bundle.putSerializable("result", httpsResponse);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行登录Post请求
     * @param userName 用户名
     * @param passWorld 用户密码
     */
    private void doLoginPost(String userName, String passWorld) {
        try {
            String url = "http://192.168.1.103:8443/qserver/HttpsServlet";
            HttpsURLConnection httpsURLConnection = HttpsHelper.getHttpsURLConnection(this,url,"POST");
            String params = "userName=" + userName + "&passWord=" + passWorld;
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpsURLConnection.getOutputStream()));
            bufferedWriter.write(params.toString());
            bufferedWriter.flush();

            if (httpsURLConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String result = new String();
                String readLine;
                if ((readLine = bufferedReader.readLine()) != null) {
                    result += readLine;
                }
                bufferedReader.close();
                httpsURLConnection.disconnect();

                Message message = handler.obtainMessage();
                message.what = 1;
                Bundle bundle = new Bundle();
                HttpsResponse httpsResponse = JSON.parseObject(result, HttpsResponse.class);
                bundle.putSerializable("result", httpsResponse);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}