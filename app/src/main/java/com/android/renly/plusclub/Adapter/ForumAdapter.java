package com.android.renly.plusclub.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.plusclub.Bean.Forum;
import com.android.renly.plusclub.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页论坛模块adapter
 */
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {
    private Context context;
    private List<Forum> forumList;
    private OnItemClickListener mItemClickListener = null;


    public ForumAdapter(Context context, List<Forum> forumList) {
        this.context = context;
        this.forumList = forumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_name, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.title)
        TextView title;

        ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mItemClickListener = listener;
            if (listener != null)
                itemView.setOnClickListener(this);
        }

        void setData(int pos) {
            img.setImageResource(forumList.get(pos).getImg());
            title.setText(forumList.get(pos).getTitle());
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(this.getAdapterPosition());
            }
        }
    }
}
