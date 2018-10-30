package com.tck.daole.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.activity.DingDanPageActivity;
import com.tck.daole.activity.GasChargeActivity;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.fragment.DingDanFiveFragment;
import com.tck.daole.fragment.DingDanFourFragment;
import com.tck.daole.fragment.DingDanOneFragment;
import com.tck.daole.fragment.DingDanThreeFragment;
import com.tck.daole.fragment.DingDanTwoFragment;
import com.tck.daole.thread.HttpInterface;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.MUIToast;
import com.tck.daole.thread.ThreadPoolManager;

import okhttp3.Call;

/**
 * kylin on 2018/1/31.
 */

public class OrderUtil {
    private Activity activity;
    private HttpInterface httpInterface;

    public OrderUtil(Activity activity, HttpInterface httpInterface){
        this.activity=activity;
        this.httpInterface=httpInterface;
    }

    //取消订单
    public void cancelOrder(final String oid, final Fragment fragment, final int position){
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                httpInterface.cancelOrder(App.token,oid, new MApiResultCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        Log.e("cancelOrder", result);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bean.State data = new Gson().fromJson(result, Bean.State.class);
                                if (data.status==1){
                                    if (activity instanceof DingDanPageActivity){
                                        if (fragment instanceof DingDanOneFragment){
                                            ((DingDanOneFragment)fragment).refreshData(position,"6");
                                        }if (fragment instanceof DingDanTwoFragment){
                                            ((DingDanTwoFragment)fragment).refreshData(position,"6");
                                        }if (fragment instanceof DingDanFiveFragment){
                                            ((DingDanFiveFragment)fragment).refreshData(position,"6");
                                        }
//                                        if (fragment instanceof DingDanThreeFragment){
//                                            ((DingDanThreeFragment)fragment).refreshData(position,"6");
//                                        }if (fragment instanceof DingDanFourFragment){
//                                            ((DingDanFourFragment)fragment).refreshData(position,"6");
//                                        }
                                    }

                                }else {
                                    MUIToast.show(activity,data.message);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFail(String response) {
                        Log.e("onFail", response);
                    }

                    @Override
                    public void onError(Call call, Exception exception) {
                        Log.e("onError", exception.getMessage()+"");
                    }

                    @Override
                    public void onTokenError(String response) {

                    }
                });
                }
            });
        } else {
            MUIToast.show(activity,activity.getResources().getString(R.string.system_busy));
        }
    }

    //订单退货接口
    public void returnOrder(final String oid, final Fragment fragment, final int position){
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.returnOrder(App.token,oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            Log.e("cancelOrder", result);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bean.State data = new Gson().fromJson(result, Bean.State.class);
                                    if (data.status==1){
                                        if (activity instanceof DingDanPageActivity){
                                            if (fragment instanceof DingDanThreeFragment){
                                                ((DingDanThreeFragment)fragment).refreshData(position,"5");
                                            }
//                                            if (fragment instanceof DingDanOneFragment){
//                                                ((DingDanOneFragment)fragment).getListData();
//                                            }if (fragment instanceof DingDanTwoFragment){
//                                                ((DingDanTwoFragment)fragment).getListData();
//                                            }if (fragment instanceof DingDanFourFragment){
//                                                ((DingDanFourFragment)fragment).getListData();
//                                            }if (fragment instanceof DingDanFiveFragment){
//                                                ((DingDanFiveFragment)fragment).getListData();
//                                            }
                                        }

                                    }else {
                                        MUIToast.show(activity,data.message);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("onFail", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", exception.getMessage()+"");
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        } else {
            MUIToast.show(activity,activity.getResources().getString(R.string.system_busy));
        }
    }

    //订单确认送达接口
    public void confirmOrder(final String oid, final Fragment fragment, final int position){
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.confirmOrder(App.token,oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            Log.e("cancelOrder", result);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bean.State data = new Gson().fromJson(result, Bean.State.class);
                                    if (data.status==1){
                                        if (activity instanceof DingDanPageActivity){
//                                            if (fragment instanceof DingDanOneFragment){
//                                                ((DingDanOneFragment)fragment).getListData();
//                                            }
                                            if (fragment instanceof DingDanTwoFragment){
                                                ((DingDanTwoFragment)fragment).refreshData(position,"4");
                                            }
//                                            if (fragment instanceof DingDanThreeFragment){
//                                                ((DingDanThreeFragment)fragment).getListData();
//                                            }if (fragment instanceof DingDanFourFragment){
//                                                ((DingDanFourFragment)fragment).getListData();
//                                            }
                                            if (fragment instanceof DingDanFiveFragment){
                                                ((DingDanFiveFragment)fragment).refreshData(position,"4");
                                            }
                                        }

                                    }else {
                                        MUIToast.show(activity,data.message);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("onFail", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", exception.getMessage()+"");
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        } else {
            MUIToast.show(activity,activity.getResources().getString(R.string.system_busy));
        }
    }
}
