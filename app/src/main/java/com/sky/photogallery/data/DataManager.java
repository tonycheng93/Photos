package com.sky.photogallery.data;

import com.google.gson.Gson;

import android.util.Log;

import com.sky.photogallery.data.model.Photo;
import com.sky.photogallery.data.model.Photos;
import com.sky.photogallery.http.HttpMethod;
import com.sky.photogallery.http.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    public DataManager() {

    }

    public Observable<List<Photo>> getPhotos(int page, int perPage) {
        return HttpMethod.getInstance()
                .createService()
                .getPhotos(page, perPage)
                .map(new Function<HttpResult<Photos>, List<Photo>>() {
                    @Override
                    public List<Photo> apply(@NonNull HttpResult<Photos> photosHttpResult) throws Exception {
                        Log.d(TAG, "apply: photosHttpResult = " + new Gson().toJson(photosHttpResult));
                        if (photosHttpResult != null) {
                            final Photos photos = photosHttpResult.getPhotos();
                            if (photos != null) {
                                final List<Photo> photo = photos.getPhoto();
                                if (photo != null) {
                                    return photo;
                                }
                            }
                        }
                        return null;
                    }
                })
                .distinct();
    }
}
