package com.tck.daole.activity;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.tck.daole.R;
import com.tck.daole.adapter.ShopAppraiseAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 评论页
 */
public class AppraiseActivity extends BaseActivity {
    private LinearLayout ll_back;

    private RadioButton rb_appraise_haoping, rb_appraise_zhongping, rb_appraise_chaping;
    private NiceRecyclerView rv_appraise;
    private List<Map<Object, String>> appraise_list = new ArrayList<>();
    private ShopAppraiseAdapter adapter;

    public String projectIdOid = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_appraise_two);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        rb_appraise_haoping = findView(R.id.rb_appraise_haoping);
        rb_appraise_zhongping = findView(R.id.rb_appraise_zhongping);
        rb_appraise_chaping = findView(R.id.rb_appraise_chaping);
        rv_appraise = findView(R.id.rv_appraise);
        ll_back = findView(R.id.ll_back);

    }

    @Override
    protected void initListener() {

        rb_appraise_haoping.setOnClickListener(this);
        rb_appraise_zhongping.setOnClickListener(this);
        rb_appraise_chaping.setOnClickListener(this);
        ll_back.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        projectIdOid = "1";
        goodevaluate(projectIdOid);

        rb_appraise_haoping.setChecked(true);
        ShopData("好评");
        adapter = new ShopAppraiseAdapter(appraise_list, this);
        rv_appraise.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rb_appraise_haoping:
                goodevaluate(projectIdOid);

                break;
            case R.id.rb_appraise_zhongping:
                middleevaluate(projectIdOid);
                break;
            case R.id.rb_appraise_chaping:
                badevaluate(projectIdOid);
                break;


        }
    }


    private void ShopData(String str) {
        appraise_list.clear();
        for (int i = 0; i < 1; i++) {
            Map<Object, String> map3 = new HashMap<>();
            map3.put("name", str);
            map3.put("state", "1");

            Map<Object, String> map4 = new HashMap<>();
            map4.put("name", str);
            map4.put("state", "1");
            appraise_list.add(map3);
            appraise_list.add(map4);
        }
    }


    //好评
    public void goodevaluate(String projectIdOid) {
        Map<String, String> map = new HashMap<>();
        map.put("projectIdOid", projectIdOid);

        HttpUtils.doPost(UriUtil.appraise_goodevaluate, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("======", response.body().string());
            }
        });
    }

    //中评
    public void middleevaluate(String projectIdOid) {
        Map<String, String> map = new HashMap<>();
        map.put("projectIdOid", projectIdOid);

        HttpUtils.doPost(UriUtil.appraise_middleevaluate, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("======", response.body().string());
            }
        });
    }


    //差评
    public void badevaluate(String projectIdOid) {
        Map<String, String> map = new HashMap<>();
        map.put("projectIdOid", projectIdOid);

        HttpUtils.doPost(UriUtil.appraise_badevaluate, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("======", response.body().string());
            }
        });
    }


}
