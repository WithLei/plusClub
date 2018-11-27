package com.android.renly.plusclub.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.plusclub.module.user.userdetail.UserDetailActivity;
import com.android.renly.plusclub.api.bean.Comment;
import com.android.renly.plusclub.utils.toast.MyToast;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.widget.CircleImageView;
import com.android.renly.plusclub.utils.DateUtils;
import com.squareup.picasso.Picasso;
import com.zzhoujay.richtext.RichText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;
    private OnItemClickListener mItemClickListener = null;
    // 楼主ID
    private long lzid;

    public CommentAdapter(Context context, List<Comment> commentList, long lzid) {
        this.context = context;
        this.commentList = commentList;
        this.lzid = lzid;
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
        if (object.getUser_id() == lzid)
            holder.btLableLz.setVisibility(View.VISIBLE);
        else
            holder.btLableLz.setVisibility(View.GONE);
        holder.replayAuthor.setText(object.getUser().getName());
        holder.replayIndex.setText(position + 1 + "#");
        if (object.getCreated_at().contains("-"))
            holder.replayTime.setText(DateUtils.getFromNowOnTime(DateUtils.stringToMiles(object.getCreated_at())));
        else
            holder.replayTime.setText(object.getCreated_at());
        RichText.fromMarkdown(object.getBody()).into(holder.markdownText);
        Picasso.get()
                .load(object.getUser().getAvatar())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.articleUserImage);

        holder.markdownText.setOnLongClickListener(view -> {
            String user = object.getUser().getName();
            String content = object.getBody();
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText(null, content));
                MyToast.showText(context,"已复制" + user + "的评论");
            }
            return true;
        });
        holder.articleUserImage.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("userid", object.getUser().getId());
            context.startActivity(intent);
        });
        holder.btnReplyCz.setOnClickListener(holder);
        holder.btnMore.setOnClickListener(holder);
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
        void onItemClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.article_user_image)
        CircleImageView articleUserImage;
        @BindView(R.id.replay_author)
        TextView replayAuthor;
        @BindView(R.id.bt_lable_lz)
        TextView btLableLz;
        @BindView(R.id.btn_reply_cz)
        ImageView btnReplyCz;
        @BindView(R.id.btn_more)
        ImageView btnMore;
        @BindView(R.id.replay_index)
        TextView replayIndex;
        @BindView(R.id.replay_time)
        TextView replayTime;
        @BindView(R.id.markdown_text)
        TextView markdownText;
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
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}
