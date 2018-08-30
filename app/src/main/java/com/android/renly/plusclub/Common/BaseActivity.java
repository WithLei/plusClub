package com.android.renly.plusclub.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.RandomAccessFile;

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
        //设置数据
        initData();
        //初始化控件
        initView();

    }
    public abstract int getLayoutID();

    public abstract void initView();

    public abstract void initData();

    public void ToastLong(String msg){
        if(toast == null){
            toast = Toast.makeText(this,"",Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }

    public void ToastShort(String msg){
        if(toast == null){
            toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
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

    /**
     * Log输出
     * error
     * Filter:print
     * @param str
     */
    public void printLog(String str){
        Log.e("print",str);
    }

    /**
     * 打印str到手机src内存中
     * @param src 地址
     * @param str 打印内容
     */
    public void writeData(String src,String str) {
        try {
            File file = new File(src);
            if (!file.exists()) {
                printLog("Create the file:" + src);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
//            raf.seek(file.length());
            raf.write(str.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    /**
     * 打开targetActivity
     * @param targetActivity
     */
    public void gotoActivity(Class targetActivity){
        startActivity(new Intent(this,targetActivity));
    }
}
