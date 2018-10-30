package com.tck.daole.util;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.HttpInterface;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;

import okhttp3.Call;

/**
 * kylin on 2018/1/31.
 */

public class UserUtil {
    private BaseActivity activity;
    private HttpInterface httpInterface;

    public UserUtil(BaseActivity activity, HttpInterface httpInterface){
        this.activity=activity;
        this.httpInterface=httpInterface;
    }

    //获得单个用户信息
    public void getUserInformation(final String token){
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getUser(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("onSuccess", result);
                            Bean.LoginInfo info=new Gson().fromJson(result, Bean.LoginInfo.class);
                            if (info!=null){
                                App.login=info.model;
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取.异常", response);
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
            activity.toast(R.string.system_busy);
        }
    }
}
