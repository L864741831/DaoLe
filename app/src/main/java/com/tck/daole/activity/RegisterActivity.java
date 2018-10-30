package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.QuickLoginUtil;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.StringUtil;
import com.tck.daole.view.MyCountDownTimer;

import okhttp3.Call;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity {
    private LinearLayout ll_back;

    private TextView register_title, login_qiehuan, register_huanying, register_liji, register_wangji;
    private Button register_button;
    private ImageView qqLoginBtn,wxLoginBtn;
    private EditText register_phone_zhuce, password_register_yanzhengma, password_register_login;
    private TextView register_yanzheng;

    private String YanZhengMa = "";
    private String message = "";

    private String token = "";

    private String register_phone = "";
    private String register_yanzhengma = "";
    private String register_password = "";

    private QuickLoginUtil quickLoginUtil;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }
        findViewId();
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);

        wxLoginBtn = findView(R.id.wxLoginBtn);
        qqLoginBtn = findView(R.id.qqLoginBtn);
        register_title = findView(R.id.register_title);
        login_qiehuan = findView(R.id.login_qiehuan);
        register_huanying = findView(R.id.register_huanying);
        register_liji = findView(R.id.register_liji);
        register_wangji = findView(R.id.register_wangji);
        register_yanzheng = findView(R.id.register_yanzheng);
        register_button = findView(R.id.register_button);
        register_phone_zhuce = findView(R.id.register_phone_zhuce);
        password_register_yanzhengma = findView(R.id.password_register_yanzhengma);
        password_register_login = findView(R.id.password_register_login);
        quickLoginUtil=new QuickLoginUtil(this,httpInterface);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        qqLoginBtn.setOnClickListener(this);
        wxLoginBtn.setOnClickListener(this);
        register_button.setOnClickListener(this);
        register_yanzheng.setOnClickListener(this);
        register_wangji.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        register_title.setText("注册");
        login_qiehuan.setVisibility(View.GONE);
        register_huanying.setText("欢迎来到點8网");
        register_liji.setVisibility(View.GONE);
        register_wangji.setVisibility(View.GONE);
        register_button.setText("注册");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.register_wangji:
                startActivity(new Intent(RegisterActivity.this, ForgetPasswordActivity.class));  //忘记密码
                break;

            case R.id.register_button:
                register_phone = register_phone_zhuce.getText().toString().trim();
                register_yanzhengma = password_register_yanzhengma .getText().toString().trim();
                register_password = password_register_login.getText().toString().trim();
                if (StringUtil.isSpace(register_phone)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(register_phone)){
                    toast("请输入正确的手机号");
                    return;
                }
                if (StringUtil.isSpace(register_yanzhengma)){
                    toast("请输入验证码");
                    return;
                }
                if (register_yanzhengma.length() != 4){
                    toast("请输入4位验证码");
                    return;
                }
                if (StringUtil.isSpace(register_password)){
                    toast("请输入密码");
                    return;
                }
                if (register_password.matches("^[\u4E00-\u9FA5]{1,20}$")){
                    toast("密码不能为汉字");
                    return;
                }
                if (register_password.length() < 6){
                    toast("密码长度不能小于6位");
                    return;
                }
                Register(register_phone,register_yanzhengma,register_password);
                break;
            case R.id.register_yanzheng:
                String str = register_phone_zhuce.getText().toString().trim();
                if (StringUtil.isSpace(str)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(str)){
                    toast("请输入正确的手机号");
                    return;
                }
                password_register_yanzhengma.setText("");
                getYanZhengMa(str);
                break;
            case R.id.wxLoginBtn://微信登录
                quickLoginUtil.wx();
                break;
            case R.id.qqLoginBtn://qq登录
                quickLoginUtil.qq();
                break;
        }
    }

//注册
    public void Register(final String register_phone,final String register_yanzhengma,final String register_password){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.register(register_phone,register_yanzhengma,register_password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("注册.成功", result);

                            Bean.UserModels data= new Gson().fromJson(result,Bean.UserModels.class);

                            if(data.status==0){
                                message  = data.getMessage();
                                toast(message);
                            }else{
                                token = data.getToken();
                                SPUtil.saveData(RegisterActivity.this,"token",token);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }


/*                                SPUtil.saveData(RegisterActivity.this,"user_phone",register_phone);
                                SPUtil.saveData(RegisterActivity.this,"user_password",register_password);*/

//                                String msg_token = SPUtil.getData(RegisterActivity.this,"token","").toString();
/*                                String msg_phone = SPUtil.getData(RegisterActivity.this,"user_phone","没有").toString();
                                String msg_password = SPUtil.getData(RegisterActivity.this,"user_password","没有").toString();*/

/*                               toast(msg_token+"\n"+msg_phone+"\n"+msg_password+"\n"+message);*/
//                                toast(msg_token+"\n"+message);

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("注册.异常", response);
                            Bean.UserModels data= new Gson().fromJson(response,Bean.UserModels.class);

                            message  = data.getMessage();

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

                            YanZhengMa = data.getYzm();
                            message  = data.getMessage();

                            toast(YanZhengMa+"\n"+message);
                            MyCountDownTimer timer = new MyCountDownTimer(RegisterActivity.this, register_yanzheng, 60000, 1000);
                            timer.start();
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取验证码.异常", response);

                            Bean.YZMModel data= new Gson().fromJson(response,Bean.YZMModel.class);
                            message  = data.getMessage();
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
