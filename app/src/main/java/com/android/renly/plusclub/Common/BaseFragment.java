package com.android.renly.plusclub.Common;


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
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


public abstract class BaseFragment extends Fragment {
    private View mContentView;
    private Context mContent;
    private Unbinder unbinder;
    private boolean isViewCreated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutid(),container,false);
        mContent = getContext();
        unbinder = ButterKnife.bind(mContentView);
        isViewCreated = true;
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(mContent);
    }

    // 实现懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated){
            initView();
        }
    }

    public Activity getmActivity(){
        if (mContent == null)
            mContent = getContext();
        return (Activity)mContent;
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

    public void ToastLong(String msg){
        MyToast.showText(getContext(),msg, Toast.LENGTH_LONG);
    }

    public void ToastShort(String msg){
        MyToast.showText(getContext(),msg,Toast.LENGTH_SHORT);
    }

    public void ToastNetWorkError(){
        ToastShort("网络出状况咯ヽ(#`Д´)ﾉ");
    }
    public void ToastNetWorkError(Exception e) {
        ToastShort("网络出状况咯ヽ(#`Д´)ﾉ");
        Log.e("print", "ToastNetWorkError: " + e.getMessage());
    }

    public void ToastProgramError(){
        ToastShort("程序猿还在努力开发中 ♪(´∇`*)");
    }

    /**
     * Log输出
     * error
     * Filter:print
     * @param str
     */
    public void printLog(String str){
        Log.e("print",str);
    }

    public abstract void ScrollToTop();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
