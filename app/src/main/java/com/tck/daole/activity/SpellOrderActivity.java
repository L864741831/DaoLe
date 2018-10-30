package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.DingdanContentAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.entity.ShopCat;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.PayUtil;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 拼单订单
 */
public class SpellOrderActivity extends BaseActivity {
    private LinearLayout ll_left, ll_collection_address, ll_order_youhuiquan;
//    private String sp_token = "";
    private TextView tv_totalPrice, tv_submit_order;
    private NiceRecyclerView rv_order;

    TextView tv_order_Shop_name, tv_order_yunfei, tv_manjian_money, tv_sum_money, tv_youhui_mobey;
    List<Map<String, Object>> list = new ArrayList<>();
    DingdanContentAdapter adapter;
    CheckBox cb_order_WeChat, cb_order_Alipay, cb_order_balance;

    List<Map<String, Object>> address_default = new ArrayList<>();

    List<Map<String, Object>> cash_coupon_list = new ArrayList<>(); //优惠券列表


    Handler hd;
    double totalPrice;  //总金额

    double double_money = 0.0;  //满减金额
    double lowestPrice;     //运费


    TextView tv_defaulta_adress, tv_defaulta_name, tv_defaulta_phone;

    String payType = "2"; //0.余额支付 1支付宝支付 2微信支付

    String dingdan_json = "";   //购物车传来的商店和商品列表

    String addressId = "666666";    //地址id

    String couponId = ""; //优惠券id

    //String coupon_num = "0张优惠券可用"; //优惠券可用数量


    private EditText et_order_beizhu;

    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_spell_order);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_left = findView(R.id.ll_left);
        ll_collection_address = findView(R.id.ll_collection_address);
        tv_totalPrice = findView(R.id.tv_totalPrice);
        rv_order = findView(R.id.rv_order);
        tv_order_Shop_name = findView(R.id.tv_order_Shop_name);
        tv_defaulta_adress = findView(R.id.tv_defaulta_adress);
        tv_defaulta_name = findView(R.id.tv_defaulta_name);
        tv_defaulta_phone = findView(R.id.tv_defaulta_phone);
        tv_order_yunfei = findView(R.id.tv_order_yunfei);
        ll_order_youhuiquan = findView(R.id.ll_order_youhuiquan);
        tv_manjian_money = findView(R.id.tv_manjian_money);
        tv_sum_money = findView(R.id.tv_sum_money);
        tv_youhui_mobey = findView(R.id.tv_youhui_mobey);

        cb_order_WeChat = findView(R.id.cb_order_WeChat);
        cb_order_Alipay = findView(R.id.cb_order_Alipay);
        cb_order_balance = findView(R.id.cb_order_balance);

        tv_submit_order = findView(R.id.tv_submit_order);
        et_order_beizhu = findView(R.id.et_order_beizhu);


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

