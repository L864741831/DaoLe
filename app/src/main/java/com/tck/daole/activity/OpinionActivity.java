package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.StringUtil;

import okhttp3.Call;


/**
 * 意见反馈页
 */

public class OpinionActivity extends BaseActivity {
    private LinearLayout ll_back;
    private EditText et_opinion_feedback,et_opinion_phone;
    private Button btn_opinion_feedback;

//    private String sp_token = "";



    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_opinion);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);
        et_opinion_feedback = findView(R.id.et_opinion_feedback);
        et_opinion_phone = findView(R.id.et_opinion_phone);
        btn_opinion_feedback = findView(R.id.btn_opinion_feedback);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        btn_opinion_feedback.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(OpinionActivity.this, "token", "").toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_opinion_feedback:
                if(!App.islogin){
                    toast("请先登录");
                    startActivity(new Intent(OpinionActivity.this, LoginActivity.class));
                    return;
                }
                String feedback = et_opinion_feedback.getText().toString();
                String phone = et_opinion_phone.getText().toString();
                if (StringUtil.isSpace(feedback)){
                    toast("请输入意见");
                    return;
                }
                if (StringUtil.isSpace(phone)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(phone)){
                    toast("请输入正确的手机号");
                    return;
                }
                feedBack(App.token,phone,feedback);

                break;
        }
    }












    //意见反馈
    public void feedBack(final String token,final String phone,final String content){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.addfeedback(token,phone,content, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.e("反馈.成功", result);

     /*                       Bean data = new Gson().fromJson(result, Bean.class);
                            toast(data.getMessage());*/
                            Bean data = new Gson().fromJson(result, Bean.class);
                            toast(data.getMessage());
                            finish();

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("反馈.异常", response);
/*                            Bean data = new Gson().fromJson(response, Bean.class);
                            toast(data.getMessage());*/
                            Bean data = new Gson().fromJson(response, Bean.class);
                            toast(data.getMessage());
                            finish();

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