package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PostActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    private Unbinder unbinder;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("Title");
    }

    @Override
    protected void initView() {
        initToolBar(true,title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
