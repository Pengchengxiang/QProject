package com.qunar.hotel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qunar.hotel.R;

/**
 * 登录输入框，用来展示一个输入标题+一个输入框
 * Created by chengxiang.peng on 2016/11/27.
 */

public class LoginInputView extends LinearLayout {
    private TextView title;
    private EditText content;

    public LoginInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.inputview_login, this);

        title = (TextView) findViewById(R.id.input_title);
        content = (EditText) findViewById(R.id.input_content);
    }

    /**
     * 设置输入项目的标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * 获取用户输入的内容
     *
     * @return 用户输入的内容
     */
    public String getContent() {
        return content.getText().toString();
    }
}
