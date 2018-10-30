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
 * 商品fragment
 */
public class CommodityFragment extends Fragment{

    NiceRecyclerView rv_shoucang_shop;
    private List<Map<Object, String>> list = new ArrayList<>();
    private CollectionAdapter adapter;

//    private String sp_token = "";


    Handler hd;

    String oid;

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
                    case Constant.Commodity:
                        adapter = new CollectionAdapter(list,getActivity());
                        rv_shoucang_shop.setAdapter(adapter);

                        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
                            public void onItemClickListener(int position,String state) {

                                switch (state) {
                                    case "1":
                                        startActivity(new Intent(getActivity(), ShopActivity.class));//跳转店铺
                                        break;
                                    case "2":


                                        Intent intent_tuangou = new
                                                Intent(getActivity(), MerchandiseActivity.class);
                                        //在Intent对象当中添加一个键值对
                                        intent_tuangou.putExtra("takeProjectId", list.get(position).get("takeProjectId"));
                                        //intent_tuangou.putExtra("cartNum", list.get(position).get("cartNum"));
                                        intent_tuangou.putExtra("oid", oid);
                                        startActivity(intent_tuangou);
                                        getActivity().finish();


                                        break;
                                }

                            }
                        });

                        break;

                    case Constant.Commodity_Error:
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

        getCommodity(App.token);

/*        shopDianpuData("商品名");

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
        });*/
    }



    private void shopDianpuData(String str) {
        list.clear();
        for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap();
            map1.put("state", "2");
            map1.put("name", str);

            list.add(map1);
        }
    }




public void getCommodity(String token){

    /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
    Map<String,String> map = new HashMap<>();
    map.put("token",token);

    HttpUtils.doPost(UriUtil.commodity, map, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            //Log.i("=====",response.body().string());

            //String str = "{\"list\":[{\"memberId\":\"I7OYF97LN1\",\"projectName\":\"这是个乐拼购商品\",\"drtail\":null,\"saleNum\":null,\"loanAmounts\":199.0,\"thumPath\":\"uploadImage/FFF.jpg\"},{\"memberId\":\"I7OYF97LN1\",\"projectName\":null,\"drtail\":null,\"saleNum\":null,\"loanAmounts\":null,\"thumPath\":null},{\"memberId\":\"I7OYF97LN1\",\"projectName\":null,\"drtail\":null,\"saleNum\":null,\"loanAmounts\":null,\"thumPath\":null},{\"memberId\":\"I7OYF97LN1\",\"projectName\":null,\"drtail\":null,\"saleNum\":null,\"loanAmounts\":null,\"thumPath\":null}],\"status\":\"1\",\"message\":\"查询成功\"}";

            String str = response.body().string();

            Log.i("Commodity",str);

            Bean.CommodityEnshrineModel data = new Gson().fromJson(str, Bean.CommodityEnshrineModel.class);


            if(data.getStatus() == 0){

                Bundle b = new Bundle();
                b.putString("msg", data.getMessage());
                Message msg = new Message();
                msg.setData(b);
                msg.what = Constant.Commodity_Error;
                hd.sendMessage(msg);


            }else{


                List<Bean.CommodityEnshrine> commodity_list = data.getList();

                oid = data.oid;

                for (int i = 0; i < commodity_list.size(); i++) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("takeProjectId", commodity_list.get(i).getMemberId()); //	用户ID
                    map.put("projectName", commodity_list.get(i).getProjectName()); //	商品名称
                    map.put("drtail", commodity_list.get(i).getDrtail()); //	商品介绍
                    map.put("saleNum", commodity_list.get(i).getSaleNum()); //	商品销量
                    map.put("loanAmounts", commodity_list.get(i).getLoanAmounts()); //	商品价格
                    map.put("thumPath", commodity_list.get(i).getThumPath()); //	商品图片
                    map.put("state", "2");                //	判断跳转商品

                    list.add(map);
                }

                Message msg = new Message();
                msg.what = Constant.Commodity;
                hd.sendMessage(msg);
            }



        }
    });




}






}
