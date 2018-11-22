package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.renly.plusclub.Activity.EduLoginActivity;
import com.android.renly.plusclub.Activity.HomeActivity;
import com.android.renly.plusclub.Adapter.ScheduleGridAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Api.Bean.Course;
import com.android.renly.plusclub.Module.base.BaseFragment;
import com.android.renly.plusclub.DataBase.MyDB;
import com.android.renly.plusclub.R;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScheduleFragment extends BaseFragment {
    @BindView(R.id.iv_toolbar_menu)
    ImageView ivToolbarMenu;
    @BindView(R.id.courceDetail)
    GridView courceDetail;
    @BindView(R.id.switchWeek)
    NiceSpinner spinner;

    private String[][] contents;
    private ScheduleGridAdapter adapter;
    private int nowWeek;
    List<Course> scheduleList = new ArrayList<>();

    @Override
    public int getLayoutid() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData(Context content) {
        contents = new String[6][7];
        nowWeek = App.getScheduleNowWeek();
        if (nowWeek <= 0)
            App.setScheduleStartWeek(1);
        nowWeek = App.getScheduleNowWeek();
        printLog("ScheduleFragment_NowWeek:" + nowWeek);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        initSpinner();
        MyDB db = new MyDB(App.getContext());
        if (db.isScheduleExist())
            initScheduleDataFromDB();
    }

    public void doRefresh(){
        MyDB db = new MyDB(App.getContext());
        if (db.isScheduleExist())
            initScheduleDataFromDB();
    }

    /**
     * 初始化spinner
     */
    private void initSpinner() {
        List<String>dataset = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.scheduleWeek)));
        spinner.attachDataSource(dataset);
        spinner.setBackgroundResource(R.drawable.tv_round_border);
        spinner.setTextColor(getResources().getColor(R.color.white));
//        StateListDrawable drawable = new StateListDrawable();
//        drawable.addState(new int[]{android.R.attr.state_window_focused},getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
//        drawable.addState(new int[]{android.R.attr.state_focused},getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
        spinner.setSelectedIndex(nowWeek-1);
        spinner.setArrowTintColor(R.color.white);
        spinner.setArrowDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                nowWeek = pos+1;
                App.setScheduleStartWeek(nowWeek);
                initScheduleDataFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initScheduleDataFromDB() {
        MyDB db = new MyDB(mActivity);
        scheduleList = db.getSchedule();
        contents = new String[6][7];
        for (int x = 0; x < 6; x++)
            for (int y = 0; y < 7; y++)
                contents[x][y] = "";
        for (int i = 0; i < scheduleList.size(); i++) {
            Course course = scheduleList.get(i);
            if (course.getSd_week() == 1 && nowWeek%2 == 0){
                printLog("nowWeek=" + nowWeek);
                continue;
            }
            else if (course.getSd_week() == 2 && nowWeek%2 == 1){
                printLog("nowWeek=" + nowWeek);
                continue;
            }
            else
                contents[(course.getRows() - 1) / 2][course.getWeekday() - 1] = course.getCourseName() + "\n\n" + course.getClassRoom();
        }
        adapter = new ScheduleGridAdapter(mActivity, scheduleList);
        adapter.setContent(contents, 6, 7);
        courceDetail.setAdapter(adapter);
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    protected void initInjector() {

    }

    private HomeActivity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity)context;
    }

    @OnClick(R.id.iv_toolbar_menu)
    public void onViewClicked() {
        Intent intent = new Intent(mActivity, EduLoginActivity.class);
        startActivity(intent);
    }
}
