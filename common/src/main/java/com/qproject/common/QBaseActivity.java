package com.qproject.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

/**
 * Created by chengxiang.peng on 2016/12/9.
 */
public class QBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注入activity，用户处理Activity以内的所有注解
        x.view().inject(this);
    }
}
