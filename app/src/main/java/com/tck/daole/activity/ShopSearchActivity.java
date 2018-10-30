package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.TakeoutAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.NetUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class ShopSearchActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private NiceRecyclerView rv_market;
    private List<Map<String, Object>> list = new ArrayList<>();
    private TakeoutAdapter adapter;

    protected SmartRefreshLayout smart;

    private int index = 0, index2 = 15;
    private int num = 15;

    String type = "";
    String content = "";

    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_search);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();

        smart = findView(R.id.smart);
//            smart.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        smart.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多

        smart.setEnableRefresh(true);
        smart.setEnableLoadmore(true);


    }

    public void findViewId() {
        ll_back = findView(R.id.ll_back);
        rv_market = findView(R.id.rv_market);

    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(SystemSetActivity.this, "token", "").toString();

        adapter = new TakeoutAdapter(list, ShopSearchActivity.this);
        rv_market.setAdapter(adapter);
        adapter.setOnItemClickListener(new TakeoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //startActivity(new Intent(MarketActivity.this, MerchandiseActivity.class));

                String oid = (String) list.get(position).get("oid");
                //toast(oid);

                //超市列表到店铺

                Intent intent = new
                        Intent(ShopSearchActivity.this,ShopActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("oid",oid);
                startActivity(intent);


            }
        });


        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            type = intent.getStringExtra("type");
            content = intent.getStringExtra("content");

            if (type.equals("0")) {
                allSeekStroe(String.valueOf(index),String.valueOf(index2),content,150.1,150.1);
                //toast(type + "\n" + content);

            } else {
                //toast(type + "\n" + content);
                seekStroe(String.valueOf(index),String.valueOf(index2),content,150.1,150.1,Integer.parseInt(type));

            }

        }
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);


        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                index2 = num;
                if(type.equals("0")){
                    allSeekStroe(String.valueOf(index),String.valueOf(index2),content,150.1,150.1);
                }else{
                    seekStroe(String.valueOf(index),String.valueOf(index2),content,150.1,150.1,Integer.parseInt(type));
                }


            }
        });

        smart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                index2 += num;
                if(type.equals("0")){
                    allSeekStroe(String.valueOf(index),String.valueOf(index2),content,150.1,150.1);
                }else {
                    seekStroe(String.valueOf(index), String.valueOf(index2), content, 150.1, 150.1, Integer.parseInt(type));
                }
            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }


    //获得搜索的商家列表
    public void seekStroe(final String index, final String num, final String searchText, final double dimensionality, final double longitude, final int storeType) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getSeekStroe(index, num, searchText, dimensionality, longitude, storeType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.i("获取商家列表.成功", result);


                            Bean.IndexBigClassModel data = new Gson().fromJson(result,Bean.IndexBigClassModel.class);
                            if(data.getStatus() == 0){
                                Log.i("121","获取超市列表失败");
                            }else{

                                List<Bean.IndexBigClass> chaoshi_list = data.list;

/*                    public String oid;//=店铺oid
                    public String logoImage;//=店铺图片

                    public String evaluateIdstarlevel;//=店铺星级
                    public String sellnumber;//=月售
                    public String name;//=店铺名称
                    public String address;//=地址
                    public String jvLi;//距离
                            public List<projectImage> project;
                    */
                                if (!smart.isLoading()) {
                                    list.clear();
                                }

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


                                finishRefresh();

                                adapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onFail(String response) {

                            Log.i("获取商家列表.异常", response);

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






    //获得全部店铺搜索的商家列表
    public void allSeekStroe(final String index, final String num, final String searchText, final double dimensionality, final double longitude) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getAllSeekStroe(index, num, searchText, dimensionality, longitude, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.i("获取商家列表.成功", result);


                            Bean.IndexBigClassModel data = new Gson().fromJson(result,Bean.IndexBigClassModel.class);
                            if(data.getStatus() == 0){
                                Log.i("121","获取超市列表失败");
                            }else{

                                List<Bean.IndexBigClass> chaoshi_list = data.list;

/*                    public String oid;//=店铺oid
                    public String logoImage;//=店铺图片

                    public String evaluateIdstarlevel;//=店铺星级
                    public String sellnumber;//=月售
                    public String name;//=店铺名称
                    public String address;//=地址
                    public String jvLi;//距离
                            public List<projectImage> project;
                    */
                                if (!smart.isLoading()) {
                                    list.clear();
                                }

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

                                finishRefresh();


                                adapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onFail(String response) {

                            Log.i("获取商家列表.异常", response);

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


    protected void finishRefresh() {
        smart.finishRefresh();
        smart.finishLoadmore();
    }



}
