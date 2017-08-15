package com.sky.photogallery.ui.main;

import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.ui.base.MvpView;

import java.util.List;

/**
 * Created by tonycheng on 2017/8/9.
 */

public interface IPhotoGalleryView extends MvpView {

    /**
     * 显示 loading 状态
     */
    void showLoading();

    /**
     * 填充 Photo 到试图
     *
     * @param results List<Result>
     */
    void addPhotos(List<Result> results);

    /**
     * 显示空状态
     */
    void showPhotoEmpty();

    /**
     * 加载出错状态
     *
     * @param e {@link Throwable}
     */
    void showError(Throwable e);

    /**
     * 隐藏 loading
     */
    void hideLoading();
}
