package com.qunar.hotel.model.reponse;

/**
 * 登录参数
 * Created by chengxiang.peng on 2016/12/1.
 */

public class LoginParam extends BaseParam{
    private String userName;
    private String passWord;

    public LoginParam(String userName, String passWord) {
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
