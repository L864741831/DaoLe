package com.tck.daole.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MUIToast;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.UriUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class PurseActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private TextView tv_right,tv_balance;   // 交易明细，账户余额

    private LinearLayout ll_huaFei, ll_liuLiang, ll_youXi, ll_jiaYou,
            ll_shuiFei, ll_dianFei, ll_ranQi, ll_xiaoYuan, ll_gongJiao;
//    private String sp_token = "";
    private String balance; //账户余额


    Handler hd;



    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_purse);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();

        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.Balance:

                        //toast(balance);

                        tv_balance.setText(balance);
                        break;

                }
            }
        };

    }

    public void findViewId(){
        ll_huaFei = (LinearLayout) findViewById(R.id.ll_huaFei);
        ll_liuLiang = (LinearLayout) findViewById(R.id.ll_liuLiang);
        ll_youXi = (LinearLayout) findViewById(R.id.ll_youXi);
        ll_jiaYou = (LinearLayout) findViewById(R.id.ll_jiaYou);
        ll_shuiFei = (LinearLayout) findViewById(R.id.ll_shuiFei);
        ll_dianFei = (LinearLayout) findViewById(R.id.ll_dianFei);
        ll_ranQi = (LinearLayout) findViewById(R.id.ll_ranQi);
        ll_xiaoYuan = (LinearLayout) findViewById(R.id.ll_xiaoYuan);
        ll_gongJiao = (LinearLayout) findViewById(R.id.ll_gongJiao);
        ll_back = findView(R.id.ll_back);
        tv_right = findView(R.id.tv_right);
        tv_balance = findView(R.id.tv_balance);
    }

    @Override
    protected void initListener() {
        ll_huaFei.setOnClickListener(this);
        ll_liuLiang.setOnClickListener(this);
        ll_youXi.setOnClickListener(this);
        ll_jiaYou.setOnClickListener(this);
        ll_shuiFei.setOnClickListener(this);
        ll_dianFei.setOnClickListener(this);
        ll_ranQi.setOnClickListener(this);
        ll_xiaoYuan.setOnClickListener(this);
        ll_gongJiao.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(PurseActivity.this, "token", "").toString();
        seePurseBalance(App.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_huaFei://话费充值
                startActivity(new Intent(PurseActivity.this, PhoneChargeActivity.class));
                break;
            case R.id.ll_liuLiang://流量充值
                startActivity(new Intent(PurseActivity.this, LiuliangChargeActivity.class));
                break;
            case R.id.ll_youXi://游戏卡充值
                startActivity(new Intent(PurseActivity.this, GamesSelectActivity.class));
                break;
            case R.id.ll_jiaYou://加油卡充值
                startActivity(new Intent(PurseActivity.this, FuelChargeActivity.class));
                break;
            case R.id.ll_shuiFei://水费
                startActivity(new Intent(PurseActivity.this, WaterChargeActivity.class));
                break;
            case R.id.ll_dianFei://电费
                startActivity(new Intent(PurseActivity.this, ElectricChargeActivity.class));
                break;
            case R.id.ll_ranQi://燃气费
                startActivity(new Intent(PurseActivity.this, GasChargeActivity.class));
                break;
            case R.id.ll_xiaoYuan://校园卡充值
                MUIToast.show(PurseActivity.this,"功能开发中，暂未开放");
                break;
            case R.id.ll_gongJiao://公交卡充值
                MUIToast.show(PurseActivity.this,"功能开发中，暂未开放");
                break;
            case R.id.tv_right:
                startActivity(new Intent(PurseActivity.this, RecordActivity.class));//跳转交易明细
                break;

        }
    }




   //查询余额
    public void seePurseBalance(final String token){
  /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String,String> map = new HashMap<>();
        map.put("token",token);

        HttpUtils.doPost(UriUtil.see_balance, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("=====", response.body().string());

                    Bean.UserModels data = new Gson().fromJson(response.body().string(), Bean.UserModels.class);
                balance = data.getBalance();

/*                Message msg = new Message();
                msg.what = Constant.Balance;
                hd.sendMessage(msg);*/



                Message msg = new Message();
                msg.what = Constant.Balance;
                hd.sendMessage(msg);

            }
        });
    }













}
