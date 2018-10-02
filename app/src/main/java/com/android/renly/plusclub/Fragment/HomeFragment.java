package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Adapter.ForumAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Bean.Forum;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.ci_home_img)
    CircleImageView ciHomeImg;
    @BindView(R.id.tv_home_title)
    TextView tvHomeTitle;
    @BindView(R.id.iv_home_search)
    ImageView ivHomeSearch;
    Unbinder unbinder;
    @BindView(R.id.refresh_layout)
    CircleRefreshLayout refreshLayout;
    private ExpandableStickyListHeadersListView list;

    private List<Forum> forumList;
    private String[] headers;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Context content) {
        if (App.ISLOGIN(getActivity()))
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.mipmap.pluslogo_round));
        else
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
        forumList = new ArrayList<>();
        forumList.add(new Forum("灌水专区", R.drawable.icon_mine_collect, 0));
        forumList.add(new Forum("摄影天地", R.drawable.icon_mine_friend, 0));
        forumList.add(new Forum("校园生活", R.drawable.icon_mine_info, 1));
        forumList.add(new Forum("失物招领", R.drawable.icon_mine_friend, 1));
        forumList.add(new Forum("技术博客", R.drawable.icon_mine_history, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));

        headers = new String[]{
                "休闲娱乐",
                "梨园生活",
                "学术交流"
        };
        list = getActivity().findViewById(R.id.list);
        list.setPadding(0, 0, 0, 0);
        StickyListHeadersAdapter adapter = new ForumAdapter(getContext(), forumList, headers);
        list.setAdapter(adapter);
        list.setOnHeaderClickListener((l, header, itemPosition, headerId, currentlySticky) -> {
            if (list.isHeaderCollapsed(headerId)) {
                list.expand(headerId);
            } else {
                list.collapse(headerId);
            }
        });
    }

    private void initView() {
        refreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
            }

            @Override
            public void refreshing() {
                handler.sendEmptyMessageDelayed(REFRESH_END, 3000);
            }
        });
    }

    @Override
    public void ScrollToTop() {
        if (forumList != null && forumList.size() > 0)
            list.scrollTo(0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        unbinder.unbind();
    }

    @OnClick({R.id.ci_home_img, R.id.iv_home_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_home_img:
                if (App.ISLOGIN(getActivity())) {
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("userName", App.getUid(getActivity()));
                    intent.putExtra("avatarUrl", "");
                    getActivity().startActivityForResult(intent, UserDetailActivity.requestCode);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, LoginActivity.requestCode);
                }
                getActivity().overridePendingTransition(R.anim.translate_in,R.anim.translate_in);
                break;
            case R.id.iv_home_search:
                break;
        }
    }

    private static final int REFRESH_END = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_END:
                    refreshLayout.finishRefreshing();
                    break;
            }
        }
    };
}
