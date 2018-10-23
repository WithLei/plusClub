package com.android.renly.plusclub.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.renly.plusclub.Activity.ChangePwdActivity;
import com.android.renly.plusclub.Activity.SettingActivity;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.Utils.DataManager;
import com.android.renly.plusclub.Utils.IntentUtils;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        initTail();
        initUserGroup();
        initVersion();
        initOpenSource();
        initCache();
    }

    private void initCache() {
        // 清除缓存
        clearCache = findPreference("clean_cache");
        clearCache.setSummary("缓存大小：" + DataManager.getTotalCacheSize(getActivity()));
        clearCache.setOnPreferenceClickListener(preference -> {
            DataManager.cleanApplicationData(getActivity());

            Toast.makeText(getActivity(), "缓存清理成功!请重新登陆", Toast.LENGTH_SHORT).show();
            clearCache.setSummary("缓存大小：" + DataManager.getTotalCacheSize(getActivity()));
            return false;
        });
    }

    private void initOpenSource() {
        // 项目地址
        findPreference("open_sourse").setOnPreferenceClickListener(preference -> {
            IntentUtils.openBroswer(getActivity(), "https://github.com/WithLei/plusClub");
            return false;
        });
    }

    private void initVersion() {
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化版本
        int version_code = 1;
        String version_name = "1.0";
        if (info != null) {
            version_code = info.versionCode;
            version_name = info.versionName;
        }

        findPreference("about_this")
                .setSummary("当前版本" + version_name + "  version code:" + version_code);
    }

    private void initUserGroup() {
        group_user = (PreferenceCategory)findPreference("group_user");
        if (!App.ISLOGIN(getActivity())){
            getPreferenceScreen().removePreference(group_user);
        }else{
            user_logout = findPreference("user_logout");
            user_logout.setOnPreferenceClickListener(preference -> {
                App.setIsLogout(getActivity());
                MyToast.showText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT, true);
                SettingActivity settingActivity = (SettingActivity)getActivity();
                settingActivity.setResult(Activity.RESULT_OK);
                settingActivity.finishActivity();
                return true;
            });
            user_changepwd = findPreference("user_changepwd");
            user_changepwd.setOnPreferenceClickListener(preference -> {
                startActivity(new Intent(getActivity(),ChangePwdActivity.class));
                return true;
            });
        }
    }

    private void initTail() {
        setting_show_tail = (SwitchPreference) findPreference(App.TEXT_SHOW_TAIL);

        setting_show_tail.setOnPreferenceChangeListener((preference, o) -> {
            App.setTextShowTail(getActivity(), !App.isTextShowTail(getActivity()));
            return true;
        });
        setting_user_tail = (EditTextPreference) findPreference(App.TEXT_TAIL);
        setting_user_tail.setEnabled(App.isTextShowTail(getActivity()));
        setting_user_tail.setSummary(sharedPreferences.getString(App.TEXT_TAIL, "无小尾巴"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case App.TEXT_SHOW_TAIL:
                // 这里除了问题
                setting_user_tail.setEnabled(App.isTextShowTail(getContext()));
                setting_user_tail.setSummary(sharedPreferences.getString(App.TEXT_TAIL, "无小尾巴"));
                break;
            case App.TEXT_TAIL:
                setting_user_tail.setSummary(sharedPreferences.getString(App.TEXT_TAIL, "无小尾巴"));
                break;
        }
    }
}
