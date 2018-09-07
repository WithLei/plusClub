package com.android.renly.plusclub.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.android.renly.plusclub.Adapter.MainPageAdapter;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.Fragment.HomeFragment;
import com.android.renly.plusclub.Fragment.HotNewsFragment;
import com.android.renly.plusclub.Fragment.MineFragment;
import com.android.renly.plusclub.Fragment.MsgFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.MyBottomTab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @BindView(R.id.bottom_bar)
    MyBottomTab bottomBar;
    private ViewPager viewPager;
    private List<BaseFragment>fragments = new ArrayList<>();

    private long mExitTime;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        initViewpager();
    }

    @Override
    protected void initView() {
        bottomBar.setOnTabChangeListener((v, position, isChange) -> setSelect(position, isChange));
    }

    private void setSelect(int position, boolean isChange) {
        if (isChange)
            viewPager.setCurrentItem(position, false);
        else
            fragments.get(position).ScrollToTop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ThemeActivity.requestCode && resultCode == RESULT_OK){
            // 切换主题
            printLog("切换主题");
            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    private void initViewpager() {
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(this);
        fragments.add(new HomeFragment());
        fragments.add(new HotNewsFragment());
        fragments.add(new MsgFragment());
        fragments.add(new MineFragment());
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 1500) {
                Toast.makeText(this, "再按一次退出Plus客户端(｡･ω･｡)~~", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomBar.setSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
