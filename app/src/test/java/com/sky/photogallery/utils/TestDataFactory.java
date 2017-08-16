package com.sky.photogallery.utils;

import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.HttpListResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tonycheng on 2017/8/16.
 */

public class TestDataFactory {

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static String createTime() {
        Date date = new Date();
        return date.toString();
    }

    public static HttpListResult<Result> makeHttpListResult(String error, int number) {
        HttpListResult<Result> httpListResults = new HttpListResult<>();
        httpListResults.setError(error);
        httpListResults.setResults(makeListResults(number));
        return httpListResults;
    }

    public static Result makeResult(String uniqueSuffix) {
        Result result = new Result();
        result.setId(randomUuid());
        result.setCreatedAt(createTime());
        result.setDesc("desc " + uniqueSuffix);
        result.setPublishedAt(createTime());
        result.setSource("web " + uniqueSuffix);
        result.setType("type " + uniqueSuffix);
        result.setUrl("http://gank.io/api/data/" + uniqueSuffix);
        result.setUsed(true);
        result.setWho("author " + uniqueSuffix);
        return result;
    }

    public static List<Result> makeListResults(int number) {
        List<Result> results = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            results.add(makeResult(String.valueOf(i)));
        }
        return results;
    }
}
