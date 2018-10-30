package com.tck.daole.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.activity.GroupActivity;
import com.tck.daole.activity.MarketActivity;
import com.tck.daole.activity.MerchandiseActivity;
import com.tck.daole.activity.PostedParticularsActivity;
import com.tck.daole.activity.SearchActivity;
import com.tck.daole.activity.ShopActivity;
import com.tck.daole.activity.ShopNameActivity;
import com.tck.daole.activity.ShopcatActivity;
import com.tck.daole.activity.SpellActivity;
import com.tck.daole.activity.SpellParticularsActivity;
import com.tck.daole.activity.SystemMsgActivity;
import com.tck.daole.activity.TakeoutActivity;
import com.tck.daole.activity.TravelActivity;
import com.tck.daole.adapter.IndexAdapter;
import com.tck.daole.adapter.TakeoutAdapter;
import com.tck.daole.entity.Bean;
import com.tck.daole.entity.IndexTuiJian;
import com.tck.daole.thread.HttpInterface;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.EasyPermissionsEx;
import com.tck.daole.util.LocationHelper;
import com.tck.daole.util.LocationUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.AutoScrollTextView;
import com.tck.daole.view.NiceRecyclerView;
import com.tck.daole.view.SetBanner;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 首页的Fragment
 * <p>
 * 首页
 */
public class IndexFragment extends Fragment implements View.OnClickListener {
    private Banner banner;
    private AutoScrollTextView autoTextView, autoTextView_two;
    private NiceRecyclerView productView;
    private List<Map<Object, String>> list = new ArrayList<>();
    private LinearLayout ll_index_lePinGou, ll_waiMai, ll_tuanGou, ll_chaoShi, ll_index_luxing, ll_location, ll_search;
    private LinearLayout ll_index_Flower, ll_index_Leisure, ll_index_Motion, ll_index_Life, ll_index_Fruits;
    private RelativeLayout rv_system_msg;
    private ImageView iv_index_img1, iv_index_img2, iv_index_img3, iv_index_cat;

    private ImageView iv_index_waimai_one, iv_index_waimai_two, iv_index_waimai_there, iv_index_waimai_four;   //外卖图片

    private ImageView iv_index_pindan_one;

    private TextView tv_index_pindan_name, tv_index_pindan_price, tv_index_pindan_xiaoliang;

    private String message;

    protected HttpInterface httpInterface;

    List<String> listBanner = new ArrayList<>();    //首页轮番图地址列表
    List<String> demographicsList = new ArrayList<String>();    //首页第一行滚动播报列表
    List<String> demographicsList_two = new ArrayList<String>();    //首页第二行滚动播报列表


    private String[] mNeedPermissionsList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private String longitude = "";//经度
    private String latitude = "";//纬度

    private String sp_token = "";

    private String takeProjectId_one = "";
    private String takeProjectId_two = "";
    private String takeProjectId_there = "";
    private String takeProjectId_four = "";

    private String adminStoreId_one = "";
    private String adminStoreId_two = "";
    private String adminStoreId_there = "";
    private String adminStoreId_four = "";

    LinearLayout ll_pindan_takeproject; //首页拼单
    String str_index_pindan_takeProjectId = ""; //乐拼购id

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        httpInterface = new HttpInterface(getActivity());

