package com.sky.photogallery.data;

import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.HttpListResult;
import com.sky.photogallery.http.HttpMethod;
import com.sky.photogallery.http.HttpService;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tonycheng on 2017/8/16.
 */
public class DataManagerTest {

    @Test
    public void getGanks() throws Exception {

        HttpMethod httpMethod = Mockito.mock(HttpMethod.class);
        HttpService httpService = Mockito.mock(HttpService.class);

        Mockito.when(httpMethod.createService())
                .thenReturn(httpService);

        final Observable<HttpListResult<Result>> error = Observable.<HttpListResult<Result>>error(new Throwable());

        Mockito.when(httpService.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(error);

        TestObserver<List<Result>> testObserver = new TestObserver<>();
        DataManager dataManager = new DataManager();
        dataManager.getGanks(10, 1)
                .subscribeOn(Schedulers.io())
                .subscribe(testObserver);
        testObserver.assertNoErrors();
    }
}