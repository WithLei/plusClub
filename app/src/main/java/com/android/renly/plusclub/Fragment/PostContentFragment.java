package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.PostActivity;
import com.android.renly.plusclub.Adapter.CommentAdapter;
import com.android.renly.plusclub.Bean.Comment;
import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.Utils.DateUtils;
import com.android.renly.plusclub.Utils.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PostContentFragment extends BaseFragment {
    @BindView(R.id.article_title)
    TextView articleTitle;
    @BindView(R.id.main_window)
    RelativeLayout mainWindow;
    @BindView(R.id.article_user_image)
    CircleImageView articleUserImage;
    @BindView(R.id.article_username)
    TextView articleUsername;
    @BindView(R.id.bt_lable_lz)
    TextView btLableLz;
    @BindView(R.id.article_post_time)
    TextView articlePostTime;
    @BindView(R.id.btn_more)
    ImageView btnMore;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    Unbinder unbinder;

    private List<Comment> commentList;
    private Post postObj;
    private String PostJsonString;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_postcontent;
    }

    @Override
    protected void initData(Context content) {
        getPostObj();
        initCommentListData();
        initView();
    }

    private void initView() {
        initHead();
        initCommentList();
    }

    /**
     * 从activity 获取帖子对象
     */
    private void getPostObj() {
        PostJsonString = getArguments().getString("PostJsonObject");
        postObj = JSON.parseObject(PostJsonString,Post.class);
    }

    private void initCommentListData() {
        commentList = new ArrayList<>();
        printLog("PostJsonString " + PostJsonString);
        JSONObject obj = JSON.parseObject(PostJsonString);
        printLog("obj.getString(\"comments\")"+obj.getString("comments"));
        commentList = JSON.parseArray(obj.getString("comments"),Comment.class);
    }

    /**
     * 初始化标题等等信息
     */
    private void initHead() {
        articleTitle.setText(postObj.getTitle());
        articleUsername.setText(postObj.getUser().getName());
        articlePostTime.setText(postObj.getCreated_at());
        content.setText(postObj.getBody());
        Picasso.get()
                .load(postObj.getUser().getAvatar())
                .placeholder(R.drawable.image_placeholder)
                .into(articleUserImage);
    }

    private void initCommentList() {
        CommentAdapter adapter = new CommentAdapter(getContext(), commentList);
        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        // 解决数据加载不全的问题
        rvComment.setNestedScrollingEnabled(false);
        rvComment.setHasFixedSize(true);
        //解决数据加载完成后，没有停留在顶部的问题
        rvComment.setFocusable(false);

        rvComment.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // 调整draw缓存,加速recyclerview加载
        rvComment.setItemViewCacheSize(20);
        rvComment.setDrawingCacheEnabled(true);
        rvComment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.share_panel, R.id.close_panel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_panel:
                String data = "这篇文章不错，分享给你们 【" + articleTitle.getText() + "】";
                IntentUtils.sharePost(getActivity(), data);
                break;
            case R.id.close_panel:
                PostActivity postActivity = (PostActivity)getActivity();
                postActivity.hidePanel();
                break;
        }
    }
}
