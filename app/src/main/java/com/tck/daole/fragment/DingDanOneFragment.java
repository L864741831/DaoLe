package com.tck.daole.fragment;

/*
 * kylin on 2018/2/3.
 */

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.activity.BookingParticularsActivity;
import com.tck.daole.activity.LoginActivity;
import com.tck.daole.activity.MerchandiseActivity;
import com.tck.daole.activity.OrderDetailsActivity;
import com.tck.daole.activity.PayActivity;
import com.tck.daole.activity.SpellParticularsActivity;
import com.tck.daole.adapter.OrderAdapter;
import com.tck.daole.base.BaseFragment;
import com.tck.daole.entity.OrderList;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.DialogUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.OrderUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 订单待付款fragment
 */
public class DingDanOneFragment extends BaseFragment {
    private NiceRecyclerView rlv_allorder;
    private List<OrderList.Shop> shop_list = new ArrayList<>();
    private OrderAdapter adapter;
    private int index=0,index2=15;
    private int num=15;
    private OrderUtil orderUtil;
    private DialogUtils dialogUtils;

//    @Override
//    public int getLayoutID() {
//        return R.layout.fragment_order_one;
//    }

    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_order_one);
        smart.setEnableRefresh(true);
        smart.setEnableLoadmore(true);
        rlv_allorder =(NiceRecyclerView) view.findViewById(R.id.rlv_allorder);
        adapter = new OrderAdapter(shop_list, activity);
        rlv_allorder.setAdapter(adapter);

        orderUtil=new OrderUtil(activity,httpInterface);
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(activity, OrderDetailsActivity.class);
                intent.putExtra("oid",shop_list.get(position).orderId);
                activity.startActivity(intent);
            }

            @Override
            public void onProjectItemClick(int position,int projectPosition) {
                int type=shop_list.get(position).orderType;
                if (type==1){
                    Intent intent=new Intent(activity, SpellParticularsActivity.class);
                    intent.putExtra("oid",shop_list.get(position).TakeOutOrderProject.get(projectPosition).orderProjectId);
                    activity.startActivity(intent);
                }else if (type==2){
                    Intent intent=new Intent(activity, BookingParticularsActivity.class);
                    intent.putExtra("travelType","ll_booking");
                    intent.putExtra("oid",shop_list.get(position).TakeOutOrderProject.get(projectPosition).orderProjectId);
                    activity.startActivity(intent);
                }else if (type==3){
                    Intent intent=new Intent(activity, BookingParticularsActivity.class);
                    intent.putExtra("travelType","ll_All");
                    intent.putExtra("oid",shop_list.get(position).TakeOutOrderProject.get(projectPosition).orderProjectId);
                    activity.startActivity(intent);
                }else if (type != 4) {
                    Intent intent=new Intent(activity, MerchandiseActivity.class);
                    intent.putExtra("takeProjectId",shop_list.get(position).TakeOutOrderProject.get(projectPosition).orderProjectId);
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onJuJueClick(final int position) {

                dialogUtils=new DialogUtils(activity,true,"取消订单","您确认要取消这条订单吗？","取消","确认",true,true);
                dialogUtils.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
                    @Override
                    public void onClick() {
                        dialogUtils.hide();
                    }
                });
                dialogUtils.setOnTwoBtnClickListener(new DialogUtils.OnTwoBtnClickListener() {
                    @Override
                    public void onClick() {
                        dialogUtils.hide();
                        orderUtil.cancelOrder(shop_list.get(position).orderId,DingDanOneFragment.this,position);
                    }
                });
                dialogUtils.show();

            }

            @Override
            public void onJieShouClick(int position) {
                Intent intent=new Intent(activity, PayActivity.class);
                intent.putExtra("allMoney",shop_list.get(position).payMoney);
                intent.putExtra("ORDER_ID",shop_list.get(position).orderId);
                activity.startActivityForResult(intent,6);
            }
        });

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index=0;
                index2=num;
                getListData();
            }
        });
        smart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index+=num;
                index2+=num;
                getListData();
            }
        });
    }

    @Override
    protected void initData() {
        getListData();
    }

    public void getListData() {
        if (App.islogin) {
            getOrder(App.token, index+"", index2+"", "1");
        } else {
            startActivity(new Intent(activity, LoginActivity.class));//跳转登录
        }
    }

    public void refreshData(int position,String orderState) {
//        shop_list.get(position).orderState=orderState;
        shop_list.remove(position);
//        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    //获得全部订单
    public void getOrder(final String token, final String index, final String num, final String orderState) {
        if (NetUtil.isNetWorking(activity)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getOrderList(token, index, num, orderState, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("getOrderList", result);
                            OrderList data = new Gson().fromJson(result, OrderList.class);
                            if (data.status==1){
                                if (!smart.isLoading()) {
                                    shop_list.clear();
                                }
                                shop_list.addAll(data.list);
                                adapter.notifyDataSetChanged();
                            }else {
                                toast(data.message);
                            }
                            finishRefresh();
                        }

                        @Override
                        public void onFail(String response) {
                            finishRefresh();
                            Log.e("获取订单列表.异常", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            finishRefresh();
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(activity, R.string.system_busy, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==6&&resultCode==8){
            refreshData(data.getIntExtra("position",0),"0");
        }
    }
}




