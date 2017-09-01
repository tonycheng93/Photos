package com.sky.photogallery.data;

import android.text.TextUtils;

import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.HttpListResult;
import com.sky.photogallery.http.HttpService;
import com.sky.photogallery.http.exception.Error;
import com.sky.photogallery.http.exception.ServerException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    public DataManager() {

    }

    private static final DataManager INSTANCE = new DataManager();

    public static DataManager getInstance() {
        return INSTANCE;
    }

    private HttpService httpService;

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    public Observable<List<Result>> getGanks(int size, int page) {
//        return HttpMethod.getInstance()
//                .createService()
//                .getGanks(size, page)
//                .map(new Function<HttpListResult<Result>, List<Result>>() {
//                    @Override
//                    public List<Result> apply(@NonNull HttpListResult<Result> resultHttpListResult) throws Exception {
//                        Log.d(TAG, "network response = " + new Gson().toJson(resultHttpListResult));
//                        if (resultHttpListResult != null) {
//                            final List<Result> results = resultHttpListResult.getResults();
//                            if (results != null) {
//                                return results;
//                            }
//                        }
//                        return null;
//                    }
//                })
//                .distinct();
//        return HttpMethod.getInstance()
//                .getGanks(size, page)
//                .distinct();

        return httpService
                .getGanks(size, page)
                .compose(DataManager.<HttpListResult<Result>>switchSchedulers())
                .compose(DataManager.<Result>sTransformer())
                .distinct();
    }

    private static <T> ObservableTransformer<T, T> switchSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static <T> ObservableTransformer<HttpListResult<T>, List<T>> sTransformer() {
        return new ObservableTransformer<HttpListResult<T>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(@NonNull Observable<HttpListResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpListResult<T>, ObservableSource<List<T>>>() {
                    @Override
                    public ObservableSource<List<T>> apply(@NonNull HttpListResult<T> tHttpListResult) throws Exception {
                        if (tHttpListResult == null) {
                            throw new ServerException(Error.UNKNOWN, "");
                        }
//                        if (!TextUtils.isEmpty(tHttpListResult.getError())
//                                && "true".equals(tHttpListResult.getError())) {
//                            throw new ServerException(Error.UNKNOWN, "");
//                        }
                        return Observable.just(tHttpListResult.getResults());
                    }
                });
            }
        };
    }
}
