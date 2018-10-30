package com.tck.daole.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.adapter.ShopAppraiseAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 商家评价
 */
public class ShopAppraiseActivity extends BaseActivity {
    private LinearLayout ll_back;
    private NiceRecyclerView rv_shop;

    private List<Map<Object, String>> list = new ArrayList<>();
    private ShopAppraiseAdapter adapter;
    private RadioButton rb_shop_all, rb_shop_satisfaction, rb_shop_notSatisfied;


    String oid = "";

    private List<Map<Object, String>> listname = new ArrayList<>(); //商品平理论列表
    private ShopAppraiseAdapter nameadapter;

    private String spell_oid = "";
    private String name = "";
    private double loanAmounts = 0.0;    //价格
    public String imgpath = ""; //图片地址

    private ImageView iv_shop_img;  //店铺头像
    private TextView tv_shop_name,str_ratingBar;  //店铺名称,店铺星级
    RatingBar ratingBar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_appraise);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);
        rv_shop = findView(R.id.rv_shop);
        rb_shop_all = findView(R.id.rb_shop_all);
        rb_shop_satisfaction = findView(R.id.rb_shop_satisfaction);
        rb_shop_notSatisfied = findView(R.id.rb_shop_notSatisfied);

        iv_shop_img = findView(R.id.iv_shop_img);
        tv_shop_name = findView(R.id.tv_shop_name);
        ratingBar = findView(R.id.ratingBar);
        str_ratingBar = findView(R.id.str_ratingBar);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        rb_shop_all.setOnClickListener(this);
        rb_shop_satisfaction.setOnClickListener(this);
        rb_shop_notSatisfied.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        rb_shop_all.setChecked(true);
        nameadapter = new ShopAppraiseAdapter(listname, ShopAppraiseActivity.this);
        rv_shop.setAdapter(nameadapter);

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            oid = intent.getStringExtra("oid");
            getSpellInformation("0", "10", "1", oid);
        }

/*        ShopData("全部");
        adapter = new ShopAppraiseAdapter(list, this);
        rv_shop.setAdapter(adapter);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rb_shop_all://全部
                rb_shop_all.setChecked(true);
                getSpellInformation("0", "10", "1", oid);
                break;
            case R.id.rb_shop_satisfaction://满意
                rb_shop_satisfaction.setChecked(true);
                getSpellInformation("0", "10", "2", oid);
                break;
            case R.id.rb_shop_notSatisfied://不满意
                rb_shop_notSatisfied.setChecked(true);
                getSpellInformation("0", "10", "3", oid);
                break;
        }
    }

/*    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 3; i++) {
            Map<Object, String> map1 = new HashMap<>();
            map1.put("name", str);
            map1.put("state", "1");

            Map<Object, String> map2 = new HashMap<>();
            map2.put("name", str);
            map2.put("state", "2");
            list.add(map1);
            list.add(map2);
        }
    }*/



    //获得拼购商品详情
    public void getSpellInformation(final String index, final String num, final String coommenLevel, final String oid) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getSpellData(index, num, coommenLevel, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取拼购详情信息.成功", result);

/*                            img_spell = (ImageView) findViewById(R.id.img_spell);   //商品图片
                            spell_price = (TextView) findViewById(R.id.spell_price);   //商品价格
                            spell_yuanjia = findView(R.id.spell_yuanjia);   //商品原价
                            spell_saleNum = findView(R.id.spell_saleNum);   //商品销量
                            spell_spell_title = findView(R.id.spell_title);   //商品标题
                            spell_detail = findView(R.id.spell_detail);   //商品详情
                            spell_data_img = (ImageView) findViewById(R.id.spell_data_img);   //商品介绍图片*/

                            Bean.Spell data = new Gson().fromJson(result, Bean.Spell.class);

                            Log.i("获取", result);


/*                            private ImageView iv_shop_img;  //店铺头像
                            private TextView tv_shop_name,str_ratingBar;  //店铺名称,店铺星级
                            RatingBar ratingBar;*/





/*                            public int status;
                            public String message;

                      private String oid = "";
    private String name = "";
    private String loanAmounts = "";    //价格
    private String saleNum = "";    //销量
    private List<Bean.ProjectImage> projectImage = new ArrayList<>();
    private String detail = "";     //详情*/

                            if (data.status == 1) {

                                spell_oid = data.model.oid;
                                name = data.model.name;
                                loanAmounts = data.model.loanAmounts;    //价格

                                spell_oid = data.model.oid;

                                if (data.model.path.size() > 0) {
                                    imgpath = data.model.path.get(0).thumPath; //图片地址

                                    //显示图片的配置
                                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                                            .showImageOnLoading(R.mipmap.ic_launcher)//等待加载显示图片
                                            .showImageOnFail(R.mipmap.ic_launcher)//显示错误图片
                                            .cacheInMemory(true)
                                            .bitmapConfig(Bitmap.Config.RGB_565)
                                            .build();

                                    ImageLoader.getInstance().displayImage(UriUtil.update_Head+imgpath,iv_shop_img, options);
                                }
                                tv_shop_name.setText(name);

                                List<Bean.SpellEveluate> spell_eveluate = data.model.evaluate;
                                listname.clear();
                                for (int i = 0; i < spell_eveluate.size(); i++) {
                                    Map<Object, String> map = new HashMap<>();
                                    map.put("state", "1"); //	店铺ID
                                    map.put("cryptonym", spell_eveluate.get(i).cryptonym); //=是否匿名（匿名返回“匿名用户”，不匿名返回“用户nickName”）;
                                    map.put("head", spell_eveluate.get(i).head); //评论人头像;
                                    map.put("starlevel", spell_eveluate.get(i).starlevel); //评论星级;
                                    map.put("evaluatetime", spell_eveluate.get(i).evaluatetime); //=评论时间;
                                    map.put("content", spell_eveluate.get(i).content); //=评论内容;


 /*                                   public String cryptonym;    //用户名
                                    public String head; //用户头像
                                    public String starlevel;    //评价星级
                                    public String evaluatetime; //评论时间
                                    public String content;  //评论内容
                                    public String commentLevel; //评价类型      评论等级 1好评 2中评 3差评*/

                                    listname.add(map);
                                }

                                nameadapter.notifyDataSetChanged();

                            }


                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取拼购详情信息.异常", response);
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
