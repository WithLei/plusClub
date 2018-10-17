package com.android.renly.plusclub.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class EduActivity extends BaseActivity {

    @BindView(R.id.btn_main_exit)
    Button btnMainExit;
    @BindView(R.id.tv_main_name)
    TextView tvMainName;
    @BindView(R.id.btn_main_jump)
    Button btnMainJump;

    private Unbinder unbinder;

    private String Cookie;
    private String id;
    private String stuName;
    private SharedPreferences sp;

    private static final int LOGOUT_SUCCESS = 1;
    private static final int LOGIN_FAIL_SYSTEMBUSY = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGOUT_SUCCESS:
                    afterSuccessLogout();
                    break;
                case LOGIN_FAIL_SYSTEMBUSY:
                    ToastShort("错误原因：系统正忙！");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        printLog("butterKnife bind");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edumain;
    }

    @Override
    protected void initData() {
        sp = getSharedPreferences(App.MY_SP_NAME, MODE_PRIVATE);
        Cookie = sp.getString(App.COOKIE, "");
        id = sp.getString(App.USER_UID_KEY, "");
        stuName = sp.getString(App.USER_EDUNAME_KEY, "");
    }


    @Override
    protected void initView() {
        initSlidr();
        if (!stuName.isEmpty() && stuName != null)
            tvMainName.setText("你好，" + stuName + "同学");
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    private void doLogout() {
        printLog("退出登陆");
        OkHttpUtils.post()
                .url(NetConfig.BASE_EDU_HOST_ME + id)
                .addHeader("Cookie", Cookie)
                .addParams("__EVENTARGUMENT", "")
                .addParams("__EVENTTARGET", "likTc")
                //无__VIEWSTATE参数不能够正常post
                .addParams("__VIEWSTATE", App.get__VIEWSTATE(this))
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        writeData("/sdcard/Test/LogoutHTML.txt", responseHTML);
                        getLogout();
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("doLogout onError");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("doLogout onResponse");
                    }
                });
    }

    /**
     * 注册二次退出
     */
    private void getLogout() {
        OkHttpUtils.get()
                .url(NetConfig.GET_LOGOUT_URL_RS)
                .addHeader("Cookie", Cookie)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getLogout onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        printLog(response);
                        writeData("/sdcard/Test/LogoutGetHTML.txt", response);
                        checkLogoutSuccess(response);
                    }
                });
    }

    /**
     * 完成退出登录操作
     */
    private void afterSuccessLogout() {
        SharedPreferences.Editor editor = sp.edit();
        String Cookie = sp.getString("Cookie", "");
        editor.clear();
        editor.putString("Cookie", Cookie);
        editor.apply();
        printLog("退出登录成功");
        MyToast.showText(this, "退出登录成功", Toast.LENGTH_SHORT, true);
        finishActivity();
    }

    /**
     * 检查是否退出登录成功
     */
    private void checkLogoutSuccess(String logoutresult) {
        Document doc = Jsoup.parse(logoutresult);
        Elements success = doc.select("title");
        Elements err = doc.select("p[class]");

        for (Element link : success) {
            //获取所要查询的URL,这里相应地址button的名字叫成绩查询
            if (link.text().equals("欢迎使用正方教务管理系统！请登录")) {
                handler.sendEmptyMessage(LOGOUT_SUCCESS);
                return;
            }
        }

        for (Element link : err) {
            if (link.text().equals("错误原因：系统正忙！"))
                handler.sendEmptyMessage(LOGIN_FAIL_SYSTEMBUSY);
            return;
        }
    }

    @OnClick({R.id.btn_main_exit, R.id.btn_main_jump, R.id.btn_main_ecard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_exit:
                doLogout();
                break;
            case R.id.btn_main_jump:
                gotoActivity(ScheduleActivity.class);
                break;
            case R.id.btn_main_ecard:
                gotoActivity(ECardActivity.class);
                break;
        }
    }

}