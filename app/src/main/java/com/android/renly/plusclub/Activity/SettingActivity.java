package com.android.renly.plusclub.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.module.setting.SettingFragment;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.container)
    FrameLayout container;
    private Unbinder unbinder;

    public static final int requestCode = 256;

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
        Fragment to = new SettingFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, to).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    public void afterLogout(){
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
