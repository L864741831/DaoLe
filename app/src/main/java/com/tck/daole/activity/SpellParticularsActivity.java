package com.tck.daole.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.ShopAppraiseAdapter;
import com.tck.daole.adapter.SpellParticularsAdapter;
import com.tck.daole.adapter.SpellParticularsImgAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;
import com.tck.daole.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 拼单物品详情
 */
public class SpellParticularsActivity extends BaseActivity implements ObservableScrollView.OnObservableScrollViewListener {
    private LinearLayout ll_back;
    private NiceRecyclerView rv_spell, rv_spell_img;
    private List<Map<String, Object>> list = new ArrayList<>();
    private List<Map<String, Object>> imgList = new ArrayList<>();
    private TextView spell_yuanjia;
    private RadioButton spell_haoping, spell_zhongping, spell_chaping;
    private NiceRecyclerView rv_spell_particulars;
    private List<Map<Object, String>> pingjia_list = new ArrayList<>();
    private ShopAppraiseAdapter adapter;
    private LinearLayout ll_goumai_lepingou;

    private ObservableScrollView sv_spell_content;
    private LinearLayout header_spell;

    private RelativeLayout rl_appraise; //更多评价

    private int mHeight;

    private ImageView img_spell;    //商品图片
    TextView spell_price, spell_saleNum, spell_spell_title, spell_detail;   //商品价格\销量,标题,详情
    private ImageView spell_data_img;   //商品介绍图片

//    private String sp_token = "";




    /*                            public int status;
                            public String message;

                            public String oid;  //商品主键
                            public String name;  //商品名称
                            public String loanAmounts;     //商品价格
                            public String saleNum;  //销量
                            public Bean.ProjectImage projectImage; //乐拼购图片
                            public String detail;   //商品详情
                            public String introduction; //商品介绍
                            public String path; //商品图片*/

    private String spell_oid = "";
    private String name = "";
    private double loanAmounts = 0.0;    //价格
    public String imgpath = ""; //图片地址

    String oid = "";

