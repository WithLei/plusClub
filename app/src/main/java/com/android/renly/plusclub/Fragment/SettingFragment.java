package com.android.renly.plusclub.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.ChangePwdActivity;
import com.android.renly.plusclub.Activity.SettingActivity;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.Utils.DataManager;
import com.android.renly.plusclub.Utils.IntentUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    //小尾巴string
    private EditTextPreference setting_user_tail;
    private SwitchPreference setting_show_tail;
    // 清理缓存
    private Preference clearCache;

    // 账号设置
    private PreferenceCategory group_user;
    private Preference user_logout;
    private Preference user_changepwd;


    private SharedPreferences sharedPreferences;

    private static final int GET_VERSION = 2333;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_VERSION:
                    afterGetVersion(msg.getData().getString("obj"));
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mActivity = getActivity();
        initTail();
        initUserGroup();
        initVersion();
        initOpenSource();
        initCache();
    }

    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    private void initCache() {
        // 清除缓存
        clearCache = findPreference("clean_cache");
        clearCache.setSummary("缓存大小：" + DataManager.getTotalCacheSize(mActivity));
        clearCache.setOnPreferenceClickListener(preference -> {
            DataManager.cleanApplicationData(mActivity);

            Toast.makeText(mActivity, "缓存清理成功!请重新登陆", Toast.LENGTH_SHORT).show();
            clearCache.setSummary("缓存大小：" + DataManager.getTotalCacheSize(mActivity));
            ((SettingActivity) mActivity).afterLogout();
            return false;
        });
    }

    private void initOpenSource() {
        // 项目地址
        findPreference("open_sourse").setOnPreferenceClickListener(preference -> {
            IntentUtils.openBroswer(mActivity, "https://github.com/WithLei/plusClub");
            return false;
        });
    }

    private int version_code;
    private String version_name;

    private void initVersion() {
        PackageManager manager = mActivity.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mActivity.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化版本
        version_code = 1;
        version_name = "1.0";
        if (info != null) {
            version_code = info.versionCode;
            version_name = info.versionName;
        }

        findPreference("about_this")
                .setSummary("当前版本" + version_name + "  version code:" + version_code);
        findPreference("about_this")
                .setOnPreferenceClickListener(preference -> {
                    RetrofitService.getRelease()
                            .subscribe(responseBody -> {
                                String response = responseBody.string();
                                if (!response.contains("url"))
                                    return;
                                afterGetVersion(response);
                            }, throwable -> Log.e("print", "SettingFragment_discoverVersion_onError:" + throwable.getMessage()));
                    return true;
                });
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

        if (tag_name.equals(version_name)) {
            MyToast.showText(mActivity, "已经是最新版本");
        } else {
            new AlertDialog.Builder(mActivity)
                    .setTitle("检测到新版本")
                    .setMessage("版本名：" + name + "\n" +
                            "版本号：" + tag_name + "\n" +
                            "更新内容：" + body + "\n\n" +
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

    private void initUserGroup() {
        group_user = (PreferenceCategory) findPreference("group_user");
        if (!App.ISLOGIN()) {
            getPreferenceScreen().removePreference(group_user);
        } else {
            user_logout = findPreference("user_logout");
            user_logout.setOnPreferenceClickListener(preference -> {
                App.setIsLogout();
                MyToast.showText(mActivity, "退出登录成功", Toast.LENGTH_SHORT, true);
                ((SettingActivity) mActivity).afterLogout();
                return true;
            });
            user_changepwd = findPreference("user_changepwd");
            user_changepwd.setOnPreferenceClickListener(preference -> {
                startActivity(new Intent(mActivity, ChangePwdActivity.class));
                return true;
            });
        }
    }

    private void initTail() {
        setting_show_tail = (SwitchPreference) findPreference(App.TEXT_SHOW_TAIL);

        setting_show_tail.setOnPreferenceChangeListener((preference, o) -> {
            App.setTextShowTail(!App.isTextShowTail());
            return true;
        });
        setting_user_tail = (EditTextPreference) findPreference(App.TEXT_TAIL);
        setting_user_tail.setEnabled(App.isTextShowTail());
        setting_user_tail.setSummary(sharedPreferences.getString(App.TEXT_TAIL, "无小尾巴"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case App.TEXT_SHOW_TAIL:
                setting_user_tail.setEnabled(App.isTextShowTail());
                setting_user_tail.setSummary(sharedPreferences.getString(App.TEXT_TAIL, "无小尾巴"));
                break;
            case App.TEXT_TAIL:
                setting_user_tail.setSummary(sharedPreferences.getString(App.TEXT_TAIL, "无小尾巴"));
                break;
        }
    }
}
