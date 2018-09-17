package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ScheduleActivity extends BaseActivity {
    @BindView(R.id.tv_schedule_html)
    TextView tvScheduleHtml;

    private Unbinder unbinder;
    private String eduid;
    private String userName;
    private String cookie;

    private static final int SHOW_HTML = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_HTML:
                    String HTML = msg.getData().getString("html");
                    showHTML(HTML);
                    break;
            }
        }
    };

    private void showHTML(String html) {
        tvScheduleHtml.setText(html);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData() {
        eduid = App.getEduid(this);
        userName = App.getName(this);
        cookie = App.getCookie(this);
    }

    @Override
    protected void initView() {
        getSchedule();
    }

    private void getSchedule() {
        printLog("Cookie " + cookie);
        String gbkName = null;
        try {
            gbkName = new String(userName.getBytes("GB2312"));
            printLog(gbkName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtils.get()
                .url(NetConfig.BASE_EDU_GETINFO)
                .addParams("xh",eduid)
                .addParams("xm","%D6%A3%D1%F6%D1%AB")
                .addParams(App.queryScheduleParam,App.queryScheduleValue)
                .addHeader("Cookie",cookie)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        writeData("/sdcard/Test/getScheduleHTML.txt", responseHTML);
                        printLog(responseHTML);

                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getSchedule onError");
                        printLog(e.getMessage() + call.toString());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("getSchedule onResponse");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
