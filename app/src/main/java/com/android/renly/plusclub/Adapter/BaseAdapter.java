package com.android.renly.plusclub.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.renly.plusclub.Listener.ListItemClickListener;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 简单封装
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder>{
    private ListItemClickListener itemListener;

    private static final int TYPE_LOADMORE = 1001;
    private static final int TYPE_NO_DATA = 1002;
    private static final int TYPE_NORMAL =1003;


    public static final int STATE_LOADING = 1;
    public static final int STATE_LOAD_FAIL = 2;
    public static final int STATE_LOAD_NOTHING = 3;
    public static final int STATE_LOAD_OK = 4;
    public static final int STATE_NEED_LOGIN = 5;

    private int loadState = STATE_LOADING;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADMORE:
            case TYPE_NO_DATA:
                return new LoadMoreViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_load_more, parent, false));
            default:
                return getItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getDataCount() == 0) {
            return TYPE_NO_DATA;
        }
        if (position == getItemCount() - 1) {
            return TYPE_LOADMORE;
        }
        return getItemType(position);
    }

    @Override
    public int getItemCount() {
        int count = getDataCount();
        if (count == 0) {
            return 1;
        }else {
            return count;
        }
    }

    protected abstract int getDataCount();

    protected abstract int getItemType(int pos);

    protected abstract BaseViewHolder getItemViewHolder(ViewGroup parent, int viewType);

    // 改变状态
    public void changeLoadMoreState(int state) {
        this.loadState = state;
        int pos = getItemCount() - 1;
        if (pos >= 0 && getItemViewType(pos) == TYPE_LOADMORE) {
            notifyItemChanged(pos);
        }
    }

    public void setOnItemClickListener(ListItemClickListener listener) {
        this.itemListener = listener;
    }

    // 加载更多ViewHolder
    class LoadMoreViewHolder extends BaseViewHolder {
        @BindView(R.id.main_container)
        LinearLayout container;
        @BindView(R.id.load_more_progress)
        ProgressBar progressBar;
        @BindView(R.id.load_more_text)
        TextView loadMoreText;

        LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        void setData(int pos) {
            // 不是第一次加载
            container.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            switch (loadState) {
                case STATE_LOAD_FAIL:
                    loadMoreText.setText("加载失败");
                    progressBar.setVisibility(View.GONE);
                    break;
                case STATE_NEED_LOGIN:
                    loadMoreText.setText("需要登录");
                    progressBar.setVisibility(View.GONE);
                    // 没有数据填充第一次加载
                    if (getItemCount() == 1){
                        container.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                    break;
                case STATE_LOAD_OK:
                    loadMoreText.setText("");
                    progressBar.setVisibility(View.GONE);
                    break;
                case STATE_LOADING:
                    loadMoreText.setText("加载中");
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case STATE_LOAD_NOTHING:
                    // 没有数据填充无数据
                    progressBar.setVisibility(View.GONE);
                    if (getItemCount() == 1){
                        loadMoreText.setText("暂无数据");
                    }else{
                        loadMoreText.setText("暂无更多");
                    }
                    break;
            }
        }
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        BaseViewHolder(View itemView) {
            super(itemView);
            if (itemListener != null)
                itemView.setOnClickListener(this);
        }

        void setData(int pos) {

        }

        @Override
        public void onClick(View view) {
            if (itemListener != null) {
                itemListener.onListItemClick(view, this.getAdapterPosition());
            }
        }
    }
}