/*                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");*/


                switch (msg.what) {
                    case Constant.Address_Default:
                        if (address_default.size() == 0) {

                        }
                        if (address_default.size() == 1) {


                            addressId = (String) address_default.get(0).get("addressId");    //地址id


                            final String people = (String) address_default.get(0).get("people");    //联系人名字
                            final String phone = (String) address_default.get(0).get("phone");  //联系人电话
                            final String province = (String) address_default.get(0).get("province");    //省
                            final String city = (String) address_default.get(0).get("city");    //市
                            final String district = (String) address_default.get(0).get("district");    //区
                            final String street = (String) address_default.get(0).get("street");    //街道
                            final String address = (String) address_default.get(0).get("address");  //地址
                            final String defaultAddress = (String) address_default.get(0).get("defaultAddress");  //是否地址 0是 1不是

                            tv_defaulta_adress.setText(province + city + district + street + address);
                            tv_defaulta_name.setText(people);
                            tv_defaulta_phone.setText(phone);


                        }
                        break;

                    case Constant.cash_sum:
                        tv_manjian_money.setText(cash_coupon_list.size()+"张优惠券可用");
                        break;
                }

            }
        };


    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
        ll_collection_address.setOnClickListener(this);
        ll_order_youhuiquan.setOnClickListener(this);
        cb_order_WeChat.setOnClickListener(this);
        cb_order_Alipay.setOnClickListener(this);
        cb_order_balance.setOnClickListener(this);
        tv_submit_order.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        cb_order_WeChat.setChecked(true);
        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {

            dingdan_json = intent.getStringExtra("dingdan_json");
            totalPrice = intent.getDoubleExtra("totalPrice", 0.0);


            tv_totalPrice.setText("￥" + totalPrice);    //显示总金额

            //toast(dingdan_json);

            Log.i("dingdan_json", dingdan_json);

            ShopCat.ShopName data = new Gson().fromJson(dingdan_json,
                    ShopCat.ShopName.class);

            tv_order_Shop_name.setText(data.name);
            //tv_order_Shop_name.setText(String.valueOf(data.lowestPrice));
            //tv_order_Shop_name.setText(String.valueOf(data.servicePrice));
            lowestPrice = data.lowestPrice; //运费
            tv_order_yunfei.setText("￥" + String.valueOf(data.lowestPrice)); //显示运费
            tv_sum_money.setText("￥" + (totalPrice + lowestPrice));   //总价和运费

            //toast(data.name+"\n"+data.AdminStoreOid);


/*            public String oid;  //购物车oid
            public String projectOid;   //店铺oid
            public String takeProjectName; //name=商品名称
            public String mainImage;    //mainImage=商品照片
            public double price;   //price=价格
            public int purchaseNumber;  //purchaseNumber=购买数量
            public String detail;       //商品详情备注*/

            List<ShopCat.ShopContent> ShopContent_list = data.business;
            for (int i = 0; i < ShopContent_list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("oid", ShopContent_list.get(i).oid); //购物车oid
                map.put("projectOid", ShopContent_list.get(i).projectOid); //店铺oid
                map.put("takeProjectName", ShopContent_list.get(i).takeProjectName); //name=商品名称
                map.put("mainImage", ShopContent_list.get(i).mainImage); //mainImage=商品照片
                map.put("price", ShopContent_list.get(i).price); //price=价格
                map.put("purchaseNumber", ShopContent_list.get(i).purchaseNumber); //purchaseNumber=购买数量
                map.put("detail", ShopContent_list.get(i).detail); //商品详情备注*/

                list.add(map);

                //Log.i("显示一个商品", "++++++");
            }


            adapter = new DingdanContentAdapter(list, SpellOrderActivity.this);
            rv_order.setAdapter(adapter);
            adapter.setOnItemClickListener(new DingdanContentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });


            //List<ShopCat.ShopName> list = data.getList();
      /*      List<ShopCat.ShopContent> business = list.get(0).business;
            for(int i = 0; i < business.size(); i++){
                Log.i("business","商品"+i);
            }*/


