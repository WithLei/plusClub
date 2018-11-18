package com.android.renly.plusclub.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.android.renly.plusclub.Api.Bean.Course;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScheduleDetailSetActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.et_setinfo)
    EditText etSetinfo;

    private Unbinder unbinder;
    private Course object;

    public static final int requestCode = 256;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_schedule_detail_set;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String JsonObj = intent.getStringExtra("JsonObj");
        object = JSON.parseObject(JsonObj,Course.class);
        switch (intent.getStringExtra("set")){
            case "courseName":
                initToolBar(true,"修改课名");
                etSetinfo.setText(object.getCourseName());
                break;
            case "class":
                initToolBar(true,"修改教室");
                etSetinfo.setText(object.getCourseName());

                break;
            case "weeks":
                initToolBar(true,"修改周数");
                etSetinfo.setText(object.getStartWeek() + "~" + object.getEndWeek());

                break;
            case "sdWeek":
                initToolBar(true,"修改单双周");
                etSetinfo.setText(object.printSD_week());

                break;
            case "js":
                initToolBar(true,"修改节数");
                etSetinfo.setText(object.getCourseTime());
                break;
            case "teacher":
                initToolBar(true,"修改老师");
                etSetinfo.setText(object.getTeacher());

                break;
        }
    }

    @Override
    protected void initView() {
        initSlidr();
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
