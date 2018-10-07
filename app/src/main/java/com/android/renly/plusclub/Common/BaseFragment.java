package com.android.renly.plusclub.Common;


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

import com.android.renly.plusclub.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    private View mContentView;
    private Context mContent;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutid(),container,false);
        mContent = getContext();
        unbinder = ButterKnife.bind(mContentView);
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initData(mContent);
        super.onViewCreated(view, savedInstanceState);
    }

    public abstract int getLayoutid();

    //初始化界面的数据
    protected abstract void initData(Context content);

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
