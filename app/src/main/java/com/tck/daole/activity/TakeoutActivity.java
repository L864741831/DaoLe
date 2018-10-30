package com.tck.daole.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.R;
import com.tck.daole.adapter.TakeoutAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;
import com.tck.daole.view.SetBanner;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 外卖快递页面
 */
public class TakeoutActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private Banner banner_takeout;
    private NiceRecyclerView rv_takeout;
    private List<Map<String, Object>> list = new ArrayList<>();
    private TakeoutAdapter adapter;
    private RadioButton rb_zongHe, rb_xiaoLiang, rb_juLi, rb_pingFen;

    LinearLayout ll_takeout_sort_one,ll_takeout_sort_two,ll_takeout_sort_there,ll_takeout_sort_four,ll_takeout_sort_five,ll_takeout_sort_six,ll_takeout_sort_seven,ll_takeout_sort_eight,ll_takeout_sort_nine,ll_takeout_sort_ten;

    Handler hd;

    String one_sort = "A";
    String two_sort = "0";

    private EditText et_search;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_takeout);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        tv_left = findView(R.id.tv_left);
        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        ll_left = findView(R.id.ll_left);
        banner_takeout = findView(R.id.banner_takeout);
        rv_takeout = findView(R.id.rv_takeout);
        rb_zongHe = findView(R.id.rb_zongHe);
        rb_xiaoLiang = findView(R.id.rb_xiaoLiang);
        rb_juLi = findView(R.id.rb_juLi);
        rb_pingFen = findView(R.id.rb_pingFen);

        ll_takeout_sort_one = findView(R.id.ll_takeout_sort_one);
        ll_takeout_sort_two = findView(R.id.ll_takeout_sort_two);
        ll_takeout_sort_there = findView(R.id.ll_takeout_sort_there);
        ll_takeout_sort_four = findView(R.id.ll_takeout_sort_four);
        ll_takeout_sort_five = findView(R.id.ll_takeout_sort_five);
        ll_takeout_sort_six = findView(R.id.ll_takeout_sort_six);
        ll_takeout_sort_seven = findView(R.id.ll_takeout_sort_seven);
        ll_takeout_sort_eight = findView(R.id.ll_takeout_sort_eight);
        ll_takeout_sort_nine = findView(R.id.ll_takeout_sort_nine);
        ll_takeout_sort_ten = findView(R.id.ll_takeout_sort_ten);


        et_search = findView(R.id.et_search);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    Intent intent_tuangou = new
                            Intent(TakeoutActivity.this, ShopSearchActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_tuangou.putExtra("type", "3");
                    intent_tuangou.putExtra("content", et_search.getText().toString().trim());
                    startActivity(intent_tuangou);

                    et_search.setText("");


                    //隐藏软键盘，如果当前打开则隐藏，如果当前隐藏则打开
                    InputMethodManager imm = (InputMethodManager) getSystemService
                            (Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });



        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");


                switch (msg.what) {
                    case Constant.Takeout_List:
                        //toast(msgStr);

                        adapter = new TakeoutAdapter(list, TakeoutActivity.this);
                        rv_takeout.setAdapter(adapter);
                        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(int position) {
                                //startActivity(new Intent(MarketActivity.this, MerchandiseActivity.class));

                                String oid = (String) list.get(position).get("oid");
                                //toast(oid);

                                Intent intent = new
                                        Intent(TakeoutActivity.this, ShopActivity.class);
                                //在Intent对象当中添加一个键值对
                                intent.putExtra("oid", oid);
                                startActivity(intent);

                            }
                        });

                        break;

                    case Constant.Change_Takeout_List:
                        adapter.notifyDataSetChanged();
                        break;



                }

            }
        };





    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
        rb_zongHe.setOnClickListener(this);
        rb_xiaoLiang.setOnClickListener(this);
        rb_juLi.setOnClickListener(this);
        rb_pingFen.setOnClickListener(this);

        ll_takeout_sort_one.setOnClickListener(this);
        ll_takeout_sort_two.setOnClickListener(this);
        ll_takeout_sort_there.setOnClickListener(this);
        ll_takeout_sort_four.setOnClickListener(this);
        ll_takeout_sort_five.setOnClickListener(this);
        ll_takeout_sort_six.setOnClickListener(this);
        ll_takeout_sort_seven.setOnClickListener(this);
        ll_takeout_sort_eight.setOnClickListener(this);
        ll_takeout_sort_nine.setOnClickListener(this);
        ll_takeout_sort_ten.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_left.setText("首页");
        tv_title.setText("外卖快递");
        tv_right.setVisibility(View.GONE);

        //banner数据
