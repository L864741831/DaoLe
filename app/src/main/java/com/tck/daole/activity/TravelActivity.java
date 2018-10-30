package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;


/**
 * 大爱旅行
 */

public class TravelActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back,ll_forum,ll_booking,ll_luyougonglue,ll_zijiayouzhaoji,ll_All;   //论坛贴吧，大爱旅行,旅游攻略,自驾游召集,全球签证

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_travel);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        findViewId();
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        ll_forum = findView(R.id.ll_forum);
        ll_booking = findView(R.id.ll_booking);
        ll_luyougonglue = findView(R.id.ll_luyougonglue);
        ll_zijiayouzhaoji = findView(R.id.ll_zijiayouzhaoji);
        ll_All = findView(R.id.ll_All);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        ll_forum.setOnClickListener(this);
        ll_booking.setOnClickListener(this);
        ll_luyougonglue.setOnClickListener(this);
        ll_zijiayouzhaoji.setOnClickListener(this);
        ll_All.setOnClickListener(this);
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
            case R.id.ll_forum:
                //startActivity(new Intent(TravelActivity.this, ForumActivity.class));//跳转论坛贴吧页

                Intent intent5 = new
                        Intent(TravelActivity.this,ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent5.putExtra("PostedType","luntantieba");
                startActivity(intent5);
                break;
            case R.id.ll_booking:
                //startActivity(new Intent(TravelActivity.this, BookingActivity.class));//跳转景区订票

                Intent intent = new
                        Intent(TravelActivity.this,BookingActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("travelType","ll_booking");
                startActivity(intent);

                break;
            case R.id.ll_luyougonglue:
                Intent intent6 = new
                        Intent(TravelActivity.this,ForumActivity.class);    //跳转旅游攻略
                //在Intent对象当中添加一个键值对
                intent6.putExtra("PostedType","lvyougonglue");
                startActivity(intent6);
                break;
            case R.id.ll_zijiayouzhaoji:
                Intent intent7 = new
                        Intent(TravelActivity.this,ForumActivity.class);    //跳转自驾游召集
                //在Intent对象当中添加一个键值对
                intent7.putExtra("PostedType","zijiayouzhaoji");
                startActivity(intent7);
                break;
            case R.id.ll_All:
                //startActivity(new Intent(TravelActivity.this, BookingActivity.class));//跳转全球签证办理

                Intent intent2 = new
                        Intent(TravelActivity.this,BookingActivity.class);
                //在Intent对象当中添加一个键值对
                intent2.putExtra("travelType","ll_All");
                startActivity(intent2);

                break;





        }
    }
}
