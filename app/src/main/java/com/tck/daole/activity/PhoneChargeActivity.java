package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.ChargeListAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.PayUtil;
import com.tck.daole.util.StringUtil;
import com.tck.daole.view.NiceRecyclerView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 话费充值
 */
public class PhoneChargeActivity extends BaseActivity {
    private LinearLayout ll_back;
    private EditText ed_phone;//,ed_charge_money
    private Button btn_submit;

    private NiceRecyclerView items;
    private ChargeListAdapter adapter;
    private List<Map<String,Object>> data = new ArrayList<>();
    private String itemId="";
    private String rechargeAmount="";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_phone_charge);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.store_View).setVisibility(View.GONE);
        }
        findView();
    }

    public void findView(){
        items = (NiceRecyclerView)findViewById(R.id.items);
        adapter =new ChargeListAdapter(R.layout.charge_item_bg, data);
        items.setAdapter(adapter);
        ll_back = findView(R.id.ll_back);
        ed_phone = findView(R.id.ed_phone);
//        ed_charge_money = findView(R.id.ed_charge_money);
        btn_submit = findView(R.id.btn_forget_modify);
    }
    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        adapter.setOnItemClickListener(new ChargeListAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemId=data.get(position).get("itemId")+"";
                rechargeAmount=data.get(position).get("rechargeAmount")+"";
            }
        });
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (!StringUtil.isSpace(newText)&&newText.length()==11) {
                    phone1_charge(newText);
                }
                else {
                    toast("请输入您的手机号");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        ed_phone.setText(App.phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_forget_modify:
                String modify_phone = ed_phone.getText().toString().trim();
//                String charge_money = ed_charge_money.getText().toString().trim();
                if (StringUtil.isSpace(itemId)){
                    toast("请选择充值项目");
                    return;
                }
//                if (StringUtil.isSpace(modify_phone)){
//                    toast("请输入手机号");
//                    return;
//                }
//                if (!StringUtil.isPhone(modify_phone)){
//                    toast("请输入正确的手机号");
//                    return;
//                }
//                if (StringUtil.isSpace(charge_money)){
//                    toast("请输入充值金额");
//                    return;
//                }

                @SuppressLint("SimpleDateFormat")
                String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                new PayUtil(PhoneChargeActivity.this,httpInterface).ApliyPay(no,modify_phone,"","0");
                break;
        }
    }

    //确认充值按钮
    public void phone2_charge() {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.phone2_charge(ed_phone.getText()+"",rechargeAmount,itemId, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            Log.e("onSuccess", result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject=new JSONObject(result);
                                        int status=jsonObject.optInt("status");
//                                        String message=jsonObject.optString("message");
                                        String sub_code=jsonObject.optJSONObject("model").optString("error_response");
                                        if (status==1 && StringUtil.isSpace(sub_code)){
                                            toast("充值成功！");
                                            finish();
                                        }else {
                                            toast("充值失败，请重试");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {

                            //toast(response+"");
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            toast(R.string.system_busy);
        }
    }

    //确认充值按钮
    public void phone1_charge(final String phone) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.phone1_charge(phone, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            Log.e("onSuccess", result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Bean.PhoneItems items=new Gson().fromJson(result, Bean.PhoneItems.class);
                                        if (items.status==1){// && StringUtil.isSpace(sub_code)
                                            data.clear();
                                            if (items.model.size() > 0){
                                                for (int i = 0; i < items.model.size(); i++) {
                                                    Map<String,Object> map=new HashMap<>();
                                                    map.put("itemName",items.model.get(i).itemName);
                                                    map.put("itemId",items.model.get(i).itemId);
                                                    map.put("rechargeAmount",items.model.get(i).rechargeAmount);
                                                    map.put("inPrice",items.model.get(i).inPrice);
                                                    map.put("check",false);
                                                    data.add(map);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }
                                            else {
                                                PhoneChargeActivity.this.items.setShowEmptyText(true);
                                            }
                                        }else {
                                            toast("充值失败，请重试");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        PhoneChargeActivity.this.items.setShowEmptyText(true);
                                        toast("数据获取失败，请重试");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {

                            //toast(response+"");
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            toast(R.string.system_busy);
        }
    }
}
