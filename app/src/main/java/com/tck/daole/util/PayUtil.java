package com.tck.daole.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.tck.daole.R;
import com.tck.daole.activity.ElectricChargeActivity;
import com.tck.daole.activity.FuelChargeActivity;
import com.tck.daole.activity.GamesChargeActivity;
import com.tck.daole.activity.GasChargeActivity;
import com.tck.daole.activity.LiuliangChargeActivity;
import com.tck.daole.activity.PayActivity;
import com.tck.daole.activity.PhoneChargeActivity;
import com.tck.daole.activity.WaterChargeActivity;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.thread.HttpInterface;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.MUIToast;
import com.tck.daole.thread.ThreadPoolManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * kylin on 2018/1/31.
 */

public class PayUtil {
    private static final int SDK_PAY_FLAG = 0x11;

    private BaseActivity activity;
    private HttpInterface httpInterface;
    private String phone;
    private String num;
    private String itemId;

    public PayUtil(BaseActivity activity, HttpInterface httpInterface){
        this.activity=activity;
        this.httpInterface=httpInterface;
    }

    //支付宝支付
    public void ApliyPay(final String no,final String phone,final String num,final String itemId){
        this.phone=phone;
        this.num=num;
        this.itemId=itemId;
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
//                @SuppressLint("SimpleDateFormat") String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                Log.e("no",no);
                httpInterface.alipaypay(no, new MApiResultCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("alipaypay", result);
                        JSONObject jsonObject;
                        try {
                            jsonObject=new JSONObject(result);
//                            if (jsonObject.optInt("code")==200){
                                final String orderInfo=jsonObject.optString("data");

                            //Log.e("alipaypay233", orderInfo);


                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        PayTask alipay = new PayTask(activity);
                                        Map result = alipay.payV2(orderInfo,true);

                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };
                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
//                            }else {

//                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            activity.toast(R.string.system_busy);
        }
    }




    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map result =(HashMap)msg.obj;
            Log.e("result","result="+result);
            String resultStatus=result.get("resultStatus")+"";
//            switch (resultStatus){
//                case "9000":
//                    MUIToast.show(activity,"支付成功！");
//                    if (activity instanceof PhoneChargeActivity){
//                        ((PhoneChargeActivity)activity).phone1_charge(phone,num);
//                    }
//                    activity.finish();
//                    break;
//                case "4000":
//                    MUIToast.show(activity,"系统异常，请重试！");
//                    break;
//                case "6001":
//                    MUIToast.show(activity,"已取消支付");
//                    break;
//                case "6002":
//                    MUIToast.show(activity,"网络连接出错，请检查网络");
//                    break;
//            }

            //MUIToast.show(activity,"支付成功！");

            if (activity instanceof PhoneChargeActivity){
                ((PhoneChargeActivity)activity).phone2_charge();
            }
            if (activity instanceof LiuliangChargeActivity){
                ((LiuliangChargeActivity)activity).liuliang2_charge(phone);
            }
            if (activity instanceof GamesChargeActivity){
                ((GamesChargeActivity)activity).charge(num,phone);
            }
            if (activity instanceof FuelChargeActivity){
                ((FuelChargeActivity)activity).charge();
            }
            if (activity instanceof ElectricChargeActivity){
                ((ElectricChargeActivity)activity).charge();
            }
            if (activity instanceof WaterChargeActivity){
                ((WaterChargeActivity)activity).charge();
            }
            if (activity instanceof GasChargeActivity){
                ((GasChargeActivity)activity).charge();
            }
            if (activity instanceof PayActivity){
//                Intent intent=new Intent();
                MUIToast.show(activity,"支付成功！");
                activity.setResult(8);
                activity.finish();
            }
        }
    };
}
