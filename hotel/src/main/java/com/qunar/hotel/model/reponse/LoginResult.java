package com.qunar.hotel.model.reponse;

import java.io.Serializable;

/**
 * 登录响应
 * Created by chengxiang.peng on 2016/11/4.
 */
public class LoginResult implements Serializable{
    private String code;
    //登录结果
    private String message;

    public LoginResult() {
    }

    public LoginResult(String code, String message) {
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

    @Override
    public String toString() {
        return "LoginResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
