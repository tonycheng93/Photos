package com.sky.photogallery.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sky.photogallery.R;
import com.sky.photogallery.data.model.Photo;

import java.util.List;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoHolder> {

    private Context mContext;
    private List<Photo> mPhotos;

    public PhotoGalleryAdapter(Context context) {
        mContext = context;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }


    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gallery_item,parent,false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        final Photo photo = mPhotos.get(position);
        holder.bindPhotoItem(photo);
    }

    @Override
    public int getItemCount() {
        return mPhotos == null ? 0 : mPhotos.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindPhotoItem(Photo photo) {
            if (photo != null && !TextUtils.isEmpty(photo.getUrlS())) {
                Glide.with(mContext).load(photo.getUrlS());
            }
        }
    }
}
