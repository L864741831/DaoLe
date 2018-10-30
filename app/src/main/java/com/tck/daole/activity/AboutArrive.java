package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;


/**
 * 关于到了
 */

public class AboutArrive extends BaseActivity {
    private LinearLayout ll_back;
    private EditText et_my_posted_zhuti,et_my_posted_neirong;
    private Button btn_my_posted_add;

    private String sp_token = "";

    public String add_type = "";
    public String int_add_type = "8";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_arrive);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }








}