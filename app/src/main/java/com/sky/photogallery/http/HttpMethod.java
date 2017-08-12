package com.sky.photogallery.http;

import com.sky.photogallery.constant.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

    private static final long TIME_OUT = 60;

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
}
