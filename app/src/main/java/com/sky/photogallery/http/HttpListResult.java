package com.sky.photogallery.http;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HttpListResult<T> {

    @SerializedName("error")
    private String error;

    @SerializedName("results")
    private List<T> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "HttpListResult{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }
}