        View view = inflater.inflate(R.layout.fragment_index, container, false);
        Log.e("dsfsdf", Build.VERSION.SDK_INT + "=====" + Build.VERSION_CODES.KITKAT);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            view.findViewById(R.id.View).setVisibility(View.GONE);
        }

        //Toast.makeText(getActivity(), "线程ID："+Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();


        sendRedEnvelopes(getActivity(), "", "", "", "");

        initView(view);
        initListener();
        initData();


        return view;
    }


    private void initView(View view) {
        banner = (Banner) view.findViewById(R.id.banner);
        autoTextView = (AutoScrollTextView) view.findViewById(R.id.autoTextView);
        autoTextView_two = (AutoScrollTextView) view.findViewById(R.id.autoTextView_two);
        productView = (NiceRecyclerView) view.findViewById(R.id.productView);
        ll_index_lePinGou = (LinearLayout) view.findViewById(R.id.ll_index_lePinGou);
        ll_waiMai = (LinearLayout) view.findViewById(R.id.ll_waiMai);
        ll_tuanGou = (LinearLayout) view.findViewById(R.id.ll_tuanGou);
        ll_chaoShi = (LinearLayout) view.findViewById(R.id.ll_chaoShi);
        iv_index_img1 = (ImageView) view.findViewById(R.id.iv_index_img1);
        iv_index_img2 = (ImageView) view.findViewById(R.id.iv_index_img2);
        iv_index_img3 = (ImageView) view.findViewById(R.id.iv_index_img3);
        ll_index_luxing = (LinearLayout) view.findViewById(R.id.ll_index_luxing);
        ll_location = (LinearLayout) view.findViewById(R.id.ll_location);
        rv_system_msg = (RelativeLayout) view.findViewById(R.id.rv_system_msg);
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        ll_index_Flower = (LinearLayout) view.findViewById(R.id.ll_index_Flower);
        iv_index_cat = (ImageView) view.findViewById(R.id.iv_index_cat);

        ll_index_Leisure = (LinearLayout) view.findViewById(R.id.ll_index_Leisure);
        ll_index_Motion = (LinearLayout) view.findViewById(R.id.ll_index_Motion);
        ll_index_Life = (LinearLayout) view.findViewById(R.id.ll_index_Life);
        ll_index_Fruits = (LinearLayout) view.findViewById(R.id.ll_index_Fruits);


        //private ImageView iv_index_waimai_one,iv_index_waimai_two,iv_index_waimai_there,iv_index_waimai_four;
        iv_index_waimai_one = (ImageView) view.findViewById(R.id.iv_index_waimai_one);
        iv_index_waimai_two = (ImageView) view.findViewById(R.id.iv_index_waimai_two);
        iv_index_waimai_there = (ImageView) view.findViewById(R.id.iv_index_waimai_there);
        iv_index_waimai_four = (ImageView) view.findViewById(R.id.iv_index_waimai_four);

        iv_index_pindan_one = (ImageView) view.findViewById(R.id.iv_index_pindan_one);

        //private TextView tv_index_pindan_name,tv_index_pindan_price,tv_index_pindan_xiaoliang;
        tv_index_pindan_name = (TextView) view.findViewById(R.id.tv_index_pindan_name);
        tv_index_pindan_price = (TextView) view.findViewById(R.id.tv_index_pindan_price);
        tv_index_pindan_xiaoliang = (TextView) view.findViewById(R.id.tv_index_pindan_xiaoliang);

        ll_pindan_takeproject = (LinearLayout) view.findViewById(R.id.ll_pindan_takeproject);
    }

    private void initData() {


        // 使用了 EasyPermissionsEx 类来管理动态权限配置
        if (EasyPermissionsEx.hasPermissions(getActivity(), mNeedPermissionsList)) {
            initLocation();
        } else {
            EasyPermissionsEx.requestPermissions(getActivity(), "需要定位权限来获取当地天气信息", 1, mNeedPermissionsList);
        }


        //首页轮番图
        getIndexBenner();
        //banner数据

/*        listBanner.add("http://pic.qiantucdn.com/58pic/26/65/67/27w58PIC5HU_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/18/10/15/30Q58PICemX_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/18/10/15/60w58PICZUu_1024.jpg");
        listBanner.add("http://pic.qiantucdn.com/58pic/11/89/15/97Y58PICV3M.jpg");*/


        //文字滚动
        getRoll();
        //推荐
        TuiJian("78954", "54545", "1", "5");


        //RecyclerView数据
/*        Map<Object, String> map1 = new HashMap();
        map1.put("state", "1");
        Map<Object, String> map2 = new HashMap();
        map2.put("state", "1");
        Map<Object, String> map3 = new HashMap();
        map3.put("state", "1");
        Map<Object, String> map4 = new HashMap();
        map4.put("state", "1");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        IndexAdapter adapter = new IndexAdapter(list, getActivity());
        productView.setAdapter(adapter);

        adapter.setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            public void onItemClickListener(int position, String state) {
                Toast.makeText(getActivity(), state, Toast.LENGTH_SHORT).show();

                switch (state) {
                    case "1":
                        //startActivity(new Intent(getActivity(), ShopActivity.class));//跳转店铺
                        break;
                    case "2":
                        //startActivity(new Intent(getActivity(), PostedParticularsActivity.class));//跳转推荐
                        break;
                    case "3":
                        //startActivity(new Intent(getActivity(), ShopNameActivity.class));//跳转门店
                        break;
                }

            }
        });*/


    }


    //获取当前经纬度
    private void initLocation() {
        LocationUtils.getInstance(getActivity()).initLocation(new LocationHelper() {
            @Override
            public void UpdateLocation(Location location) {
                //Log.e("MoLin", "location.getLatitude():" + location.getLatitude());
/*                mLatitudeView.setText(location.getLatitude() + "");
                mLongtitudeView.setText(location.getLongitude() + "");*/
                longitude = location.getLongitude() + "";   //经度
                latitude = location.getLatitude() + "";     //纬度
                //Toast.makeText(getActivity(),"纬度"+location.getLatitude() + "\n"+"经度"+location.getLongitude() + "",Toast.LENGTH_SHORT).show();
                //Log.e("=====", longitude);


            }

            @Override
            public void UpdateStatus(String provider, int status, Bundle extras) {
            }

            @Override
            public void UpdateGPSStatus(GpsStatus pGpsStatus) {

            }

            @Override
            public void UpdateLastLocation(Location location) {
                //Log.e("MoLin", "UpdateLastLocation_location.getLatitude():" + location.getLatitude());
  /*              mLatitudeView.setText(location.getLatitude() + "");
                mLongtitudeView.setText(location.getLongitude() + "");*/
                //Toast.makeText(getActivity(),"纬度"+location.getLatitude() + "\n"+"经度"+location.getLongitude() + "",Toast.LENGTH_SHORT).show();
                longitude = location.getLongitude() + "";   //经度
                latitude = location.getLatitude() + "";     //纬度

                //Log.e("=====", longitude+"                    经纬度"+latitude);

                SPUtil.saveData(getActivity(), "longitude", longitude);
                SPUtil.saveData(getActivity(), "latitude", latitude);

                String sp_longitude = SPUtil.getData(getActivity(), "longitude", "").toString();
                String sp_latitude = SPUtil.getData(getActivity(), "latitude", "").toString();


                Log.e("=====", sp_longitude + "                    经纬度" + sp_latitude);


            }
        });
    }


    public void onDestroy() {
        // 在页面销毁时取消定位监听
        LocationUtils.getInstance(getActivity()).removeLocationUpdatesListener();
        super.onDestroy();
    }


    private void initListener() {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                //Toast.makeText(getActivity(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
            }
        });

        ll_index_lePinGou.setOnClickListener(this);
        ll_waiMai.setOnClickListener(this);
        ll_tuanGou.setOnClickListener(this);
        ll_chaoShi.setOnClickListener(this);
        iv_index_img1.setOnClickListener(this);
        iv_index_img2.setOnClickListener(this);
        iv_index_img3.setOnClickListener(this);
        ll_index_luxing.setOnClickListener(this);
        //ll_location.setOnClickListener(this);
        rv_system_msg.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_index_Flower.setOnClickListener(this);
        iv_index_cat.setOnClickListener(this);

        ll_index_Leisure.setOnClickListener(this);
        ll_index_Motion.setOnClickListener(this);
        ll_index_Life.setOnClickListener(this);
        ll_index_Fruits.setOnClickListener(this);

        iv_index_waimai_one.setOnClickListener(this);
        iv_index_waimai_two.setOnClickListener(this);
        iv_index_waimai_there.setOnClickListener(this);
        iv_index_waimai_four.setOnClickListener(this);

        ll_pindan_takeproject.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_index_lePinGou:
                startActivity(new Intent(getActivity(), SpellActivity.class));//跳转乐拼购
                break;
            case R.id.ll_chaoShi:
