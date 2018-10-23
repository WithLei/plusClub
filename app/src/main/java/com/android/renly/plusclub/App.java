package com.android.renly.plusclub;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.android.renly.plusclub.Activity.ThemeActivity;
import com.android.renly.plusclub.Checknet.NetworkReceiver;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.DataBase.SQLiteHelper;
import com.android.renly.plusclub.Utils.DateUtils;

import java.text.SimpleDateFormat;

public class App extends Application {

    public static Context context;
    private NetworkReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();

        regReciever();
    }

    @Override
    public void onTerminate() {
        // 关闭数据库
        new SQLiteHelper(context).close();
        unRegRecieve();

        context = null;
        super.onTerminate();
    }

    private void unRegRecieve() {
        if (receiver != null)
            return ;
        receiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
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
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        if (sp.getBoolean(IS_LOGIN, false) && !TextUtils.isEmpty(String.valueOf(sp.getLong(USER_UID_KEY, 0))))
            return true;
        else
            return false;
    }

    public static void setIsLogout(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.remove(USER_TOKEN_KEY);
        editor.remove(USER_NAME_KEY);
        editor.remove(USER_ROLE_KEY);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_EMAIL_KEY, "");
    }

    public static void setEmail(Context context, String email) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_EMAIL_KEY, email);
        editor.apply();
    }

    public static long getUid(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getLong(USER_UID_KEY, 0);
    }

    public static void setUid(Context context, long uid) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(USER_UID_KEY, uid);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_NAME_KEY, "");
    }

    public static void setName(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_NAME_KEY, name);
        editor.apply();
    }

    public static String getRole(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_ROLE_KEY, "");
    }

    public static void setRole(Context context, String role) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_ROLE_KEY, role);
        editor.apply();
    }

    public static String getToken(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_TOKEN_KEY, "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_TOKEN_KEY, token);
        editor.apply();
    }

    public static String getEduid(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_EDUID_KEY,"");
    }

    public static void setEduid(Context context, String eduid){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_EDUID_KEY, eduid);
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

    public static String getEduName(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_EDUNAME_KEY,"");
    }

    public static void setEduName(Context context, String name){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_EDUNAME_KEY, name);
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

    public static String getEduPwd(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_EDUPWD_KEY,"");
    }

    public static void setEduPwd(Context context, String pwd){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_EDUPWD_KEY, pwd);
        editor.apply();
    }

    public static int getCustomTheme(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getInt(THEME_KEY, 0);
    }

    public static void setCustomTheme(Context context, int theme) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(THEME_KEY, theme);
        editor.apply();
    }

    public static boolean isAutoDarkMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getBoolean(AUTO_DARK_MODE_KEY, true);
    }

    public static void setAutoDarkMode(Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(AUTO_DARK_MODE_KEY, value);
        editor.apply();
    }

    public static void setDarkModeTime(Context context, boolean isStart, int value) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isStart) {
            editor.putInt(START_DARK_TIME_KEY, value);
        } else {
            editor.putInt(END_DARK_TIME_KEY, value);
        }
        editor.apply();
    }

    public static int[] getDarkModeTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        int[] ret = new int[2];
        ret[0] = sp.getInt(START_DARK_TIME_KEY, 21);
        ret[1] = sp.getInt(END_DARK_TIME_KEY, 6);
        return ret;
    }

    public static boolean isRemeberPwdUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getBoolean(IS_REMEBER_PWD_USER,false);
    }

    public static void setRemeberPwdUser(Context context,boolean isRemeber){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_REMEBER_PWD_USER,isRemeber);
        editor.apply();
    }

    public static void setScheduleStartWeek(Context context,int nowWeek){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        long nowTime = System.currentTimeMillis();
        long startWeekTime = nowTime - DateUtils.weekToMiles(nowWeek - 1);
        editor.putLong(SCHEDULE_START_WEEK,startWeekTime);
        editor.apply();
    }

    public static int getScheduleNowWeek(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        long startWeekTime = sp.getLong(SCHEDULE_START_WEEK,0);
        if (startWeekTime == 0)
            return 1;
        long nowTime = System.currentTimeMillis();
        int week = (int)(nowTime - startWeekTime)/1000/3600/24/7;
        return week + 1;
    }

    public static String get__VIEWSTATE(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(_VIEWSTATE_VALUE,"");
    }

    public static void set__VIEWSTATE(Context context,String value){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(_VIEWSTATE_VALUE,value);
        editor.apply();
    }

    public static boolean isTextShowTail(Context context){
        if (context == null)
            Log.e("print","context == null");
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getBoolean(TEXT_SHOW_TAIL,false);
    }

    public static void setTextShowTail(Context context,boolean value){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(TEXT_SHOW_TAIL,value);
        editor.apply();
    }

    public static final String MY_SP_NAME = "PlusClub";
    public static final String USER_EMAIL_KEY = "user_email";
    public static final String USER_UID_KEY = "user_uid";
    public static final String USER_PWD_KEY = "user_pwd";
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_ROLE_KEY = "user_role";
    public static final String USER_TOKEN_KEY = "user_token";
    public static final String TEXT_SHOW_TAIL = "setting_show_tail";
    public static final String TEXT_TAIL = "setting_user_tail";
    public static final String USER_EDUID_KEY = "edu_id";
    public static final String USER_EDUNAME_KEY = "edu_name";
    public static final String USER_EDUPWD_KEY = "edu_pwd";
    public static final String COOKIE = "cookie";
    public static final String THEME_KEY = "theme";
    public static final String AUTO_DARK_MODE_KEY = "auto_dark_mode";
    public static final String START_DARK_TIME_KEY = "start_dart_time";
    public static final String END_DARK_TIME_KEY = "end_dark_time";
    public static final String IS_REMEBER_PWD_USER = "is_remember_pwd_user";
    public static final String IS_LOGIN ="is_login";
    public static final String SCHEDULE_START_WEEK = "schedule_start_week";
    public static final String _VIEWSTATE_VALUE = "viewstate";

    public static final String GitHubURL = "https://github.com/WithLei/plusClub";

    // 查询学生课程表信息参数及值
    public static final String queryScheduleParam = "gnmkdm";
    public static final String queryScheduleValue = "N121603";
}
