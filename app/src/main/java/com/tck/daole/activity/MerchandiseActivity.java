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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.ShopAppraiseAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
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

/**
 * 店铺单个的商品页面
 */
public class MerchandiseActivity extends BaseActivity {
    private LinearLayout ll_back,linear;
    private RadioButton merchandise_haoping, merchandise_zhongping, merchandise_chaping;
    private NiceRecyclerView rv_merchandise;
    private List<Map<Object, String>> list = new ArrayList<>();
    private ShopAppraiseAdapter adapter;
    private TextView tv_Merchandise_sum;
    private ImageView btn_Merchandise_jian, btn_Merchandise_jia,img_merchandise;    //第三个是图片
    private int img_zhuangtai = 1; //表示收藏，0表示未收藏

    private LinearLayout ll_merchandise_path;   //商品图片
    //商品名称、月售、价格、备注、店铺名、商品信息、商品介绍
    private TextView tv_merchandise_name, tv_merchandise_yueshou,tv_merchandise_jiage, tv_merchandise_beizhu, tv_merchandise_dianpuming, tv_merchandise_shangpinxinxi, tv_merchandise_shangpinjieshao;

    String num = "";    //商品已选数量
    String merchandise_oid = "";//商品ID
    String adminStoreName = "";//店铺名字;
    String projectName = "";//商品名字;
    String saleNum = "";//月售;
    String detail = "";//商品详情(备注)
    String loanAmounts = ""; //商品价格;
    String introduction = "";//商品介绍(商品信息);
    String path = "";//商品图片;

    Handler hd;
//    private String sp_token = "";

    private String[] url = {UriUtil.satisfaction,UriUtil.entire,UriUtil.yawp};

    String takeProjectId = "2";
    private String oid = "1";
    private String cartNum = "0";

    private int Merchandise_sum = 1;//商品数量
    private double price = 0.0; //商品单价
    public double price_sum = 0;    //商品总价
    private TextView tv_price_sum;  //显示总价


    private CheckBox merchandise_shoucang;
    private String dianzan = "1";   //设置默认为收藏


    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_merchandise);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);
        merchandise_haoping = findView(R.id.merchandise_haoping);
        merchandise_zhongping = findView(R.id.merchandise_zhongping);
        merchandise_chaping = findView(R.id.merchandise_chaping);
        rv_merchandise = findView(R.id.rv_merchandise);
        tv_Merchandise_sum = findView(R.id.tv_Merchandise_sum);
        btn_Merchandise_jian = findView(R.id.btn_Merchandise_jian);
        btn_Merchandise_jia = findView(R.id.btn_Merchandise_jia);
        merchandise_shoucang = findView(R.id.merchandise_shoucang);


        ll_merchandise_path = findView(R.id.ll_merchandise_path);
        tv_merchandise_name = findView(R.id.tv_merchandise_name);
        tv_merchandise_yueshou = findView(R.id.tv_merchandise_yueshou);
        tv_merchandise_jiage = findView(R.id.tv_merchandise_jiage);
        tv_merchandise_beizhu = findView(R.id.tv_merchandise_beizhu);
        tv_merchandise_dianpuming = findView(R.id.tv_merchandise_dianpuming);
        tv_merchandise_shangpinxinxi = findView(R.id.tv_merchandise_shangpinxinxi);
        tv_merchandise_shangpinjieshao = findView(R.id.tv_merchandise_shangpinjieshao);

        img_merchandise = findView(R.id.img_merchandise);
        tv_price_sum = findView(R.id.tv_price_sum);

        linear = findView(R.id.linear);


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

