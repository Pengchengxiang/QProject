package com.qunar.hotel.model.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.qunar.hotel.model.reponse.login.LoginParam;
import com.qunar.hotel.model.reponse.login.LoginResult;
import com.qunar.hotel.tools.HttpsTools;

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