/*                Intent intent_chaoshi = new
                        Intent(getActivity(),MarketActivity.class);
                //在Intent对象当中添加一个键值对
                intent_chaoshi.putExtra("type","chaoshi");
                startActivity(intent_chaoshi);*/
                startActivity(new Intent(getActivity(), MarketActivity.class));//跳转超市
                break;
            case R.id.ll_tuanGou:
                Intent intent_tuangou = new
                        Intent(getActivity(), GroupActivity.class);
                //在Intent对象当中添加一个键值对
                intent_tuangou.putExtra("type", "tuangou");
                startActivity(intent_tuangou);
                //startActivity(new Intent(getActivity(), GroupActivity.class));//跳转美食团购
                break;
            case R.id.ll_waiMai:
                startActivity(new Intent(getActivity(), TakeoutActivity.class));//跳转外卖快递
                break;
            case R.id.iv_index_img1://首页横向第一张图片
                startActivity(new Intent(getActivity(), MerchandiseActivity.class));//跳转单个商品
                break;
            case R.id.iv_index_img2://首页横向第二张图片
                startActivity(new Intent(getActivity(), MerchandiseActivity.class));//跳转单个商品
                break;
            case R.id.iv_index_img3://首页横向第三张图片
                startActivity(new Intent(getActivity(), MerchandiseActivity.class));//跳转单个商品
                break;
            case R.id.ll_index_luxing:
                startActivity(new Intent(getActivity(), TravelActivity.class));//跳转大爱旅行
                break;
