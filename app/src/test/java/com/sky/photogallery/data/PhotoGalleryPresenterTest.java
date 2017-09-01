package com.sky.photogallery.data;

import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.ui.main.IPhotoGalleryView;
import com.sky.photogallery.ui.main.PhotoGalleryPresenter;
import com.sky.photogallery.utils.RxSchedulersOverrideRule;
import com.sky.photogallery.utils.TestDataFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by tonycheng on 2017/8/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class PhotoGalleryPresenterTest {

    @Mock
    IPhotoGalleryView mIPhotoGalleryView;

    @Mock
    DataManager mDataManager;

    private PhotoGalleryPresenter mPhotoGalleryPresenter;

    @ClassRule
    public static RxSchedulersOverrideRule mOverrideRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mPhotoGalleryPresenter = new PhotoGalleryPresenter();
        mPhotoGalleryPresenter.attachView(mIPhotoGalleryView);
        mPhotoGalleryPresenter.setDataManager(mDataManager);
    }

    @After
    public void tearDown() {
        mPhotoGalleryPresenter.detachView();
    }

    @Test
    public void loadPhotosReturnsPhotos() {
        List<Result> results = TestDataFactory.makeListResults(10);

        Mockito.when(mDataManager.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Observable.just(results));

        mPhotoGalleryPresenter.loadPhotos(10, 1);

        Mockito.verify(mIPhotoGalleryView).showLoading();
        Mockito.verify(mIPhotoGalleryView).addPhotos(results);
        Mockito.verify(mIPhotoGalleryView, Mockito.never()).showPhotoEmpty();
        Mockito.verify(mIPhotoGalleryView, Mockito.never()).showError();
        Mockito.verify(mIPhotoGalleryView, Mockito.times(2)).hideLoading();//onNext() and onComplete() will invoke hideLoading()
    }

    @Test
    public void loadPhotosReturnsEmptyList() {
        List<Result> results = new ArrayList<>();
        Mockito.when(mDataManager.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Observable.just(results));

        mPhotoGalleryPresenter.loadPhotos(10, 1);

        Mockito.verify(mIPhotoGalleryView).showLoading();
        Mockito.verify(mIPhotoGalleryView).showPhotoEmpty();
        Mockito.verify(mIPhotoGalleryView, Mockito.never()).addPhotos(ArgumentMatchers.<Result>anyList());
        Mockito.verify(mIPhotoGalleryView, Mockito.never()).showError();
        Mockito.verify(mIPhotoGalleryView, Mockito.times(2)).hideLoading();
    }

    @Test
    public void loadPhotosFails() {
        Mockito.when(mDataManager.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Observable.<List<Result>>error(new Throwable()));

        mPhotoGalleryPresenter.loadPhotos(10, 1);

        Mockito.verify(mIPhotoGalleryView).showLoading();
        Mockito.verify(mIPhotoGalleryView, Mockito.never()).showPhotoEmpty();
        Mockito.verify(mIPhotoGalleryView, Mockito.never()).addPhotos(ArgumentMatchers.<Result>anyList());
        Mockito.verify(mIPhotoGalleryView).showError();
        Mockito.verify(mIPhotoGalleryView).hideLoading();
    }
}