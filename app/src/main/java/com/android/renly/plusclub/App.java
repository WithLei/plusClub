package com.android.renly.plusclub;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.renly.plusclub.Checknet.NetworkReceiver;

public class App extends Application {

    public static Context context;
    private NetworkReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();

        regReciever();
    }

    public void regReciever() {
        if (receiver != null) return;
        //注册网络变化广播
        receiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
    }

    //是否为校园网
    public static boolean IS_SCHOOL_NET = false;

    public static boolean ISLOGIN(Context context) {
        return !TextUtils.isEmpty(App.getUid(context));
    }

    public static String getUid(Context context) {
        SharedPreferences shp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return shp.getString(USER_UID_KEY, "");
    }

    public static void setUid(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_UID_KEY, uid);
        editor.apply();
    }

    public static String getCookie(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(COOKIE,"");
    }

    public static void setCookie(Context context, String Cookie){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COOKIE, Cookie);
        editor.apply();
    }

    public static String getName(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_NAME_KEY,"");
    }

    public static void setName(Context context, String name){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_NAME_KEY, name);
        editor.apply();
    }

    public static String getPwd(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_PWD_KEY,"");
    }

    public static void setPwd(Context context, String pwd){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_PWD_KEY, pwd);
        editor.apply();
    }

    public static final String MY_SP_NAME = "PlusClub";
    public static final String USER_UID_KEY = "user_uid";
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_PWD_KEY = "user_pwd";
    public static final String COOKIE = "cookie";
}
