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
public class LiuliangChargeActivity extends BaseActivity {
    private LinearLayout ll_back;
    private EditText ed_phone;//,ed_charge_money
    private Button btn_submit;

    private NiceRecyclerView items,items2;
    private ChargeListAdapter adapter,adapter2;
    private List<Map<String,Object>> data = new ArrayList<>();
    private List<Map<String,Object>> data2 = new ArrayList<>();
    private String itemId="";
    private String itemId2="";
    private String rechargeAmount="";
    private String rechargeAmount2="";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_liuliang_charge);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.store_View).setVisibility(View.GONE);
        }
        findView();
    }

    public void findView(){
        items = (NiceRecyclerView)findViewById(R.id.items);
        adapter =new ChargeListAdapter(R.layout.charge_item_bg, data);
        items.setAdapter(adapter);
        items2 = (NiceRecyclerView)findViewById(R.id.items2);
        adapter2 =new ChargeListAdapter(R.layout.charge_item_bg, data2);
        items2.setAdapter(adapter2);
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
        adapter2.setOnItemClickListener(new ChargeListAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemId2=data2.get(position).get("itemId")+"";
//                rechargeAmount2=data2.get(position).get("rechargeAmount")+"";
                liuliang1_charge(ed_phone.getText()+"",itemId2);
            }
        });
//        ed_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String newText = s.toString().trim();
//                if (!StringUtil.isSpace(newText)&&newText.length()==11) {
//                    phone1_charge(newText);
//                }
//                else {
//                    toast("请输入您的手机号");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    @Override
    protected void initData() {
        ed_phone.setText(App.phone);
        Map<String,Object> map100=new HashMap<>();
        map100.put("itemName","100M");
        map100.put("itemId","100M");
        map100.put("rechargeAmount","100M");
        map100.put("check",false);
        data2.add(map100);
        Map<String,Object> map200=new HashMap<>();
        map200.put("itemName","200M");
        map200.put("itemId","200M");
        map200.put("rechargeAmount","200M");
        map200.put("check",false);
        data2.add(map200);
        Map<String,Object> map300=new HashMap<>();
        map300.put("itemName","300M");
        map300.put("itemId","300M");
        map300.put("rechargeAmount","300M");
        map300.put("check",false);
        data2.add(map300);
        Map<String,Object> map500=new HashMap<>();
        map500.put("itemName","500M");
        map500.put("itemId","500M");
        map500.put("rechargeAmount","500M");
        map500.put("check",false);
        data2.add(map500);
        Map<String,Object> map1G=new HashMap<>();
        map1G.put("itemName","1G");
        map1G.put("itemId","1G");
        map1G.put("rechargeAmount","1G");
        map1G.put("check",false);
        data2.add(map1G);
        Map<String,Object> map2G=new HashMap<>();
        map2G.put("itemName","2G");
        map2G.put("itemId","2G");
        map2G.put("rechargeAmount","2G");
        map2G.put("check",false);
        data2.add(map2G);
        Map<String,Object> map3G=new HashMap<>();
        map3G.put("itemName","3G");
        map3G.put("itemId","3G");
        map3G.put("rechargeAmount","3G");
        map3G.put("check",false);
        data2.add(map3G);
        adapter2.notifyDataSetChanged();
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
                new PayUtil(LiuliangChargeActivity.this,httpInterface).ApliyPay(no,modify_phone,"","0");
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
    public void liuliang1_charge(final String phone,final String flowOne) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.liuliang1_charge(phone,flowOne, new MApiResultCallback() {
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
                                            Bean.FuelItems items=new Gson().fromJson(jsonObject.optJSONObject("model").optJSONObject("mobile_flow_items_list2_response").optString("items"), Bean.FuelItems.class);
//                                        if (items.status==1){// && StringUtil.isSpace(sub_code)
                                            data.clear();
                                            if (items.item.size() > 0){
                                                for (int i = 0; i < items.item.size(); i++) {
                                                    Map<String,Object> map=new HashMap<>();
                                                    map.put("itemName",items.item.get(i).itemName);
                                                    map.put("itemId",items.item.get(i).itemId);
                                                    map.put("rechargeAmount",items.item.get(i).rechargeAmount);
                                                    map.put("inPrice",items.item.get(i).inPrice);
                                                    map.put("check",false);
                                                    data.add(map);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }
                                            else {
                                                LiuliangChargeActivity.this.items.setShowEmptyText(true);
                                            }
                                        }else {
                                            toast("充值失败，请重试");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LiuliangChargeActivity.this.items.setShowEmptyText(true);
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

    //支付回调
    public void liuliang2_charge(final String phone) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.liuliang2_charge(phone,itemId, new MApiResultCallback() {
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
}
