package com.qunar.hotel.model.login;

import android.content.Context;

import com.qunar.hotel.model.reponse.login.LoginParam;
import com.qunar.hotel.model.reponse.login.LoginResult;

/**
 * 登录模型接口
 * Created by chengxiang.peng on 2016/11/27.
 */

public interface LoginModel {
    LoginResult loginByUserNameAndPassword(Context context, LoginParam loginParam);
}
