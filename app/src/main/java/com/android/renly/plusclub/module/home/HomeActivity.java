package com.android.renly.plusclub.module.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.module.user.login.LoginActivity;
import com.android.renly.plusclub.module.setting.theme.ThemeActivity;
import com.android.renly.plusclub.module.user.userdetail.UserDetailActivity;
import com.android.renly.plusclub.adapter.MainPageAdapter;
import com.android.renly.plusclub.api.RetrofitService;
import com.android.renly.plusclub.api.bean.MessageEvent;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.module.base.BaseFragment;
import com.android.renly.plusclub.module.home.fullscreen.HomeFragment;
import com.android.renly.plusclub.module.hotnews.HotNewsFragment;
import com.android.renly.plusclub.module.mine.MineFragment;
import com.android.renly.plusclub.module.schedule.edu.main.ScheduleActivity;
import com.android.renly.plusclub.module.schedule.home.ScheduleFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.module.setting.main.SettingActivity;
import com.android.renly.plusclub.widget.MyBottomTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener {
    @BindView(R.id.bottom_bar)
    MyBottomTab bottomBar;
    private ViewPager viewPager;
    private List<BaseFragment> fragments = new ArrayList<>();

    private long mExitTime;
    private String version_name;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        initViewpager();
        discoverVersion();
    }

    @SuppressLint("CheckResult")
    private void discoverVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        version_name = "1.0";
        if (info != null) {
            version_name = info.versionName;
        } 
        RetrofitService.getRelease()
                .subscribe(responseBody -> {
                    String response = responseBody.string();
                    if (!response.contains("url"))
                        return;
                    afterGetVersion(response);
                }, throwable -> printLog("HomeActivity_discoverVersion_onError:" + throwable.getMessage()));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        printLog("HomeActivity onActivityResult");
        if (resultCode == RESULT_OK) {
            printLog("resultCode == RESULT_OK");
            switch (requestCode) {
                case ThemeActivity.requestCode://32
                    recreate();
                    printLog("onActivityResult ThemeActivity");
                    break;
                case LoginActivity.requestCode://64
                    doRefresh();
                    printLog("onActivityResult LoginActivity");
                    break;
                case UserDetailActivity.requestCode://128
                    doRefresh();
                    printLog("onActivityResult UserDetailActivity");
                    break;
                case SettingActivity.requestCode://256
                    doRefresh();
                    printLog("onActivityResult SettingActivity");
                    break;
                case ScheduleActivity.requestCode://512
                    doRefresh();
                    printLog("onActivityResult SettingActivity");
                    break;
            }
        } else
            printLog("resultCode != RESULT_OK");
        hideKeyBoard();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyBoard();
    }

    public static void doRefresh() {
        if (homeFragment != null)
            homeFragment.doRefresh();
        if (hotNewsFragment != null)
            hotNewsFragment.doRefresh();
        if (scheduleFragment != null)
            scheduleFragment.doRefresh();
        if (mineFragment != null)
            mineFragment.doRefresh();
    }

    private static HomeFragment homeFragment;
    private static HotNewsFragment hotNewsFragment;
    private static ScheduleFragment scheduleFragment;
    private static MineFragment mineFragment;

    private void initViewpager() {
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(this);
        homeFragment = new HomeFragment();
        hotNewsFragment = new HotNewsFragment();
        scheduleFragment = new ScheduleFragment();
        mineFragment = new MineFragment();
        fragments.add(homeFragment);
        fragments.add(hotNewsFragment);
        fragments.add(scheduleFragment);
        fragments.add(mineFragment);
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    private void afterGetVersion(String jsonObj) {
        JSONObject jsonObject = JSON.parseObject(jsonObj);
        String tag_name = jsonObject.getString("tag_name"); // eg:1.4.0
        String name = jsonObject.getString("name"); // eg:正式推送版本
        String body = jsonObject.getString("body"); // eg:不要改需求了！
        JSONArray assets = JSONArray.parseArray(jsonObject.getString("assets"));
        JSONObject assetObj = assets.getJSONObject(0);
        String updated_at = assetObj.getString("updated_at"); // eg:2018-10-09T07:06:09Z
        String browser_download_url = assetObj.getString("browser_download_url");
        // eg:https://github.com/WithLei/DistanceMeasure/releases/download/1.4.0/DistanceMeasure.apk

        if (tag_name.equals(version_name)){
//            MyToast.showText(this,"已经是最新版本");
            printLog("HomeActivity_afterGetVersion:已经是最新版本");
        }else{
            printLog("HomeActivity_afterGetVersion:检测到新版本");
            new AlertDialog.Builder(this)
                    .setTitle("检测到新版本")
                    .setMessage("版本名：" + name + "\n" +
                            "版本号：" + tag_name + "\n" +
                            "更新内容：\n" + body + "\n\n" +
                            "更新时间：" + updated_at)
                    .setCancelable(!body.contains("重要更新"))
                    .setPositiveButton("下载", (dialogInterface, i) -> {
                        Intent intent = new Intent();
                        Uri uri = Uri.parse(browser_download_url);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .create()
                    .show();
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageEvent(MessageEvent messageEvent){
        // 使用EventBus接受消息，当更新schedule后返回主页刷新课程表
        if (scheduleFragment != null)
            scheduleFragment.doRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyBoard();
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