//            sp_token = SPUtil.getData(SpellOrderActivity.this, "token", "").toString();
            if (App.islogin) {
                getAddress(App.token);
                getCashCouponList(App.token,0,99);
            } else {
                startActivity(new Intent(SpellOrderActivity.this, LoginActivity.class));//跳转登录
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                finish();
                break;

            case R.id.ll_collection_address:    //选择地址
                //startActivity(new Intent(SpellOrderActivity.this, AddressCollectionActivity.class));//收货地址


                startActivityForResult(new Intent(SpellOrderActivity.this,
                        AddressCollectionActivity.class), 1);

                break;

            case R.id.ll_order_youhuiquan:  //选择优惠券

                Intent intent = new Intent(SpellOrderActivity.this,
                        CashCouponActivity.class);
                intent.putExtra("totalPrice", totalPrice);
                startActivityForResult(intent, 0);

                break;

            case R.id.cb_order_WeChat:  //微信支付
                cb_order_WeChat.setChecked(true);
                cb_order_Alipay.setChecked(false);
                cb_order_balance.setChecked(false);

                payType = "2";
                break;
            case R.id.cb_order_Alipay:  //支付宝支付
                cb_order_WeChat.setChecked(false);
                cb_order_Alipay.setChecked(true);
                cb_order_balance.setChecked(false);
                payType = "1";

                break;
            case R.id.cb_order_balance:  //余额支付

                cb_order_WeChat.setChecked(false);
                cb_order_Alipay.setChecked(false);
                cb_order_balance.setChecked(true);
                payType = "0";

                break;

            case R.id.tv_submit_order:  //提交订单


/*            {
                "adminStoreOid":"1",    //店铺id
                    "projects":[    //商品列表
                {
                    "projectOid":"I7PCPK6C41",  //商品id
                        "count":1      //商品数量
                }
    ],
                "addressOid":"I7P4K45KP1",  //地址id
                    "coupon":"2",   //优惠券id
                    "servicePrice":"2", //配送费
                    "price":500,    //总价
                    "remark":"我是备注",    //备注
                    "payType":"1"   //支付方式 0.余额支付 1支付宝支付 2微信支付
            }*/


                //toast("提交订单");

                ShopCat.ShopName data = new Gson().fromJson(dingdan_json,
                        ShopCat.ShopName.class);

                tv_order_Shop_name.setText(data.name);
                //tv_order_Shop_name.setText(String.valueOf(data.lowestPrice));
                //tv_order_Shop_name.setText(String.valueOf(data.servicePrice));
                lowestPrice = data.lowestPrice; //运费
                tv_order_yunfei.setText("￥" + String.valueOf(data.lowestPrice)); //显示运费
                tv_sum_money.setText("￥" + (totalPrice + lowestPrice));   //总价和运费

                //toast(data.name+"\n"+data.AdminStoreOid);


/*            public String oid;  //购物车oid
            public String projectOid;   //店铺oid
            public String takeProjectName; //name=商品名称
            public String mainImage;    //mainImage=商品照片
            public double price;   //price=价格
            public int purchaseNumber;  //purchaseNumber=购买数量
            public String detail;       //商品详情备注*/

                list.clear();
                List<ShopCat.ShopContent> ShopContent_list = data.business;
                for (int i = 0; i < ShopContent_list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("oid", ShopContent_list.get(i).oid); //购物车oid
                    map.put("projectOid", ShopContent_list.get(i).projectOid); //店铺oid
                    map.put("takeProjectName", ShopContent_list.get(i).takeProjectName); //name=商品名称
                    map.put("mainImage", ShopContent_list.get(i).mainImage); //mainImage=商品照片
                    map.put("price", ShopContent_list.get(i).price); //price=价格
                    map.put("count", ShopContent_list.get(i).purchaseNumber); //purchaseNumber=购买数量
                    map.put("detail", ShopContent_list.get(i).detail); //商品详情备注*/

                    list.add(map);

                    //Log.i("显示一个商品", "++++++");
                }

/*                Gson gson = new Gson();
                String dingdan_json = gson.toJson(shop_name_list.get(0));*/

                Gson gson = new Gson();
                String dingdan_json = gson.toJson(list);

                //Log.i("显示一个商品", dingdan_json);

                if (addressId.equals("666666")) {
                    toast("请添加地址");
                } else {

                    //couponId

                    String order_str = "{\"adminStoreOid\":\"" + data.AdminStoreOid + "\",\"projects\":" + dingdan_json + ",\"addressOid\":\""+addressId+"\",\"coupon\":\""+couponId+"\",\"servicePrice\":\""+data.lowestPrice+"\",\"price\":"+(totalPrice + lowestPrice)+",\"remark\":\""+et_order_beizhu.getText().toString().trim()+"\",\"payType\":\""+payType+"\"}";

                    //Log.i("提交订单json", order_str);

                    //String str = "{\"adminStoreOid\":\"1\",\"projects\":[{\"projectOid\":\"I7PCPK6C41\",\"count\":1}],\"addressOid\":\"I7P4K45KP1\",\"coupon\":\"2\",\"servicePrice\":\"2\",\"price\":500,\"remark\":\"我是备注\",\"payType\":\"1\"}";
                    onSubmitOrder(App.token, order_str,"5");
                }


                break;


          /*  {
                "adminStoreOid":"1",    //店铺id
                    "projects":[    //商品列表
                {
                    "projectOid":"I7PCPK6C41",  //商品id
                        "count":1      //商品数量
                }
    ],
                "addressOid":"I7P4K45KP1",  //地址id
                    "coupon":"2",   //优惠券id
                    "servicePrice":"2", //配送费
                    "price":500,    //总价
                    "remark":"我是备注",    //备注
                    "payType":"1"   //支付方式 0.余额支付 1支付宝支付 2微信支付
            }*/

        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {

/*            i.putExtra("people", people);
            i.putExtra("phone", phone);
            i.putExtra("province", province);
            i.putExtra("city", city);
            i.putExtra("district", district);
            i.putExtra("street", street);
            i.putExtra("address", address);*/

            addressId = data.getStringExtra("addressId");
            String people = data.getStringExtra("people");
            String phone = data.getStringExtra("phone");
            String province = data.getStringExtra("province");
            String city = data.getStringExtra("city");
            String district = data.getStringExtra("district");
            String street = data.getStringExtra("street");
            String address = data.getStringExtra("address");

            tv_defaulta_name.setText(people);
            tv_defaulta_phone.setText(phone);
            tv_defaulta_adress.setText(province + city + district + street + address);
/*            tv_defaulta_adress.setText(province+city+district+street+address);
            tv_defaulta_name.setText(people);
            tv_defaulta_phone.setText(phone);*/

        }

        if (requestCode == 0 && resultCode == 3) {

            couponId = data.getStringExtra("couponId");

            double_money = data.getDoubleExtra("double_money", 0.0);
            //toast(double_money+"");
            tv_manjian_money.setTextColor(this.getResources().getColor(R.color.red));
            tv_manjian_money.setText("￥" + double_money);    //显示满减金额
            tv_sum_money.setText("合计￥" + (totalPrice - double_money + lowestPrice));  //总价减满减加运费
            tv_youhui_mobey.setText("(已优惠¥" + double_money + ")");

        }

    }


    //token
    public void getAddress(final String token) {

            /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("token", token);


        HttpUtils.doPost(UriUtil.address_default, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //Log.i("23323",response.body().string());

                Bean.AddressList data = new Gson().fromJson(response.body().string(), Bean.AddressList.class);
                List<Bean.Address> address_list = data.getList();

/*
     memberId=用户ID
 	 nickName=用户呢称
 	 addressId=地址ID
 	 people=地址联系人
 	 phone=联系人电话
 	 province=联系人省
 	 city=联系人市
 	 district=联系人区
 	 street=联系人街道
 	 address=联系人地址
 	 defaultAddress=是否为默认地址 0是 1不是

                */


                for (int i = 0; i < address_list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", token); //	token用于修改默认地址
                    map.put("memberId", address_list.get(i).getMemberId()); //	用户ID
                    map.put("nickName", address_list.get(i).getNickName()); //	用户呢称
                    map.put("addressId", address_list.get(i).getAddressId()); //	地址ID
                    map.put("people", address_list.get(i).getPeople()); //	地址联系人
                    map.put("phone", address_list.get(i).getPhone()); //	联系人电话
                    map.put("province", address_list.get(i).getProvince()); //	联系人省
                    map.put("city", address_list.get(i).getCity()); //	联系人市
                    map.put("district", address_list.get(i).getDistrict()); //	联系人区
                    map.put("street", address_list.get(i).getStreet()); //	联系人街道
                    map.put("address", address_list.get(i).getAddress()); //	联系人地址
                    map.put("defaultAddress", address_list.get(i).getDefaultAddress()); //	是否为默认地址 0是 1不是
                    if (address_list.get(i).getDefaultAddress().equals("1")) {
                        map.put("check", false);
                    } else {
                        map.put("check", true);
                    }
                    address_default.add(map);
                }


                Message msg = new Message();
                msg.what = Constant.Address_Default;
                hd.sendMessage(msg);


            }
        });

    }


    public void onSubmitOrder(final String token, final String order, final String type) {

        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.submitOrder(token, order,type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("提交订单.成功", result);

                            //PayUtil ApliyPay  进行支付
                            @SuppressLint("SimpleDateFormat") String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                            //Log.i("提交233",no);

                            new PayUtil(SpellOrderActivity.this,httpInterface).ApliyPay(no,"","","");

                            finish();

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("提交订单.异常", response);
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

                    cash_coupon_list.add(map1);
                }


                Message msg = new Message();
                msg.what = Constant.cash_sum;
                hd.sendMessage(msg);

            }
        });
    }


}
