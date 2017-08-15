package com.sky.photogallery.http;

import com.sky.photogallery.data.model.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tonycheng on 2017/8/9.
 */

public interface HttpService {

    @GET("data/福利/{size}/{page}")
    Observable<HttpListResult<Result>> getGanks(@Path("size") int size, @Path("page") int page);
}
