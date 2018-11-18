package com.android.renly.plusclub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Api.Bean.Post;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子列表adapter
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.BaseViewHolder> {
    private List<Post> postList;
    private Context context;
    private OnItemClickListener mItemClickListener = null;

    private static final int TYPE_LOADMORE = 101;
    private static final int TYPE_NO_DATA = 102;
    private static final int TYPE_NORMAL =103;

    public static final int STATE_LOADING = 1;
    public static final int STATE_LOAD_FAIL = 2;
    public static final int STATE_LOAD_NOTHING = 3;
    public static final int STATE_LOAD_OK = 4;
    public static final int STATE_NEED_LOGIN = 5;

    private int loadState = STATE_LOADING;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_LOADMORE:
                View loadView = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
                LoadMoreViewHolder loadMoreViewHolder = new LoadMoreViewHolder(loadView, mItemClickListener);
                return loadMoreViewHolder;
            default:
                View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
                NormalViewHolder viewHolder = new NormalViewHolder(view, mItemClickListener);
                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == postList.size()) {
            return TYPE_LOADMORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return postList.size() + 1;
    }

    //改变状态
    public void changeLoadMoreState(int i) {
        this.loadState = i;
        notifyItemChanged(0);
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class LoadMoreViewHolder extends BaseViewHolder {
        @BindView(R.id.main_container)
        LinearLayout container;
        @BindView(R.id.load_more_progress)
        ProgressBar progressBar;
        @BindView(R.id.load_more_text)
        TextView loadMoreText;

        LoadMoreViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView, listener);
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

    class NormalViewHolder extends BaseViewHolder {
        @BindView(R.id.article_title)
        TextView articleTitle;
        @BindView(R.id.author_img)
        CircleImageView authorImg;
        @BindView(R.id.author_name)
        TextView authorName;
        @BindView(R.id.post_time)
        TextView postTime;
        @BindView(R.id.reply_count)
        TextView replyCount;
        @BindView(R.id.view_count)
        TextView viewCount;

        NormalViewHolder(View view, OnItemClickListener listener) {
            super(view, listener);
            ButterKnife.bind(this, view);
        }

        @Override
        void setData(int pos) {
            Post object = postList.get(pos);
            articleTitle.setText(object.getTitle());
            authorName.setText(" " + object.getUser().getName());
            postTime.setText(" " + object.getCreated_at());
            replyCount.setText(" " + object.getComments_total());
            viewCount.setText(" " + object.getPageViewsCount());
            Picasso.get()
                    .load(object.getUser().getAvatar())
                    .placeholder(R.drawable.image_placeholder)
                    .into(authorImg);
            authorImg.setOnClickListener(view -> {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("userid",object.getUser().getId());
                context.startActivity(intent);
            });
        }
    }

    class BaseViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        BaseViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mItemClickListener = listener;
            if (listener != null)
                itemView.setOnClickListener(this);
        }

        void setData(int pos) {

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(this.getAdapterPosition());
            }
        }
    }
}
