package com.tck.daole.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.MUIToast;
import com.tck.daole.util.PayUtil;

import okhttp3.Call;

public class PayActivity extends BaseActivity {
    private Button ok;
    private View yueView;
    private RadioButton yueRadio;
    private View zhifubaoView;
    private RadioButton zhifubaoRadio;
    private View weixinView;
    private RadioButton weixinRadio;
    private TextView money;
    private View ll_left;

    private boolean isWeiXin = true;
    private double allMoney = 0d;
    private String ORDER_ID = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        allMoney =getIntent().getDoubleExtra("allMoney",0d);
        ORDER_ID=getIntent().getStringExtra("ORDER_ID");
        setContentView(R.layout.activity_pay);
        ok = (Button)findViewById(R.id.ok);
        yueView = findViewById(R.id.yueView);
        yueRadio = (RadioButton) findViewById(R.id.yueRadio);
        zhifubaoView = findViewById(R.id.zhifubaoView);
        zhifubaoRadio = (RadioButton) findViewById(R.id.zhifubaoRadio);
        weixinView =  findViewById(R.id.weixinView);
        weixinRadio = (RadioButton) findViewById(R.id.weixinRadio);
        money = (TextView) findViewById(R.id.money);
        money.setText(allMoney +"");

        ll_left = findViewById(R.id.ll_left);
    }

    @Override
    protected void initListener() {
        yueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yueRadio.setChecked(true);
                weixinRadio.setChecked(false);
                zhifubaoRadio.setChecked(false);
            }
        });

        weixinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yueRadio.setChecked(false);
                weixinRadio.setChecked(true);
                zhifubaoRadio.setChecked(false);
            }
        });

        zhifubaoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yueRadio.setChecked(false);
                weixinRadio.setChecked(false);
                zhifubaoRadio.setChecked(true);
            }
        });

        ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yueRadio.isChecked()) {
                    personalpay();
                }
                else if (zhifubaoRadio.isChecked()){
                    new PayUtil(PayActivity.this,httpInterface).ApliyPay(allMoney+"20180207143406","","","");
                }else{
                    MUIToast.show(PayActivity.this,"即将开放");
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void personalpay() {
        MUIToast.show(PayActivity.this,"即将开放");
        httpInterface.yuePayOrder(App.token,ORDER_ID,new MApiResultCallback(){//
            @Override
            public void onSuccess(String result) {
                Log.e("yuePayOrder-onSuccess",result);
                final Bean.State msg=new Gson().fromJson(result, Bean.State.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.status==1){
                            MUIToast.show(PayActivity.this,"付款成功!");
                            setResult(888);
                            finish();
                        }else{
                            MUIToast.show(PayActivity.this,msg.message);
                        }
                    }
                });
            }

            @Override
            public void onFail(String response) {
                Log.e("personalpay-onFail",response!=null?response:"错误信息为空");
            }

            @Override
            public void onTokenError(String response) {

            }

            @Override
            public void onError(Call call, Exception exception) {
                Log.e("personalpay-onError",exception.getMessage()+"");
            }
        });
    }
}