/*            case R.id.ll_location:
                startActivity(new Intent(getActivity(), LocationActivity.class));//跳转定位
                break;*/

            case R.id.rv_system_msg:
                startActivity(new Intent(getActivity(), SystemMsgActivity.class));//跳转系统消息
                break;

            case R.id.ll_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));//跳转搜索页
                break;

            case R.id.iv_index_cat:
                startActivity(new Intent(getActivity(), ShopcatActivity.class));//跳转购物车
                break;

            case R.id.ll_index_Flower:

                Intent intent_flower = new
                        Intent(getActivity(), GroupActivity.class);
                //在Intent对象当中添加一个键值对
                intent_flower.putExtra("type", "flower");
                startActivity(intent_flower);

                //startActivity(new Intent(getActivity(), FlowerActivity.class));//跳转花之语
                break;
            case R.id.ll_index_Leisure:

                Intent intent_leisure = new
                        Intent(getActivity(), GroupActivity.class);
                //在Intent对象当中添加一个键值对
                intent_leisure.putExtra("type", "leisure");
                startActivity(intent_leisure);

                //startActivity(new Intent(getActivity(), FlowerActivity.class));//跳转休闲娱乐
                break;
            case R.id.ll_index_Motion:

                Intent intent_motion = new
                        Intent(getActivity(), GroupActivity.class);
                //在Intent对象当中添加一个键值对
                intent_motion.putExtra("type", "motion");
                startActivity(intent_motion);

                //startActivity(new Intent(getActivity(), FlowerActivity.class));//跳转运动健康
                break;
            case R.id.ll_index_Life:

                Intent intent_life = new
                        Intent(getActivity(), GroupActivity.class);
                //在Intent对象当中添加一个键值对
                intent_life.putExtra("type", "life");
                startActivity(intent_life);

                //startActivity(new Intent(getActivity(), FlowerActivity.class));//跳转生活服务
                break;
            case R.id.ll_index_Fruits:

                Intent intent_fruite = new
                        Intent(getActivity(), GroupActivity.class);
                //在Intent对象当中添加一个键值对
                intent_fruite.putExtra("type", "fruite");
                startActivity(intent_fruite);

                //startActivity(new Intent(getActivity(), FlowerActivity.class));//跳转生鲜果蔬
                break;


