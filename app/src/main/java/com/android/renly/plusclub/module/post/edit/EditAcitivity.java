package com.android.renly.plusclub.module.post.edit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.R;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class EditAcitivity extends BaseActivity {
    @BindView(R.id.editor)
    EditText editor;
    @BindView(R.id.et_post_title)
    EditText etPostTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.spinner)
    NiceSpinner spinner;

    private String[] categories = new String[]{
            "daily","code","qa","suggests","feedback","transaction","activity",
    };
    String currentCategory = categories[0];

    public static final int requestCode = 110;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initData() {
        initSpinner();
    }

    private void initSpinner() {
        List<String> dataset = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.post_categories)));
        spinner.attachDataSource(dataset);
        spinner.setBackgroundColor(getResources().getColor(R.color.bg_secondary));
        spinner.setTextColor(getResources().getColor(R.color.text_color_sec));
        spinner.setArrowDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                currentCategory = categories[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Intent intent = getIntent();
        String temp = intent.getExtras().getString("category");
        if (temp.equals("E-M-P-T-Y"))
            return;
        for (int pos = 0;pos < categories.length;pos++)
            if (temp.equals(categories[pos])){
                currentCategory = temp;
                spinner.setSelectedIndex(pos);
                break;
            }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        initToolBar(true, "发帖子");
        ivToolbarBack.setOnClickListener(view -> {
            if (!etPostTitle.getText().toString().isEmpty() || !editor.getText().toString().isEmpty())
                new AlertDialog.Builder(EditAcitivity.this)
                        .setMessage("程序猿还没开发出保存的功能喔，确定返回吗|ω・）")
                        .setCancelable(true)
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            setResult(RESULT_OK);
                            finishActivity();
                        })
                        .setNegativeButton("取消", (dialogInterface, i) -> {
                        })
                        .create()
                        .show();
            else{
                setResult(RESULT_OK);
                finishActivity();
            }
        });
        addToolbarMenu(R.drawable.ic_check_black_24dp).setOnClickListener(view -> {
            if (checkInput())
                doPost(etPostTitle.getText().toString(), editor.getText().toString());
        });
        initEditor();
        initTextWatcher();
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> showSoftInput());
    }

    private void initTextWatcher() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etPostTitle.getText().toString())) {
                    etPostTitle.setError("标题不能为空");
                }
            }
        };
        editor.addTextChangedListener(watcher);
    }

    /**
     * 检查输入是否正确
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(etPostTitle.getText().toString())) {
            etPostTitle.setError("标题不能为空");
            return false;
        }
        if (TextUtils.isEmpty(editor.getText().toString())) {
            ToastShort("帖子内容不能为空");
            return false;
        }
        return true;
    }

    /**
     * 发送帖子
     */
    @SuppressLint("CheckResult")
    private void doPost(String title, String content) {
        if (currentCategory.equals(categories[6]) && !App.getRole().equals("admin"))
            return;
        RetrofitService.doPost(title, content, currentCategory)
                .subscribe(responseBody -> {
                    String response = responseBody.string();
                    if (!response.contains("code")) {
                        ToastNetWorkError();
                        return;
                    }
                    JSONObject jsonObject = JSON.parseObject(response);
                    if (jsonObject.getInteger("code") == 50011) {
                        getNewToken(title, content);
                    } else if (jsonObject.getInteger("code") != 20000) {
                        ToastShort("服务器出状况惹，再试试( • ̀ω•́ )✧");
                        printLog("getInfoError" + response);
                    } else {
                        setResult(RESULT_OK);
                        ToastShort("发布成功");
                        finishActivity();
                    }
                }, throwable -> {
                    printLog("EditActivity doPost onError:" + throwable.getMessage());
                    ToastNetWorkError();
                });
    }

    /**
     * 获取新的Token
     */
    @SuppressLint("CheckResult")
    private void getNewToken(String title, String content) {
        RetrofitService.getNewToken()
                .subscribe(s -> doPost(title,content));
    }

    /**
     * 初始化输入框
     */
    private void initEditor() {
        editor.setSelection(editor.getText().length());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (etPostTitle.getText().toString().isEmpty() && editor.getText().toString().isEmpty()){
            setResult(RESULT_OK);
            super.onBackPressed();
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("程序猿还没开发保存的功能喔，确定返回吗|ω・）")
                    .setCancelable(true)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        setResult(RESULT_OK);
                        finishActivity();
                    })
                    .setNegativeButton("取消", (dialogInterface, i) -> {
                    })
                    .create()
                    .show();
        }
    }
}
