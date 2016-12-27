package com.qproject.common;

import android.app.Activity;
import android.content.Intent;
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

    /**
     * 跳转到指定Activity的class对象的页面
     *
     * @param activityClass
     */
    protected void toActivityByClass(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