/*                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");*/


                switch (msg.what) {
                    case Constant.Merchandise:

                        //商品图片


                        tv_Merchandise_sum.setText(num);

                        Merchandise_sum = Integer.parseInt(num);
                        //商品名称、月售、\价格、备注、店铺名、商品信息（商品介绍）
                        tv_merchandise_name.setText(projectName);
                        tv_merchandise_yueshou.setText("月售"+saleNum);
                        tv_merchandise_jiage.setText("¥"+loanAmounts);
                        tv_merchandise_beizhu.setText(detail);

                        tv_merchandise_dianpuming.setText(adminStoreName);
                        //tv_merchandise_shangpinxinxi.setText(loanAmounts);
                        tv_merchandise_shangpinjieshao.setText(introduction);


                        //显示图片的配置
                       DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                                .showImageOnFail(R.mipmap.def)//显示错误图片
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(UriUtil.ip+path, img_merchandise, options);


                        price = Double.parseDouble(loanAmounts);

                        price_sum = Merchandise_sum * price;
                        tv_price_sum.setText("￥"+price_sum);


 /*                       String merchandise_oid = "";//商品主键
                        String projectName = "";//商品名称
                        String saleNum = "";//销量
                        String detail = "";//商品详情
                        String loanAmounts = ""; //商品价格
                        String introduction = "";//商品介绍
                        String path = "";//商品图片

                        */
                        break;


                    case Constant.Ping_Jia:
                        adapter = new ShopAppraiseAdapter(list, MerchandiseActivity.this);
                        rv_merchandise.setAdapter(adapter);
                        break;

                }

            }
        };


    }


    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        merchandise_haoping.setOnClickListener(this);
        merchandise_zhongping.setOnClickListener(this);
        merchandise_chaping.setOnClickListener(this);
        btn_Merchandise_jian.setOnClickListener(this);
        btn_Merchandise_jia.setOnClickListener(this);
        merchandise_shoucang.setOnClickListener(this);
        linear.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            //cartNum = intent.getStringExtra("cartNum");

            //tv_Merchandise_sum.setText(cartNum);

            if (App.islogin) {
                oid = intent.getStringExtra("oid");
                takeProjectId = intent.getStringExtra("takeProjectId");
                getMerchandise(App.token, takeProjectId);

                getType(App.token,takeProjectId);

            }


        }

//        sp_token = SPUtil.getData(MerchandiseActivity.this, "token",
//                "").toString();

        merchandise_haoping.setChecked(true);

        getPingJia("0","9",takeProjectId,url[0]);

/*        merchandise_haoping.setChecked(true);
        ShopData("好评");
        adapter = new ShopAppraiseAdapter(list, this);
        rv_merchandise.setAdapter(adapter);*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:

                //startActivity(new Intent(MerchandiseActivity.this, ShopActivity.class));//跳转回店铺和分类

//                Intent intent_back = new
//                        Intent(MerchandiseActivity.this,ShopActivity.class);
//                //在Intent对象当中添加一个键值对
//                intent_back.putExtra("oid",oid);
//                startActivity(intent_back);

                finish();


                break;
            case R.id.merchandise_haoping://好评
/*                ShopData("好评");
                adapter.notifyDataSetChanged();*/
                getPingJia("0","9",takeProjectId,url[0]);
                adapter.notifyDataSetChanged();
                break;
            case R.id.merchandise_zhongping://中评
/*                ShopData("中评");
                adapter.notifyDataSetChanged();*/
                getPingJia("0","9",takeProjectId,url[1]);
                adapter.notifyDataSetChanged();
                break;
            case R.id.merchandise_chaping://差评
