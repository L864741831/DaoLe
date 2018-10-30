package com.tck.daole.util;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.activity.BindPhoneActivity;
import com.tck.daole.activity.LoginActivity;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.HttpInterface;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.MUIToast;
import com.tck.daole.thread.ThreadPoolManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * kylin on 2018/1/31.
 */

public class QuickLoginUtil {
    private BaseActivity activity;
    private HttpInterface httpInterface;

    public QuickLoginUtil(BaseActivity activity, HttpInterface httpInterface){
        this.activity=activity;
        this.httpInterface=httpInterface;
    }

    public void wx() {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, authListener);
    }

    public void qq() {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.QQ, authListener);
    }

    private UMAuthListener authListener=new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.e(share_media.getName(),"map="+map);
            final String no=map.get("openid");
            Log.e("no",no);
            if ("qq".equals(share_media.getName())){
                qqLogin(no);
            }else {
                wxLogin(no);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.e(share_media.getName(),"i="+i+",throwable="+throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.e(share_media.getName(),"i="+i);
        }
    };

    public void wxLogin(final String no) {
        httpInterface.wxLogin(no, new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            int phoneBind=jsonObject.optInt("phonebind");
                            String token=jsonObject.optString("token");
                            String phone=jsonObject.optString("phone");
                            if (status==1){
                                if (phoneBind==1){
                                    SPUtil.saveData(activity,"token",token);
                                    SPUtil.saveData(activity,"phone",phone);
                                    SPUtil.saveData(activity,"islogin",true);
                                    App.phone=phone;
                                    App.token=token;
                                    App.islogin=true;
                                    activity.finish();
                                }
                                else {
                                    Intent intent =new Intent(activity,BindPhoneActivity.class);
                                    intent.putExtra("wechaOpenid",no);
                                    intent.putExtra("flag",0);
                                    activity.startActivityForResult(intent,666);
                                }
                            }
                            else {
                                MUIToast.show(activity,jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    public void qqLogin(final String no) {
        httpInterface.qqLogin(no, new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            int phoneBind=jsonObject.optInt("phoneBinds");
                            String token=jsonObject.optString("token");
                            String phone=jsonObject.optString("phone");
                            if (status==1){
                                if (phoneBind==1){
                                    SPUtil.saveData(activity,"token",token);
                                    SPUtil.saveData(activity,"phone",phone);
                                    SPUtil.saveData(activity,"islogin",true);
                                    App.phone=phone;
                                    App.token=token;
                                    App.islogin=true;
                                    activity.finish();
                                }
                                else {
                                    Intent intent =new Intent(activity,BindPhoneActivity.class);
                                    intent.putExtra("wechaOpenid",no);
                                    intent.putExtra("flag",1);
                                    activity.startActivityForResult(intent,666);
                                }
                            }
                            else {
                                MUIToast.show(activity,jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
}
