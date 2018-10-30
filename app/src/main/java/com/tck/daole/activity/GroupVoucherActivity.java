package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tck.daole.R;
import com.tck.daole.adapter.GroupVoucherAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 团购单详情页
 */

public class GroupVoucherActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;

    private NiceRecyclerView rv_group_voucher;
    private List<Map<Object, String>> locationlist = new ArrayList<>();
    private GroupVoucherAdapter group_voucher_adapter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_voucher);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        findViewId();
        initListener();
        initData();

    }



    public void findViewId(){

        ll_back = findView(R.id.ll_back);

        rv_group_voucher = findView(R.id.rv_group_voucher);

    }

    @Override
    protected void initListener() {

        ll_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ShopData("宫保鸡丁");
        group_voucher_adapter = new GroupVoucherAdapter(locationlist, this);
        rv_group_voucher.setAdapter(group_voucher_adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void ShopData(String str) {
        locationlist.clear();
        for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap<>();
            map1.put("caiming", str);
            map1.put("fenshu", "(1份)");
            map1.put("jiage", "¥25");
            locationlist.add(map1);
        }
    }

}
