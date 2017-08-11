package com.sky.photogallery.data.model;

import com.google.gson.annotations.SerializedName;

import com.sky.photogallery.data.model.Photo;

import java.util.List;

public class Photos {

    @SerializedName("perpage")
    private int perpage;

    @SerializedName("total")
    private int total;

    @SerializedName("pages")
    private int pages;

    @SerializedName("photo")
    private List<Photo> photo;

    @SerializedName("page")
    private int page;

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return
                "Photos{" +
                        "perpage = '" + perpage + '\'' +
                        ",total = '" + total + '\'' +
                        ",pages = '" + pages + '\'' +
                        ",photo = '" + photo + '\'' +
                        ",page = '" + page + '\'' +
                        "}";
    }
}