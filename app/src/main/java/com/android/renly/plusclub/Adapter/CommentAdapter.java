package com.android.renly.plusclub.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.renly.plusclub.Bean.Comment;
import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;
    private OnItemClickListener mItemClickListener = null;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comment object = commentList.get(position);
        holder.replayAuthor.setText(object.getName());
        holder.replayIndex.setText(position+1 + "#");
        holder.replayTime.setText(object.getFromNowOnTime());
        holder.htmlText.setText(object.getContent());
        holder.articleUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_mine_friend));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.article_user_image)
        CircleImageView articleUserImage;
        @BindView(R.id.replay_author)
        TextView replayAuthor;
        @BindView(R.id.bt_lable_lz)
        TextView btLableLz;
        @BindView(R.id.btn_reply_cz)
        TextView btnReplyCz;
        @BindView(R.id.btn_more)
        TextView btnMore;
        @BindView(R.id.replay_index)
        TextView replayIndex;
        @BindView(R.id.replay_time)
        TextView replayTime;
        @BindView(R.id.html_text)
        TextView htmlText;
        @BindView(R.id.main_window)
        ConstraintLayout mainWindow;

        OnItemClickListener mItemClickListener;

        ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mItemClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getPosition());
            }
        }
    }

}
