package com.sky.photogallery.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        setupAdapter();

        mPhotoGalleryPresenter = new PhotoGalleryPresenter();
        mPhotoGalleryPresenter.attachView(this);
        mPhotoGalleryPresenter.loadPhotos(SIZE, page);

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

    @Override
    public void addPhotos(List<Result> results) {
        String str1 = mResults.toString();
        String str2 = results.toString();
        Log.d(TAG, "addPhotos: " + str1.contains(str2));
        if (page == 1) {
            if (results != null && !str1.contains(str2)) {
                mResults.addAll(0, results);
                mPhotoGalleryAdapter.setPhotos(mResults);
                mPhotoGalleryAdapter.notifyDataSetChanged();
            }
        } else {
            if (results != null && !str1.contains(str2)) {
                mResults.addAll(results);
                mPhotoGalleryAdapter.setPhotos(mResults);
                mPhotoGalleryAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showPhotoEmpty() {
        mPhotoGalleryAdapter.setPhotos(Collections.<Result>emptyList());
        mPhotoGalleryAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "photos is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(Throwable e) {
        Log.d(TAG, "showError: " + e.toString());
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
                page ++;
                mPhotoGalleryPresenter.loadPhotos(SIZE,page);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
