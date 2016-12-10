package com.qunar.hotel.login.model;

import android.content.Context;

/**
 * 登录模型接口
 * Created by chengxiang.peng on 2016/11/27.
 */

public interface LoginModel {
    LoginResult loginByUserNameAndPassword(Context context, LoginParam loginParam);
}
