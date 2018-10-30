package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.adapter.BookingAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.Callback;


/**
 * 景区订票页面
 */
public class BookingActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private NiceRecyclerView rv_booking;
    private List<Map<Object, String>> list = new ArrayList<>();

    Handler hd;
    BookingAdapter adapter;

    private EditText et_search;
    private String travelType;  //景区还是签证类型

    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_booking);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }
        tv_left = findView(R.id.tv_left);
        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        ll_left = findView(R.id.ll_left);
        rv_booking = findView(R.id.rv_booking);

        et_search = findView(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    if(travelType.equals("ll_booking")){
                        bookingSearch("0","30",et_search.getText().toString().trim());
                        et_search.setText("");
                    }
                    if(travelType.equals("ll_All")){
                        allSearch("0","30",et_search.getText().toString().trim());
                    }



                    //隐藏软键盘，如果当前打开则隐藏，如果当前隐藏则打开
                    InputMethodManager imm = (InputMethodManager) getSystemService
                            (Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.Booking:
                        adapter = new BookingAdapter(list, BookingActivity.this);
                        rv_booking.setAdapter(adapter);

                        adapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent=new Intent(BookingActivity.this, BookingParticularsActivity.class);
                                intent.putExtra("oid",list.get(position).get("oid"));
                                intent.putExtra("travelType",travelType);
                                startActivity(intent);//跳转订票物品详情

                            }
                        });

                        break;

                }

            }
        };

    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        tv_left.setText("首页");
        tv_right.setVisibility(View.GONE);

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            travelType = intent.getStringExtra("travelType");
            if(travelType.equals("ll_booking")){
                tv_title.setText("景区订票");
                BookingList();
            }
            if(travelType.equals("ll_All")){
                tv_title.setText("签证办理");
                allList();
            }

        }


/*        for (int i = 0; i < 6; i++) {
            Map<Object, String> map = new HashMap<>();
            map.put("money", "¥10" + i);
            list.add(map);
        }*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                finish();
                break;
        }
    }


    //景区订票列表
    public void BookingList() {
       /*
 * Get请求
 * 参数一：请求Url
 * 参数二：请求回调
 */
        HttpUtils.doGet(UriUtil.booking_list, new Callback() {

            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //toast(response.body().string());

                    String result=response.body().string();
                    Log.i("result",result);
                    Bean.BookingList data = new Gson().fromJson(result, Bean.BookingList.class);
                    List<Bean.Booking> booking_list = data.list;

                    for (int i = 0; i < booking_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("oid", booking_list.get(i).takeProjectId); //	oid主键
                        map.put("name", booking_list.get(i).takeProjectName); //票名称
                        map.put("price", booking_list.get(i).price); //价格
                        map.put("sellNumber", booking_list.get(i).saleNum); //销量
                        map.put("ctime", booking_list.get(i).ctime); //开业时间
                        map.put("ftime", booking_list.get(i).ftime); //关门时间
                        map.put("thumPath", booking_list.get(i).projectImage); //图片地址
                        list.add(map);
                    }

                    Message msg = new Message();
                    msg.what = Constant.Booking;
                    hd.sendMessage(msg);

                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }







//全球签证办理

    public void allList() {
       /*
 * Get请求
 * 参数一：请求Url
 * 参数二：请求回调
 */
        HttpUtils.doGet(UriUtil.all_getDetail, new Callback() {

            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //toast(response.body().string());

                    String result=response.body().string();
                    Log.i("result",result);
                    Bean.BookingList data = new Gson().fromJson(result, Bean.BookingList.class);
                    List<Bean.Booking> booking_list = data.list;

                    for (int i = 0; i < booking_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("oid", booking_list.get(i).takeProjectId); //	oid主键
                        map.put("name", booking_list.get(i).takeProjectName); //票名称
                        map.put("price", booking_list.get(i).price); //价格
                        map.put("sellNumber", booking_list.get(i).saleNum); //销量
                        map.put("ctime", booking_list.get(i).ctime); //开业时间
                        map.put("ftime", booking_list.get(i).ftime); //关门时间
                        map.put("thumPath", booking_list.get(i).projectImage); //图片地址
                        list.add(map);
                    }

                    Message msg = new Message();
                    msg.what = Constant.Booking;
                    hd.sendMessage(msg);

                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }



    public void bookingSearch(final String index,final String num,final String search){
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getBookingSearch(index,num,search, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            //Log.e("获取景区搜索.成功", result);

                            Bean.BookingList data = new Gson().fromJson(result, Bean.BookingList.class);
                            List<Bean.Booking> booking_list = data.list;

                            list.clear();
                            for (int i = 0; i < booking_list.size(); i++) {
                                Map<Object, String> map = new HashMap<>();
                                map.put("oid", booking_list.get(i).takeProjectId); //	oid主键
                                map.put("name", booking_list.get(i).takeProjectName); //票名称
                                map.put("price", booking_list.get(i).price); //价格
                                map.put("sellNumber", booking_list.get(i).saleNum); //销量
                                map.put("ctime", booking_list.get(i).ctime); //开业时间
                                map.put("ftime", booking_list.get(i).ftime); //关门时间
                                map.put("thumPath", booking_list.get(i).projectImage); //图片地址
                                list.add(map);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取景区搜索.异常", response);

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






    public void allSearch(final String index,final String num,final String search){
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getAllSearch(index,num,search, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            //Log.e("获取景区搜索.成功", result);

                            Bean.BookingList data = new Gson().fromJson(result, Bean.BookingList.class);
                            List<Bean.Booking> booking_list = data.list;

                            list.clear();
                            for (int i = 0; i < booking_list.size(); i++) {
                                Map<Object, String> map = new HashMap<>();
                                map.put("oid", booking_list.get(i).takeProjectId); //	oid主键
                                map.put("name", booking_list.get(i).takeProjectName); //票名称
                                map.put("price", booking_list.get(i).price); //价格
                                map.put("sellNumber", booking_list.get(i).saleNum); //销量
                                map.put("ctime", booking_list.get(i).ctime); //开业时间
                                map.put("ftime", booking_list.get(i).ftime); //关门时间
                                map.put("thumPath", booking_list.get(i).projectImage); //图片地址
                                list.add(map);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取景区搜索.异常", response);

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
