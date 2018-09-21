package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Bean.Course;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final int SHOW_SCHEDULE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_SCHEDULE:
                    String JsonObjs = msg.getData().getString("JsonObjs");
                    List<Course>list = JSON.parseArray(JsonObjs, Course.class);
                    String html = "";
                    for (int i = 0;i < list.size();i++)
                        html += list.get(i).toString();
                    showHTML(html);
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
        printLog("Cookie " + cookie + " xh " + eduid );
        String gbkName = null;
        try {
            gbkName = new String(userName.getBytes("GB2312"));
            printLog(gbkName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtils.get()
                .url(NetConfig.BASE_EDU_GETINFO_RS)
                .addParams("xh",eduid)
                .addParams("xm",gbkName)
                .addParams(App.queryScheduleParam,App.queryScheduleValue)
                .addHeader("Cookie",cookie)
                .addHeader("Host","jwgl.webvpn.lsu.edu.cn")
                .addHeader("Referer",NetConfig.BASE_EDU_HOST_ME + eduid)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        writeData("/sdcard/Test/getScheduleHTML.txt", responseHTML);
                        List<Course> scheduleList = getScheduleList(responseHTML);
                        String JsonObjs = JSON.toJSONString(scheduleList);
                        Bundle bundle = new Bundle();
                        bundle.putString("JsonObjs",JsonObjs);
                        Message msg = new Message();
                        msg.what = SHOW_SCHEDULE;
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getSchedule onError");
                        printLog(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("getSchedule onResponse");
                    }
                });
    }

    private ArrayList<Course> getScheduleList(String responseHTML) {
        // 获取schedule的HTML
        String scheduleHTML = findScheduleHtml(responseHTML);
        ArrayList<Course>courses = new ArrayList<>();

        // 按上课时间分隔
        String[] rows = scheduleHTML.split("</tr><tr>");
        for (int i = 2;i < rows.length;i += 2){
            String r = rows[i];
            // 按星期几分隔
            String[] cols = r.split("</td><td([\\S\\s]*?)>");

            int j = 1;
            if (i == 2||i == 6||i == 10){
                j = 2;
            }

            int x = 1;
            for (;j < cols.length; x++,j++){
                String c = cols[j];
                String[] info = c.split("<br>");
                if (info[0].contains(" "))
                    continue;
                String[] tem = new String[4];
                int t = 0;

                for(int k = 0;k < info.length;k++){
                    String item = info[k].trim();
                    if(item.equals("")){
                        // 处理同一时间不同周数的课程
                        t = 0;
                        tem = new String[4];
                        continue;
                    }
                    tem[t++] = item;
                    if (t == 4){
                        courses.add(new Course(tem,i - 1,x));
                    }
                }
            }
        }
        return courses;
    }

    /**
     * 将接收到的HTML代码查找出课程表相关的HTML代码后返回
     * @param responseHTML
     * @return
     */
    private String findScheduleHtml(String responseHTML) {
        String scheduleHtml = "";
        String tar = "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">";
        String pattern = "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">([\\S\\s]+?)</table>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(responseHTML);
        if (m.find()){
            scheduleHtml = m.group(0);
            scheduleHtml = scheduleHtml.substring(scheduleHtml.indexOf(tar) + tar.length(),scheduleHtml.lastIndexOf("</table>")).trim();
        }else{
            printLog("findScheduleHtml fail");
        }
        return scheduleHtml;
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
