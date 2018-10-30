package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.OrderDetailsGoodsAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.OrderDetails;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class OrderDetailsActivity extends BaseActivity {
//    private View view;
    private LinearLayout ll_left;
    private TextView orderState;
//    private LinearLayout ll_collection_address;
    private TextView tv_defaulta_adress;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView shopName;
    private TextView tv_totalPrice;
//    private TextView tv_order_yunfei;
//    private LinearLayout ll_order_youhuiquan;
//    private TextView tv_manjian_money;
    private TextView orderNo;
    private TextView payType;
    private TextView submitDate;

    private NiceRecyclerView goods;
    private OrderDetailsGoodsAdapter adapter;
    private List<OrderDetails.Goods> list=new ArrayList<>();

    private String oid="";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_details);
        oid=getIntent().getStringExtra("oid");
        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        orderState = (TextView) findViewById(R.id.orderState);
//        ll_collection_address = (LinearLayout) findViewById(R.id.ll_collection_address);
        tv_defaulta_adress = (TextView) findViewById(R.id.tv_defaulta_adress);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        shopName = (TextView) findViewById(R.id.shopName);
        goods = (NiceRecyclerView) findViewById(R.id.goods);
        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);
//        tv_order_yunfei = (TextView) findViewById(R.id.tv_order_yunfei);
//        ll_order_youhuiquan = (LinearLayout) findViewById(R.id.ll_order_youhuiquan);
//        tv_manjian_money = (TextView) findViewById(R.id.tv_manjian_money);
        orderNo = (TextView) findViewById(R.id.orderNo);
        payType = (TextView) findViewById(R.id.payType);
        submitDate = (TextView) findViewById(R.id.submitDate);

        adapter=new OrderDetailsGoodsAdapter(list,OrderDetailsActivity.this);
        goods.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(listener);
        adapter.setOnItemClickListener(new OrderDetailsGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(OrderDetailsActivity.this, MerchandiseActivity.class);
                intent.putExtra("takeProjectId",list.get(position).poid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        //调接口
        httpInterface.getOrderDetails(App.token, oid, new MApiResultCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String result) {
                Log.e("getOrderDetails", result);
                OrderDetails data = new Gson().fromJson(result, OrderDetails.class);
                if (data.status==1){
                    int state=data.models.orderState;
                    int type=data.models.type;
                    switch (state){
                        case 1:
                            orderState.setText("未付款");
                            break;
                        case 2:
                            if (type==5) {
                                orderState.setText("待送货");
                            }else if (type==1||type==3){
                                orderState.setText("待平台发货");
                            }else if (type==2){
                                orderState.setText("待验票");
                            }else if (type==4){
                                orderState.setText("待消费");
                            }
                            break;
                        case 3:
                            orderState.setText("送货中");
                            break;
                        case 4:
                            orderState.setText("已送达");
                            break;
                        case 5:
                            orderState.setText("退货退款");
                            break;
                        case 6:
                            orderState.setText("订单取消");
                            break;
                    }
                    tv_defaulta_adress.setText(data.models.address.address);
                    tv_name.setText(data.models.address.people);
                    tv_phone.setText(data.models.address.phone);
                    shopName.setText(data.models.shop.name);
                    tv_totalPrice.setText("¥"+data.models.payMoney);
                    orderNo.setText(data.models.orderNum);
                    if ("0".equals(data.models.payType)){
                        payType.setText("余额");
                    }else if ("1".equals(data.models.payType)) {
                        payType.setText("支付宝");
                    }else if ("2".equals(data.models.payType)) {
                        payType.setText("微信");
                    }
                    submitDate.setText(data.models.submitTime);
                    list.addAll(data.models.goods);
                    adapter.notifyDataSetChanged();
                }else {
                    toast(data.message);
                }
            }

            @Override
            public void onFail(String response) {
                Log.e("获取订单列表.异常", response);
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

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_left:
                    finish();
                    break;
            }
        }
    };
}
