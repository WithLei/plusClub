package com.android.renly.plusclub.Fragment;

import android.content.Context;

import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;

public class HomeFragment extends BaseFragment {
    @Override
    public int getLayoutid() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Context content) {
        printLog("onCreate HomeFragment");
    }
}
