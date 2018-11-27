package com.android.renly.plusclub.module.post.postcontent.fullscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.api.RetrofitService;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.module.post.postcontent.main.PostFragment;
import com.android.renly.plusclub.R;

import butterknife.BindView;

public class PostActivity extends BaseActivity {
    @BindView(R.id.fragment)
    ScrollView fragment;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras().getBoolean("isNormalPost"))
            setIntentData(getIntent().getExtras().getString("PostJsonObject"));
        else
            getPost();
    }

    @Override
    protected void initView() {
        initSlidr();

    }

    @SuppressLint("CheckResult")
    private void getPost() {
        long id = getIntent().getLongExtra("id",0);
        if (id == 0)
            return;
        RetrofitService.getPost(id)
                .subscribe(responseBody -> {
                    String response = responseBody.string();
                    if (!response.contains("code")) {
                        ToastNetWorkError();
                        printLog("getPost onResponse !response.contains(\"code\")");
                        return;
                    }
                    JSONObject dataObj = JSON.parseObject(response);
                    if (dataObj.getInteger("code") != 20000) {
                        ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                    } else {
                        setIntentData(dataObj.getString("data"));
                    }
                }, throwable -> {
                    ToastNetWorkError();
                    printLog("getPost onResponse !response.contains(\"code\")" + throwable.getMessage());
                });
    }

    private void setIntentData(String postJsonObj) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PostJsonObject", postJsonObj);
        bundle.putString("from", "PostActivity");
        postFragment.setArguments(bundle);
        transaction.add(R.id.fragment, postFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
