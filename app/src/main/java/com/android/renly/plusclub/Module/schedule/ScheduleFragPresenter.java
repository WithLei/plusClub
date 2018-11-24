package com.android.renly.plusclub.Module.schedule;

import android.app.Activity;
import android.util.Log;

import com.android.renly.plusclub.Adapter.ScheduleGridAdapter;
import com.android.renly.plusclub.Api.Bean.Course;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.DataBase.MyDB;
import com.android.renly.plusclub.Module.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragPresenter implements BasePresenter {
    private final ScheduleFragment mView;

    private String[][] contents;
    private List<Course> scheduleList;
    private int nowWeek;

    public ScheduleFragPresenter(ScheduleFragment mView) {
        this.mView = mView;
    }

    @Override
    public void getData(boolean isRefresh) {
        contents = new String[6][7];
        scheduleList = new ArrayList<>();
        mView.loadSchedule();
    }

    @Override
    public void getMoreData() {

    }

    private void updateNowWeek() {
        if (App.getScheduleNowWeek() <= 0)
            App.setScheduleStartWeek(1);
        nowWeek = App.getScheduleNowWeek();
        Log.e("print","ScheduleFragment_NowWeek:" + nowWeek);
    }

    ScheduleGridAdapter getAdapter(Activity mActivity) {
        updateNowWeek();
        MyDB db = new MyDB(mActivity);
        if (!db.isScheduleExist())
            return new ScheduleGridAdapter(mActivity, scheduleList);
        scheduleList = db.getSchedule();
        contents = new String[6][7];
        for (int x = 0; x < 6; x++)
            for (int y = 0; y < 7; y++)
                contents[x][y] = "";
        for (int i = 0; i < scheduleList.size(); i++) {
            Course course = scheduleList.get(i);
            if (course.getSd_week() == 1 && nowWeek%2 == 0)
                continue;
            else if (course.getSd_week() == 2 && nowWeek%2 == 1)
                continue;
            else
                contents[(course.getRows() - 1) / 2][course.getWeekday() - 1] = course.getCourseName() + "\n\n" + course.getClassRoom();
        }
        ScheduleGridAdapter adapter = new ScheduleGridAdapter(mActivity, scheduleList);
        adapter.setContent(contents, 6, 7);
        return adapter;
    }

}
