package com.sky.photogallery.ui.main;

import com.google.gson.Gson;

import android.util.Log;

import com.sky.photogallery.data.DataManager;
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

    public void loadPhotos(final int page, int perPage) {
        checkViewAttached();
        if (page == 1)
            getMvpView().showLoading();
        mDataManager.getPhotos(page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Photo> photos) {
                        Log.d(TAG, "onNext: " + new Gson().toJson(photos));
                        if (page == 1)
                            getMvpView().hideLoading();
                        if (photos.isEmpty()) {
                            getMvpView().showPhotoEmpty();
                        } else {
                            getMvpView().addPhotos(photos);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (page == 1)
                            getMvpView().hideLoading();
                        getMvpView().showError(e);
                        if (e instanceof SocketTimeoutException) {
                            Log.d(TAG, "onError: SocketTimeoutException");
                        }
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().hideLoading();
                    }
                });
    }
}
