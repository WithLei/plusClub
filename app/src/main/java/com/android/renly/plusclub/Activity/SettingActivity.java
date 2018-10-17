package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    private Unbinder unbinder;

    private final String[] titles = new String[]{
            "手动切换网络类型",
            "论坛小尾巴",
            "清理缓存",
            "检查更新",
            "热爱开源，感谢分享",
            "实验室功能",
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initSlidr();
        initToolBar(true, "设置");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }
}
