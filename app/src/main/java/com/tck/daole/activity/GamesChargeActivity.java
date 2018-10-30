package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
 * 游戏点卡充值
 */
public class GamesChargeActivity extends BaseActivity {
    private LinearLayout ll_back;
    private EditText ed_game_acount, ed_num;
    private Button btn_charge;
    private NiceRecyclerView items;
    private ChargeListAdapter adapter;
    private List<Map<String,Object>> data = new ArrayList<>();
    private String gameId="";
    private String itemId="";

    @Override
    protected void initView(Bundle savedInstanceState) {
        gameId=getIntent().getStringExtra("gameId");
        setContentView(R.layout.activity_games_charge);
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
        ed_game_acount = findView(R.id.ed_game_acount);
        ed_num = findView(R.id.ed_num);

        btn_charge = findView(R.id.btn_forget_modify);
    }
    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
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
        chargeList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_forget_modify:
                String game_acount = ed_game_acount.getText().toString().trim();
                String num = ed_num.getText().toString().trim();
                if (StringUtil.isSpace(itemId)){
                    toast("请选择充值项目");
                    return;
                }
                if (StringUtil.isSpace(game_acount)){
                    toast("请输入游戏账号");
                    return;
                }
                if (StringUtil.isSpace(num)){
                    toast("请输入充值数量");
                    return;
                }

                @SuppressLint("SimpleDateFormat")
                String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                new PayUtil(GamesChargeActivity.this,httpInterface).ApliyPay(no,game_acount,num,itemId);
//                charge(num,game_acount);
                break;
        }
    }

    //确认充值
    public void charge(final String num,final String game_acount) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.games3_charge(num,game_acount,itemId, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("games3_charge", result);
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
    public void chargeList() {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.games2_charge(gameId, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject=new JSONObject(result);
                                        int status=jsonObject.optInt("status");
//                                        String message=jsonObject.optString("message");
                                        String sub_code=jsonObject.optJSONObject("model").optString("error_response");
                                        if (status==1 && StringUtil.isSpace(sub_code)){
                                            Bean.GameItems gameItems=new Gson().fromJson(jsonObject.optJSONObject("model").optJSONObject("game_items_list_response").optString("items"), Bean.GameItems.class);
                                            if (gameItems!=null&&gameItems.item.size()>0){
                                                for (int i = 0; i < gameItems.item.size(); i++) {
                                                    Map<String,Object> map=new HashMap<>();
                                                    map.put("itemName",gameItems.item.get(i).itemName);
                                                    map.put("itemId",gameItems.item.get(i).itemId);
                                                    map.put("facePriceValue",gameItems.item.get(i).facePriceValue);
                                                    map.put("check",false);
                                                    data.add(map);
                                                }
                                                adapter.notifyDataSetChanged();
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
