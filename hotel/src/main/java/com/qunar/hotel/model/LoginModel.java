package com.qunar.hotel.model;

import android.content.Context;

import com.qunar.hotel.model.reponse.LoginResult;

/**
 * 登录模型接口
 * Created by chengxiang.peng on 2016/11/27.
 */

public interface LoginModel {
    LoginResult loginByUserNameAndPassword(Context context, String userName, String passWord);
}
