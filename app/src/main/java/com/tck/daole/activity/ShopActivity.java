package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.ShopTypeAdapter;
import com.tck.daole.adapter.SortAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.StringUtil;
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

/**
 * 店铺页面 商品分类和商品列表
 */
public class ShopActivity extends BaseActivity {
    private LinearLayout ll_back, ll_shop,linear;
    private NiceRecyclerView rv_shop;
    private List<Map<Object, String>> typelist = new ArrayList<>();
    private List<Map<Object, String>> list = new ArrayList<>();
    private ShopTypeAdapter adapter;

    private ImageView shop_information;
    private TextView tv_shop_type_name, tv_shop_type_time, tv_shop_type_qisong, tv_shop_type_peisong, tv_shop_type_youhui;

    private NiceRecyclerView rv_sort;

    private Handler hd;
    private Context mContext;
//    private String sp_token;

    //店铺ID,店铺图片、店铺名称、起送价、配送价、送达时间、店铺优惠情况
    private String adminStoreId, logoImage, storeName, lowestPrice, servicePrice, sendTime, storeMessage, cartNum = "";

    private SortAdapter mSortAdapter;

    private String oid = "1";

    double price_sum = 0.0;

    private TextView tv_sum_price;

    private CheckBox img_posted_particulars_shoucang;

    private String dianzan = "1";   //设置默认为收藏

    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }
        mContext = this;


        ll_back = findView(R.id.ll_back);
        ll_shop = findView(R.id.ll_shop);
        linear = findView(R.id.linear);
        rv_shop = findView(R.id.rv_shop);
        shop_information = findView(R.id.shop_information);

        rv_sort = findView(R.id.rv_sort);

        tv_shop_type_name = findView(R.id.tv_shop_type_name);
        tv_shop_type_time = findView(R.id.tv_shop_type_time);
        tv_shop_type_qisong = findView(R.id.tv_shop_type_qisong);
        tv_shop_type_peisong = findView(R.id.tv_shop_type_peisong);
        tv_shop_type_youhui = findView(R.id.tv_shop_type_youhui);
        tv_sum_price = findView(R.id.tv_sum_price);

        img_posted_particulars_shoucang = findView(R.id.img_posted_particulars_shoucang);

        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                Bundle b;
                b = msg.getData();
                String count_price = b.getString("count_price");


                switch (msg.what) {
                    case Constant.Shop_Type_List:

                        tv_sum_price.setText(String.valueOf(price_sum));


                        //shop_information

                        //tv_shop_type_name,tv_shop_type_time,tv_shop_type_qisong,tv_shop_type_peisong,tv_shop_type_youhui;

                        //private String logoImage,storeName,lowestPrice,servicePrice,sendTime,storeMessage = "";


                        //显示图片的配置
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                                .showImageOnFail(R.mipmap.def)//显示错误图片
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(UriUtil.ip + logoImage, shop_information, options);

                        tv_shop_type_name.setText(storeName);
                        tv_shop_type_time.setText(sendTime+"分钟");
                        tv_shop_type_qisong.setText("起送价￥"+lowestPrice);
                        tv_shop_type_peisong.setText("配送价￥"+servicePrice);
                        tv_shop_type_youhui.setText("优惠￥"+storeMessage);


                        //Log.i("147",typelist.get(0).get("GoodsCategoryName"));


                        mSortAdapter = new SortAdapter(typelist, ShopActivity.this);
                        rv_sort.setAdapter(mSortAdapter);

                        if (typelist.size() != 0) {
                            ////////////////////////////////
                            getShopContent(typelist.get(0).get("GoodsCategoryId"), App.token, adminStoreId);
                        }


                        mSortAdapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                //toast((String)typelist.get(position).get("GoodsCategoryId"));
                                //Log.i("147",(String)typelist.get(position).get("GoodsCategoryId"));


                                //商品分类、店铺ID、
                                //(String)typelist.get(position).get("GoodsCategoryId")、adminStoreId、

                                String category_oid = (String) typelist.get(position).get("GoodsCategoryId");    //商品分类oid


                                if (App.islogin) {

                                    //Log.i("ShopActivity",sp_token);
                                    //getShopContent("1", sp_token,"1");

                                    getShopContent(category_oid, App.token, adminStoreId);

                                    //getUserInformation(sp_token);
                                } else {
                                    startActivity(new Intent(ShopActivity.this, LoginActivity.class));
                                }


                            }
                        });

                        break;


                    case Constant.Shop_Content_List:


                        adapter.notifyDataSetChanged();
