package com.sky.photogallery.http;

import com.sky.photogallery.constant.Constants;
import com.sky.photogallery.data.model.Photos;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tonycheng on 2017/8/9.
 */

public interface HttpService {

    @GET("?method=flickr.photos.getRecent" +
            "&api_key=" + Constants.API_KEY +
            "&format=json&nojsoncallback=1&extras=url_s")
    Observable<HttpResult<Photos>> getPhotos(@Query("page") int page, @Query("per_page") int perPage);
}
