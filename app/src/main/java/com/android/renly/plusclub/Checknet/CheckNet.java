package com.android.renly.plusclub.Checknet;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.renly.plusclub.App;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by free2 on 16-4-13.
 * 判断现在的网络状态
 * 校园网or 外网
 */
public class CheckNet {

//    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private Context context;
//
    public CheckNet(Context context) {
        this.context = context;
    }
//
    public void startCheck(final CheckNetResponse handler) {
//        threadPool.execute(() -> request(handler));
    }
//
//    private void request(final CheckNetResponse checkNetResponse) {
//        finishCount = 0;
//        errCount = 0;
//
//        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
//        if (activeNetwork != null && activeNetwork.isConnected()) {
//            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                errCount++;
//                App.IS_SCHOOL_NET = false;
//                checkOutNet(context);
//            } else {
//                checkSchoolNet(context);
//                checkOutNet(context);
//            }
//        } else {
//            checkNetResponse.sendFinishMessage(0, "无法连接到睿思,请打开网络连接");
//        }
//    }
//
//    private int finishCount = 0;
//    private int errCount = 0;
//    private String pcResponse = "";
//    private String mobileRes = "";
//
//    private void checkSchoolNet(Context context) {
//        HttpUtil.get(App.LOGIN_RS, new ResponseHandler() {
//            @Override
//            public void onSuccess(byte[] response) {
//                pcResponse = new String(response);
//            }
//
//            @Override
//            public void onFinish() {
//                checkLoginResult(context, true, pcResponse);
//            }
//        });
//    }
//
//    private void checkOutNet(Context context) {
//        HttpUtil.get(App.LOGIN_ME, new ResponseHandler() {
//            @Override
//            public void onSuccess(byte[] response) {
//                mobileRes = new String(response);
//            }
//
//            @Override
//            public void onFinish() {
//                checkLoginResult(context, false, mobileRes);
//            }
//        });
//    }
//
//    public void checkLoginResult(Context context, boolean isInner, String response) {
//        if (TextUtils.isEmpty(response)) {
//            errCount++;
//            if (errCount == 2) {
//                Toast.makeText(context, "无法连接到睿思,请检查你的网络连接！",
//                        Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//
//        if (isInner) {
//            finishCount++;
//            App.IS_SCHOOL_NET = true;
//        } else { //外网 忽略
//            if (finishCount == 0) {
//                finishCount++;
//                App.IS_SCHOOL_NET = false;
//            } else {
//                return;
//            }
//        }
//
//        int i = response.indexOf("欢迎您回来");
//        if (i > 0) {
//            String grade, name;
//            //<p>欢迎您回来，实习版主 激萌路小叔，现在将转入登录前页面</p>
//            //<p>欢迎您回来，<font color="#0099FF">实习版主</font> 激萌路小叔，现在将转入登录前页面</p>
//            String info = response.substring(i, i + 70);
//            int pos = info.indexOf("，");
//            if (info.charAt(pos + 1) == '<') {//管理员
//                int pos2 = info.indexOf(">", pos);
//                int pos3 = info.indexOf("<", pos2);
//                grade = info.substring(pos2 + 1, pos3);
//                int pos4 = info.indexOf(" ", pos3);
//                int pos5 = info.indexOf("，", pos4);
//                name = info.substring(pos4 + 1, pos5);
//            } else {
//                int pos2 = info.indexOf(" ", pos);
//                grade = info.substring(pos + 1, pos2);
//                int pos3 = info.indexOf("，", pos2);
//                name = info.substring(pos2 + 1, pos3);
//            }
//
//            String uid = GetId.getId("uid=", response.substring(i));
//            int indexhash = response.indexOf("formhash");
//            String hash = response.substring(indexhash + 9, indexhash + 17);
//            SharedPreferences shp = context.getSharedPreferences(App.MY_SHP_NAME, MODE_PRIVATE);
//            SharedPreferences.Editor ed = shp.edit();
//            ed.putString(App.USER_UID_KEY, uid);
//            ed.putString(App.USER_NAME_KEY, name);
//            ed.putString(App.USER_GRADE_KEY, grade);
//            ed.putString(App.HASH_KEY, hash);
//            ed.apply();
//            Log.v("res", "grade " + grade + " uid " + uid + " name " + name + " hash " + hash);
//        }
//        Log.v("res", "校园网:" + App.IS_SCHOOL_NET);
//    }
}
