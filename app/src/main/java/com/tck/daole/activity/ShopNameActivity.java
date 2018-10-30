package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.ShopAppraiseAdapter;
import com.tck.daole.adapter.ShopNameAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.PayUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.HorNiceRecyclerView;
import com.tck.daole.view.NiceRecyclerView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 店铺名页
 */

public class ShopNameActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back, ll_shop_details;
    private ImageView shop_information;
    private HorNiceRecyclerView dianpu_recyclerview;
    private List<Map<Object, String>> list = new ArrayList<>();
    private ShopNameAdapter adapter;

    private RadioButton rb_shopname_all, rb_shopname_satisfaction, rb_shopname_notSatisfied;
    private NiceRecyclerView rv_shop_name;
    private List<Map<Object, String>> listname = new ArrayList<>();
    private ShopAppraiseAdapter nameadapter;
    private int img_zhuangtai = 1; //表示收藏，0表示未收藏
    private ImageButton dianpu_shoucang;

    private TextView tv_shop_name,tv_shop_name_qisong,tv_shop_name_peisong; //店铺名、起送价、配送价


    private String oid = "";   //店铺id
    private String shap_name, shop_qisong, shop_peisong = ""; //店铺名,起送价、配送价
    private String path = "";   //图片地址
    private String detail = "";  //详情


    private String[] url = {UriUtil.goodComment,UriUtil.mediumComment,UriUtil.badComment};

    Handler hd;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_name);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
        initListener();
        //initData();


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.Shop_Name:



                        //显示图片的配置
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                                .showImageOnFail(R.mipmap.def)//显示错误图片
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(UriUtil.ip+path, shop_information, options);


      /*                  tv_shop_name = findView(R.id.tv_shop_name);
                        tv_shop_name_qisong = findView(R.id.tv_shop_name_qisong);
                        tv_shop_name_peisong = findView(R.id.tv_shop_name_peisong);*/

   /*                     private String shap_name, shop_qisong, shop_peisong = ""; //店铺名,起送价、配送价*/

                        tv_shop_name.setText(shap_name);
                        tv_shop_name_qisong.setText("￥"+shop_qisong);
                        tv_shop_name_peisong.setText(shop_peisong);

                        tv_shop_name_qisong.setVisibility(View.GONE);
                        tv_shop_name_peisong.setVisibility(View.GONE);

                        adapter = new ShopNameAdapter(list, ShopNameActivity.this);
                        dianpu_recyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new ShopNameAdapter.OnItemClickListener() {
                            public void OnItemClick(int position) {

                                String projectId = list.get(position).get("projectId"); //获得商品id
                                String projectLoanAmounts = list.get(position).get("projectLoanAmounts"); //获得商品价格

                                String json_str = "{\"adminStoreOid\":\""+oid+"\",\"projects\":[{\"projectOid\":\""+projectId+"\",\"count\":1}],\"addressOid\":\"\",\"coupon\":\"\",\"servicePrice\":\"\",\"price\":"+projectLoanAmounts+",\"remark\":\"\"}";

                                if (App.islogin) {

                                    Log.i("订单团购",App.token+"\n"+projectId);

                                    //到详情页再支付

                                    //这里到订单页
                                    //onSubmitBooking(App.token,json_str);


                                    Intent intent = new
                                            Intent(ShopNameActivity.this,GroupParticularsActivity.class);
                                    //在Intent对象当中添加一个键值对

                                    intent.putExtra("path",path);   //团商品图片地址
                                    intent.putExtra("shap_name",shap_name); //团商品名字
                                    intent.putExtra("projectLoanAmounts",projectLoanAmounts);   //团商品价格
                                    intent.putExtra("detail",detail);   //团商品介绍


                                    intent.putExtra("json_str",json_str);

                                    startActivity(intent);



                                    //toast("获得商品id去购买商品");

                                } else {
                                    startActivity(new Intent(ShopNameActivity.this, LoginActivity.class));//跳转登录
                                }


                            }
                        });

                        break;

                    case Constant.good_comment:
                        nameadapter = new ShopAppraiseAdapter(listname, ShopNameActivity.this);
                        rv_shop_name.setAdapter(nameadapter);
                        break;

                }

            }
        };

    }

    public void findViewId() {

        ll_back = findView(R.id.ll_back);
        shop_information = findView(R.id.shop_information);
        dianpu_recyclerview = findView(R.id.dianpu_recyclerview);

        rb_shopname_all = findView(R.id.rb_shopname_all);
        rb_shopname_satisfaction = findView(R.id.rb_shopname_satisfaction);
        rb_shopname_notSatisfied = findView(R.id.rb_shopname_notSatisfied);
        rv_shop_name = findView(R.id.rv_shop_name);
        ll_shop_details = findView(R.id.ll_shop_details);
        dianpu_shoucang = findView(R.id.dianpu_shoucang);

        tv_shop_name = findView(R.id.tv_shop_name);
                tv_shop_name_qisong = findView(R.id.tv_shop_name_qisong);
        tv_shop_name_peisong = findView(R.id.tv_shop_name_peisong);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        shop_information.setOnClickListener(this);

        rb_shopname_all.setOnClickListener(this);
        rb_shopname_satisfaction.setOnClickListener(this);
        rb_shopname_notSatisfied.setOnClickListener(this);

        ll_shop_details.setOnClickListener(this);
        dianpu_shoucang.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            oid = intent.getStringExtra("oid");
            getShopName(oid);
        }


/*        ShopData("商品名称");
        adapter = new ShopNameAdapter(list, this);
        dianpu_recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShopNameAdapter.OnItemClickListener() {
            public void OnItemClick(int position) {
                startActivity(new Intent(ShopNameActivity.this, MerchandiseActivity.class));//跳转单个商品
            }
        });*/


        rb_shopname_all.setChecked(true);
        getHaoPing("0","9","1",url[0]);

