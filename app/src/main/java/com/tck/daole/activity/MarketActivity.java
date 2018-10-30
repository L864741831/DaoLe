package com.tck.daole.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.R;
import com.tck.daole.adapter.TakeoutAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
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

public class MarketActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private RadioButton rb_market_zongHe, rb_market_xiaoLiang, rb_market_juLi, rb_market_pingFen;
    private NiceRecyclerView rv_market;
    private List<Map<String, Object>> list = new ArrayList<>();
    private TakeoutAdapter adapter;

    private ImageView iv_market_sort_one,iv_market_sort_two,iv_market_sort_there,iv_market_sort_four,iv_market_sort_five,iv_market_sort_six,iv_market_sort_seven,iv_market_sort_eight,iv_market_sort_nice;

    Handler hd;

    private String oneSort = "A";

    private String twoSort = "1";

    private EditText et_search;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_market);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        tv_left = findView(R.id.tv_left);
        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        ll_left = findView(R.id.ll_left);
        rv_market = findView(R.id.rv_market);
        rb_market_zongHe = findView(R.id.rb_market_zongHe);
        rb_market_xiaoLiang = findView(R.id.rb_market_xiaoLiang);
        rb_market_juLi = findView(R.id.rb_market_juLi);
        rb_market_pingFen = findView(R.id.rb_market_pingFen);


        iv_market_sort_one = findView(R.id.iv_market_sort_one);
        iv_market_sort_two = findView(R.id.iv_market_sort_two);
        iv_market_sort_there = findView(R.id.iv_market_sort_there);
        iv_market_sort_four = findView(R.id.iv_market_sort_four);
        iv_market_sort_five = findView(R.id.iv_market_sort_five);
        iv_market_sort_six = findView(R.id.iv_market_sort_six);
        iv_market_sort_seven = findView(R.id.iv_market_sort_seven);
        iv_market_sort_eight = findView(R.id.iv_market_sort_eight);
        iv_market_sort_nice = findView(R.id.iv_market_sort_nice);


        et_search = findView(R.id.et_search);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {



                    Intent intent_tuangou = new
                            Intent(MarketActivity.this, ShopSearchActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_tuangou.putExtra("type", "1");
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
                    case Constant.Market_List:
                        //toast(msgStr);

                       adapter = new TakeoutAdapter(list, MarketActivity.this);
                        rv_market.setAdapter(adapter);
                        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(int position) {
                                //startActivity(new Intent(MarketActivity.this, MerchandiseActivity.class));

                                String oid = (String) list.get(position).get("oid");
                                //toast(oid);

                                //超市列表到店铺

                                Intent intent = new
                                        Intent(MarketActivity.this,ShopActivity.class);
                                //在Intent对象当中添加一个键值对
                                intent.putExtra("oid",oid);
                                startActivity(intent);


                            }
                        });

                        break;

                    case Constant.Change_Market_List:
                            adapter.notifyDataSetChanged();
                        break;

                }

            }
        };


    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
        rb_market_zongHe.setOnClickListener(this);
        rb_market_xiaoLiang.setOnClickListener(this);
        rb_market_juLi.setOnClickListener(this);
        rb_market_pingFen.setOnClickListener(this);

        iv_market_sort_one.setOnClickListener(this);
        iv_market_sort_two.setOnClickListener(this);
        iv_market_sort_there.setOnClickListener(this);
        iv_market_sort_four.setOnClickListener(this);
        iv_market_sort_five.setOnClickListener(this);
        iv_market_sort_six.setOnClickListener(this);
        iv_market_sort_seven.setOnClickListener(this);
        iv_market_sort_eight.setOnClickListener(this);
        iv_market_sort_nice.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_left.setText("首页");
        tv_title.setText("超市");
        tv_right.setVisibility(View.GONE);

        rb_market_zongHe.setChecked(true);
/*        ShopData("综合排序");
        adapter = new TakeoutAdapter(list, this);
        rv_market.setAdapter(adapter);
        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                startActivity(new Intent(MarketActivity.this, MerchandiseActivity.class));
            }
        });*/

        MarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.rb_market_zongHe://综合排序
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                oneSort = "A";

/*                ShopData("综合排序");
                adapter.notifyDataSetChanged();*/
                break;
            case R.id.rb_market_xiaoLiang://销量最高
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                oneSort = "B";

/*                ShopData("销量最高");
                adapter.notifyDataSetChanged();*/
                break;
            case R.id.rb_market_juLi://距离最近
                changeMarketList("1234567", "1234567",oneSort, "0", "9", "1",twoSort);
                oneSort = "C";

/*                ShopData("距离最近");
                adapter.notifyDataSetChanged();*/
                break;
            case R.id.rb_market_pingFen://评分最好
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                oneSort = "D";

/*                ShopData("评分最好");
                adapter.notifyDataSetChanged();*/
                break;


            case R.id.iv_market_sort_one:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "1";
                break;
            case R.id.iv_market_sort_two:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "2";
                break;
            case R.id.iv_market_sort_there:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "3";
                break;
            case R.id.iv_market_sort_four:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "4";
                break;
            case R.id.iv_market_sort_five:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "5";
                break;
            case R.id.iv_market_sort_six:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "6";
                break;
            case R.id.iv_market_sort_seven:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "7";
                break;
            case R.id.iv_market_sort_eight:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "8";
                break;
            case R.id.iv_market_sort_nice:
                changeMarketList("1234567", "1234567", oneSort, "0", "9", "1", twoSort);
                twoSort = "9";
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
            map2.put("TuPian", "1");
            map2.put("WaiMai", true);
            map2.put("name", str);
            list.add(map1);
            list.add(map2);
        }
    }


