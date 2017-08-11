package com.sky.photogallery.http;

import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {

    @SerializedName("stat")
    private String stat;

    @SerializedName("photos")
    private T photos;

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getStat() {
        return stat;
    }

    public void setPhotos(T photos) {
        this.photos = photos;
    }

    public T getPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return
                "HttpResult{" +
                        "stat = '" + stat + '\'' +
                        ",photos = '" + photos + '\'' +
                        "}";
    }
}