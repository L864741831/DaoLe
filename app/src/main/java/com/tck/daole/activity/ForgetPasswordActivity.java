package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.util.NetUtil;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.StringUtil;
import com.tck.daole.view.MyCountDownTimer;
import com.google.gson.Gson;


import okhttp3.Call;

/**
 * 忘记密码页面
 */
public class ForgetPasswordActivity extends BaseActivity {
    private LinearLayout ll_back;

    private EditText ed_forget_phone,ed_forget_yanzhengma,ed_forget_new_password,ed_forget_again_new_password;

    private TextView forget_yanzheng;

    private Button btn_forget_modify;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_password);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.store_View).setVisibility(View.GONE);
        }
        findView();
    }

    public void findView(){
        ll_back = findView(R.id.ll_back);

        ed_forget_phone = findView(R.id.ed_phone);
        ed_forget_yanzhengma = findView(R.id.ed_forget_yanzhengma);
        ed_forget_new_password = findView(R.id.ed_forget_new_password);
        ed_forget_again_new_password = findView(R.id.ed_forget_again_new_password);

        forget_yanzheng = findView(R.id.forget_yanzheng);

        btn_forget_modify = findView(R.id.btn_forget_modify);



    }
    @Override
    protected void initListener() {

        ll_back.setOnClickListener(this);

        forget_yanzheng.setOnClickListener(this);
        btn_forget_modify.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.forget_yanzheng:
                String phone = ed_forget_phone.getText().toString().trim();
                if (StringUtil.isSpace(phone)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(phone)){
                    toast("请输入正确的手机号");
                    return;
                }
                getYanZhengMa(phone);
                break;
            case R.id.btn_forget_modify:
                String modify_phone = ed_forget_phone.getText().toString().trim();
                String modify_yanzhengma = ed_forget_yanzhengma.getText().toString().trim();
                String modify_new_password = ed_forget_new_password.getText().toString().trim();
                String modify_again_new_password = ed_forget_again_new_password.getText().toString().trim();
                if (StringUtil.isSpace(modify_phone)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(modify_phone)){
                    toast("请输入正确的手机号");
                    return;
                }
                if (StringUtil.isSpace(modify_yanzhengma)){
                    toast("请输入验证码");
                    return;
                }
                if (StringUtil.isSpace(modify_new_password)){
                    toast("请输入新密码");
                    return;
                }
                if (StringUtil.isSpace(modify_again_new_password)){
                    toast("请输入新密码");
                    return;
                }
                if(!modify_new_password.equals(modify_again_new_password)){
                    toast("两次密码输入不一致");
                    return;
                }

                setUpdatePassword(modify_yanzhengma,modify_phone,modify_new_password);
                break;


        }
    }







    //提交忘记密码
    public void setUpdatePassword(final String inputYzm,final String phone,final String password) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.updatePassword(inputYzm,phone,password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取验证码.成功", result);

                            Bean.YZMModel data= new Gson().fromJson(result,Bean.YZMModel.class);

                            String YanZhengMa = data.getYzm();
                            String message  = data.getMessage();

                            //toast(YanZhengMa+"\n"+message);
                            MyCountDownTimer timer = new MyCountDownTimer(ForgetPasswordActivity.this, forget_yanzheng, 60000, 1000);
                            timer.start();

                            finish();
                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取验证码.异常", response);

                            Bean.YZMModel data= new Gson().fromJson(response,Bean.YZMModel.class);
                            String message  = data.getMessage();
                            toast(message);
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












    //获得验证码
    public void getYanZhengMa(final String phone) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.sendYzm(phone, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取验证码.成功", result);

                            Bean.YZMModel data= new Gson().fromJson(result,Bean.YZMModel.class);

                            String YanZhengMa = data.getYzm();
                            String message  = data.getMessage();

                            //toast(YanZhengMa+"\n"+message);
                            MyCountDownTimer timer = new MyCountDownTimer(ForgetPasswordActivity.this, forget_yanzheng, 60000, 1000);
                            timer.start();
                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取验证码.异常", response);

                            Bean.YZMModel data= new Gson().fromJson(response,Bean.YZMModel.class);
                            String message  = data.getMessage();
                            toast(message);
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

}