/*                        adapter = new ShopTypeAdapter(list, ShopActivity.this);
                        rv_shop.setAdapter(adapter);
                        adapter.setOnItemClickListener(new ShopTypeAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                toast("商品");
                            }
                        });*/

                        //startActivity(new Intent(ShopActivity.this, MerchandiseActivity.class));//跳转单个商品

                        break;

                    case Constant.Shop_Type_List_Error:
//                        toast(msgStr);
                        break;

                    case Constant.Add_Count_Price:
                        //toast(count_price); //商品价格

                        if (StringUtil.isSpace(count_price)){
                            count_price="0.00";
                        }
                        double add_price = Double.parseDouble(count_price);

                        price_sum = price_sum + add_price;

                        tv_sum_price.setText(String.valueOf(price_sum));


                        break;

                    case Constant.Jian_Count_Price:
                        //toast(count_price); //商品价格

                        if (StringUtil.isSpace(count_price)){
                            count_price="0.00";
                        }
                        double jian_price = Double.parseDouble(count_price);

                        price_sum = price_sum - jian_price;

                        tv_sum_price.setText(String.valueOf(price_sum));

                        break;






                }

            }
        };

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        linear.setOnClickListener(this);
        shop_information.setOnClickListener(this);
        img_posted_particulars_shoucang.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ll_shop.getBackground().setAlpha(50);//0~255透明度值
        price_sum = 0.00;

//取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            oid = intent.getStringExtra("oid");

            if (App.islogin) {
                getShopType(oid);

                ShoucangType(App.token,oid);

            } else {
                startActivity(new Intent(ShopActivity.this, LoginActivity.class));//跳转登录
                finish();
            }


        }




/*        ShopData("分类1");
        adapter = new ShopAdapter(list, this);
        rv_shop.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                startActivity(new Intent(ShopActivity.this, MerchandiseActivity.class));//跳转单个商品
            }
        });*/

        adapter = new ShopTypeAdapter(list, ShopActivity.this,hd);
        rv_shop.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShopTypeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
