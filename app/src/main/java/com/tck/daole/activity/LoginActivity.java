package com.tck.daole.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.MainActivity;
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
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    private LinearLayout ll_back;
    private TextView login_qiehuan,register_yanzheng,register_liji,register_wangji;
    private EditText register_phone_zhuce,password_register_yanzhengma,password_register_login;
    private Button register_button;
    private ImageView qqLoginBtn,wxLoginBtn;

    private int login_type = 0; //1为验证码登录 0为密码登录

    private String YanZhengMa = "";
    private String message = "";

    private String register_phone = "";
    private String register_password = "";
    private String register_yanzhengma = "";


    private int status = 0;
    private String token = "";

    private QuickLoginUtil quickLoginUtil;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }
        findView();
    }

    public void findView(){
        ll_back = findView(R.id.ll_back);

        wxLoginBtn = findView(R.id.wxLoginBtn);
        qqLoginBtn = findView(R.id.qqLoginBtn);
        login_qiehuan = findView(R.id.login_qiehuan);
        register_yanzheng = findView(R.id.register_yanzheng);
        register_phone_zhuce = findView(R.id.register_phone_zhuce);
        password_register_login = findView(R.id.password_register_login);
        password_register_yanzhengma = findView(R.id.password_register_yanzhengma);
        register_button = findView(R.id.register_button);

        register_liji= findView(R.id.register_liji);

        register_wangji= findView(R.id.register_wangji);
        quickLoginUtil=new QuickLoginUtil(this,httpInterface);
    }
    @Override
    protected void initListener() {

        ll_back.setOnClickListener(this);
        qqLoginBtn.setOnClickListener(this);
        wxLoginBtn.setOnClickListener(this);

        login_qiehuan.setOnClickListener(this);
        register_yanzheng.setOnClickListener(this);

        register_phone_zhuce.setOnClickListener(this);
        password_register_login.setOnClickListener(this);


        register_button.setOnClickListener(this);

        register_liji.setOnClickListener(this);

        register_wangji.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        login_qiehuan.setText("验证码登录");
        password_register_yanzhengma.setVisibility(View.GONE);
        register_yanzheng.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.register_wangji:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));  //忘记密码
                break;


            case R.id.register_liji:    //到注册页
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.register_yanzheng:    //得到验证码

                String str = register_phone_zhuce.getText().toString().trim();
                if (StringUtil.isSpace(str)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(str)){
                    toast("请输入正确的手机号");
                    return;
                }
                getYanZhengMa(str);
                break;
            case R.id.register_button:  //密码或验证码登录
                register_phone = register_phone_zhuce.getText().toString().trim();
                register_password = password_register_login.getText().toString().trim();
                register_yanzhengma = password_register_yanzhengma.getText().toString().trim();

                if (StringUtil.isSpace(register_phone)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(register_phone)){
                    toast("请输入正确的手机号");
                    return;
                }
                if(login_type==1) {
                    if (StringUtil.isSpace(register_yanzhengma)) {
                        toast("请输入验证码");
                        return;
                    }
                    if (register_yanzhengma.matches("^[\u4E00-\u9FA5]{1,20}$")) {
                        toast("验证码不能为汉字");
                        return;
                    }
                    yanZhengMaLogin(register_phone, register_yanzhengma);
                }else{
                    if (StringUtil.isSpace(register_password)){
                        toast("请输入密码");
                        return;
                    }
                    passwordLogin(register_phone,register_password);
                }
                break;
            case R.id.login_qiehuan:    //切换登录切换
                if(login_type==1) {
                    login_qiehuan.setText("验证码登录");
                    register_yanzheng.setVisibility(View.GONE);
                    login_type = 0;
                    password_register_login.setText("");
                    password_register_yanzhengma.setVisibility(View.GONE);
                    password_register_login.setVisibility(View.VISIBLE);
                }else{
                    login_qiehuan.setText("密码登录");
                    register_yanzheng.setVisibility(View.VISIBLE);
                    login_type = 1;
                    password_register_yanzhengma.setText("");
                    password_register_yanzhengma.setVisibility(View.VISIBLE);
                    password_register_login.setVisibility(View.GONE);

                }
                break;
            case R.id.wxLoginBtn://微信登录
                quickLoginUtil.wx();
                break;
            case R.id.qqLoginBtn://qq登录
                quickLoginUtil.qq();
                break;

        }
    }

    //密码登录
    public void passwordLogin(final String register_phone,final String register_password){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.psdLogin(register_phone,register_password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {


                            Log.e("onSuccess", result);

                            Bean.UserModels data= new Gson().fromJson(result,Bean.UserModels.class);
                            if (data.status==1) {
                                token = data.getToken();
                                message = data.getMessage();

                                SPUtil.saveData(LoginActivity.this, "token", token);
                                SPUtil.saveData(LoginActivity.this, "phone", register_phone);
                                SPUtil.saveData(LoginActivity.this, "islogin", true);
                                App.phone = register_phone;
                                App.token = token;
                                App.islogin = true;

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));//跳转首页


                                //隐藏软键盘，如果当前打开则隐藏，如果当前隐藏则打开
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                            }
                            else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("登录.异常", response);

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



    //验证码登录
    public void yanZhengMaLogin(final String register_phone,final String register_password){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.YzmLogin(register_phone,register_password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.e("登录.成功", register_phone+"\n"+register_password);


                            Log.e("登录.成功", result);
                            try {
                                JSONObject object = new JSONObject(result);
                                status  = object.optInt("status");
                                message  = object.getString("message");
                                token  = object.getString("token");

                                if (status==1) {
//                                SPUtil.saveData(LoginActivity.this,"token",token);
//                                SPUtil.saveData(LoginActivity.this,"user_phone",register_phone);
//                                SPUtil.saveData(LoginActivity.this,"user_password",register_password);

                                    SPUtil.saveData(LoginActivity.this, "token", token);
                                    SPUtil.saveData(LoginActivity.this, "phone", register_phone);
                                    SPUtil.saveData(LoginActivity.this, "islogin", true);
                                    App.phone = register_phone;
                                    App.token = token;
                                    App.islogin = true;

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));//跳转首页


                                    //隐藏软键盘，如果当前打开则隐藏，如果当前隐藏则打开
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                    }

                                }else {
                                    toast(message);
                                }
//                                String msg_token = SPUtil.getData(LoginActivity.this,"token","").toString();
//                                String msg_phone = SPUtil.getData(LoginActivity.this,"user_phone","").toString();
//                                String msg_password = SPUtil.getData(LoginActivity.this,"user_password","").toString();
//
//                                toast(msg_token+"\n"+msg_phone+"\n"+msg_password+"\n"+message);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("登录.异常", response);
                            try {
                                JSONObject object = new JSONObject(response);
                                message  = object.getString("message");
                                toast(message);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
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

                            //toast(YanZhengMa+"\n"+message);
                            MyCountDownTimer timer = new MyCountDownTimer(LoginActivity.this, register_yanzheng, 60000, 1000);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==666&&resultCode==888){
            finish();
        }else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }
}
