package com.android.renly.plusclub.module.schedule.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.renly.plusclub.module.edu.login.EduLoginActivity;
import com.android.renly.plusclub.module.home.HomeActivity;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.injector.components.DaggerScheduleFragComponent;
import com.android.renly.plusclub.injector.modules.ScheduleFragModule;
import com.android.renly.plusclub.module.base.BaseFragment;
import com.android.renly.plusclub.R;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ScheduleFragment extends BaseFragment
        implements ScheduleFragView{
    @BindView(R.id.courceDetail)
    GridView courceDetail;
    @BindView(R.id.switchWeek)
    NiceSpinner spinner;

    @Inject
    protected ScheduleFragPresenter mPresenter;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData(Context content) {
        mPresenter.getData(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        initSpinner();
    }

    public void doRefresh() {
        mPresenter.getData(true);
    }

    /**
     * 初始化spinner
     */
    private void initSpinner() {
        List<String> dataset = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.scheduleWeek)));
        spinner.attachDataSource(dataset);
        spinner.setBackgroundResource(R.drawable.tv_round_border);
        spinner.setTextColor(getResources().getColor(R.color.white));
        spinner.setSelectedIndex(App.getScheduleNowWeek() - 1);
        spinner.setArrowTintColor(R.color.white);
        spinner.setArrowDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                App.setScheduleStartWeek(pos + 1);
                loadSchedule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    protected void initInjector() {
            DaggerScheduleFragComponent.builder()
                .applicationComponent(App.getAppComponent())
                .scheduleFragModule(new ScheduleFragModule(this))
                .build()
                .inject(this);
    }

    private HomeActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @OnClick(R.id.iv_toolbar_menu)
    public void onViewClicked() {
        Intent intent = new Intent(mActivity, EduLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadSchedule() {
        courceDetail.setAdapter(mPresenter.getAdapter(mActivity));
    }
}