/*                ShopData("差评");
                adapter.notifyDataSetChanged();*/
                getPingJia("0","9",takeProjectId,url[2]);
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_Merchandise_jia://加商品数量
                Merchandise_sum = Merchandise_sum + 1;
                tv_Merchandise_sum.setText(String.valueOf(Merchandise_sum));

                if (App.islogin){
                    //Toast.makeText(context,"加一个商品",Toast.LENGTH_SHORT).show();
                    addShop(App.token,takeProjectId,"1");

                    price_sum = Merchandise_sum * price;
                    tv_price_sum.setText("￥"+price_sum);
                    //addShop(sp_token,"1","1");
                    //Log.i("ShopTypeAdapter",take_Project_Id);
                }else{
                    Intent intent = new
                            Intent(MerchandiseActivity.this,LoginActivity.class);
                    //在Intent对象当中添加一个键值对
                    //intent.putExtra("key","value");
                    startActivity(intent);
                    Toast.makeText(MerchandiseActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_Merchandise_jian://减商品数量
                if (Merchandise_sum == 0) {
                    Merchandise_sum = 0;
                } else {
                    Merchandise_sum = Merchandise_sum - 1;

                    if (App.islogin){
                        //Toast.makeText(context,"加一个商品",Toast.LENGTH_SHORT).show();
                        deleteShop(App.token,takeProjectId,"1");

                        price_sum = Merchandise_sum * price;
                        tv_price_sum.setText("￥"+price_sum);
                        //addShop(sp_token,"1","1");
                        //Log.i("ShopTypeAdapter",take_Project_Id);
                    }else{
                        Intent intent = new
                                Intent(MerchandiseActivity.this,LoginActivity.class);
                        //在Intent对象当中添加一个键值对
                        //intent.putExtra("key","value");
                        startActivity(intent);
                        Toast.makeText(MerchandiseActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                    }

                }
                tv_Merchandise_sum.setText(String.valueOf(Merchandise_sum));
                break;

            case R.id.linear:
                startActivityForResult(new Intent(MerchandiseActivity.this, ShopcatActivity.class),666);//跳转购物车
                break;

            case R.id.merchandise_shoucang://收藏
                if(dianzan.equals("1")){    //如果点赞了
                    merchandise_shoucang.setChecked(false);      //变为未点赞
                    dianzan = "2";  ////变为未点赞
                    //changeType(App.token,oid);;

                    Log.i("typeoid",App.token+"\n"+oid);
                    changeType(App.token,takeProjectId);

                }else{  //如果未点赞
                    merchandise_shoucang.setChecked(true);   //变为点赞
                    dianzan = "1";  //变为已点赞

                    Log.i("typeoid",App.token+"\n"+oid);
                    changeType(App.token,takeProjectId);

                    //changeType(App.token,oid);


                }
                break;


        }
    }

    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 3; i++) {
            Map<Object, String> map1 = new HashMap<>();
            map1.put("name", str);
            map1.put("state", "1");

            Map<Object, String> map2 = new HashMap<>();
            map2.put("name", str);
            map2.put("state", "1");
            list.add(map1);
            list.add(map2);
        }
    }


    public void getMerchandise(final String token,final String oid) {

    /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("takeProject.oid", oid);


        Log.i("map",map+"");

        HttpUtils.doPost(UriUtil.storeMaterial, map, new Callback() {
            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("66666", response.body().string());

                String str = response.body().string();
                Log.i("66666", str);

                Bean.MerchandiseModel merchandise = new Gson().fromJson(str, Bean.MerchandiseModel.class);

                if(merchandise.status==1){
                    Bean.Merchandise merchandise_msg = merchandise.getModel();
                    //Log.i("66666", str);

                    num = merchandise_msg.num;//商品已选数量
                    //merchandise_oid =merchandise_msg.getOid();//商品ID
                    adminStoreName = merchandise_msg.getAdminStoreName();//店铺名字;
                    projectName = merchandise_msg.getProjectName();//商品名字;
                    saleNum = merchandise_msg.getSaleNum();//月售;
                    detail = merchandise_msg.getDetail();//商品详情(备注)
                    loanAmounts =merchandise_msg.getLoanAmounts(); //商品价格;
                    introduction = merchandise_msg.getIntroduction();//商品介绍(商品信息);
                    path = merchandise_msg.getPath();//商品图片;

                    //Log.i("99999",merchandise_oid);

                    Message msg = new Message();
                    msg.what = Constant.Merchandise;
                    hd.sendMessage(msg);
                }

            }
        });



    }



    public void getPingJia(final String index,final String num,final String oid,final String url){

        /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String,String> map = new HashMap<>();
        map.put("page.index",index);
        map.put("page.num",num);
        map.put("evaluate_projectId_oid",oid);


        HttpUtils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Bean.CommentModel data = new Gson().fromJson(response.body().string(), Bean.CommentModel.class);


                if(data.getStatus() == 0){
                    Log.i("Merchan",data.getMessage());
                }else{
                    Log.i("Merchan",data.getMessage());

                    list.clear();
                    List<Bean.Comment> comment_list = data.getList();
                    for (int i = 0; i < comment_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("state","1"); //	店铺ID
                        map.put("adminsStoreId", comment_list.get(i).getProjectId()); //	商品ID
                        map.put("name", comment_list.get(i).getProjectName()); //商品名字;
                        map.put("cryptonym", comment_list.get(i).getCryptonym()); //=是否匿名（匿名返回“匿名用户”，不匿名返回“用户nickName”）;
                        map.put("head", comment_list.get(i).getHead()); //评论人头像;
                        map.put("starlevel", comment_list.get(i).getStarlevel()); //评论星级;
                        map.put("evaluatetime", comment_list.get(i).getEvaluatetime()); //=评论时间;
                        map.put("content", comment_list.get(i).getContent()); //=评论内容;
                        map.put("commentLevel", comment_list.get(i).getCommentLevel()); //=评论等级;

                        list.add(map);
                    }

                    Message msg = new Message();
                    msg.what = Constant.Ping_Jia;
                    hd.sendMessage(msg);
                }


            }
        });

    }







    //添加商品
    public void addShop(final String token,final String take_Project_Id,final String num) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                //Thread.sleep(3000);//休眠3秒
                /**
                 * 要执行的操作
                 */

                Map<String,String> map = new HashMap<>();
                map.put("token",token);
                map.put("commodityOid",take_Project_Id);
                map.put("shoppingCart.num",num);


                HttpUtils.doPost(UriUtil.Add_Type_Shop, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.i("ShopTypeAdapter",response.body().string());

                        Bean data = new Gson().fromJson(response.body().string(),
                                Bean.class);
                        int status = data.getStatus();
                        if(status == 9){
                            Log.i("ShopTypeAdapter",data.getMessage());
                        }else{
                            Log.i("ShopTypeAdapter",data.getMessage());
                        }
                    }
                });

            }
        }.start();

    }




    //减少商品
    public void deleteShop(final String token,final String take_Project_Id,final String num) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                //Thread.sleep(3000);//休眠3秒
                /**
                 * 要执行的操作
                 */

                Map<String,String> map = new HashMap<>();
                map.put("token",token);
                map.put("commodityOid",take_Project_Id);
                map.put("shoppingCart.num",num);


                HttpUtils.doPost(UriUtil.Delete_Type_Shop, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.i("ShopTypeAdapter",response.body().string());

                        Bean data = new Gson().fromJson(response.body().string(),
                                Bean.class);
                        int status = data.getStatus();
                        if(status == 9){
                            Log.i("ShopTypeAdapter",data.getMessage());
                        }else{
                            Log.i("ShopTypeAdapter",data.getMessage());
                        }
                    }
                });

            }
        }.start();

    }





    //获得商品收藏状态收藏
    public void getType(final String token,final String projectId) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getMerchandiseType(token, projectId,new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.i("typeoid+++",App.token+"\n"+projectId);


                            Log.e("typeoid+++获得收藏状态.成功", result);

                            Bean.ShoucangType shoucang_type = new Gson().fromJson(result,Bean.ShoucangType.class);
                            if(shoucang_type.status == 1){
                                String type = shoucang_type.commodityStatus;
                                if(type.equals("1")){
                                    merchandise_shoucang.setChecked(true);
                                    dianzan = "1";
                                }
                                if(type.equals("2")){
                                    merchandise_shoucang.setChecked(false);
                                    dianzan = "2";
                                }
                            }

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获得收藏状态.异常", response);

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












    //获得商品收藏状态收藏
    public void changeType(final String token,final String projectId) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.changeMerchandiseType(token, projectId,new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("typeoid++修改收藏状态.成功", result);

/*                            Bean.ShoucangType shoucang_type = new Gson().fromJson(result,Bean.ShoucangType.class);
                            if(shoucang_type.status == 1){
                                String type = shoucang_type.commodityStatus;
                                if(type.equals("1")){
                                    merchandise_shoucang.setChecked(true);
                                    dianzan = "1";
                                }
                                if(type.equals("2")){
                                    merchandise_shoucang.setChecked(false);
                                    dianzan = "2";
                                }
                            }*/

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获得收藏状态.异常", response);

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
