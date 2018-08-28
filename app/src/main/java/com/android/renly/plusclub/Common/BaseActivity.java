package com.android.renly.plusclub.Common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
    /***是否显示标题栏*/
    private  boolean isshowtitle = true;
    /***是否显示标题栏*/
    private  boolean isshowstate = true;
    /***封装toast对象**/
    private static Toast toast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isshowtitle){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if(isshowstate){
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);
        }
        setContentView(getLayoutID());
        //初始化控件
        initView();
        //设置数据
        initData();

    }
    public abstract void initData();

    public abstract void initView();

    public abstract int getLayoutID();

    public void ToastLong(Context context,String msg){
        if(toast == null){
            toast = Toast.makeText(context,"",Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }

    public void ToastShort(Context context,String msg){
        if(toast == null){
            toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    /**
     * 是否设置标题栏
     *
     * @return
     */
    public void setTitle(boolean ishow) {
        isshowtitle=ishow;
    }

    /**
     * 设置是否显示状态栏
     * @param ishow
     */
    public void setState(boolean ishow) {
        isshowstate=ishow;
    }
}
