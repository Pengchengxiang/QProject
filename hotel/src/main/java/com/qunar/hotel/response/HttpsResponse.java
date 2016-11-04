package com.qunar.hotel.response;

import java.io.Serializable;

/**
 * Created by chengxiang.peng on 2016/11/4.
 */
public class HttpsResponse implements Serializable {

    private String code;
    private String message;

    public HttpsResponse() {
    }

    public HttpsResponse(String code, String message) {
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
        return "HttpsResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
