package com.qunar.hotel.login.model;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.qunar.common.utils.HttpsTools;

/**
 * Created by chengxiang.peng on 2016/11/27.
 */

public class LoginModelImp implements LoginModel {
    @Override
    public LoginResult loginByUserNameAndPassword(Context context, LoginParam loginParam) {
        String result = HttpsTools.doPost(context, loginParam);
        LoginResult loginResult = JSON.parseObject(result, LoginResult.class);
        return loginResult;
    }
}