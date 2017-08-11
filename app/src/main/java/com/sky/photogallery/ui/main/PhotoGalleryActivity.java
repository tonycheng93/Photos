package com.sky.photogallery.ui.main;

import android.support.v4.app.Fragment;

import com.sky.photogallery.ui.base.SingleFragmentActivity;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
