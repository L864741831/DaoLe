package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.PayUtil;
import com.tck.daole.util.UriUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

/**
 * 美食团购物品详情
 */
public class GroupParticularsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;

    private ImageView img_booking;
    private TextView tltle;
    private TextView price;
    private TextView tv_booking_detail;

    LinearLayout ll_goumai_lepingou;

    String json_str = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_booking_particulars);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        img_booking = (ImageView) findViewById(R.id.img_booking);
        tltle = (TextView) findViewById(R.id.tltle);
        price = (TextView) findViewById(R.id.price);
        tv_booking_detail = (TextView) findViewById(R.id.tv_booking_detail);

        ll_goumai_lepingou = (LinearLayout) findViewById(R.id.ll_goumai_lepingou);

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

 /*           intent.putExtra("path",path);   //团商品图片地址
            intent.putExtra("shap_name",shap_name); //团商品名字
            intent.putExtra("projectLoanAmounts",projectLoanAmounts);   //团商品价格
            intent.putExtra("path",path);   //团商品介绍


            intent.putExtra("oid",oid);
            intent.putExtra("projectId",projectId);*/


            String path = intent.getStringExtra("path");
            String shap_name = intent.getStringExtra("shap_name");
            String projectLoanAmounts = intent.getStringExtra("projectLoanAmounts");
            String detail = intent.getStringExtra("detail");

            json_str = intent.getStringExtra("json_str");

            //toast(path + "\n" + shap_name + "\n" + projectLoanAmounts + "\n" + json_str  + "\n" + detail);


/*            img_booking  = (ImageView) findViewById(R.id.img_booking);
            tltle  = (TextView) findViewById(R.id.tltle);
            price  = (TextView) findViewById(R.id.price);
            tv_booking_detail  = (TextView) findViewById(R.id.tv_booking_detail);*/

            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)//等待加载显示图片
                    .showImageOnFail(R.mipmap.ic_launcher)//显示错误图片
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoader.getInstance().displayImage(UriUtil.ip + path, img_booking, options);
            tltle.setText(shap_name);
            price.setText("￥" + projectLoanAmounts);
            tv_booking_detail.setText(detail);


        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_goumai_lepingou:
                if (App.islogin) {
                    onSubmitBooking(App.token, json_str);
                }
                break;

        }
    }


    public void onSubmitBooking(final String token, final String param) {

        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.submitBooking(token, param, "4", new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("提交订单.成功", result);

                            //PayUtil ApliyPay  进行支付
                            @SuppressLint("SimpleDateFormat") String no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                            //Log.i("提交233",no);

                            new PayUtil(GroupParticularsActivity.this, httpInterface).ApliyPay(no, "", "", "");

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
