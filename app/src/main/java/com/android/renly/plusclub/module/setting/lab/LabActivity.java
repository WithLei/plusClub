package com.android.renly.plusclub.module.setting.lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.module.edu.login.EduLoginActivity;
import com.android.renly.plusclub.module.webview.WebViewActivity;
import com.android.renly.plusclub.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LabActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.lv_lab)
    ListView lvLab;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                ToastUtils.ToastShort("由于技术升级，爬虫不可用，此功能暂时下线");
                // TODO:暂时下线功能
//                gotoActivity(EduLoginActivity.class);
                break;
        }
    }

    @OnClick(R.id.iv_toolbar_back)
    public void onViewClicked() {
        finishActivity();
    }
}
