package com.sky.photogallery.http.exception;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * Created by tonycheng on 2017/8/17.
 */

public class ExceptionEngine {

    //Http状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException apiException;
        if (e instanceof HttpException) {//Http错误
            HttpException httpException = (HttpException) e;
            apiException = new ApiException(Error.HTTP_ERROR, e);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    apiException.setErrorMessage("网络错误");//均视为网络错误
                    break;
            }
            return apiException;
        } else if (e instanceof ServerException) {//服务器返回的错误
            ServerException serverException = (ServerException) e;
            apiException = new ApiException(serverException.getCode(), serverException);
            apiException.setErrorMessage(serverException.getErrorMessage());
            return apiException;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            apiException = new ApiException(Error.PARSE_ERROR, e);
            apiException.setErrorMessage("解析错误");//均视为解析错误
            return apiException;
        } else if (e instanceof ConnectException) {
            apiException = new ApiException(Error.NETWORD_ERROR, e);
            apiException.setErrorMessage("连接失败");//均视为网络错误
            return apiException;
        } else {
            apiException = new ApiException(Error.UNKNOWN, e);
            apiException.setErrorMessage("未知错误");//均视为未知错误
            return apiException;
        }
    }
}