/*        List<String> listBanner = new ArrayList<>();
        listBanner.add("http://pic.qiantucdn.com/58pic/26/65/67/27w58PIC5HU_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/18/10/15/30Q58PICemX_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/18/10/15/60w58PICZUu_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/11/89/15/97Y58PICV3M.jpg");
        SetBanner.startBanner(TakeoutActivity.this, banner_takeout, listBanner);*/

/*        rb_zongHe.setChecked(true);
        ShopData("综合排序");
        adapter = new TakeoutAdapter(list, TakeoutActivity.this);
        rv_takeout.setAdapter(adapter);
        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                startActivity(new Intent(TakeoutActivity.this, MerchandiseActivity.class));
            }
        });*/

        //3外卖快递
        MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
        rb_zongHe.setChecked(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.rb_zongHe://综合排序
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                one_sort = "A";
/*                ShopData("综合排序");
                adapter.notifyDataSetChanged();*/
                break;
            case R.id.rb_xiaoLiang://销量最高
                MarketList("1234567", "1234567", one_sort, "0", "9", "3",two_sort);
                one_sort = "B";
/*                ShopData("销量最高");
                adapter.notifyDataSetChanged();*/
                break;
            case R.id.rb_juLi://距离最近
                MarketList("1234567", "1234567", one_sort, "0", "9", "3",two_sort);
                one_sort = "C";
 /*               ShopData("距离最近");
                adapter.notifyDataSetChanged();*/
                break;
            case R.id.rb_pingFen://评分最好
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                one_sort = "D";
/*                ShopData("评分最好");
                adapter.notifyDataSetChanged();*/
                break;



            case R.id.ll_takeout_sort_one://全部
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "0";
                break;
            case R.id.ll_takeout_sort_two://营养
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "10";
                break;
            case R.id.ll_takeout_sort_there://特色
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "11";
                break;
            case R.id.ll_takeout_sort_four://火锅
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "12";
                break;
            case R.id.ll_takeout_sort_five://汉堡
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "13";
                break;
            case R.id.ll_takeout_sort_six://加长
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "14";
                break;
            case R.id.ll_takeout_sort_seven://饺子
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "15";
                break;
            case R.id.ll_takeout_sort_eight://炸鸡
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "16";
                break;
            case R.id.ll_takeout_sort_nine://川菜
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "17";
                break;
            case R.id.ll_takeout_sort_ten://其他
                MarketList("1234567", "1234567", one_sort, "0", "9", "3", two_sort);
                two_sort = "18";
                break;
        }
    }

    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("TuPian", "1");
            map1.put("WaiMai", true);
            map1.put("name", str);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("TuPian", "2");
            map2.put("WaiMai", true);
            map2.put("name", str);
            list.add(map1);
            list.add(map2);
        }
    }







    public void MarketList(final String dimensionality, final String longitude, final String titleState, final String index, final String num, final String storeType, final String twoType) {

        Map<String, String> map = new HashMap<>();
        map.put("dimensionality", dimensionality);
        map.put("longitude", longitude);
        map.put("titleState", titleState);
        map.put("page.index", index);
        map.put("page.num", num);
        map.put("adminStore.storeType", storeType);
        map.put("adminStore.twoType", twoType);


        HttpUtils.doPost(UriUtil.get_Business_Store_Commodity, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("121",response.body().string());


                Bean.IndexBigClassModel data = new Gson().fromJson(response.body().string(), Bean.IndexBigClassModel.class);
                if (data.getStatus() == 0) {
                    Log.i("121", "获取超市列表失败");
                } else {

                    List<Bean.IndexBigClass> chaoshi_list = data.getModel();

/*                    public String oid;//=店铺oid
                    public String logoImage;//=店铺图片

                    public String evaluateIdstarlevel;//=店铺星级
                    public String sellnumber;//=月售
                    public String name;//=店铺名称
                    public String address;//=地址
                    public String jvLi;//距离
                            public List<projectImage> project;
                    */
                    list.clear();

                    for (int i = 0; i < chaoshi_list.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("oid", chaoshi_list.get(i).getOid()); //	店铺oid
                        map.put("logoImage", chaoshi_list.get(i).getLogoImage()); //店铺图片
                        map.put("name", chaoshi_list.get(i).getName()); //店铺名称
                        map.put("evaluateIdstarlevel", chaoshi_list.get(i).getEvaluateIdstarlevel()); //=店铺星级
                        map.put("sellnumber", chaoshi_list.get(i).getSellnumber()); //月售
                        map.put("address", chaoshi_list.get(i).getAddress()); //地址
                        map.put("jvLi", chaoshi_list.get(i).getJvLi()); //距离


                        //Log.i("1211",chaoshi_list.get(i).getOid());


                        List<Bean.ChaoShiImage> projectImage_list = chaoshi_list.get(i).getProject();
                        if (projectImage_list.size() == 0) {
                            map.put("TuPian", "1"); //没有图片
                        }
                        if (projectImage_list.size() == 3) {
                            map.put("TuPian", "2"); //有图片
                            for (int j = 0; j < projectImage_list.size(); j++) {
                                map.put("projectId" + j, projectImage_list.get(j).getProjectId()); //商品id
                                map.put("projectName" + j, projectImage_list.get(j).getProjectName()); //商品名称
                                map.put("loanAmounts" + j, projectImage_list.get(j).getLoanAmounts()); //商品价钱
                                map.put("projectImage" + j, projectImage_list.get(j).getProjectImage()); //商品图片


                                //Log.i("121",projectImage_list.get(i).getProjectImage());
                            }
                        }

                        list.add(map);
                    }


                    Bundle b = new Bundle();
                    b.putString("msg", data.getMessage());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Takeout_List;
                    hd.sendMessage(msg);


                }

                //Log.i("121","获取超市列表失败");


            }
        });


    }









    public void changeMarketList(final String dimensionality, final String longitude, final String titleState, final String index, final String num, final String storeType, final String twoType) {

        Map<String, String> map = new HashMap<>();
        map.put("dimensionality", dimensionality);
        map.put("longitude", longitude);
        map.put("titleState", titleState);
        map.put("page.index", index);
        map.put("page.num", num);
        map.put("adminStore.storeType", storeType);
        map.put("adminStore.twoType", twoType);


        HttpUtils.doPost(UriUtil.get_Business_Store_Commodity, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("121",response.body().string());


                Bean.IndexBigClassModel data = new Gson().fromJson(response.body().string(), Bean.IndexBigClassModel.class);
                if (data.getStatus() == 0) {
                    Log.i("121", "获取超市列表失败");
                } else {

                    List<Bean.IndexBigClass> chaoshi_list = data.getModel();

/*                    public String oid;//=店铺oid
                    public String logoImage;//=店铺图片

                    public String evaluateIdstarlevel;//=店铺星级
                    public String sellnumber;//=月售
                    public String name;//=店铺名称
                    public String address;//=地址
                    public String jvLi;//距离
                            public List<projectImage> project;
                    */
                list.clear();
                    for (int i = 0; i < chaoshi_list.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("oid", chaoshi_list.get(i).getOid()); //	店铺oid
                        map.put("logoImage", chaoshi_list.get(i).getLogoImage()); //店铺图片
                        map.put("name", chaoshi_list.get(i).getName()); //店铺名称
                        map.put("evaluateIdstarlevel", chaoshi_list.get(i).getEvaluateIdstarlevel()); //=店铺星级
                        map.put("sellnumber", chaoshi_list.get(i).getSellnumber()); //月售
                        map.put("address", chaoshi_list.get(i).getAddress()); //地址
                        map.put("jvLi", chaoshi_list.get(i).getJvLi()); //距离


                        //Log.i("1211",chaoshi_list.get(i).getOid());


                        List<Bean.ChaoShiImage> projectImage_list = chaoshi_list.get(i).getProject();
                        if (projectImage_list.size() == 0) {
                            map.put("TuPian", "1"); //没有图片
                        }
                        if (projectImage_list.size() == 3) {
                            map.put("TuPian", "2"); //有图片
                            for (int j = 0; j < projectImage_list.size(); j++) {
                                map.put("projectId" + j, projectImage_list.get(j).getProjectId()); //商品id
                                map.put("projectName" + j, projectImage_list.get(j).getProjectName()); //商品名称
                                map.put("loanAmounts" + j, projectImage_list.get(j).getLoanAmounts()); //商品价钱
                                map.put("projectImage" + j, projectImage_list.get(j).getProjectImage()); //商品图片


                                //Log.i("121",projectImage_list.get(i).getProjectImage());
                            }
                        }

                        list.add(map);
                    }


                    Bundle b = new Bundle();
                    b.putString("msg", data.getMessage());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Change_Takeout_List;
                    hd.sendMessage(msg);


                }

                //Log.i("121","获取超市列表失败");


            }
        });


    }




}
