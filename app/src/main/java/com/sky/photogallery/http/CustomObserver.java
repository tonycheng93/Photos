package com.sky.photogallery.http;

import com.sky.photogallery.http.exception.ApiException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 * Created by tonycheng on 2017/8/17.
 */

public abstract class CustomObserver<T> implements Observer<T> {

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(123, e));
        }
    }

    protected abstract void onError(ApiException e);
}
