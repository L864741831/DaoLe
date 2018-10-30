package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.AddressAdminAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AddressCollectionActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private Button add_new_address;

    private NiceRecyclerView rv_address_admin;
    private List<Map<String, Object>> list = new ArrayList<>();
    AddressAdminAdapter adapter;

    private int mSelectedPos = -1;//保存当前选中的position 重点！

//    private String sp_token = "";

    private Handler hd;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_collection);
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
                    case Constant.Address_List:
                        adapter = new AddressAdminAdapter(list, AddressCollectionActivity.this);
                        rv_address_admin.setAdapter(adapter);

/*                        adapter.setOnItemClickListener(new AddressAdminAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(int position) {
                            *//*    startActivity(new Intent(AddressCollectionActivity.this, SpellOrderActivity.class));//跳转订单页
                                finish();*//*
                            toast("选择地址");
                            }
                        });*/

                        break;

                }

            }
        };


    }

    public void findViewId() {
        ll_back = findView(R.id.ll_back);
        add_new_address = findView(R.id.add_new_address);
        rv_address_admin = findView(R.id.rv_address_admin);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        add_new_address.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(AddressCollectionActivity.this, "token", "").toString();
        if(App.islogin){
            getAddressList(App.token);
        }else{
            startActivity(new Intent(AddressCollectionActivity.this, LoginActivity.class));//跳转登录
        }


/*
        Map<String, Object> map = new HashMap();
        map.put("address", "收货地址");
        map.put("check", true);
        list.add(map);

        for (int i = 0; i < 5; i++) {
            Map<String, Object> map1 = new HashMap();
            map1.put("address", "收货地址" + i);
            map1.put("check", false);
            list.add(map1);
        }
        adapter = new AddressAdminAdapter(list, AddressCollectionActivity.this);
        rv_address_admin.setAdapter(adapter);

        adapter.setOnItemClickListener(new AddressAdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.add_new_address:
                Intent intent = new
                        Intent(AddressCollectionActivity.this, AddAdressActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("addresstype", "addaddress");
                startActivity(intent);
                finish();
                //startActivity(new Intent(AddressCollectionActivity.this, AddAdressActivity.class));//添加地址
                break;

        }
    }


    //token
    public void getAddressList(final String token) {

            /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("token", token);


        HttpUtils.doPost(UriUtil.address_list, map, new Callback() {
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
                    list.add(map);
                }


                Message msg = new Message();
                msg.what = Constant.Address_List;
                hd.sendMessage(msg);


            }
        });

    }


}
