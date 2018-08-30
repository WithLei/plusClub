package com.android.renly.plusclub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_main_exit)
    Button btnMainExit;

    private String Cookie;
    private String id;

    private static final int LOGOUT_SUCCESS = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGOUT_SUCCESS:
                    printLog("退出登陆成功");
                    gotoActivity(LoginActivity.class);
                    break;
            }
        }
    };
    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        Cookie = sp.getString("Cookie","");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_main_exit)
    public void onViewClicked() {
        printLog("退出登陆");
        OkHttpUtils.post()
                .url(NetConfig.MY_URL + id)
                .addHeader("Cookie",Cookie)
                .addParams("__EVENTARGUMENT","")
                .addParams("__EVENTTARGET","likTc")
                //需要测是无__VIEWSTATE参数是否能够正常post
                .addParams("__VIEWSTATE","dDwxMjg4MjkxNjE4Ozs+Yo6b5fdmedUiZnMoVnDLU8xsqLw=")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String str = new String(response.body().bytes(),"GB2312");
                        printLog(str);
                        writeData("/sdcard/Test/output3.txt",str);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
    }
}