//                toast(list.get(position).get("takeProjectId"));

                //Log.i("进入商品详情", list.get(position).get("takeProjectId"));


                Intent intent_tuangou = new
                        Intent(ShopActivity.this, MerchandiseActivity.class);
                //在Intent对象当中添加一个键值对
                intent_tuangou.putExtra("takeProjectId", list.get(position).get("takeProjectId"));
                //intent_tuangou.putExtra("cartNum", list.get(position).get("cartNum"));
                intent_tuangou.putExtra("oid", oid);

                startActivity(intent_tuangou);

                finish();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.linear:
                startActivityForResult(new Intent(ShopActivity.this, ShopcatActivity.class),666);//跳转购物车
                break;

            case R.id.shop_information:
                //startActivity(new Intent(ShopActivity.this, ShopAppraiseActivity.class));//跳转单个商品

                Intent intent = new
                        Intent(ShopActivity.this,StoreInformationActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("oid",oid);
                startActivity(intent);

                break;
            case R.id.img_posted_particulars_shoucang:

                if(dianzan.equals("1")){    //如果点赞了
                    img_posted_particulars_shoucang.setChecked(false);      //变为未点赞
                    dianzan = "2";  ////变为未点赞
                    //dianzan_url_zhuangtai = dianzan_url[1];   //使用取消点赞接口
                    changeType(App.token,oid);;

                    Log.i("typeoid",App.token+"\n"+oid);


                }else{  //如果未点赞
                    img_posted_particulars_shoucang.setChecked(true);   //变为点赞
                    dianzan = "1";  //变为已点赞
                    //dianzan_url_zhuangtai = dianzan_url[0];   //使用点赞接口

                    Log.i("typeoid",App.token+"\n"+oid);

                    changeType(App.token,oid);


                }
                break;



        }
    }

    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            Map<Object, String> map1 = new HashMap<>();
            map1.put("name", str);
            list.add(map1);
        }
    }


    public void getShopType(final String oid) {
        Map<String, String> map = new HashMap<>();
        map.put("adminStore.oid", oid);

        HttpUtils.doPost(UriUtil.Get_Shop_Type, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("147",response.body().string());


                Bean.ShopTypeModel data = new Gson().fromJson(response.body().string(),
                        Bean.ShopTypeModel.class);

                if (data.getStatus() == 0) {

                    Bundle b = new Bundle();
                    b.putString("msg", data.getMessage());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Shop_Type_List_Error;
                    hd.sendMessage(msg);

                } else {
                    Bean.ShopType shop_Information = data.getModel();

                    //店铺图片、店铺名称、起送价、配送价、送达时间、店铺优惠情况
/*            private String logoImage,storeName,lowestPrice,servicePrice,sendTime,storeMessage = "";*/

                    adminStoreId = shop_Information.getAdminStoreId();

                    logoImage = shop_Information.getLogoImage();
                    storeName = shop_Information.getStoreName();
                    lowestPrice = shop_Information.getLowestPrice();
                    servicePrice = shop_Information.getServicePrice();
                    sendTime = shop_Information.getSendTime();
                    storeMessage = shop_Information.getStoreMessage();


//Log.i("147",logoImage+"\n"+storeName+"\n"+lowestPrice+"\n"+servicePrice+"\n"+sendTime+"\n"+storeMessage+"\n");

                    List<Bean.GoodsCategory> type_list = shop_Information.getGoodsCategory();

                    for (int i = 0; i < type_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("GoodsCategoryId", type_list.get(i).getGoodsCategoryId()); //商品分类ID
                        map.put("GoodsCategoryName", type_list.get(i).getGoodsCategoryName()); //商品分类的类名
                        map.put("storeId", type_list.get(i).getStoreId()); //商品分类的归属店铺ID

                        //Log.i("147",type_list.get(i).getGoodsCategoryName());
                        typelist.add(map);






                        if (App.islogin) {
                            //Log.i("ShopActivity",sp_token);
                            //getShopContent("1", sp_token,"1");
                            getSumContent(type_list.get(i).getGoodsCategoryId(), App.token, adminStoreId);  //商品id,token,店铺id
                            //getUserInformation(sp_token);
                        } else {
                            startActivity(new Intent(ShopActivity.this, LoginActivity.class));
                        }


                    }




                    //Bundle b = new Bundle();
                    //b.putString("msg", result);
                    Message msg = new Message();
                    //msg.setData(b);
                    msg.what = Constant.Shop_Type_List;
                    hd.sendMessage(msg);
                }



/*            for (int i = 0; i < spell_list.size(); i++) {
                Map<Object, String> map = new HashMap<>();
                map.put("Oid", spell_list.get(i).getOid()); //	oid主键
                map.put("ProjectName", spell_list.get(i).getProjectName()); //商品
                名称
                map.put("LoanAmounts", spell_list.get(i).getLoanAmounts()); //价格
                map.put("SaleNum", spell_list.get(i).getSaleNum()); //销量
                map.put("ThumPath", spell_list.get(i).getProjectImage
                        ().getThumPath()); //图片地址
                list.add(map);
            }*/


            }

        });

    }