/*        ShopNameData("用户");
        nameadapter = new ShopAppraiseAdapter(listname, this);
        rv_shop_name.setAdapter(nameadapter);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.shop_information:
                //startActivity(new Intent(ShopNameActivity.this, StoreInformationActivity.class));

                Intent intent = new
                        Intent(ShopNameActivity.this,StoreInformationActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("oid",oid);
                startActivity(intent);

                break;

            case R.id.rb_shopname_all://好评
/*                ShopNameData("好评");
                adapter.notifyDataSetChanged();*/
                getHaoPing("0","9","1",url[0]);
                adapter.notifyDataSetChanged();
                break;
            case R.id.rb_shopname_satisfaction://中评
/*                ShopNameData("中评");
                adapter.notifyDataSetChanged();*/
                getHaoPing("0","9","1",url[1]);
                adapter.notifyDataSetChanged();
                break;
            case R.id.rb_shopname_notSatisfied://差评
/*                ShopNameData("差评");
                adapter.notifyDataSetChanged();*/
                getHaoPing("0","9","1",url[2]);
                adapter.notifyDataSetChanged();
                break;

            case R.id.ll_shop_details://有图
                startActivity(new Intent(ShopNameActivity.this, GroupVoucherActivity.class));
                break;
            case R.id.dianpu_shoucang://收藏店铺
                if (img_zhuangtai == 1) {
                    dianpu_shoucang.setImageResource(R.mipmap.ic_shoucang_no);
                    img_zhuangtai = 0;
                } else {
                    dianpu_shoucang.setImageResource(R.mipmap.ic_shoucang_yes);
                    img_zhuangtai = 1;
                }
        }
    }


    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            Map<Object, String> map1 = new HashMap<>();
            map1.put("img", "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1514341664&di=538e8e2eddf98f2157a95617c33dc235&src=http://pic122.nipic.com/file/20170217/20860925_143422405000_2.jpg");
            map1.put("name", str);
            map1.put("xianjia", "¥25");
            map1.put("yuanjia", "¥33");
            list.add(map1);
        }
    }


    private void ShopNameData(String str) {
        listname.clear();
        for (int i = 0; i < 3; i++) {
            Map<Object, String> map2 = new HashMap<>();
            map2.put("name", str);
            map2.put("state", "1");

            Map<Object, String> map3 = new HashMap<>();
            map3.put("name", str);
            map3.put("state", "1");
            listname.add(map2);
            listname.add(map3);
        }
    }


    public void getShopName(final String oid) {

    /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("adminStore.oid", oid);

        HttpUtils.doPost(UriUtil.shop_name, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("66666",response.body().string());

                String str = response.body().string();

                Log.i("团购",str);

                Bean.ShopNameModel data = new Gson().fromJson(str, Bean.ShopNameModel.class);
                Bean.ShopName shopName = data.getModel();

                shap_name = shopName.getName();
                shop_qisong = shopName.getLowestPrice();
                shop_peisong = shopName.getServicePrice();
                path = shopName.getThumPath();
                detail = shopName.getDetail();




                List<Bean.projectThumPath> shopName_list = data.list;
                list.clear();
                for (int i = 0; i < shopName_list.size(); i++) {
                    Map<Object, String> map2 = new HashMap<>();

                    map2.put("projectId", shopName_list.get(i).projectId);
                    map2.put("projectName", shopName_list.get(i).projectName);
                    map2.put("projectThumPath", shopName_list.get(i).projectThumPath);
                    map2.put("projectLoanAmounts", shopName_list.get(i).projectLoanAmounts);


                    list.add(map2);
                }


                Message msg = new Message();
                msg.what = Constant.Shop_Name;
                hd.sendMessage(msg);

            }
        });

    }




    public void getHaoPing(final String index,final String num,final String oid,final String url){

        /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String,String> map = new HashMap<>();
        map.put("page.index",index);
        map.put("page.num",num);
        map.put("evaluate_adminStoreId_oid",oid);


        HttpUtils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Bean.CommentModel data = new Gson().fromJson(response.body().string(), Bean.CommentModel.class);
                List<Bean.Comment> comment_list = data.getList();


                listname.clear();

                for (int i = 0; i < comment_list.size(); i++) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("state","1"); //	店铺ID
                    map.put("adminsStoreId", comment_list.get(i).getAdminsStoreId()); //	店铺ID
                    map.put("name", comment_list.get(i).getName()); //店铺名字;
                    map.put("cryptonym", comment_list.get(i).getCryptonym()); //=是否匿名（匿名返回“匿名用户”，不匿名返回“用户nickName”）;
                    map.put("head", comment_list.get(i).getHead()); //评论人头像;
                    map.put("starlevel", comment_list.get(i).getStarlevel()); //评论星级;
                    map.put("evaluatetime", comment_list.get(i).getEvaluatetime()); //=评论时间;
                    map.put("content", comment_list.get(i).getContent()); //=评论内容;
                    map.put("commentLevel", comment_list.get(i).getCommentLevel()); //=评论等级;

                    listname.add(map);
                }

                Message msg = new Message();
                msg.what = Constant.good_comment;
                hd.sendMessage(msg);

            }
        });

    }



    public void onSubmitBooking(final String token, final String param) {

        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.submitBooking(token, param,"4", new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("提交订单.成功", result);

                            //PayUtil ApliyPay  进行支付
                            @SuppressLint("SimpleDateFormat") String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                            //Log.i("提交233",no);

                            new PayUtil(ShopNameActivity.this,httpInterface).ApliyPay(no,"","","");

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


}
