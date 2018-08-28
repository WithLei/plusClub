package com.android.renly.plusclub.Common;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    private View mContentView;
    private Context mContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutid(),container,false);
        mContent = getContext();
//        ButterKnife.bind(mContentView);
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initData(mContent);
        super.onViewCreated(view, savedInstanceState);
    }

    //初始化界面的数据
    protected abstract void initData(Context content);

    public abstract int getLayoutid();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
