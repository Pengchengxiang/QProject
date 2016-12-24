package com.qproject.feature.login.model;

import android.content.Context;

import com.qproject.feature.login.model.LoginParamQ;
import com.qproject.feature.login.model.LoginResultQ;

/**
 * 登录模型接口
 * Created by chengxiang.peng on 2016/11/27.
 */

public interface LoginModel {
    LoginResultQ loginByUserNameAndPassword(Context context, LoginParamQ loginParam);
}
