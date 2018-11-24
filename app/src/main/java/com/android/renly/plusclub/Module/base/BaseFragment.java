package com.android.renly.plusclub.Module.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


public abstract class BaseFragment extends Fragment {
    protected Context mContent;
    protected Unbinder unbinder;
    //缓存Fragment View
    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutid(),container,false);
            unbinder = ButterKnife.bind(this, mRootView);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    protected abstract void initInjector();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector();
        initData(mContent);
    }

    // 实现懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible() && mRootView != null){
            initView();
        }
    }

    public abstract int getLayoutid();

    // 初始化数据
    protected abstract void initData(Context content);
    // 初始化界面
    protected abstract void initView();

    /**
     * 打开targetActivity
     * @param targetActivity
     */
    public void gotoActivity(Class<?> targetActivity){
        startActivity(new Intent(getActivity(),targetActivity));
        getActivity().overridePendingTransition(R.anim.translate_in,R.anim.translate_out);

    }

    public abstract void ScrollToTop();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
