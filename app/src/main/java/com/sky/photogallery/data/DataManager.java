package com.sky.photogallery.data;

import android.util.Log;

import com.google.gson.Gson;
import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.HttpListResult;
import com.sky.photogallery.http.HttpMethod;

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

    public Observable<List<Result>> getGanks(int size, int page) {
        return HttpMethod.getInstance()
                .createService()
                .getGanks(size, page)
                .map(new Function<HttpListResult<Result>, List<Result>>() {
                    @Override
                    public List<Result> apply(@NonNull HttpListResult<Result> resultHttpListResult) throws Exception {
                        Log.d(TAG, "network response = " + new Gson().toJson(resultHttpListResult));
                        if (resultHttpListResult != null) {
                            final List<Result> results = resultHttpListResult.getResults();
                            if (results != null) {
                                return results;
                            }
                        }
                        return null;
                    }
                })
                .distinct();
    }
}
