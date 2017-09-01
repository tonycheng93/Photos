package com.sky.photogallery.ui.main;

import android.util.Log;

import com.sky.photogallery.data.DataManager;
import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.CustomObserver;
import com.sky.photogallery.http.exception.ApiException;
import com.sky.photogallery.ui.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class PhotoGalleryPresenter extends BasePresenter<IPhotoGalleryView> {

    private static final String TAG = "PhotoGalleryPresenter";

    private DataManager mDataManager;
    private Disposable mDisposable = null;

    public PhotoGalleryPresenter() {
        mDataManager = new DataManager();
    }

    public void setDataManager(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(IPhotoGalleryView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public void loadPhotos(final int size, final int page) {
        checkViewAttached();
        if (page == 1)
            getMvpView().showLoading();
        mDataManager.getGanks(size, page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<List<Result>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Result> photos) {
//                        Log.d(TAG, "onNext: " + new Gson().toJson(photos));
                        if (page == 1)
                            getMvpView().hideLoading();
                        if (photos.isEmpty()) {
                            getMvpView().showPhotoEmpty();
                        } else {
                            getMvpView().addPhotos(photos);
                        }
                    }

//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }

                    @Override
                    protected void onError(ApiException e) {
                        Log.d(TAG, "onError: " + e.getCode() + " , " + e.getErrorMessage());
                        if (page == 1)
                            getMvpView().hideLoading();
                        getMvpView().showError();

                    }

                    @Override
                    public void onComplete() {
                        getMvpView().hideLoading();
                    }
                });
    }
}
