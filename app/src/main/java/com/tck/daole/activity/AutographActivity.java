package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
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
public class AutographActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private EditText et_autograph;
    private TextView btn_autograph;

//    private String token ="";

    private String message = "";
//    private String signature = "";

    private int num = 50;//个性签名限制的最大字数


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_autograph);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        et_autograph = findView(R.id.et_autograph);
        btn_autograph = findView(R.id.btn_autograph);


        et_autograph.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_autograph.getText();
                int len = editable.length();

                if (len > num) {
                    toast("个性签名不能超过50个字");
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, num);
                    et_autograph.setText(newStr);
                    editable = et_autograph.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        btn_autograph.setOnClickListener(this);

    }

    @Override
    protected void initData() {
//        token = SPUtil.getData(AutographActivity.this,"token","").toString();
        et_autograph.setText(App.login.Signature);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_autograph:
                String autograph = et_autograph.getText().toString().trim();
                setAutograph(App.token,autograph);

                break;

        }
    }

    //提交昵称
    public void setAutograph(final String token,final String autograph){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.modifyAutograph(token,autograph, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("修改.成功", result);
                                    Bean.UserModels data = new Gson().fromJson(result, Bean.UserModels.class);
                                    if (data.status == 1) {
                                        toast("修改成功！");
                                        App.login.Signature = autograph;
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
