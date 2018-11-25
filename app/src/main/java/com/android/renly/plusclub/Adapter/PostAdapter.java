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
public class PostAdapter extends BaseAdapter {
    private List<Post> postList;
    private Context context;

    private int loadState = STATE_LOADING;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return postList.size() + 1;
    }

    @Override
    protected int getDataCount() {
        return postList.size() > 0 ? 1 : postList.size();
    }

    @Override
    protected int getItemType(int pos) {
        return 0;
    }

    @Override
    protected BaseAdapter.BaseViewHolder getItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        NormalViewHolder viewHolder = new NormalViewHolder(view);
        return viewHolder;
    }

    //改变状态
    public void changeLoadMoreState(int i) {
        this.loadState = i;
        notifyItemChanged(0);
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

        NormalViewHolder(View view) {
            super(view);
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
}
