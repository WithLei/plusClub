package com.android.renly.plusclub.Listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class LoadMoreListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager linearLayoutManager;
    private OnLoadMoreListener onLoadMoreListener;
    private int limit;

    public LoadMoreListener(LinearLayoutManager linearLayoutManager, OnLoadMoreListener onLoadMoreListener, int limit) {
        super();
        this.linearLayoutManager = linearLayoutManager;
        this.onLoadMoreListener = onLoadMoreListener;
        this.limit = limit;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (linearLayoutManager.findLastVisibleItemPosition() > limit &&
                linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1){
            onLoadMoreListener.onLoadMore();
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
