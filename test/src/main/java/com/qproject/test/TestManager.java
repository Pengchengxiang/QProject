package com.qproject.test;

import android.content.Context;

/**
 * Created by chengxiang.peng on 2016/12/25.
 */
public class TestManager {
    private static TestManager instance;
    private Context context;

    /**
     * 传入的Context的生命周期很重要：
     *   如果传入的是Application的Context，则生命周期和单例生命周期一样长；
     *   如果传入的是Activity的Context，由于该Context和Activity的生命周期一样长，当Activity退出的时候它的内存不会被回收，因为单例对象持有它的引用；
     * @param context
     */
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
