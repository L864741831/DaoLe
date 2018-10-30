package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.adapter.TakeoutAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.view.NiceRecyclerView;
import com.tck.daole.view.SetBanner;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 花之语页面
 */
public class FlowerActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private Banner banner_takeout;
    private NiceRecyclerView rv_takeout;
    private List<Map<String, Object>> list = new ArrayList<>();
    private TakeoutAdapter adapter;
    private RadioButton rb_zongHe, rb_xiaoLiang, rb_juLi, rb_pingFen;

    //flower    leisure     motion      life        fruite
    private String type = "flower";
    //请求接口地址数组
    private String[] uri = {};

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_flower);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            type = intent.getStringExtra("type");
        }

        tv_left = findView(R.id.tv_left);
        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        ll_left = findView(R.id.ll_left);
        banner_takeout = findView(R.id.banner_takeout);
        rv_takeout = findView(R.id.rv_takeout);
        rb_zongHe = findView(R.id.rb_zongHe);
        rb_xiaoLiang = findView(R.id.rb_xiaoLiang);
        rb_juLi = findView(R.id.rb_juLi);
        rb_pingFen = findView(R.id.rb_pingFen);
    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
        rb_zongHe.setOnClickListener(this);
        rb_xiaoLiang.setOnClickListener(this);
        rb_juLi.setOnClickListener(this);
        rb_pingFen.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_left.setText("首页");
        tv_right.setVisibility(View.GONE);
        rb_zongHe.setChecked(true);

        //flower    leisure     motion      life        fruite

        if(type.equals("flower")){
            tv_title.setText("花之语");
        }
        if(type.equals("leisure")){
            tv_title.setText("休闲娱乐");
        }
        if(type.equals("motion")){
            tv_title.setText("运动健康");
        }
        if(type.equals("life")){
            tv_title.setText("生活服务");
        }
        if(type.equals("fruite")){
            tv_title.setText("生鲜果蔬");
        }

        //banner数据
        List<String> listBanner = new ArrayList<>();
        listBanner.add("http://pic.qiantucdn.com/58pic/26/65/67/27w58PIC5HU_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/18/10/15/30Q58PICemX_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/18/10/15/60w58PICZUu_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/11/89/15/97Y58PICV3M.jpg");
        SetBanner.startBanner(FlowerActivity.this, banner_takeout, listBanner);

        ShopData("综合排序");
        adapter = new TakeoutAdapter(list, FlowerActivity.this);
        rv_takeout.setAdapter(adapter);
        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                startActivity(new Intent(FlowerActivity.this, ShopActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.rb_zongHe://综合排序
                ShopData("综合排序");
                adapter.notifyDataSetChanged();
                break;
            case R.id.rb_xiaoLiang://销量最高
                ShopData("销量最高");
                adapter.notifyDataSetChanged();
                break;
            case R.id.rb_juLi://距离最近
                ShopData("距离最近");
                adapter.notifyDataSetChanged();
                break;
            case R.id.rb_pingFen://评分最好
                ShopData("评分最好");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("TuPian", "1");
            map1.put("WaiMai", true);
            map1.put("name", str);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("TuPian", "2");
            map2.put("WaiMai", true);
            map2.put("name", str);
            list.add(map1);
            list.add(map2);
        }
    }
}
