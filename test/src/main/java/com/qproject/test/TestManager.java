package com.qproject.test;

import android.content.Context;

import junit.framework.Test;

/**
 * Created by chengxiang.peng on 2016/12/25.
 */
public class TestManager {
    private static TestManager instance;
    private Context context;

    private TestManager(Context context) {
        this.context = context;
    }

    public static TestManager getInstance(Context context) {
        if (instance == null) {
            instance = new TestManager(context);
        }
        return instance;
    }
}
