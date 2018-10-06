package com.android.renly.plusclub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.plusclub.Activity.PostActivity;
import com.android.renly.plusclub.Bean.Forum;
import com.android.renly.plusclub.R;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * 首页论坛模块adapter
 */
public class ForumAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Forum> forumList;
    private String[] headerList;

    public ForumAdapter(Context context, List<Forum> forumList, String[] headerList) {
        this.context = context;
        this.forumList = forumList;
        this.headerList = headerList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.item_forum_header, parent, false);
            holder.header_title = convertView.findViewById(R.id.header_title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        holder.header_title.setText(headerList[forumList.get(position).getHeader()]);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return forumList.get(position).getHeader();
    }

    @Override
    public int getCount() {
        return forumList.size();
    }

    @Override
    public Object getItem(int pos) {
        return forumList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_forum_name, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Forum obj = forumList.get(pos);
        holder.img.setImageDrawable(context.getResources().getDrawable(obj.getImg()));
        holder.title.setText(obj.getTitle());

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView title;

        public ViewHolder(View view){
            img = view.findViewById(R.id.img);
            title = view.findViewById(R.id.title);
        }
    }

    static class HeaderViewHolder {
        TextView header_title;
    }
}
