package com.sky.photogallery.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.sky.photogallery.ui.base.SingleFragmentActivity;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class PhotoGalleryActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
