package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.BatchRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HotNewsFragment extends BaseFragment {
    @BindView(R.id.btn_1)
    BatchRadioButton btn1;
    @BindView(R.id.btn_2)
    BatchRadioButton btn2;
    @BindView(R.id.btn_3)
    BatchRadioButton btn3;
    @BindView(R.id.tv_hotnews_showlogin)
    TextView tvHotnewsShowlogin;
    @BindView(R.id.rv_hotnews)
    RecyclerView rvHotnews;
    @BindView(R.id.swiperefresh_hotnews)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_hotnews;
    }

    @Override
    protected void initData(Context content) {
        initView();
    }

    private void initView() {
        if (App.ISLOGIN(getActivity())){
            tvHotnewsShowlogin.setVisibility(View.GONE);
            rvHotnews.setVisibility(View.VISIBLE);
        }else{
            tvHotnewsShowlogin.setVisibility(View.VISIBLE);
            rvHotnews.setVisibility(View.GONE);
        }
        initRefreshLayout();
    }

    /**
     * 下拉刷新样式
     */
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(() -> new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(() -> refreshLayout.setRefreshing(false));
            }
        });
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

