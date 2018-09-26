package com.android.renly.plusclub;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;

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
        if (sp.getBoolean(IS_LOGIN, false) && !TextUtils.isEmpty(sp.getString(USER_UID_KEY, "")))
            return true;
        else
            return false;
    }

    public static void setIsLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.apply();
    }

    public static void setIsLogout(Context context){
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.apply();
    }

    public static String getUid(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getString(USER_UID_KEY, "");
    }

    public static void setUid(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences(MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_UID_KEY, uid);
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
        long startWeekTime = nowTime - DateUtils.weekToMiles(nowWeek);
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

    public static final String MY_SP_NAME = "PlusClub";
    public static final String USER_UID_KEY = "user_uid";
    public static final String USER_PWD_KEY = "user_pwd";
    public static final String USER_EDUID_KEY = "xh";
    public static final String USER_NAME_KEY = "user_name";
    public static final String COOKIE = "cookie";
    public static final String THEME_KEY = "theme";
    public static final String AUTO_DARK_MODE_KEY = "auto_dark_mode";
    public static final String START_DARK_TIME_KEY = "start_dart_time";
    public static final String END_DARK_TIME_KEY = "end_dark_time";
    public static final String IS_REMEBER_PWD_USER = "is_remember_pwd_user";
    public static final String IS_LOGIN ="is_login";
    public static final String SCHEDULE_START_WEEK = "schedule_start_week";

    public static final String GitHubURL = "https://github.com/WithLei/plusClub";

    // 查询学生课程表信息参数及值
    public static final String queryScheduleParam = "gnmkdm";
    public static final String queryScheduleValue = "N121603";
    public static final String _VIEWSTATE_VALUE = "dDwzOTI4ODU2MjU7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8MT47aTwyPjtpPDQ%2BO2k8Nz47aTw5PjtpPDExPjtpPDEzPjtpPDE1PjtpPDI0PjtpPDI2PjtpPDI4PjtpPDMwPjtpPDMyPjtpPDM0Pjs%2BO2w8dDxwPHA8bDxUZXh0Oz47bDxcZTs%2BPjs%2BOzs%2BO3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDx4bjt4bjs%2BPjs%2BO3Q8aTwzPjtAPDIwMTgtMjAxOTsyMDE3LTIwMTg7MjAxNi0yMDE3Oz47QDwyMDE4LTIwMTk7MjAxNy0yMDE4OzIwMTYtMjAxNzs%2BPjtsPGk8MD47Pj47Oz47dDx0PDs7bDxpPDA%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w85a2m5Y%2B377yaMTYxMDMyMjAyMzc7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWnk%2BWQje%2B8muWRqOejijs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w85a2m6Zmi77ya5bel5a2m6ZmiOz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDzkuJPkuJrvvJrorqHnrpfmnLrnp5HlrabkuI7mioDmnK87Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOihjOaUv%2BePre%2B8muiuoTE2Mjs%2BPjs%2BOzs%2BO3Q8O2w8aTwxPjs%2BO2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs%2BPjt0PHA8bDxWaXNpYmxlOz47bDxvPGY%2BOz4%2BO2w8aTwxPjs%2BO2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs%2BPjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA%2BO2w8Pjs%2BPjs%2BOzs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE%2BO2k8MT47aTwxPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs%2BO2w8aTwwPjs%2BO2w8dDw7bDxpPDE%2BOz47bDx0PDtsPGk8MD47aTwxPjtpPDI%2BO2k8Mz47aTw0PjtpPDU%2BO2k8Nj47PjtsPHQ8cDxwPGw8VGV4dDs%2BO2w8d2Vi5bqU55So5byA5Y%2BR6aG555uu5a6e6Le177yI5LqM57qn6aG555uu77yJOz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDznvZfmsZ%2Foi7E7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDEuMDs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MDEtMTY7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPEphdmHmlrnlkJEKCgoKOz4%2BOz47Oz47Pj47Pj47Pj47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE%2BO2k8MD47aTwwPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs%2BOzs%2BO3Q8QDA8cDxwPGw8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs%2BO2w8aTwxPjtpPDE%2BO2k8MT47bDw%2BOz4%2BOz47Ozs7Ozs7Ozs7PjtsPGk8MD47PjtsPHQ8O2w8aTwxPjs%2BO2w8dDw7bDxpPDA%2BO2k8MT47aTwyPjtpPDM%2BO2k8ND47PjtsPHQ8cDxwPGw8VGV4dDs%2BO2w8MjAxOC0yMDE5Oz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDwxOz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDx3ZWLlupTnlKjlvIDlj5Hpobnnm67lrp7ot7XvvIjkuoznuqfpobnnm67vvIk7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOe9l%2Baxn%2BiLsTs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MS4wOz4%2BOz47Oz47Pj47Pj47Pj47Pj47Pj47PrX8jl%2FmmA7XNxQAOu0yfhg1hpDJ";
}
