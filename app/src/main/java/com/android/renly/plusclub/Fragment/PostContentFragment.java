package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.renly.plusclub.Adapter.CommentAdapter;
import com.android.renly.plusclub.Bean.Comment;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    TextView btnMore;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    Unbinder unbinder;

    private List<Comment>commentList;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_postcontent;
    }

    @Override
    protected void initData(Context content) {
        initCommentListData();
        initView();
    }

    private void initView() {
        initCommentList();
    }

    private void initCommentListData() {
        commentList = new ArrayList<>();
        commentList.add(new Comment("测试回复","2018-10-4 15:03:01","一天前测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));
        commentList.add(new Comment("测试回复","2018-10-5 15:03:01","几小时内测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));
        commentList.add(new Comment("测试回复","2018-10-5 18:01:01","1-2小时测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));
        commentList.add(new Comment("测试回复","2018-10-5 18:59:01","1小时内测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));
        commentList.add(new Comment("测试回复","2018-10-4 19:10:01","5分钟内测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));
        commentList.add(new Comment("测试回复","2018-10-4 19:03:01","1分钟内测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));
        commentList.add(new Comment("测试回复","2015-10-4 15:03:01","几年前测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容测试内容内容"));

    }

    private void initCommentList() {
        CommentAdapter adapter = new CommentAdapter(getContext(),commentList);
        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()){
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

        rvComment.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
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
}
