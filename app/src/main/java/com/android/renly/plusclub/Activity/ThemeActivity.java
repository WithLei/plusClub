package com.android.renly.plusclub.Activity;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;


public class ThemeActivity extends BaseActivity implements AdapterView.OnClickListener {
    public static final int requestCode = 10;
    public static final int THEME_DEFAULT = R.style.AppTheme;
    public static final int THEME_NIGHT = 1;

    private int[] colors = new int[]{
            0xd12121, 0x1e1e1e, 0xf44836, 0xf2821e, 0x7bb736, 0x16c24b,
            0x16a8c2, 0x2b86e3, 0x3f51b5, 0x9c27b0, 0xcc268f, 0x39c5bb
    };

    private int[] colorsDark = new int[]{
            0xac1c1b, 0x141414, 0xf44836, 0xf2821e, 0x7bb736, 0x16c24b,
            0x16a8c2, 0x2b86e3, 0x3f51b5, 0x9c27b0, 0xcc268f, 0x39c5bb
    };

    private int[] themeIds = new int[]{
            R.style.AppTheme, THEME_NIGHT, R.style.AppTheme_2,
            R.style.AppTheme_3, R.style.AppTheme_4, R.style.AppTheme_5,
            R.style.AppTheme_6, R.style.AppTheme_7, R.style.AppTheme_8,
            R.style.AppTheme_9, R.style.AppTheme_10, R.style.AppTheme_11,
    };

    private String[] names = new String[]{
            "默认", "黑色", "橘红", "橘黄", "原谅", "翠绿",
            "青色", "天蓝", "蓝色", "紫色", "紫红", "初音"
    };


    private int currentSelect = 0;
    private int currentTheme = THEME_DEFAULT;

//    private ColorAdapter adapter;
    private View startView, endView, nightViews;
    private TextView startText, endText;
    private CheckBox auto;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View view) {

    }
}
