package com.sky.photogallery.data;

import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.http.HttpListResult;
import com.sky.photogallery.http.HttpService;
import com.sky.photogallery.utils.RxSchedulersOverrideRule;
import com.sky.photogallery.utils.TestDataFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by tonycheng on 2017/8/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {
    @Mock
    HttpService mHttpService;

    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager();
        mDataManager.setHttpService(mHttpService);
    }

    @BeforeClass
    public static void setUpRxSchedulers(){
        final Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@android.support.annotation.NonNull Runnable command) {
                        command.run();
                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
    }

    @ClassRule
    public static RxSchedulersOverrideRule sSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Test
    public void getGanks() throws Exception {
        final HttpListResult<Result> httpListResult = TestDataFactory.makeHttpListResult("false", 10);
        Mockito.when(mHttpService.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Observable.just(httpListResult));

        mDataManager.getGanks(10, 1);

        Mockito.verify(mHttpService).getGanks(10, 1);

        TestObserver<List<Result>> testObserver = new TestObserver<>();
        mDataManager.getGanks(10, 1).subscribe(testObserver);
        testObserver.assertNoErrors();
        testObserver.assertValue(httpListResult.getResults());
    }

    @Test
    public void getGanksApiFails() {
        RuntimeException runtimeException = new RuntimeException("test");
        Mockito.when(mHttpService.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Observable.<HttpListResult<Result>>error(runtimeException));

        TestObserver<List<Result>> testObserver = new TestObserver<>();
        mDataManager.getGanks(10, 1).subscribe(testObserver);

        Mockito.verify(mHttpService).getGanks(10, 1);

        testObserver.assertError(runtimeException);
        testObserver.assertNotComplete();
    }

    @Test
    public void getGanksEmpty() {
        Mockito.when(mHttpService.getGanks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Observable.<HttpListResult<Result>>empty());

        TestObserver<List<Result>> testObserver = new TestObserver<>();
        mDataManager.getGanks(10, 10).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }
}