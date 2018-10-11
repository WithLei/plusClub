package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.PostActivity;
import com.android.renly.plusclub.Adapter.CommentAdapter;
import com.android.renly.plusclub.Bean.Comment;
import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.Utils.IntentUtils;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

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
    @BindView(R.id.tv_comment_suggest)
    TextView tvCommentSuggest;

    private List<Comment> commentList;
    private Post postObj;
    private long postID;
    private String PostJsonString;

    private static final int GET_COMMENTDATA = 2;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_COMMENTDATA:
                    initCommentListData(msg.getData().getString("CommentJsonObj"));
                    initCommentList();
                    break;
            }
        }
    };

    @Override
    public int getLayoutid() {
        return R.layout.fragment_postcontent;
    }

    @Override
    protected void initData(Context content) {
        getPostObj();
        getCommentListData();
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
        postObj = JSON.parseObject(PostJsonString, Post.class);
        postID = postObj.getId();
    }

    /**
     * 根据acitivity获取的POSTID从服务器获取[回复对象含User对象]详情
     */
    private void getCommentListData() {
        OkHttpUtils.get()
                .url(NetConfig.BASE_POST_PLUS + "/" + postID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastShort("网络出状况咯ヽ(#`Д´)ﾉ");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000){
                            ToastShort("获取评论失败惹，再试试( • ̀ω•́ )✧");
                        }else{
                            Message msg = new Message();
                            msg.what = GET_COMMENTDATA;
                            Bundle bundle = new Bundle();
                            bundle.putString("CommentJsonObj",obj.getString("data"));
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
    }

    private void initCommentListData(String CommentJsonObj) {
        commentList = new ArrayList<>();
        JSONObject postObj = JSON.parseObject(CommentJsonObj);
        commentList = JSON.parseArray(postObj.getString("comments"),Comment.class);
        if (commentList.size() == 0) {
            rvComment.setVisibility(View.GONE);
            tvCommentSuggest.setVisibility(View.VISIBLE);
        }else{
            rvComment.setVisibility(View.VISIBLE);
            tvCommentSuggest.setVisibility(View.GONE);
        }
//        JSONObject obj = JSON.parseObject(PostJsonString);
//        commentList = JSON.parseArray(obj.getString("comments"),Comment.class);
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
        CommentAdapter adapter = new CommentAdapter(getContext(), commentList, postObj.getUser_id());
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
                String data = "这篇文章不错，分享给你们 【" + articleTitle.getText() + " \n链接地址：http://118.24.0.78/#/forum/"+ postID +"】\n来自PlusClub客户端";
                IntentUtils.sharePost(getActivity(), data);
                break;
            case R.id.close_panel:
                PostActivity postActivity = (PostActivity) getActivity();
                postActivity.hidePanel();
                break;
        }
    }
}
