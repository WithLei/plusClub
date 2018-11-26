package com.android.renly.plusclub.Activity;

import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.widget.MyCircleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ThemeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.ll_theme_start_time)
    View startView;
    @BindView(R.id.ll_theme_end_time)
    View endView;
    @BindView(R.id.ll_theme_night_views)
    View nightViews;

    private Unbinder unbinder;
    public static final int requestCode = 32;
    public static final int THEME_DEFAULT = R.style.AppTheme;
    public static final int THEME_NIGHT = 1;

    @BindView(R.id.gv_theme_commons_colors)
    GridView gridView;
    @BindView(R.id.cb_theme_auto_dark_mode)
    CheckBox cbThemeAutoDarkMode;
    @BindView(R.id.tv_theme_start_time_text)
    TextView tvThemeStartTimeText;
    @BindView(R.id.tv_theme_end_time_text)
    TextView tvThemeEndTimeText;

    private int[] colors = new int[]{
            0x7bb736, 0x1e1e1e, 0xf44836, 0xf2821e, 0xd12121, 0x16c24b,
            0x16a8c2, 0x2b86e3, 0x3f51b5, 0x9c27b0, 0xcc268f, 0x39c5bb
    };

    private int[] colorsDark = new int[]{
            0x7bb736, 0x141414, 0xf44836, 0xf2821e, 0xd12121, 0x16c24b,
            0x16a8c2, 0x2b86e3, 0x3f51b5, 0x9c27b0, 0xcc268f, 0x39c5bb
    };

    private int[] themeIds = new int[]{
            R.style.AppTheme, THEME_NIGHT, R.style.AppTheme_2,
            R.style.AppTheme_3, R.style.AppTheme_4, R.style.AppTheme_5,
            R.style.AppTheme_6, R.style.AppTheme_7, R.style.AppTheme_8,
            R.style.AppTheme_9, R.style.AppTheme_10, R.style.AppTheme_11,
    };

    private String[] names = new String[]{
            "原谅", "黑色", "橘红", "橘黄", "红色", "翠绿",
            "青色", "天蓝", "蓝色", "紫色", "紫红", "初音"
    };


    private int currentSelect = 0;
    private int currentTheme = THEME_DEFAULT;

    private ColorAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initSlidr();
        initToolBar(true, "主题设置");
        findViewById(R.id.iv_toolbar_back).setOnClickListener(view -> finish());
        addToolbarMenu(R.drawable.ic_done_24dp).setOnClickListener(v -> {
            onChooseTheme();
        });
        adapter = new ColorAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        currentTheme = App.getCustomTheme();
        currentSelect = getSelect();

        if (currentSelect == THEME_NIGHT) {
            nightViews.setVisibility(View.GONE);
        } else {
            nightViews.setVisibility(View.VISIBLE);
        }

        tvThemeStartTimeText.setText(App.getDarkModeTime()[0] + ":00");
        tvThemeEndTimeText.setText(App.getDarkModeTime()[1] + ":00");

//        auto.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            App.setAutoDarkMode(this, isChecked);
//        });

        startView.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                tvThemeStartTimeText.setText(hourOfDay + ":00");
                App.setDarkModeTime( true, hourOfDay);
                Log.d("==", hourOfDay + "");
            }, App.getDarkModeTime()[0], 0, true).show();
        });

        endView.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                tvThemeEndTimeText.setText(hourOfDay + ":00");
                Log.d("==", hourOfDay + "");
                App.setDarkModeTime(false, hourOfDay);
            }, App.getDarkModeTime()[1], 0, true).show();
        });
    }

    /**
     * 点击确认主题
     */
    private void onChooseTheme() {
        boolean isChange = false;
        int curr = AppCompatDelegate.getDefaultNightMode();
        int to = curr;

        // 选择的主题与之前的主题不同
        if (App.getCustomTheme() != themeIds[currentSelect]) {
            App.setCustomTheme(themeIds[currentSelect]);
            isChange = true;
            if (themeIds[currentSelect] == THEME_NIGHT) {
                // 选择的是夜间模式
                to = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                to = AppCompatDelegate.MODE_NIGHT_NO;
            }
        }

//        if (themeIds[currentSelect] != THEME_NIGHT) {
//            //黑主题 自动主题无效
//            if (App.isAutoDarkMode(this)) {
//                int[] time = App.getDarkModeTime(this);
//                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//                if ((hour >= time[0] || hour < time[1])) {
//                    //自动切换
//                    to = AppCompatDelegate.MODE_NIGHT_YES;
//                } else {
//                    to = AppCompatDelegate.MODE_NIGHT_NO;
//                }
//            } else {
//                to = AppCompatDelegate.MODE_NIGHT_NO;
//            }
//        }


        if (curr != to) {
            AppCompatDelegate.setDefaultNightMode(to);
            isChange = true;
        }

        if (isChange) {
            ToastShort("已更改主题");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);

        if (savedInstanceState != null) {
            printLog("ThemeActivity savedInstanceState:" + savedInstanceState.getInt("position", 0));
            currentSelect = savedInstanceState.getInt("position", 0);
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (currentSelect == position) return;
        currentSelect = position;
        adapter.notifyDataSetChanged();
        changeTheme(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentSelect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    public int getSelect() {
        for (int i = 0; i < themeIds.length; i++) {
            if (currentTheme == themeIds[i]) {
                return i;
            }
        }

        return 0;
    }

    private void changeTheme(int position) {
        View toolbar = findViewById(R.id.myToolBar);
        toolbar.setBackgroundColor(0xff000000 | colors[position]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0xff000000 | colorsDark[position]);
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }

        if (themeIds[position] == THEME_NIGHT) {
            nightViews.setVisibility(View.GONE);
            //window背景颜色
            getWindow().setBackgroundDrawable(new ColorDrawable(0xff333333));
        } else {
            getWindow().setBackgroundDrawable(new ColorDrawable(0xfff5f5f5));
            nightViews.setVisibility(View.VISIBLE);
        }
    }

    private class ColorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return colors[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_color, null);
            MyCircleView circleView = convertView.findViewById(R.id.color);
            circleView.setColor(colors[position]);
            circleView.setSelect(position == currentSelect);
            ((TextView) convertView.findViewById(R.id.name)).setText(names[position]);
            return convertView;
        }
    }
}
