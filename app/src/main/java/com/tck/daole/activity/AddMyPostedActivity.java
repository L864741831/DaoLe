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
 * 发布文章页
 */

public class AddMyPostedActivity extends BaseActivity {
    private LinearLayout ll_back;
    private EditText et_my_posted_zhuti,et_my_posted_neirong;
    private Button btn_my_posted_add;

//    private String sp_token = "";

    public String add_type = "";
    public String int_add_type = "8";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_my_posted);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);
        et_my_posted_zhuti = findView(R.id.et_my_posted_zhuti);
        et_my_posted_neirong = findView(R.id.et_my_posted_neirong);
        btn_my_posted_add = findView(R.id.btn_my_posted_add);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        btn_my_posted_add.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(AddMyPostedActivity.this, "token", "").toString();



        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            String addtype = intent.getStringExtra("addtype");
            //toast(addtype);

            /*
            文章类型 1招聘求职 2二手车辆 3二手交易 4家政服务 5房产交易 6更多选择 7旅游攻略 8论坛贴吧 9自驾游召集 10生活问答 11技巧分享 12唠一唠
             */

            if(addtype.equals("zhaopinqiuzhi")){    //1招聘求职
                add_type = "zhaopinqiuzhi";
                int_add_type = "1";
            }else  if(addtype.equals("ershoucheliang")){ //2二手车辆
                add_type = "ershoucheliang";
                int_add_type = "2";
            }else if(addtype.equals("ershoujiaoyi")){ //3二手交易
                add_type = "ershoujiaoyi";
                int_add_type = "3";
            }else if(addtype.equals("jiazhengfuwu")){ //4家政服务
                add_type = "jiazhengfuwu";
                int_add_type = "4";
            }else if(addtype.equals("fangchanjiaoyi")){ //5房产交易
                add_type = "fangchanjiaoyi";
                int_add_type = "5";
            }else if(addtype.equals("gengduoxuanze")){ //6更多选择
                add_type = "gengduoxuanze";
                int_add_type = "6";
            }else if(addtype.equals("luntantieba")){ //8论坛贴吧
                add_type = "luntantieba";
                int_add_type = "8";
                //getForum();
            }else if(addtype.equals("lvyougonglue")){ //7旅游攻略
                add_type = "lvyougonglue";
                int_add_type = "7";
                //getForum();
            }else if(addtype.equals("zijiayouzhaoji")){ //9自驾游召集
                add_type = "zijiayouzhaoji";
                int_add_type = "9";
                //getForum();
            }else if(addtype.equals("shenghuowenda")){ //10生活问答
                add_type = "shenghuowenda";
                int_add_type = "10";
                //getForum();
            }else if(addtype.equals("jiqiaofenxiang")){ //11技巧分享
                add_type = "jiqiaofenxiang";
                int_add_type = "11";
                //getForum();
            }else if(addtype.equals("laoyilao")){ //12唠一唠
                add_type = "laoyilao";
                int_add_type = "12";
                //getForum();
            }





        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_my_posted_add:
                if(!App.islogin){
                    toast("请先登录");
                    startActivity(new Intent(AddMyPostedActivity.this, LoginActivity.class));
                    return;
                }
                String zhuti = et_my_posted_zhuti.getText().toString();
                String neirong = et_my_posted_neirong.getText().toString();

                if (StringUtil.isSpace(zhuti)){
                    toast("请输入主题");
                    return;
                }

                if (StringUtil.isSpace(neirong)){
                    toast("请输入内容");
                    return;
                }

                //feedBack(sp_token,zhuti,neirong);
                //发布类型
                //toast(int_add_type);

                MyPosted(App.token,zhuti,int_add_type,neirong);

                break;
        }
    }












    //发布文章
    public void MyPosted(final String token,final String title,final String articletype,final String content){
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.addMyPosted(token,title,articletype,content, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.e("发布.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
//                    if (data.getStatus())
                            toast(data.getMessage());
                            //startActivity(new Intent(AddMyPostedActivity.this, ForumActivity.class));
                            Intent intent = new
                                    Intent(AddMyPostedActivity.this,ForumActivity.class);
                            //在Intent对象当中添加一个键值对
                            intent.putExtra("PostedType",articletype);
                            startActivity(intent);
                            //结束当前页面
                            finish();
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("发布.异常", response);
                            Bean data = new Gson().fromJson(response, Bean.class);
                            toast(data.getMessage());
                            //startActivity(new Intent(AddMyPostedActivity.this, ForumActivity.class));

                            Intent intent = new
                                    Intent(AddMyPostedActivity.this,ForumActivity.class);
                            //在Intent对象当中添加一个键值对
                            intent.putExtra("PostedType",articletype);
                            startActivity(intent);
                            //结束当前页面
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