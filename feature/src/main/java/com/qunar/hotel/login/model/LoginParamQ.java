package com.qunar.hotel.login.model;

import com.qunar.common.QBaseParam;

/**
 * 登录参数
 * Created by chengxiang.peng on 2016/12/1.
 */

public class LoginParamQ extends QBaseParam {
    private String userName;
    private String passWord;

    public LoginParamQ(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
