package com.sky.photogallery.http;

import android.text.TextUtils;

import com.sky.photogallery.constant.Constants;
import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.exception.Error;
import com.sky.photogallery.http.exception.ExceptionEngine;
import com.sky.photogallery.http.exception.ServerException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tonycheng on 2017/8/10.
 */

public class HttpMethod {

    private static HttpMethod sInstance = null;
    private static final Object LOCK = new Object();

    private static final long TIME_OUT = 10;

    private HttpMethod() {

    }

    public static HttpMethod getInstance() {
        if (null == sInstance) {
            synchronized (LOCK) {
                if (null == sInstance) {
                    sInstance = new HttpMethod();
                }
            }
        }
        return sInstance;
    }

    public HttpService createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(HttpService.class);
    }

    private OkHttpClient createClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor);
        return builder.build();
    }

    public Observable<List<Result>> getGanks(int size, int page) {
        return createService().getGanks(size, page)
                .map(new ServerResultFunc<Result>())
                .onErrorResumeNext(new HttpResultFunc<Result>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private class ServerResultFunc<T> implements Function<HttpListResult<T>, List<T>> {

        @Override
        public List<T> apply(@NonNull HttpListResult<T> tHttpListResult) throws Exception {
            if (tHttpListResult == null) {
                throw new ServerException(Error.UNKNOWN, "未从服务器获取到数据");
            }
            if (!TextUtils.isEmpty(tHttpListResult.getError()) && "true".equals(tHttpListResult.getError())) {
                throw new ServerException(Error.UNKNOWN, tHttpListResult.getError());
            }
            return tHttpListResult.getResults();
        }
    }

    private class HttpResultFunc<T> implements Function<Throwable, Observable<List<T>>> {

        @Override
        public Observable<List<T>> apply(@NonNull Throwable throwable) throws Exception {
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }
}
