package com.tck.daole.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.activity.MarketActivity;
import com.tck.daole.activity.MerchandiseActivity;
import com.tck.daole.activity.ShopActivity;
import com.tck.daole.adapter.CollectionAdapter;
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
 * 店铺fragment
 */
public class ShopFragment extends Fragment{

    NiceRecyclerView rv_shoucang_shop;
    private List<Map<Object, String>> list = new ArrayList<>();
    private CollectionAdapter adapter;

//    private String sp_token = "";

    Handler hd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        initView(view);
        initData();





        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");


                switch (msg.what) {
                    case Constant.Shop:

                        adapter = new CollectionAdapter(list,getActivity());
                        rv_shoucang_shop.setAdapter(adapter);

                        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
                            public void onItemClickListener(int position,String state) {

                                switch (state) {
                                    case "1":
                                        String oid = (String) list.get(position).get("adminStoreId");
                                        //toast(oid);

                                        //超市列表到店铺

                                        Intent intent = new
                                                Intent(getActivity(),ShopActivity.class);
                                        //在Intent对象当中添加一个键值对
                                        intent.putExtra("oid",oid);
                                        startActivity(intent);

                                        getActivity().finish();

                                        break;
                                    case "2":
                                        startActivity(new Intent(getActivity(), MerchandiseActivity.class));//跳转商品
                                        break;
                                }

                            }
                        });

                        break;

                    case Constant.Shop_Error:

                        Toast.makeText(getActivity(),msgStr,Toast.LENGTH_SHORT).show();

                        break;



                }

            }
        };

        return view;

    }


    private void initView(View view) {
        rv_shoucang_shop = (NiceRecyclerView) view.findViewById(R.id.rv_shoucang_shop);
    }

    private void initData() {

//        sp_token = SPUtil.getData(getActivity(), "token", "").toString();
        String sp_longitude = SPUtil.getData(getActivity(),"longitude","").toString();
        String sp_latitude = SPUtil.getData(getActivity(),"latitude","").toString();

        //Log.e("=====", sp_longitude+"                    经纬度"+sp_latitude);

        getShop(App.token,sp_longitude,sp_latitude);
/*
        shopDianpuData("店铺名");

        adapter = new CollectionAdapter(list,getActivity());
        rv_shoucang_shop.setAdapter(adapter);

        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            public void onItemClickListener(int position,String state) {

                switch (state) {
                    case "1":
                        startActivity(new Intent(getActivity(), ShopActivity.class));//跳转店铺
                        break;
                    case "2":
                        startActivity(new Intent(getActivity(), MerchandiseActivity.class));//跳转商品
                        break;
                }

            }
        });

        */
    }



    private void shopDianpuData(String str) {
        list.clear();
        for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap();
            map1.put("state", "1");
            map1.put("name", str);

            list.add(map1);
        }
    }


    //token、经度、纬度
    public void getShop(String token,String longitude,String sp_latitude) {

            /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        map.put("longitude",longitude);
        map.put("dimensionality",sp_latitude);


        HttpUtils.doPost(UriUtil.shop, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("==233=",response.body().string());

                //String str = "{\"model\":[{\"memberId\":\"I7OYF97LN1\",\"projectName\":\"这是个乐拼购商品\",\"drtail\":null,\"saleNum\":null,\"loanAmounts\":199.0,\"thumPath\":\"uploadImage/FFF.jpg\"},{\"memberId\":\"I7OYF97LN1\",\"projectName\":null,\"drtail\":null,\"saleNum\":null,\"loanAmounts\":null,\"thumPath\":null},{\"memberId\":\"I7OYF97LN1\",\"projectName\":null,\"drtail\":null,\"saleNum\":null,\"loanAmounts\":null,\"thumPath\":null},{\"memberId\":\"I7OYF97LN1\",\"projectName\":null,\"drtail\":null,\"saleNum\":null,\"loanAmounts\":null,\"thumPath\":null}],\"status\":\"1\",\"message\":\"查询成功\"}";

                //String str = "{\"list\":[{\"memberId\":\"I7OYF97LN1\",\"oid\":\"1\",\"name\":\"这是家黄焖鸡啊\",\"address\":\"地址\",\"thumPath\":\"assets/admin_default.jpg\",\"starlevel\":\"4.5\",\"jvli\":10106.0,\"jvLi\":\"10.11km\"},{\"memberId\":\"I7OYF97LN1\",\"oid\":\"2\",\"name\":\"789\",\"address\":\"地址\",\"thumPath\":null,\"starlevel\":\"3\",\"jvli\":10233.0,\"jvLi\":\"10.23km\"},{\"memberId\":\"I7OYF97LN1\",\"oid\":\"3\",\"name\":\"名称\",\"address\":\"地址\",\"thumPath\":null,\"starlevel\":\"2.5\",\"jvli\":7450.0,\"jvLi\":\"7.45km\"}],\"status\":\"1\",\"message\":\"查询成功\"}";


                String str = response.body().string();

                Log.i("typeoid",str);


                Bean.ShopModel data = new Gson().fromJson(str, Bean.ShopModel.class);
                List<Bean.Shop> shop_list = data.getList();

/*                {
                    memberId=用户ID;
                    oid=店铺收藏ID;
                    name=店铺名称;
                    address=店铺地址;
                    thumPath=店铺图片;
                    starlevel=店铺评价星级;
                    jvli=距离;
                }
                */

                if(data.getStatus()==0){

                    Bundle b = new Bundle();
                    b.putString("msg", data.getMessage());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Shop_Error;
                    hd.sendMessage(msg);


                }else{
                    for (int i = 0; i < shop_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("memberId", shop_list.get(i).getMemberId()); //	用户ID
                        map.put("oid", shop_list.get(i).getOid()); //	店铺收藏ID
                        map.put("name", shop_list.get(i).getName()); //	店铺名称
                        map.put("address", shop_list.get(i).getAddress()); //	店铺地址
                        map.put("thumPath", shop_list.get(i).getThumPath()); //	店铺图片
                        map.put("starlevel", shop_list.get(i).getStarlevel()); //	店铺评价星级
                        map.put("jvLi", shop_list.get(i).getJvliStr()); //	距离
                        map.put("adminStoreId", shop_list.get(i).adminStoreId); //	店铺id



                        map.put("state", "1");                //	列表类别，店铺

                        Log.i("typeoid",shop_list.get(i).getOid());

                        list.add(map);
                    }



                    Message msg = new Message();
                    msg.what = Constant.Shop;
                    hd.sendMessage(msg);
                }



            }
        });

    }



    }
