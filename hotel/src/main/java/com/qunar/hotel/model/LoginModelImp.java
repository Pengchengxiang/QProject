package com.qunar.hotel.model;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.qunar.hotel.model.reponse.LoginResult;
import com.qunar.hotel.tools.HttpsTools;

import java.util.HashMap;

/**
 * Created by chengxiang.peng on 2016/11/27.
 */

public class LoginModelImp implements LoginModel {
    @Override
    public LoginResult loginByUserNameAndPassword(Context context, String userName, String passWord) {
        HashMap<String, String> loginParam = new HashMap<>();
        loginParam.put("userName", userName);
        loginParam.put("passWord", passWord);

        String result = HttpsTools.doPost(context, loginParam);
        LoginResult loginResult = JSON.parseObject(result, LoginResult.class);

        return loginResult;
    }
}
