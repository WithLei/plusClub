package com.android.renly.plusclub.Module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.renly.plusclub.Activity.EditAcitivity;
import com.android.renly.plusclub.Activity.HomeActivity;
import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Activity.PostsActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Adapter.ForumAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Api.Bean.Forum;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.Utils.DateUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements HomeFragView{
    @BindView(R.id.ci_home_img)
    CircleImageView ciHomeImg;
    @BindView(R.id.tv_home_title)
    TextView tvHomeTitle;
    @BindView(R.id.iv_home_search)
    ImageView ivHomeSearch;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.ll_logintip)
    RelativeLayout llLogintip;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_edittip)
    RelativeLayout llEdittip;
    private Unbinder unbinder;


    private List<Forum> forumList;
    private String[] headers;
    private HomeFragPresenter mPresenter;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Context content) {
        mPresenter = new HomeFragPresenter("101210801", this);
        mPresenter.getData(false);
        if (App.ISLOGIN()) {
            llLogintip.setVisibility(View.GONE);
            llEdittip.setVisibility(View.VISIBLE);
        } else {
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
            llLogintip.setVisibility(View.VISIBLE);
            llEdittip.setVisibility(View.GONE);
        }
        setUserVisibleHint(true);
    }

    @Override
    protected void initView() {
        initForumList();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            doRefresh();
            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.finishLoadMore(2000));

    }

    public void doRefresh() {
        if (App.ISLOGIN()) {
            llLogintip.setVisibility(View.GONE);
            llEdittip.setVisibility(View.VISIBLE);
        } else {
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
            llLogintip.setVisibility(View.VISIBLE);
            llEdittip.setVisibility(View.GONE);
        }
        mPresenter.getData(true);
    }

    private void initForumList() {
        ForumAdapter adapter = new ForumAdapter(mActivity, forumList);
        adapter.setOnItemClickListener(pos -> {
            Intent intent = new Intent(mActivity, PostsActivity.class);
            intent.putExtra("Title", forumList.get(pos).getTitle());
            intent.putExtra("category", forumList.get(pos).getCategory());
            startActivity(intent);
        });
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 4);
        list.setClipToPadding(false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
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

    @OnClick({R.id.ci_home_img, R.id.iv_home_search, R.id.tip_login, R.id.tip_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_home_img:
                if (App.ISLOGIN()) {
                    Intent intent = new Intent(mActivity, UserDetailActivity.class);
                    intent.putExtra("userid", App.getUid());
                    mActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivityForResult(intent, LoginActivity.requestCode);
                }
                mActivity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                break;
            case R.id.iv_home_search:
                ToastProgramError();
                break;
            case R.id.tip_login:
                Intent intent = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivityForResult(intent, LoginActivity.requestCode);
                break;
            case R.id.tip_edit:
                Intent in = new Intent(mActivity, EditAcitivity.class);
                in.putExtra("category", "E-M-P-T-Y");
                mActivity.startActivity(in);
        }
    }

    private HomeActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    public void loadWeather(boolean isSuccess, String weather) {
        ivWeather.setImageResource(R.drawable.ic_sun);
        int currentHour = DateUtils.getHourTimeOfDay();
        if (currentHour <= 5 || currentHour >= 19) {
            tvWeather.setText("晚上好！");
            ivWeather.setImageResource(R.drawable.ic_moon);
        } else if (currentHour <= 10) {
            tvWeather.setText("早上好！");
        } else if (currentHour <= 13) {
            tvWeather.setText("中午好！");
        } else {
            tvWeather.setText("下午好！");
        }

        if (isSuccess) {
            if (weather.contains("晴"))
                return;
            else if (weather.contains("雨"))
                ivWeather.setImageResource(R.drawable.ic_rain);
            else if (weather.contains("风"))
                ivWeather.setImageResource(R.drawable.ic_cloudy);
            else if (weather.contains("雪"))
                ivWeather.setImageResource(R.drawable.ic_snow);
        }
    }

    @Override
    public void loadAvatar(String path) {
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.image_placeholder)
                .into(ciHomeImg);
    }

    @Override
    public void setForumList(List<Forum> forumList) {
        this.forumList = forumList;
    }
}
