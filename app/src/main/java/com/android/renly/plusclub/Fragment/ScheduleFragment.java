package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScheduleFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.iv_toolbar_menu)
    ImageView ivToolbarMenu;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData(Context content) {
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean isChecked = false;

    @OnClick(R.id.iv_toolbar_menu)
    public void onViewClicked() {
        if (isChecked){
            ivToolbarMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_24dp));
            isChecked = false;
        }else{
            ivToolbarMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_24dp));
            isChecked = true;
        }
    }
}
