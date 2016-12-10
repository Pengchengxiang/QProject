package com.qunar.hotel.login.model;

import com.qunar.common.QBaseResult;

/**
 * 登录响应
 * Created by chengxiang.peng on 2016/11/4.
 */
public class LoginResultQ extends QBaseResult {
    private String code;
    //登录结果
    private String message;

    public LoginResultQ() {
    }

    public LoginResultQ(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
