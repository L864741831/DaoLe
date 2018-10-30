package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.tck.daole.R;
import com.tck.daole.adapter.CollectionAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    RadioButton shoucang_dianpu,shoucang_shangpin;
    NiceRecyclerView rv_shoucang;
    private List<Map<Object, String>> list = new ArrayList<>();
    private CollectionAdapter adapter;

    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        shoucang_dianpu = findView(R.id.shoucang_dianpu);
        shoucang_shangpin = findView(R.id.shoucang_shangpin);
        rv_shoucang =  findView(R.id.rv_shoucang);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        shoucang_dianpu.setOnClickListener(this);
        shoucang_shangpin.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        shoucang_dianpu.setChecked(true);
        shopDianpuData("店铺名");
       adapter = new CollectionAdapter(list,CollectionActivity.this);


      rv_shoucang.setAdapter(adapter);

        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            public void onItemClickListener(int position,String state) {

                switch (state) {
                    case "1":
                        startActivity(new Intent(CollectionActivity.this, ShopActivity.class));//跳转店铺
                        break;
                    case "2":
                        startActivity(new Intent(CollectionActivity.this, MerchandiseActivity.class));//跳转商品
                        break;
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.shoucang_dianpu://切换店铺
                shopDianpuData("店铺名");
                adapter.notifyDataSetChanged();
                break;
            case R.id.shoucang_shangpin://切换商品
                ShopShangpinData("商品名");
                adapter.notifyDataSetChanged();
                break;

        }
    }


    private void shopDianpuData(String str) {
        list.clear();
        for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap();
            map1.put("state", "1");
            map1.put("name", str);

            list.add(map1);
        }
    }

    private void ShopShangpinData(String str) {
        list.clear();
        for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap();
            map1.put("state", "2");
            map1.put("name", str);

            list.add(map1);
        }
    }
}
