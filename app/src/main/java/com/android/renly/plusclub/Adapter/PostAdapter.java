package com.android.renly.plusclub.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.Bean.User;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子列表adapter
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> postList;
    private Context context;
    private OnItemClickListener mItemClickListener = null;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view,mItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post object = postList.get(position);
        holder.articleTitle.setText(object.getTitle());
        holder.authorName.setText(" " + object.getUser().getName());
        holder.postTime.setText(" " + object.getCreated_at());
        holder.replyCount.setText(" " + object.getComments_total());
        holder.viewCount.setText(" " + object.getPageViewsCount());
        Picasso.get()
                .load(object.getUser().getAvatar())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.authorImg);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        OnItemClickListener mItemClickListener;

        ViewHolder(View view,OnItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mItemClickListener = listener;
            view.setOnClickListener(this );
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getPosition());
            }
        }
    }
}
