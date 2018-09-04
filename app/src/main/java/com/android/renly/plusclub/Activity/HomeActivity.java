package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Fragment.HomeFragment;
import com.android.renly.plusclub.Fragment.HotNewsFragment;
import com.android.renly.plusclub.Fragment.MineFragment;
import com.android.renly.plusclub.Fragment.MsgFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.MyBottomTab;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.bottom_bar)
    MyBottomTab bottomBar;

    private static HomeFragment homeFragment;
    private static HotNewsFragment hotNewsFragment;
    private static MsgFragment msgFragment;
    private static MineFragment mineFragment;
    private FragmentTransaction transaction;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        setSelect(0);
    }

    @Override
    protected void initView() {
        bottomBar.setOnTabChangeListener(new MyBottomTab.OnTabChangeListener() {
            @Override
            public void tabClicked(View v, int position, boolean isChange) {
                setSelect(position);
            }
        });
    }

    private void setSelect(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏所有fragment
        hideFragments();
        switch (position){
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_view,homeFragment);
                }
                transaction.show(homeFragment);
                break;
            case 1:
                if (hotNewsFragment == null) {
                    hotNewsFragment = new HotNewsFragment();
                    transaction.add(R.id.fl_view,hotNewsFragment);
                }
                transaction.show(hotNewsFragment);
                break;
            case 2:
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    transaction.add(R.id.fl_view,msgFragment);
                }
                transaction.show(msgFragment);
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fl_view,mineFragment);
                }
                transaction.show(mineFragment);
                break;
        }
        // 提交事务
        transaction.commit();
    }

    private void hideFragments() {
        if (homeFragment != null)
            transaction.hide(homeFragment);
        if (hotNewsFragment != null)
            transaction.hide(hotNewsFragment);
        if (msgFragment != null)
            transaction.hide(msgFragment);
        if (mineFragment != null)
            transaction.hide(mineFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
