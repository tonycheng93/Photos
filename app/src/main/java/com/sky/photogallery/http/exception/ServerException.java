package com.sky.photogallery.http.exception;

/**
 * Created by tonycheng on 2017/8/17.
 */

public class ServerException extends RuntimeException {

    private int code;
    private String errorMessage;

    public ServerException(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
