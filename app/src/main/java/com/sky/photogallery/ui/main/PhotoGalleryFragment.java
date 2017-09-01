package com.sky.photogallery.ui.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sky.photogallery.R;
import com.sky.photogallery.data.model.Result;
import com.sky.photogallery.receiver.CountDownReceiver;
import com.sky.photogallery.service.CountDownService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tonycheng on 2017/8/9.
 */

public class PhotoGalleryFragment extends Fragment implements IPhotoGalleryView {

    private static final String TAG = "PhotoGalleryFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mPhotoRecyclerView;
    private List<Result> mResults = new ArrayList<>();

    private PhotoGalleryPresenter mPhotoGalleryPresenter;
    private PhotoGalleryAdapter mPhotoGalleryAdapter;

    private static final int SIZE = 10;
    private int page = 1;

//    private LoadMoreWrapper mLoadMoreWrapper;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        mPhotoRecyclerView = (RecyclerView) rootView.findViewById(R.id
                .fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        setupAdapter();
//
//        mPhotoGalleryPresenter = new PhotoGalleryPresenter();
//        mPhotoGalleryPresenter.attachView(this);
//        mPhotoGalleryPresenter.loadPhotos(SIZE, page);

        test();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcardPath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(sdcardPath);
            if (file.exists()) {
                final boolean directory = file.isDirectory();
                Log.d(TAG, "file is directory = " + directory);
                final File[] files = file.listFiles();
                Log.d(TAG, "files = " + files);
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        Log.d(TAG, "file name = " + f.getName());
                    }
                } else {
                    Log.d(TAG, "files == null or files.length == 0");
                }
            } else {
                Log.d(TAG, "file does not exists.");
            }
        } else {
            Log.d(TAG, "sdcard is not mounted.");
        }

        Intent intent = CountDownService.newIntent(getActivity());
        intent.putExtra("time",30000L);
        getActivity().startService(intent);

//        CountDownService.newInstance(getActivity());
//        CountDownTimer timer = new CountDownTimer(30000, 30000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Log.d(TAG, "onTick: current thread = " + Thread.currentThread().getName());
//            }
//
//            @Override
//            public void onFinish() {
//                Log.d(TAG, "onFinish: current thread name = " + Thread.currentThread().getName());
//            }
//        }.start();

//        CountDownReceiver receiver = new CountDownReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CountDownReceiver.RECEIVER_ACTION);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
//        getActivity().registerReceiver(receiver,filter);

        return rootView;
    }

    /**
     * 在刷新数据的时候调用一下showLoadMore()，
     * 数据加载出错的时候调用一下showLoadError()，
     * 数据加载完成的时候调用showLoadComplete()。
     */
    private void setupAdapter() {
        if (isAdded()) {//check whether the fragment has attached activity,otherwise getActivity
            // () may return null.
            mPhotoGalleryAdapter = new PhotoGalleryAdapter(getActivity());
//            mLoadMoreWrapper = new LoadMoreWrapper(getActivity(), mPhotoGalleryAdapter);
//            mLoadMoreWrapper.setOnLoadListener(new LoadMoreWrapper.OnLoadListener() {
//                @Override
//                public void onRetry() {
//                    //重试处理
//                }
//
//                @Override
//                public void onLoadMore() {
//                    //加载更多
//                    Log.d(TAG, "onLoadMore: ");
//                    if (mPhotoGalleryPresenter != null) {
//                        PAGE++;
//                        mPhotoGalleryPresenter.loadPhotos(PAGE, PER_PAGE);
//                    }
//                }
//            });
            mPhotoRecyclerView.setAdapter(mPhotoGalleryAdapter);
            mPhotoRecyclerView.addOnScrollListener(mScrollerListener);
        }
    }

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    private List<Result> tempList = new ArrayList<>();

    @Override
    public void addPhotos(List<Result> results) {

        if (results == null) {
            return;
        }

//        Log.d(TAG, "addPhotos: page = " + page);

        if (page == 1) {//下拉刷新
//            Log.d(TAG, "addPhotos: tempList.containsAll(results) = " + tempList.containsAll(results));
            if (!tempList.containsAll(results)) {
                tempList = results;
                mResults.addAll(0, results);
                mPhotoGalleryAdapter.setPhotos(mResults);
                mPhotoGalleryAdapter.notifyItemRangeInserted(0, results.size());
            }
        } else {//上拉加载更多
            mResults.addAll(results);
            mPhotoGalleryAdapter.setPhotos(mResults);
            mPhotoGalleryAdapter.notifyItemRangeInserted(mResults.size(), results.size());
        }
    }

    @Override
    public void showPhotoEmpty() {
        mPhotoGalleryAdapter.setPhotos(Collections.<Result>emptyList());
        mPhotoGalleryAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "photos is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError() {
//        Log.d(TAG, "showError: " + e.toString());
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPhotoGalleryPresenter != null) {
            mPhotoGalleryPresenter.detachView();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout
            .OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mPhotoGalleryPresenter != null) {
                page = 1;
                mPhotoGalleryPresenter.loadPhotos(SIZE, page);
            }
        }
    };

    private RecyclerView.OnScrollListener mScrollerListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int totalItemCount = recyclerView.getAdapter().getItemCount();
            int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
            int visibleItemCount = recyclerView.getChildCount();

            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItemPosition == totalItemCount - 1
                    && visibleItemCount > 0) {
                //加载更多
                page++;
                mPhotoGalleryPresenter.loadPhotos(SIZE, page);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final @NonNull ObservableEmitter<String> e) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.baidu.com")
                        .build();
                final Response response = client.newCall(request).execute();
                e.onNext(response.body().string());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "current thread = " + Thread.currentThread().getName());
                        Log.d(TAG, "accept: " + s);
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
