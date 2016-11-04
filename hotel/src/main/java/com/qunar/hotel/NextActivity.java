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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
                        try {
                            URL url = new URL("http://192.168.111.98:8080/qserver/HttpsServlet?userName=" + userName + "&passWord=" + passWorld);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("GET");
                            if (httpURLConnection.getResponseCode() == 200) {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                String result = new String();
                                String readLine;
                                if ((readLine = bufferedReader.readLine()) != null) {
                                    result += readLine;
                                }
                                bufferedReader.close();
                                httpURLConnection.disconnect();

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
                }).start();
            }
        });
    }
}