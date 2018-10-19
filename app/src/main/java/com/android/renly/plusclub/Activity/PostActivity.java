package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ScrollView;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Fragment.PostFragment;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PostActivity extends BaseActivity {
    @BindView(R.id.fragment)
    ScrollView fragment;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initSlidr();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PostFragment postFragment = new PostFragment();
//        fragment.stopNestedScroll();
//        fragment.scrollTo(0, 0);
        Bundle bundle = new Bundle();
        bundle.putString("PostJsonObject", getIntent().getExtras().getString("PostJsonObject"));
        bundle.putString("from", "PostActivity");
        postFragment.setArguments(bundle);
        transaction.add(R.id.fragment, postFragment);
        transaction.commit();
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
