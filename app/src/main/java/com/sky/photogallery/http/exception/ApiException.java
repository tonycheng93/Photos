package com.sky.photogallery.http.exception;

/**
 * Created by tonycheng on 2017/8/17.
 */

public class ApiException extends Exception {

    private int code;
    private String errorMessage;

    public ApiException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getCode() {
        return code;
    }
}
