package com.qproject.feature.login.model;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.qproject.common.utils.HttpsTools;

/**
 * Created by chengxiang.peng on 2016/11/27.
 */

public class LoginModelImp implements LoginModel {
    @Override
    public LoginResultQ loginByUserNameAndPassword(Context context, LoginParamQ loginParam) {
        String result = HttpsTools.doPost(context, loginParam);
        LoginResultQ loginResult = JSON.parseObject(result, LoginResultQ.class);
        return loginResult;
    }
}