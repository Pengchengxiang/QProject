package com.qproject.main;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import org.xutils.x;

/**
 * Created by chengxiang.peng on 2016/12/9.
 */
public class QAplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化XUtils框架
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能

        //初始化LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
