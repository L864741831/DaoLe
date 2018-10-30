package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;

import okhttp3.Call;

/**
 * 修改用户昵称
 */
public class NickNameActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private EditText et_nick_name;
    private TextView btn_nick_name;

    private String message = "";
//    private String signature = "";


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_nick_name);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        et_nick_name = findView(R.id.et_nick_name);
        btn_nick_name = findView(R.id.btn_nick_name);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        btn_nick_name.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        et_nick_name.setText(App.login.nickName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_nick_name:
                String nick_name = et_nick_name.getText().toString().trim();
//                String token = SPUtil.getData(NickNameActivity.this,"token","").toString();

                setNickName(App.token,nick_name);


                break;

        }
    }

    //提交昵称
    public void setNickName(final String token,final String nick_name){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.modifyNickName(token,nick_name, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {

//                            Log.e("修改.成功", result);
//
//                            Bean.UserModels data= new Gson().fromJson(result,Bean.UserModels.class);
//                            message  = data.getMessage();
//                            signature = data.getNickName();
//
//                            toast(message+""+signature);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("修改.成功", result);
                                    Bean.UserModels data = new Gson().fromJson(result, Bean.UserModels.class);
                                    if (data.status == 1) {
                                        toast("修改成功！");
                                        App.login.nickName = nick_name;
                                        finish();
                                    } else {
                                        toast(data.getMessage());
                                    }
                                }
                            });



/*                            //隐藏软键盘，如果当前打开则隐藏，如果当前隐藏则打开
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("修改.异常", response);

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
}
