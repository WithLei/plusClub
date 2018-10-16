package com.android.renly.plusclub.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LabActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.lv_lab)
    ListView lvLab;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    private Unbinder unbinder;
    private final int[] icons = new int[]{
            R.drawable.ic_school_24dp,
    };

    private final String[] titles = new String[]{
            "教务系统登录",
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_lab;
    }

    @Override
    protected void initData() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> object = new HashMap<>();
            object.put("icon", icons[i]);
            object.put("title", titles[i]);
            list.add(object);
        }
        lvLab.setAdapter(new SimpleAdapter(this, list, R.layout.item_function, new String[]{"icon", "title"}, new int[]{R.id.icon, R.id.title}));
        lvLab.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {
        initSlidr();
        tvToolbarTitle.setText("实验室功能");
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                gotoActivity(EduLoginActivity.class);
                break;
        }
    }

    @OnClick(R.id.iv_toolbar_back)
    public void onViewClicked() {
        finishActivity();
    }
}
