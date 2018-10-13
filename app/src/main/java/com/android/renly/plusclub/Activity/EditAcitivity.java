package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.widget.EditText;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAcitivity extends BaseActivity {
    @BindView(R.id.editor)
    EditText editor;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initEditor();
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
        ButterKnife.bind(this);
    }
}