    private List<Map<Object, String>> listname = new ArrayList<>(); //商品平理论列表
    private ShopAppraiseAdapter nameadapter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_spell_particulars);
        //设置透明状态栏
        //StatusbarUtils.enableTranslucentStatusbar(this);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        ll_back = findView(R.id.ll_back);
        rv_spell = findView(R.id.rv_spell);
        rv_spell_img = findView(R.id.rv_spell_img);
        spell_haoping = findView(R.id.spell_haoping);
        spell_zhongping = findView(R.id.spell_zhongping);
        spell_chaping = findView(R.id.spell_chaping);
        rv_spell_particulars = findView(R.id.rv_spell_particulars);
        ll_goumai_lepingou = findView(R.id.ll_goumai_lepingou);

        //初始化控件
        sv_spell_content = (ObservableScrollView) findViewById(R.id.sv_spell_content);
        header_spell = (LinearLayout) findViewById(R.id.ll_header_spell);

        img_spell = (ImageView) findViewById(R.id.img_spell);   //商品图片
        spell_price = (TextView) findViewById(R.id.spell_price);   //商品价格
        spell_yuanjia = findView(R.id.spell_yuanjia);   //商品原价
        spell_saleNum = findView(R.id.spell_saleNum);   //商品销量
        spell_spell_title = findView(R.id.spell_title);   //商品标题
        spell_detail = findView(R.id.spell_detail);   //商品详情
        spell_data_img = (ImageView) findViewById(R.id.spell_data_img);   //商品介绍图片


        rl_appraise = (RelativeLayout) findViewById(R.id.rl_appraise);


        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = img_spell.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                img_spell.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = img_spell.getHeight() - header_spell.getHeight();//这里取的高度应该为图片的高度-标题栏
                //注册滑动监听
                sv_spell_content.setOnObservableScrollViewListener(SpellParticularsActivity.this);
            }
        });


        //添加删除线
        spell_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }


    /**
     * 获取ObservableScrollView的滑动数据
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //顶部图处于最顶部，标题栏透明
            header_spell.setBackgroundColor(Color.argb(0, 250, 128, 10));
        } else if (t > 0 && t < mHeight) {
            //滑动过程中，渐变
            float scale = (float) t / mHeight;//算出滑动距离比例
            float alpha = (255 * scale);//得到透明度
            header_spell.setBackgroundColor(Color.argb((int) alpha, 250, 128, 10));
        } else {
            //过顶部图区域，标题栏定色
            header_spell.setBackgroundColor(Color.argb(255, 250, 128, 10));
        }
    }


    @Override
    protected void initListener() {

        ll_back.setOnClickListener(this);
        spell_haoping.setOnClickListener(this);
        spell_zhongping.setOnClickListener(this);
        spell_chaping.setOnClickListener(this);
        ll_goumai_lepingou.setOnClickListener(this);
        rl_appraise.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        spell_haoping.setChecked(true);
        nameadapter = new ShopAppraiseAdapter(listname, SpellParticularsActivity.this);
        rv_spell_particulars.setAdapter(nameadapter);

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            oid = intent.getStringExtra("oid");
            getSpellInformation("0", "10", "1", oid);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.spell_haoping://好评
                spell_haoping.setChecked(true);
                getSpellInformation("0", "2", "1", oid);
                break;
            case R.id.spell_zhongping://中评
                spell_zhongping.setChecked(true);
                getSpellInformation("0", "2", "2", oid);
                break;
            case R.id.spell_chaping://差评
                spell_chaping.setChecked(true);
                getSpellInformation("0", "2", "3", oid);
                break;
            case R.id.ll_goumai_lepingou:

                if (!App.islogin) {
                    toast("请登录");
                    startActivity(new Intent(SpellParticularsActivity.this, LoginActivity.class));//跳转登录页
                } else {
                    //startActivity(new Intent(SpellParticularsActivity.this, OrderSpellActivity.class));//跳转了拼购拼单订单
                    //toast("乐拼购订单");

                    Intent intent = new
                            Intent(SpellParticularsActivity.this, OrderSpellActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent.putExtra("spell_oid", spell_oid);
                    intent.putExtra("name", name);
                    intent.putExtra("loanAmounts", loanAmounts);
                    intent.putExtra("imgpath", imgpath);

                    startActivity(intent);

                }
                break;

            case R.id.rl_appraise://更多评价
                Intent intent = new
                        Intent(SpellParticularsActivity.this,ShopAppraiseActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("oid",oid);
                startActivity(intent);
                break;

        }
    }



/*    private void ShopData(String str) {
        pingjia_list.clear();
        for (int i = 0; i < 1; i++) {
            Map<Object, String> map3 = new HashMap<>();
            map3.put("name", str);
            map3.put("state", "1");

            Map<Object, String> map4 = new HashMap<>();
            map4.put("name", str);
            map4.put("state", "1");
            pingjia_list.add(map3);
            pingjia_list.add(map4);
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

                            //Log.i("获取", result);



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
                                loanAmounts = data.model.price;    //价格
                                if (data.model.path.size() > 0) {
                                    imgpath = data.model.path.get(0).thumPath; //图片地址
                                }

                                spell_oid = data.model.oid;
                                spell_spell_title.setText(data.model.name);   //名字
                                spell_price.setText("￥" + data.model.price + "");  //价格
                                spell_yuanjia.setText("￥" + data.model.loanAmounts);
                                //data.model.price;
                                spell_saleNum.setText("已拼" + data.model.saleNum + "件");    //销量
                                spell_detail.setText(data.model.detail);    //详情

                                //Log.i("获取", data.model.name);


                                //Log.i("获取", data.path.isEmpty()+"");

                                if (data.model.path.size() > 0) {

                                    //显示图片的配置
                                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                                            .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                                            .showImageOnFail(R.mipmap.def)//显示错误图片
                                            .cacheInMemory(true)
                                            .bitmapConfig(Bitmap.Config.RGB_565)
                                            .build();

                                    ImageLoader.getInstance().displayImage(UriUtil.ip + data.model.path.get(0).thumPath, img_spell, options);

                                }

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
