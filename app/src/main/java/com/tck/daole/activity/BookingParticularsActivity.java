package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.ImageLoadUtil;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.PayUtil;
import com.tck.daole.util.UriUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

/**
 * 拼单物品详情
 */
public class BookingParticularsActivity extends BaseActivity implements View.OnClickListener {
    private com.tck.daole.view.ObservableScrollView sv_spell_content;
    private ImageView img_booking;
    private TextView spell_yuanjia;
    private TextView tltle;
    private TextView price;
    private TextView notice;
    private com.tck.daole.view.NiceRecyclerView rv_spell;
    private com.tck.daole.view.NiceRecyclerView rv_spell_particulars;
    private RelativeLayout rl_appraise;
    private com.tck.daole.view.NiceRecyclerView rv_spell_img;
    private LinearLayout ll_header_spell;
    private View view;
    private LinearLayout ll_back;
    private LinearLayout linear;
    private LinearLayout ll_goumai_lepingou;
    private String oid="";

    private TextView tv_booking_detail; //详情

    private String json_takeProjectId = "";   //商品id
    private String json_takeProjectName = "";   //商品名称
    private String json_takeProjectImg = "";  //商品图片地址

    private double json_price = 0.0;  //商品价格

    private String travelType;  //景区还是签证类型


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_booking_particulars);
        oid=getIntent().getStringExtra("oid");

        //设置透明状态栏

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        sv_spell_content = (com.tck.daole.view.ObservableScrollView) findViewById(R.id.sv_spell_content);
        img_booking = (ImageView) findViewById(R.id.img_booking);
        spell_yuanjia = (TextView) findViewById(R.id.spell_yuanjia);
        tltle = (TextView) findViewById(R.id.tltle);
        price = (TextView) findViewById(R.id.price);
        notice = (TextView) findViewById(R.id.notice);
        rv_spell = (com.tck.daole.view.NiceRecyclerView) findViewById(R.id.rv_spell);
        rv_spell_particulars = (com.tck.daole.view.NiceRecyclerView) findViewById(R.id.rv_spell_particulars);
        rl_appraise = (RelativeLayout) findViewById(R.id.rl_appraise);
        rv_spell_img = (com.tck.daole.view.NiceRecyclerView) findViewById(R.id.rv_spell_img);
        ll_header_spell = (LinearLayout) findViewById(R.id.ll_header_spell);
        view = (View) findViewById(R.id.View);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        linear = (LinearLayout) findViewById(R.id.linear);
        ll_goumai_lepingou = (LinearLayout) findViewById(R.id.ll_goumai_lepingou);

        tv_booking_detail  = (TextView) findViewById(R.id.tv_booking_detail);   //订票介绍
    }





    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        ll_goumai_lepingou.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            travelType = intent.getStringExtra("travelType");
            if (travelType.equals("ll_booking")) {

            }
            if (travelType.equals("ll_All")) {
                notice.setText("签证须知");
            }

            httpInterface.booking_getDetail(oid, new MApiResultCallback() {
                @Override
                public void onSuccess(final String result) {
                    Log.e("booking_getDetail--Suc", result);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            Bean.BookingModel data = new Gson().fromJson(result, Bean.BookingModel.class);
                            if (data.status == 1) {
                                ImageLoadUtil.showImage(UriUtil.ip + data.model.path, img_booking); //图片地址
                                tltle.setText(data.model.takeProjectName); //票名称
                                price.setText("¥" + data.model.price); //价格
                                tv_booking_detail.setText(data.model.detail);
                                json_takeProjectId = data.model.takeProjectId;   //商品id
                                json_price = Double.parseDouble(data.model.price);  //商品价格*/
                                json_takeProjectName = data.model.takeProjectName;  //商品名称
                                json_takeProjectImg = data.model.path; //商品图片地址
                            } else {
                                toast(data.message);
                            }
                        }
                    });
                }


                @Override
                public void onFail(String response) {
                    Log.e("bindPhone-onFail", response + "");
                }

                @Override
                public void onError(Call call, Exception exception) {
                    Log.e("onError", call + "-----" + exception);
                }

                @Override
                public void onTokenError(String response) {
                    Log.e("onTokenError", response + "");
                }
            });
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_goumai_lepingou:

/*                json_takeProjectId = data.model.takeProjectId;   //商品id
                json_price = data.model.price;  //商品价格*/

String json_booking = "{\"adminStoreOid\":\"\",\"projects\":[{\"projectOid\":\""+json_takeProjectId +"\",\"count\":1}],\"addressOid\":\"I7P4EYOE71\",\"coupon\":\"\",\"servicePrice\":\"\",\"price\":"+json_price+",\"remark\":\"\"}";

                if (App.islogin) {
                    if (travelType.equals("ll_All")) {
                        Intent intent = new
                                Intent(BookingParticularsActivity.this,OrderSpellActivity.class);
                        //在Intent对象当中添加一个键值对
                        intent.putExtra("spell_oid",json_takeProjectId);
                        intent.putExtra("name",json_takeProjectName);
                        intent.putExtra("loanAmounts",json_price);
                        intent.putExtra("imgpath",json_takeProjectImg);

                        //Log.i("233",json_takeProjectId+"\n"+json_takeProjectName+"\n"+json_price+"\n"+json_takeProjectImg);

                        startActivity(intent);
                    }else{
                        onSubmitBooking(App.token,json_booking);
                    }
                } else {
                    startActivity(new Intent(BookingParticularsActivity.this, LoginActivity.class));//跳转登录
                }
                break;
        }
    }







    public void onSubmitBooking(final String token, final String param) {

        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.submitBooking(token, param,"2", new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("提交订单.成功", result);

                            //PayUtil ApliyPay  进行支付
                            @SuppressLint("SimpleDateFormat") String no=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                            //Log.i("提交233",no);

                            new PayUtil(BookingParticularsActivity.this,httpInterface).ApliyPay(no,"","","");

                            finish();

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("提交订单.异常", response);
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