//获得分类的商品列表

    public void getShopContent(final String goodsCategoryOid, final String token, final String adminStore) {


        Map<String, String> map = new HashMap<>();
        map.put("goodsCategoryOid", goodsCategoryOid);
        map.put("token", token);
        map.put("adminStore.oid", adminStore);

        Log.i("ShopActivity", goodsCategoryOid + "\n" + token + "\n" + adminStore);

        HttpUtils.doPost(UriUtil.Get_Shop_Content, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("ShopActivity", str);

                Bean.ShopTypeContentModel data = new Gson().fromJson(str, Bean.ShopTypeContentModel.class);
                if (data.getStatus() == 0) {
                    //Log.i("ShopActivity",data.getMessage());
                } else {
                    List<Bean.ShopTypeContent> shop_type_list = data.getModel();
                    if(shop_type_list==null) {
                        Log.i("没有请求到数据","没有请求到数据");
                        return;
                    }
/*            takeProjectId=商品ID
            name=商品名字
            detail=商品详情
            saleNum=商品销量
            price=商品单价
            projectImage=商品分类ID*/

//Log.i("ShopActivity",shop_type_list.get(0).getNum());

                    list.clear();
                    for (int i = 0; i < shop_type_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();

                        map.put("num", shop_type_list.get(i).getNum()); //	商品已选数量
                        map.put("takeProjectId", shop_type_list.get(i).getTakeProjectId()); //	商品ID
                        map.put("name", shop_type_list.get(i).getName()); //商品名字
                        map.put("detail", shop_type_list.get(i).getDetail()); //商品详情
                        map.put("saleNum", shop_type_list.get(i).getSaleNum()); //商品销量
                        map.put("price", shop_type_list.get(i).getPrice()); //商品单价
                        map.put("projectImage", shop_type_list.get(i).getProjectImage()); //商品图片
                        map.put("cartNum", shop_type_list.get(i).getCartNum()); //	加入购物车商品已选数量
                        map.put("title", shop_type_list.get(i).title); //	商品分类列表介绍




 /*                       String num = shop_type_list.get(i).getCartNum();
                        if (StringUtil.isSpace(num)){
                            num="0";
                        }
                        int count_num = Integer.parseInt(num);
                        double price = Double.parseDouble(shop_type_list.get(i).getPrice());
                        price_sum = price_sum+(count_num*price);

                        Log.i("sum",""+count_num+"\n"+price);*/

                        list.add(map);
                    }

                    Message msg = new Message();
                    msg.what = Constant.Shop_Content_List;
                    hd.sendMessage(msg);
                }


            }
        });

    }










    //获得店铺收藏状态
    public void ShoucangType(final String token,final String adminStoreId) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getShoucangType(token, adminStoreId,new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取收藏状态.成功", result);

                            Bean.ShoucangType shoucang_type = new Gson().fromJson(result,Bean.ShoucangType.class);
                            if(shoucang_type.status == 1){
                                String type = shoucang_type.collectionOfShopsStatus;
                                if(type.equals("1")){
                                    img_posted_particulars_shoucang.setChecked(true);
                                    dianzan = "1";
                                }
                                if(type.equals("2")){
                                    img_posted_particulars_shoucang.setChecked(false);
                                    dianzan = "2";
                                }
                            }

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取收藏状态.异常", response);

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




    //添加或取消收藏
    public void changeType(final String token,final String storeOid) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.changeShoucangType(token, storeOid,new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("修改收藏状态.成功", result);

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("修改收藏状态.异常", response);

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















    //第一次遍历商品计算总价

    public void getSumContent(final String goodsCategoryOid, final String token, final String adminStore) {


        Map<String, String> map = new HashMap<>();
        map.put("goodsCategoryOid", goodsCategoryOid);
        map.put("token", token);
        map.put("adminStore.oid", adminStore);

        Log.i("ShopActivity", goodsCategoryOid + "\n" + token + "\n" + adminStore);

        HttpUtils.doPost(UriUtil.Get_Shop_Content, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("ShopActivity", str);

                Bean.ShopTypeContentModel data = new Gson().fromJson(str, Bean.ShopTypeContentModel.class);
                if (data.getStatus() == 0) {
                    //Log.i("ShopActivity",data.getMessage());
                } else {
                    List<Bean.ShopTypeContent> shop_type_list = data.getModel();
                    if(shop_type_list==null) {
                        Log.i("没有请求到数据","没有请求到数据");
                        return;
                    }
/*            takeProjectId=商品ID
            name=商品名字
            detail=商品详情
            saleNum=商品销量
            price=商品单价
            projectImage=商品分类ID*/

//Log.i("ShopActivity",shop_type_list.get(0).getNum());

                    //list.clear();
                    for (int i = 0; i < shop_type_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();

                        map.put("num", shop_type_list.get(i).getNum()); //	商品已选数量
                        map.put("takeProjectId", shop_type_list.get(i).getTakeProjectId()); //	商品ID
                        map.put("name", shop_type_list.get(i).getName()); //商品名字
                        map.put("detail", shop_type_list.get(i).getDetail()); //商品详情
                        map.put("saleNum", shop_type_list.get(i).getSaleNum()); //商品销量
                        map.put("price", shop_type_list.get(i).getPrice()); //商品单价
                        map.put("projectImage", shop_type_list.get(i).getProjectImage()); //商品图片
                        map.put("cartNum", shop_type_list.get(i).getCartNum()); //	加入购物车商品已选数量



                        String num = shop_type_list.get(i).getCartNum();
                        if (StringUtil.isSpace(num)){
                            num="0";
                        }
                        int count_num = Integer.parseInt(num);
                        double price = Double.parseDouble(shop_type_list.get(i).getPrice());
                        price_sum = price_sum+(count_num*price);

                        Log.i("sum",""+count_num+"\n"+price);

                        list.add(map);
                    }


                }


            }
        });

    }








}