/*            private String adminStoreId_one = "";
            private String adminStoreId_two = "";
            private String adminStoreId_there = "";
            private String adminStoreId_four = "";*/

            case R.id.iv_index_waimai_one:
                if (takeProjectId_one.equals("") || adminStoreId_one.equals("")) {

                } else {
                    //到商品详情页

                    Intent intent_one = new
                            Intent(getActivity(), MerchandiseActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_one.putExtra("takeProjectId", takeProjectId_one);
                    intent_one.putExtra("oid", adminStoreId_one);
                    startActivity(intent_one);
                }
                break;
            case R.id.iv_index_waimai_two:
                if (takeProjectId_two.equals("") || adminStoreId_two.equals("")) {

                } else {
                    Intent intent_one = new
                            Intent(getActivity(), MerchandiseActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_one.putExtra("takeProjectId", takeProjectId_two);
                    intent_one.putExtra("oid", adminStoreId_two);
                    startActivity(intent_one);
                }
                break;
            case R.id.iv_index_waimai_there:
                if (takeProjectId_there.equals("") || adminStoreId_there.equals("")) {

                } else {
                    Intent intent_one = new
                            Intent(getActivity(), MerchandiseActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_one.putExtra("takeProjectId", takeProjectId_there);
                    intent_one.putExtra("oid", adminStoreId_there);
                    startActivity(intent_one);
                }
                break;
            case R.id.iv_index_waimai_four:
                if (takeProjectId_four.equals("") || adminStoreId_four.equals("")) {

                } else {
                    Intent intent_one = new
                            Intent(getActivity(), MerchandiseActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_one.putExtra("takeProjectId", takeProjectId_four);
                    intent_one.putExtra("oid", adminStoreId_four);
                    startActivity(intent_one);
                }
                break;

            case R.id.ll_pindan_takeproject:
                Intent intent = new
                        Intent(getActivity(), SpellParticularsActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("oid", str_index_pindan_takeProjectId);
                Log.d("TaGGG", str_index_pindan_takeProjectId);
                startActivity(intent);
                break;


        }
    }


    //首页滚动播报
    public void getRoll() {
        if (NetUtil.isNetWorking(getActivity())) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getJournalism(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            //Log.e("获取新闻.成功", result);

                            Bean.JournalismModel data = new Gson().fromJson(result, Bean.JournalismModel.class);
                            List<Bean.Journalism> list = data.getModel();

                            if (list != null && !list.isEmpty()) {
                                //这个里面取list中的值
                                demographicsList.add("");
                                for (int i = 0; i < list.size(); ) {
                                    if (list.get(i) == null) {
                                        break;
                                    }
                                    demographicsList.add(list.get(i).getTitle());
                                    i = i + 2;
                                }
                                autoTextView.setList(demographicsList);
                                autoTextView.startScroll();


                            } else {
                                //做其他处理
                            }


                            if (list != null && !list.isEmpty()) {
                                //这个里面取list中的值
                                demographicsList_two.add("");
                                for (int i = 1; i <= list.size(); ) {
                                    if (list.get(i) == null) {
                                        break;
                                    }
                                    demographicsList_two.add(list.get(i).getTitle());
                                    i = i + 2;
                                }

                                autoTextView_two.setList(demographicsList_two);
                                autoTextView_two.startScroll();
                            } else {
                                //做其他处理
                            }


                        }

                        @Override
                        public void onFail(String response) {
                            //Log.e("获取新闻.异常", response);

                            Bean.UserModels data = new Gson().fromJson(response, Bean.UserModels.class);
                            message = data.getMessage();
                            //toast(message);

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            //Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            //Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            //toast(R.string.system_busy);
        }
    }


    //首页轮番图
    public void getIndexBenner() {
        if (NetUtil.isNetWorking(getActivity())) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.indexBanner(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            //Log.e("获取轮番图.成功", result);

                            Bean.BannerModel data = new Gson().fromJson(result, Bean.BannerModel.class);
                            List<Bean.Banner> list = data.getModel();
                            for (int i = 0; i < list.size(); i++) {
                                listBanner.add(UriUtil.ip + list.get(i).getImgPath());
                                //Log.i("233333",list.get(i).getImgPath());
                            }

                            SetBanner.startBanner(getActivity(), banner, listBanner);

                        }

                        @Override
                        public void onFail(String response) {
                            //Log.e("获取轮番图.异常", response);

                            Bean.UserModels data = new Gson().fromJson(response, Bean.UserModels.class);
                            message = data.getMessage();
                            //toast(message);

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            //Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            //Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            //toast(R.string.system_busy);
        }
    }


 /*   //创建对话框
    public void confirmFireMissiles() {
        DialogFragment newFragment = new HongBaoFragment();
        newFragment.show(getFragmentManager(),"missiles");
    }*/


    /**
     * 首页红包弹窗
     */
    public static void sendRedEnvelopes(final Activity ctx, String title, String content, String left, String right) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_hongbao, null);
        //       TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        RelativeLayout rl_fuli = (RelativeLayout) view.findViewById(R.id.rl_fuli);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        dialog.setCanceledOnTouchOutside(true); //设置点击对话框外关闭对话框


        rl_fuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //       dialog_title.setText(title);


//        dialog_left.setOnClickListener(new View.OnClickListener() {
        //           @Override
        //          public void onClick(View v) {
        //              dialog.dismiss();
        //          }
        //     });
        //     dialog_right.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //      public void onClick(View v) {
        //         onRightClickListener.onRightClick(dialog);
        //     }
        // });
        dialog.show();
    }


    //首页拼购推荐推荐，外卖推荐、店铺推荐
    public void TuiJian(final String dimensionality, final String longitude, final String lTakeProjecttype, final String wTakeProjecttype) {
        if (NetUtil.isNetWorking(getActivity())) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getTuiJian(dimensionality, longitude, lTakeProjecttype, wTakeProjecttype, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.i("获取推荐.成功", result);


                            IndexTuiJian data = new Gson().fromJson(result, IndexTuiJian.class);

                            if (data.status == 1) {
                                List<IndexTuiJian.WaiMai> list_waimai = data.model.waimai;

                                //private ImageView iv_index_waimai_one,iv_index_waimai_two,iv_index_waimai_there,iv_index_waimai_four;


                                DisplayImageOptions options_one = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.mipmap.zhanwei_f)//等待加载显示图片
                                        .showImageOnFail(R.mipmap.zhanwei_f)//显示错误图片
                                        .cacheInMemory(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .build();

                                //显示图片的配置
                                DisplayImageOptions options_two = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.mipmap.zhanwei_d)//等待加载显示图片
                                        .showImageOnFail(R.mipmap.zhanwei_d)//显示错误图片
                                        .cacheInMemory(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .build();

                                DisplayImageOptions options_there = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.mipmap.zhanwei_e)//等待加载显示图片
                                        .showImageOnFail(R.mipmap.zhanwei_e)//显示错误图片
                                        .cacheInMemory(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .build();

                                if (list_waimai.size() > 3) {

                                    //takeProjectId

                                    adminStoreId_one = list_waimai.get(0).adminStoreId;
                                    adminStoreId_two = list_waimai.get(1).adminStoreId;
                                    adminStoreId_there = list_waimai.get(2).adminStoreId;
                                    adminStoreId_four = list_waimai.get(3).adminStoreId;


                                    takeProjectId_one = list_waimai.get(0).takeProjectId;
                                    takeProjectId_two = list_waimai.get(1).takeProjectId;
                                    takeProjectId_there = list_waimai.get(2).takeProjectId;
                                    takeProjectId_four = list_waimai.get(3).takeProjectId;


                                    //list_waimai.get(0).takeProjectId;
                                    Log.i("外卖", list_waimai.get(0).takeProjectId);
                                    Log.i("外卖", list_waimai.get(1).takeProjectId);
                                    Log.i("外卖", list_waimai.get(2).takeProjectId);
                                    Log.i("外卖", list_waimai.get(3).takeProjectId);


                                    ImageLoader.getInstance().displayImage(UriUtil.ip + list_waimai.get(0).projectImage, iv_index_waimai_one, options_one);
                                    ImageLoader.getInstance().displayImage(UriUtil.ip + list_waimai.get(1).projectImage, iv_index_waimai_two, options_two);
                                    ImageLoader.getInstance().displayImage(UriUtil.ip + list_waimai.get(2).projectImage, iv_index_waimai_there, options_there);
                                    ImageLoader.getInstance().displayImage(UriUtil.ip + list_waimai.get(3).projectImage, iv_index_waimai_four, options_there);
                                }


                                List<IndexTuiJian.PinGou> list_pingou = data.model.lepingou;

                                //显示图片的配置
                                DisplayImageOptions options_pingou = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.mipmap.zhanwei_a)//等待加载显示图片
                                        .showImageOnFail(R.mipmap.zhanwei_a)//显示错误图片
                                        .cacheInMemory(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .build();
                                if (list_pingou.size() > 0) {
                                    ImageLoader.getInstance().displayImage(UriUtil.ip + list_pingou.get(0).projectImage, iv_index_pindan_one, options_pingou);

                                    //private TextView tv_index_pindan_name,tv_index_pindan_price,tv_index_pindan_xiaoliang;
                                    str_index_pindan_takeProjectId = list_pingou.get(0).takeProjectId;
                                    tv_index_pindan_name.setText(list_pingou.get(0).takeProjectName);
                                    tv_index_pindan_price.setText("¥" + list_pingou.get(0).price);
                                    tv_index_pindan_xiaoliang.setText("已拼" + list_pingou.get(0).saleNum + "件");
                                }


                                List<IndexTuiJian.Store> list_store = data.model.store;

                                for (int i = 0; i < list_store.size(); i++) {
                                    Map<Object, String> map = new HashMap<>();
                                    map.put("state", "1");                //	列表类别，店铺

                                    map.put("stoeId", list_store.get(i).stoeId); //	店铺ID
                                    map.put("logoImage", list_store.get(i).logoImage); //	店铺z图片
                                    map.put("name", list_store.get(i).name); //	店铺名字
                                    map.put("evaluateIdstarlevel", list_store.get(i).evaluateIdstarlevel); //	店铺星级
                                    map.put("storeAddress", list_store.get(i).storeAddress); //	店铺收藏ID
                                    map.put("jvLi", list_store.get(i).jvLi); //	店铺距离

                                    list.add(map);
                                }


                                IndexAdapter adapter = new IndexAdapter(list, getActivity());
                                productView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
                                    public void onItemClickListener(int position, String state) {
                                        //Toast.makeText(getActivity(), state, Toast.LENGTH_SHORT).show();

                                        switch (state) {
                                            case "1":
                                                //startActivity(new Intent(getActivity(), ShopActivity.class));//跳转店铺

                                                String stoeId = (String) list.get(position).get("stoeId");

                                                Intent intent = new
                                                        Intent(getActivity(), ShopActivity.class);
                                                //在Intent对象当中添加一个键值对
                                                intent.putExtra("oid", stoeId);
                                                startActivity(intent);


                                                break;
/*                    case "2":
                        //startActivity(new Intent(getActivity(), PostedParticularsActivity.class));//跳转推荐
                        break;
                    case "3":
                        //startActivity(new Intent(getActivity(), ShopNameActivity.class));//跳转门店
                        break;*/
                                        }

                                    }
                                });

                            }


                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取推荐.异常", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            //Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            //Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            //toast(R.string.system_busy);
        }
    }


}
