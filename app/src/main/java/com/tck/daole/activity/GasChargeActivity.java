package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
 * 水费缴纳
 */
public class GasChargeActivity extends BaseActivity {
    private LinearLayout ll_back;
    private EditText ed_city, ed_account, ed_money;
    private Button btn_charge;
    private TextView search_btn,title;
    private NiceRecyclerView items;
    private ChargeListAdapter adapter;
    private List<Map<String,Object>> data = new ArrayList<>();
    private String itemId="";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_elictric_charge);
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
        ed_city = findView(R.id.ed_city);
        ed_account = findView(R.id.ed_account);
        ed_money = findView(R.id.ed_money);

        btn_charge = findView(R.id.btn_forget_modify);
        search_btn = findView(R.id.search_btn);
        title = findView(R.id.title);
    }
    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        btn_charge.setOnClickListener(this);
        adapter.setOnItemClickListener(new ChargeListAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemId=data.get(position).get("itemId")+"";
            }
        });
    }

    @Override
    protected void initData() {
        title.setText("燃气费缴纳");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.search_btn:
                String city = ed_city.getText().toString().trim();
                if (StringUtil.isSpace(city)){
                    toast("请输入城市名称");
                    return;
                }
                itemId="";
                w_e_g1_charge(city);
                break;
            case R.id.btn_forget_modify:
                String account = ed_account.getText().toString().trim();
                String money = ed_money.getText().toString().trim();
                if (StringUtil.isSpace(itemId)){
                    toast("请选择充值项目");
                    return;
                }
                if (StringUtil.isSpace(account)){
                    toast("请输入缴费户号");
                    return;
                }
                if (StringUtil.isSpace(money)){
                    toast("请输入缴纳金额");
                    return;
                }
                @SuppressLint("SimpleDateFormat")
                String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                new PayUtil(com.tck.daole.activity.GasChargeActivity.this,httpInterface).ApliyPay(no,"",account,itemId);
//                charge(num,game_acount);
                break;
        }
    }

    //确认充值
    public void charge() {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.w_e_g2_charge( ed_money.getText()+"",ed_account.getText()+"",itemId, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("fuel2_charge", result);
                            try {
                                JSONObject jsonObject=new JSONObject(result);
                                int status=jsonObject.optInt("status");
//                                        String message=jsonObject.optString("message");
                                String sub_code=jsonObject.optJSONObject("model").optString("error_response");
                                if (status==1 && StringUtil.isSpace(sub_code)){
                                    setResult(888);
                                    finish();
                                }else {
                                    toast("充值失败，请重试");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                toast("充值失败，请重试");
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("onFail", response);
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

    //充值金额列表
    public void w_e_g1_charge(final String city) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.w_e_g1_charge(city,"c2681",new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            Log.e("fuel1_charge", result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject=new JSONObject(result);
                                        int status=jsonObject.optInt("status");
//                                        String message=jsonObject.optString("message");
                                        String sub_code=jsonObject.optJSONObject("model").optString("error_response");
                                        if (status==1 && StringUtil.isSpace(sub_code)){
                                            Bean.FuelItems items=new Gson().fromJson(jsonObject.optJSONObject("model").optJSONObject("admin_item_response").optString("items"), Bean.FuelItems.class);
                                            data.clear();
                                            if (items!=null&&items.item.size()>0){
                                                for (int i = 0; i < items.item.size(); i++) {
                                                    Map<String,Object> map=new HashMap<>();
                                                    map.put("itemName",items.item.get(i).itemName);
                                                    map.put("itemId",items.item.get(i).itemId);
                                                    map.put("inPrice",items.item.get(i).inPrice);
                                                    map.put("check",false);
                                                    data.add(map);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }
                                            else {
                                                GasChargeActivity.this.items.setShowEmptyText(true);
                                            }
                                        }else {
                                            toast("充值失败，请重试");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        toast("充值失败，请重试");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("onFail", response+"");
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response+"");
                        }
                    });
                }
            });
        } else {
            toast(R.string.system_busy);
        }
    }
}
