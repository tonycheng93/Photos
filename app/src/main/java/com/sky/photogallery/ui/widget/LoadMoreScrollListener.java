package com.sky.photogallery.ui.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by tonycheng on 2017/8/11.
 */

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {

    private int mPreviousTotal;
    private boolean isLoading = true;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private int[] mLastPositions;
    private int mTotalItemCount;
    private int mLastVisibleItemPosition;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            mLastPositions = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
        }

        int visibleItemCount = recyclerView.getChildCount();
        if (mLinearLayoutManager != null) {
            mTotalItemCount = mLinearLayoutManager.getItemCount();
            mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        } else if (mStaggeredGridLayoutManager != null) {
            mTotalItemCount = mStaggeredGridLayoutManager.getItemCount();
            mLastVisibleItemPosition = mLastPositions[0];
        }

        if (isLoading) {
            if (mTotalItemCount > mPreviousTotal) {//加载更多结束
                isLoading = false;
                mPreviousTotal = mTotalItemCount;
            } else if (mTotalItemCount < mPreviousTotal) {//用户刷新结束
                mPreviousTotal = mTotalItemCount;
                isLoading = false;
            } else {//有可能是在第一页刷新也有可能是加载完毕

            }
        }

        if (!isLoading && visibleItemCount > 0 && mTotalItemCount - 1 == mLastVisibleItemPosition
                && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            loadMore();
        }
    }

    public abstract void loadMore();
}