/*    //获取超市商品列表
    public void MarketList(final String dimensionality, final String longitude, final String titleState, final String index, final String num, final String storeType, final String twoType) {

        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getMarketList(dimensionality,longitude,titleState,index,num,storeType,twoType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取超市列表.成功", result);

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取超市列表.异常", response);

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
    }*/


    public void MarketList(final String dimensionality, final String longitude, final String titleState, final String index, final String num, final String storeType, final String twoType) {

        Map<String,String> map = new HashMap<>();
        map.put("dimensionality",dimensionality);
        map.put("longitude",longitude);
        map.put("titleState",titleState);
        map.put("page.index",index);
        map.put("page.num",num);
        map.put("adminStore.storeType",storeType);
        map.put("adminStore.twoType",twoType);


        HttpUtils.doPost(UriUtil.get_Business_Store_Commodity, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("121",response.body().string());

                String str = response.body().string();

                Log.i("121",str);


                Bean.IndexBigClassModel data = new Gson().fromJson(str,Bean.IndexBigClassModel.class);
                if(data.getStatus() == 0){
                        Log.i("121","获取超市列表失败");
                }else{

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
                        Map<String,Object> map = new HashMap<>();
                        map.put("oid", chaoshi_list.get(i).getOid()); //	店铺oid
                        map.put("logoImage", chaoshi_list.get(i).getLogoImage()); //店铺图片
                        map.put("name", chaoshi_list.get(i).getName()); //店铺名称
                        map.put("evaluateIdstarlevel", chaoshi_list.get(i).getEvaluateIdstarlevel()); //=店铺星级
                        map.put("sellnumber", chaoshi_list.get(i).getSellnumber()); //月售
                        map.put("address", chaoshi_list.get(i).getAddress()); //地址
                        map.put("jvLi", chaoshi_list.get(i).getJvLi()); //距离



                        //Log.i("121",chaoshi_list.get(i).getJvLi());


                        List<Bean.ChaoShiImage> projectImage_list = chaoshi_list.get(i).getProject();
                        if(projectImage_list.size()==0){
                            map.put("TuPian", "1"); //没有图片
                        }
                        if(projectImage_list.size()==3){
                            map.put("TuPian", "1"); //有图片           //这页不显示图片
                            for(int j = 0; j < projectImage_list.size(); j++){
                                map.put("projectId"+j, projectImage_list.get(j).getProjectId()); //商品id
                                map.put("projectName"+j, projectImage_list.get(j).getProjectName()); //商品名称
                                map.put("loanAmounts"+j, projectImage_list.get(j).getLoanAmounts()); //商品价钱
                                map.put("projectImage"+j, projectImage_list.get(j).getProjectImage()); //商品图片


                                //Log.i("121",projectImage_list.get(i).getProjectImage());
                            }
                        }

                        list.add(map);
                    }



                   Bundle b = new Bundle();
                    b.putString("msg", data.getMessage());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Market_List;
                    hd.sendMessage(msg);


                }

            }
        });


    }








    public void changeMarketList(final String dimensionality, final String longitude, final String titleState, final String index, final String num, final String storeType, final String twoType) {

        Map<String,String> map = new HashMap<>();
        map.put("dimensionality",dimensionality);
        map.put("longitude",longitude);
        map.put("titleState",titleState);
        map.put("page.index",index);
        map.put("page.num",num);
        map.put("adminStore.storeType",storeType);
        map.put("adminStore.twoType",twoType);


        HttpUtils.doPost(UriUtil.get_Business_Store_Commodity, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("121",response.body().string());


                Bean.IndexBigClassModel data = new Gson().fromJson(response.body().string(),Bean.IndexBigClassModel.class);
                if(data.getStatus() == 0){
                    Log.i("121","获取超市列表失败");
                }else{

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
                        Map<String,Object> map = new HashMap<>();
                        map.put("oid", chaoshi_list.get(i).getOid()); //	店铺oid
                        map.put("logoImage", chaoshi_list.get(i).getLogoImage()); //店铺图片
                        map.put("name", chaoshi_list.get(i).getName()); //店铺名称
                        map.put("evaluateIdstarlevel", chaoshi_list.get(i).getEvaluateIdstarlevel()); //=店铺星级
                        map.put("sellnumber", chaoshi_list.get(i).getSellnumber()); //月售
                        map.put("address", chaoshi_list.get(i).getAddress()); //地址
                        map.put("jvLi", chaoshi_list.get(i).getJvLi()); //距离



                        //Log.i("121",chaoshi_list.get(i).getJvLi());


                        List<Bean.ChaoShiImage> projectImage_list = chaoshi_list.get(i).getProject();
                        if(projectImage_list.size()==0){
                            map.put("TuPian", "1"); //没有图片
                        }
                        if(projectImage_list.size()==3){
                            map.put("TuPian", "1"); //有图片           //这页不显示图片
                            for(int j = 0; j < projectImage_list.size(); j++){
                                map.put("projectId"+j, projectImage_list.get(j).getProjectId()); //商品id
                                map.put("projectName"+j, projectImage_list.get(j).getProjectName()); //商品名称
                                map.put("loanAmounts"+j, projectImage_list.get(j).getLoanAmounts()); //商品价钱
                                map.put("projectImage"+j, projectImage_list.get(j).getProjectImage()); //商品图片


                                //Log.i("121",projectImage_list.get(i).getProjectImage());
                            }
                        }

                        list.add(map);
                    }



                    Bundle b = new Bundle();
                    b.putString("msg", data.getMessage());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Change_Market_List;
                    hd.sendMessage(msg);


                }

            }
        });


    }





}