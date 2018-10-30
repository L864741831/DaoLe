package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.CashCouponAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 代金券页
 */

public class CashCouponActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;

    private NiceRecyclerView rv_cash;
    private List<Map<String, Object>> list = new ArrayList<>();

    private int index = 0;  //开始位置
    private int num = 9;  //结束位置
//    private String sp_token = "";

    double totalPrice = 0.0;  //总金额
    Handler hd;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_coupon);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

/*                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");*/


                switch (msg.what) {
                    case Constant.CashCoupon:



                        CashCouponAdapter adapter = new CashCouponAdapter(list, CashCouponActivity.this);
                        rv_cash.setAdapter(adapter);
                        adapter.setOnItemClickListener(new CashCouponAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                //startActivity(new Intent(SystemMsgActivity.this,PostedParticularsActivity.class));//跳转发布详情

                                String couponId =  (String) list.get(position).get("couponId");//代金券id
                                String fullCutMoney =  (String) list.get(position).get("fullCutMoney");//满减金额
                                String money =  (String)list.get(position).get("money");   //减的金额

                                double double_fullCutMoney = Double.parseDouble(fullCutMoney);
                                double double_money = Double.parseDouble(money);


                                if(totalPrice == 0.0 ){
                                    toast("去选择商品吧");
                                }else if(totalPrice<double_fullCutMoney){
                                    toast("不符合满减条件");
                                }else{
                                    //toast("满减"+double_money);
                                    Intent i = new Intent();
                                    i.putExtra("double_money", double_money);
                                    i.putExtra("couponId", couponId);
                                    setResult(3, i);
                                    finish();
                                }



                                //返回结果
/*                                Intent i = new Intent();
                                i.putExtra("people", people);
                                i.putExtra("phone", phone);
                                i.putExtra("province", province);
                                i.putExtra("city", city);
                                i.putExtra("district", district);
                                i.putExtra("street", street);
                                i.putExtra("address", address);

                                setResult(4, i);
                                finish();*/

                            }
                        });

                        break;

                }

            }
        };


    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        rv_cash = findView(R.id.rv_cash);


    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {


//        sp_token = SPUtil.getData(CashCouponActivity.this, "token", "").toString();
        getCashCouponList(App.token,index,num);

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            totalPrice = intent.getDoubleExtra("totalPrice",0.0);
            //toast(totalPrice+"");
        }

/*        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("jine", "5" + i);
            list.add(map);
        }
        CashCouponAdapter adapter = new CashCouponAdapter(list, this);
        rv_cash.setAdapter(adapter);
        adapter.setOnItemClickListener(new CashCouponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //startActivity(new Intent(SystemMsgActivity.this,PostedParticularsActivity.class));//跳转发布详情
            }
        });
    */

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;

        }
    }


    public void getCashCouponList(final String token,final int index,final int num){
        /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String,String> map = new HashMap<>();

        //map.put("token",token);
        map.put("token",token);

        map.put("page.index",String.valueOf(index));
        map.put("page.num",String.valueOf(num));

        HttpUtils.doPost(UriUtil.cash_coupon, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("=====",response.body().string());

                //String json = "{\"list\":[{\"couponId\":\"1\",\"cpName\":\"123478\",\"cpDescribe\":\"0\",\"money\":0.0,\"fullCutMoney\":123.0,\"status\":\"0\",\"effectiveTime\":\"2018-01-08 00:00:00\"},{\"couponId\":\"20180106170706983001\",\"cpName\":\"123\",\"cpDescribe\":\"1231323\",\"money\":7459.0,\"fullCutMoney\":789.0,\"status\":\"0\",\"effectiveTime\":\"2018-01-16 00:00:00\"},{\"couponId\":\"20180106170706983001\",\"cpName\":\"123\",\"cpDescribe\":\"1231323\",\"money\":7459.0,\"fullCutMoney\":789.0,\"status\":\"0\",\"effectiveTime\":\"2018-01-16 00:00:00\"}],\"status\":\"1\",\"message\":\"查询成功\"}";


                Bean.CouponModel data = new Gson().fromJson(response.body().string(), Bean.CouponModel.class);
                List<Bean.Coupon> coupon_list = data.getList();

                for (int i = 0; i < coupon_list.size(); i++) {
                    Map<String, Object> map1 = new HashMap<>();

                    map1.put("couponId", coupon_list.get(i).getCouponId()); //	優惠券couponId
                    map1.put("cpName", coupon_list.get(i).getCpName()); //	優惠券名稱
                    map1.put("cpDescribe", coupon_list.get(i).getCpDescribe()); //	描述
                    map1.put("money", coupon_list.get(i).getMoney()); //	抵扣金額
                    map1.put("fullCutMoney", coupon_list.get(i).getFullCutMoney()); //	滿減金額
                    map1.put("status", coupon_list.get(i).getStatus()); //	登录状态
                    map1.put("effectiveTime", coupon_list.get(i).getEffectiveTime()); //	有效期

                    list.add(map1);
                }


                Message msg = new Message();
                msg.what = Constant.CashCoupon;
                hd.sendMessage(msg);

            }
        });
    }

}
