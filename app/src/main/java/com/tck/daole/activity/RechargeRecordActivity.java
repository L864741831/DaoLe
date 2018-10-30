package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.adapter.RecordAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RechargeRecordActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;

    private NiceRecyclerView rv_record;
    private List<Map<String, Object>> list = new ArrayList<>();
    private TextView tv_title;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_record);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
        tv_title.setText("充值记录");
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        rv_record = findView(R.id.rv_record);
        tv_title = findView(R.id.tv_title);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", "2017-11-1" + i + "  09:00:00");
            list.add(map);
        }
        RecordAdapter adapter = new RecordAdapter(list, this);
        rv_record.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //startActivity(new Intent(SystemMsgActivity.this,PostedParticularsActivity.class));//跳转发布详情
            }
        });
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
