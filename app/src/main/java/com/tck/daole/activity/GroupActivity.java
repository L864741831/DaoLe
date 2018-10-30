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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 美食团购
 */
public class GroupActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private RadioButton rb_group_all, rb_group_2, rb_group_3, rb_group_4, rb_group_5;
    private RadioButton rb_group_zongHe, rb_group_xiaoLiang, rb_group_juLi, rb_group_pingFen;
    private NiceRecyclerView rv_group;
    private List<Map<String, Object>> list = new ArrayList<>();
    private TakeoutAdapter adapter;

    Handler hd;

    public String group_type = "1";

    public String one_sort = "A";

    private EditText et_search;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        tv_left = findView(R.id.tv_left);
        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        ll_left = findView(R.id.ll_left);
        rb_group_all = findView(R.id.rb_group_all);
        rb_group_2 = findView(R.id.rb_group_2);
        rb_group_3 = findView(R.id.rb_group_3);
        rb_group_4 = findView(R.id.rb_group_4);
        rb_group_5 = findView(R.id.rb_group_5);
        rb_group_zongHe = findView(R.id.rb_group_zongHe);
        rb_group_xiaoLiang = findView(R.id.rb_group_xiaoLiang);
        rb_group_juLi = findView(R.id.rb_group_juLi);
        rb_group_pingFen = findView(R.id.rb_group_pingFen);
        rv_group = findView(R.id.rv_group);

        et_search = findView(R.id.et_search);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    Intent intent_tuangou = new
                            Intent(GroupActivity.this, ShopSearchActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_tuangou.putExtra("type", group_type);
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
                    case Constant.Group_List:
                        //toast(msgStr);

                        adapter = new TakeoutAdapter(list, GroupActivity.this);
                        rv_group.setAdapter(adapter);
                        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(int position) {
                                //startActivity(new Intent(MarketActivity.this, MerchandiseActivity.class));

                                String oid = (String) list.get(position).get("oid");
                                //toast(oid);




                                if(group_type.equals("2")){
                                    Intent intent = new
                                            Intent(GroupActivity.this, ShopNameActivity.class);
                                    //在Intent对象当中添加一个键值对
                                    intent.putExtra("oid", oid);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new
                                            Intent(GroupActivity.this, ShopActivity.class);
                                    //在Intent对象当中添加一个键值对
                                    intent.putExtra("oid", oid);
                                    startActivity(intent);
                                }


                            }
                        });


                        break;


                    case Constant.Change_Group_List:
                        adapter.notifyDataSetChanged();
                        break;

                }

            }
        };


    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
        rb_group_all.setOnClickListener(this);
        rb_group_2.setOnClickListener(this);
        rb_group_3.setOnClickListener(this);
        rb_group_4.setOnClickListener(this);
        rb_group_5.setOnClickListener(this);
        rb_group_zongHe.setOnClickListener(this);
        rb_group_xiaoLiang.setOnClickListener(this);
        rb_group_juLi.setOnClickListener(this);
        rb_group_pingFen.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_left.setVisibility(View.GONE);
        tv_left.setText("首页");
        tv_right.setVisibility(View.GONE);


        rb_group_all.setChecked(true);
        rb_group_zongHe.setChecked(true);

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            String Group_Type = intent.getStringExtra("type");

            //1.超市 2.美食团购 3.美食外卖 4.花之语 5.休闲娱乐 6.运动健康 7.生活服务 8.果蔬生鲜


/*            if (Group_Type.equals("chaoshi")) {
                tv_title.setText("超市");
                group_type = "1";
                MarketList("1234567", "1234567", "A", "0", "9", group_type, "0");
            }*/

            if (Group_Type.equals("tuangou")) {
                tv_title.setText("美食团购");
                group_type = "2";
                MarketList("1234567", "1234567", one_sort, "0", "9", group_type, "0");

            }
            if (Group_Type.equals("flower")) {
                tv_title.setText("花之语");
                group_type = "4";
                MarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");


            }
            if (Group_Type.equals("leisure")) {
                tv_title.setText("休闲娱乐");
                group_type = "5";
                MarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");


            }
            if (Group_Type.equals("motion")) {
                tv_title.setText("运动健康");
                group_type = "6";
                MarketList("1234567", "1234567", one_sort, "0", "9", group_type, "0");


            }
            if (Group_Type.equals("life")) {
                tv_title.setText("生活服务");
                group_type = "7";
                MarketList("1234567", "1234567", one_sort, "0", "9", group_type, "0");


            }
            if (Group_Type.equals("fruite")) {
                tv_title.setText("生鲜果蔬");
                group_type = "8";
                MarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");

            }
        }





/*        ShopData("综合排序");
        adapter = new TakeoutAdapter(list, this);
        rv_group.setAdapter(adapter);
        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                startActivity(new Intent(GroupActivity.this, ShopNameActivity.class));
            }
        });*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.ll_left:
                finish();
                break;
            case R.id.rb_group_all:
                rb_group_zongHe.setChecked(true);
                //ShopData("综合排序");
                //adapter.notifyDataSetChanged();
                //changeMarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");
                break;
            case R.id.rb_group_2:
                rb_group_zongHe.setChecked(true);
                //ShopData("综合排序");
                //adapter.notifyDataSetChanged();
                break;
            case R.id.rb_group_zongHe://综合排序
                rb_group_zongHe.setChecked(true);
                changeMarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");
                one_sort ="A";
                //ShopData("综合排序");
                //adapter.notifyDataSetChanged();
                break;
            case R.id.rb_group_xiaoLiang://销量最高
                rb_group_xiaoLiang.setChecked(true);
                changeMarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");
                one_sort ="B";
                //ShopData("销量最高");
                //adapter.notifyDataSetChanged();
                break;
            case R.id.rb_group_juLi://距离最近
                rb_group_juLi.setChecked(true);
                changeMarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");
                one_sort ="C";

                //ShopData("距离最近");
                //adapter.notifyDataSetChanged();
                break;
            case R.id.rb_group_pingFen://评分最好
                rb_group_pingFen.setChecked(true);
                changeMarketList("1234567", "1234567",one_sort, "0", "9", group_type, "0");
                one_sort ="D";
                //ShopData("评分最好");
                //adapter.notifyDataSetChanged();
                break;
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
                    msg.what = Constant.Group_List;
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
                    msg.what = Constant.Change_Group_List;
                    hd.sendMessage(msg);


                }

                //Log.i("121","获取超市列表失败");


            }
        });


    }





/*    private void ShopData(String str) {
        list.clear();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("logoImage", "1");
            map1.put("WaiMai", false);
            map1.put("evaluateIdstarlevel", 5);
            map1.put("sellnumber", 55);
            map1.put("address", "世博中心");
            map1.put("jvLi", "1.4km");

            Map<String, Object> map2 = new HashMap<>();
            map2.put("logoImage", "2");
            map2.put("WaiMai", false);
            map2.put("evaluateIdstarlevel", 4);
            map2.put("sellnumber", 44);
            map2.put("address", "百脑汇1411");
            map2.put("jvLi", "jvLi2.0km");
            list.add(map1);
            list.add(map2);
        }
    }*/


}
