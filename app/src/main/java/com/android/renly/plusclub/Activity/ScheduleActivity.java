package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.renly.plusclub.Adapter.ScheduleGridAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Api.Bean.Course;
import com.android.renly.plusclub.Api.Bean.MessageEvent;
import com.android.renly.plusclub.Module.base.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.DataBase.MyDB;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Response;

public class ScheduleActivity extends BaseActivity {
    @BindView(R.id.courceDetail)
    GridView courceDetail;
    @BindView(R.id.switchWeek)
    NiceSpinner spinner;
    @BindView(R.id.iv_toolbar_menu)
    ImageView ivToolbarMenu;

    private Unbinder unbinder;

    private String eduid;
    private String userName;
    private String cookie;
    private int nowWeek = 1;
    public static final int requestCode = 512;

    private String[][] contents;
    private ScheduleGridAdapter adapter;
    List<Course> scheduleList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData() {
        contents = new String[6][7];
        eduid = App.getEduid();
        userName = App.getEduName();
        cookie = App.getCookie();

    }

    @Override
    protected void initView() {
        ivToolbarMenu.setImageResource(R.drawable.ic_check_black_24dp);
        ivToolbarMenu.setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent("scheduleRefresh"));
            finishActivity();
        });
        initSlidr();
        initSpinner();
        clearOldSchedule();
        getScheduleFromEdu();
    }

    private void clearOldSchedule() {
        MyDB db = new MyDB(this);
        db.clearSchedule();
    }

    /**
     * 初始化spinner
     */
    private void initSpinner() {
        List<String> dataset = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.scheduleWeek)));
        spinner.attachDataSource(dataset);
        spinner.setBackgroundResource(R.drawable.tv_round_border);
        spinner.setTextColor(getResources().getColor(R.color.white));
        spinner.setSelectedIndex(nowWeek - 1);
        spinner.setArrowDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        spinner.setArrowTintColor(R.color.white);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                nowWeek = pos + 1;
                initScheduleDataFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 从教务系统抓取课程表
     */
    private void getScheduleFromEdu() {
        printLog("Cookie " + cookie + " xh " + eduid);
        String gbkName = null;
        try {
            gbkName = new String(userName.getBytes("GB2312"));
        } catch (UnsupportedEncodingException e) {
            Log.e("Exception","ScheduleActivity getScheduleFromEdu " + e.getMessage());
        }
        String finalGbkName = gbkName;
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            OkHttpUtils.get()
                    .url(NetConfig.BASE_EDU_GETINFO_EDU)
                    .addParams("xh", eduid)
                    .addParams("xm", finalGbkName)
                    .addParams(App.queryScheduleParam, App.queryScheduleValue)
                    .addHeader("Cookie", cookie)
                    .addHeader("Host", "jwgl.webvpn.lsu.edu.cn")
                    .addHeader("Referer", NetConfig.BASE_EDU_HOST_ME + eduid)
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            String responseHTML = new String(response.body().bytes(), "GB2312");
//                        writeData("/sdcard/Test/getScheduleHTML.txt", responseHTML);
                            List<Course> scheduleList = getScheduleList(responseHTML);
                            String JsonObjs = JSON.toJSONString(scheduleList);
                            emitter.onNext(JsonObjs);
                            return null;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            printLog("ScheduleActivity getSchedule onError");
                            printLog(e.getMessage());
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            printLog("ScheduleActivity getSchedule onResponse");
                        }
                    });
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        initScheduleDataFromEdu(s);
                        ToastShort("更新课表完成");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 初始化从DataBase的schedule数据
     */
    private void initScheduleDataFromDB() {
        MyDB db = new MyDB(this);
        scheduleList = db.getSchedule();
        contents = new String[6][7];
        for (int x = 0; x < 6; x++)
            for (int y = 0; y < 7; y++)
                contents[x][y] = "";
        for (int i = 0; i < scheduleList.size(); i++) {
            Course course = scheduleList.get(i);
            printLog(course.toString());
            contents[(course.getRows() - 1) / 2][course.getWeekday() - 1] = course.getCourseName() + "\n" + course.getClassRoom();
        }
        adapter = new ScheduleGridAdapter(this, scheduleList);
        adapter.setContent(contents, 6, 7);
        courceDetail.setAdapter(adapter);
    }

    /**
     * 初始化从教务系统爬来的Schedule数据 存到 DB中
     *
     * @param JsonObjs
     */
    private void initScheduleDataFromEdu(String JsonObjs) {
        scheduleList = JSON.parseArray(JsonObjs, Course.class);
        MyDB db = new MyDB(this);
        for (int i = 0; i < scheduleList.size(); i++) {
            Course course = scheduleList.get(i);
            db.handSingleReadSchedule(course);
        }
        initScheduleDataFromDB();
//        adapter.notifyDataSetChanged();
    }

    private ArrayList<Course> getScheduleList(String responseHTML) {
        // 获取schedule的HTML
        String scheduleHTML = findScheduleHtml(responseHTML);
        ArrayList<Course> courses = new ArrayList<>();
        int count = 0;

        // 按上课时间分隔
        String[] rows = scheduleHTML.split("</tr><tr>");
        for (int i = 2; i < rows.length; i += 2) {
            String r = rows[i];
            // 按星期几分隔
            String[] cols = r.split("</td><td([\\S\\s]*?)>");

            int j = 1;
            if (i == 2 || i == 6 || i == 10) {
                j = 2;
            }

            int x = 1;
            for (; j < cols.length; x++, j++) {
                String c = cols[j];
                String[] info = c.split("<br>");
                if (TextUtils.isEmpty(info[0].trim())) {
                    // 这里会屏蔽掉一些含有空格的课程
                    continue;
                }
                String[] tem = new String[4];
                int t = 0;

                for (int k = 0; k < info.length; k++) {
                    String item = info[k].trim();
                    if (item.equals("")) {
                        // 处理同一时间不同周数的课程
                        t = 0;
                        tem = new String[4];
                        continue;
                    }
                    tem[t++] = item;
                    if (t == 4) {
                        courses.add(new Course(tem, i - 1, x, count++));
                    }
                }
            }
        }
        return courses;
    }

    /**
     * 将接收到的HTML代码查找出课程表相关的HTML代码后返回
     *
     * @param responseHTML
     * @return
     */
    private String findScheduleHtml(String responseHTML) {
        String scheduleHtml = "";
        String tar = "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">";
        String pattern = "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">([\\S\\s]+?)</table>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(responseHTML);
        if (m.find()) {
            scheduleHtml = m.group(0);
            scheduleHtml = scheduleHtml.substring(scheduleHtml.indexOf(tar) + tar.length(), scheduleHtml.lastIndexOf("</table>")).trim();
        } else {
            printLog("findScheduleHtml fail");
        }
        return scheduleHtml;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
