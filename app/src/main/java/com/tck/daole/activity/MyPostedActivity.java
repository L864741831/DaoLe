package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.MyPostedAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.EndLessOnScrollListener;
import com.tck.daole.view.MyDecoration;
import com.tck.daole.view.NiceRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 我的发布文章
 */
public class MyPostedActivity extends BaseActivity {
    private LinearLayout ll_back;
    private TextView tv_myPosted_Posted;
    private NiceRecyclerView rv_myPosted;
    private List<Map<String, Object>> list = new ArrayList<>();

    private ImageView iv_myposted_head;
    private TextView iv_myposted_nickname, iv_myposted_sex;

    private MyPostedAdapter adapter;    //文章列表适配器


    protected SmartRefreshLayout smart;

    private int index = 0, index2 = 15;
    private int num = 15;

//    private String sp_token = "";


    String oid = "";
    String nick_name = "";
    String sex = "";
    String head = "";


    Handler hd;

    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_posted);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        smart = findView(R.id.smart);
//            smart.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        smart.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多

        smart.setEnableRefresh(true);
        smart.setEnableLoadmore(true);


        ll_back = findView(R.id.ll_back);
        tv_myPosted_Posted = findView(R.id.tv_myPosted_Posted);
        rv_myPosted = findView(R.id.rv_myPosted);
        iv_myposted_head = findView(R.id.iv_myposted_head);
        iv_myposted_nickname = findView(R.id.iv_myposted_nickname);
        iv_myposted_sex = findView(R.id.iv_myposted_sex);


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.My_Posted:

                        //显示图片的配置
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.mipmap.touxiang)//等待加载显示图片
                                .showImageOnFail(R.mipmap.touxiang)//显示错误图片
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(UriUtil.ip + head, iv_myposted_head, options);

                        iv_myposted_nickname.setText(nick_name);
                        iv_myposted_sex.setText(sex);
/*
                        String nick_name = "";
                        String sex = "";
                        String head = "";
*/

                        break;


                    case Constant.My_Posted_Statu:

                        sendRedEnvelopes(MyPostedActivity.this, "", "", "", "");

                        break;

                    case Constant.My_Posted_List_Statu:

                        sendRedEnvelopes(MyPostedActivity.this, "", "", "", "");

                        break;

                    case Constant.JiaZai_MyPosted:
                        adapter.notifyDataSetChanged();
                        break;


                }

            }
        };
    }


    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        tv_myPosted_Posted.setOnClickListener(this);

        adapter = new MyPostedAdapter(list, MyPostedActivity.this);
        //为RecyclerView加载Adapter
        rv_myPosted.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyPostedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String articleId = (String) list.get(position).get("articleId");
                //startActivity(new Intent(MyPostedActivity.this,PostedParticularsActivity.class));//跳转发布详情


                Intent intent = new
                        Intent(MyPostedActivity.this, PostedParticularsActivity.class);  //跳转发布详情
                //在Intent对象当中添加一个键值对
                intent.putExtra("articleId", articleId);
                startActivity(intent);
            }
        });

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                index2 = num;

                postedList(App.token, index, index2);

            }
        });

        smart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                index2 += num;

                postedList(App.token, index, index2);

            }
        });

    }

    @Override
    protected void initData() {

        if (App.islogin) {
            iv_myposted_nickname.setText(App.login.nickName);
            iv_myposted_sex.setText(App.login.sex);
            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.touxiang)//等待加载显示图片
                    .showImageOnFail(R.mipmap.touxiang)//显示错误图片
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoader.getInstance().displayImage(UriUtil.ip + App.login.head, iv_myposted_head, options);

            postedList(App.token, index, index2);

        } else {
            iv_myposted_nickname.setText("未登录");
            iv_myposted_head.setImageResource(R.mipmap.touxiang);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_myPosted_Posted://发布

                break;
        }
    }


    //获得验证码
    public void postedList(final String token, final int index, final int num) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getMyPostedList(token, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            //Log.e("获取文章列表.成功", token+"\n"+index+"\n"+num);

                            Log.e("获取文章列表.成功", result);

                            Bean.MyPostedListModel data = new Gson().fromJson(result, Bean.MyPostedListModel.class);

                            if (data.status == 1) {
                                List<Bean.MyPostedList> my_posted = data.getList();

                                if (!smart.isLoading()) {
                                    list.clear();
                                }


                /*
                 articleId=文章ID;
	 title=文章标题;
	 content=文章内容;
	 publishdate=文章发表时间;
	 likenumber=文章点赞数量;
	 commentnumber=文章评论数量;
                 */

                                for (int i = 0; i < my_posted.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("articleId", my_posted.get(i).getArticleId()); //	文章ID;
                                    map.put("title", my_posted.get(i).getTitle()); //	文章标题;
                                    map.put("content", my_posted.get(i).getContent()); //	文章内容;
                                    map.put("publishdate", my_posted.get(i).getPublishdate()); //	文章发表时间;
                                    map.put("likenumber", my_posted.get(i).getLikenumber()); //	文章点赞数量;
                                    map.put("commentnumber", my_posted.get(i).getCommentnumber()); //	文章评论数量;

                                    list.add(map);

                                }

                                finishRefresh();

                                adapter.notifyDataSetChanged();

                            }


                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取文章列表.异常", response);
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


    //获得我的发布个人信息
    public void MyPostedInformation(final String token, final String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("articleId", articleId);

        HttpUtils.doPost(UriUtil.My_Posted_Information, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("12123",response.body().string());

                Bean data = new Gson().fromJson(response.body().string(),
                        Bean.class);

                int status = data.getStatus();

                //Log.i("12121", status);


                if (status == 9) {
                    //sendRedEnvelopes(MyPostedActivity.this, "", "", "", "");


                    //startActivity(new Intent(MyPostedActivity.this, LoginActivity.class));

                    Message msg = new Message();
                    msg.what = Constant.My_Posted_Statu;
                    hd.sendMessage(msg);

                } else if (status == 1) {

                    oid = data.getModel().getOid();
                    nick_name = data.getModel().getNickName();
                    sex = data.getModel().getSex();
                    head = data.getModel().getHead();

                    //Log.i("获得发布我的信息", oid + "\n" + nick_name + "\n" + sex + "\n" + head);


                    Message msg = new Message();
                    msg.what = Constant.My_Posted;
                    hd.sendMessage(msg);

                }


            }
        });

    }


    /**
     * 其他设备登录提示
     */
    public void sendRedEnvelopes(final Activity ctx, String title, String content, String left, String right) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_token_error, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.tv_content);
        //TextView dialog_left = (TextView) view.findViewById(R.id.tv_left);
        TextView dialog_right = (TextView) view.findViewById(R.id.tv_right);


        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        dialog.setCanceledOnTouchOutside(true); //设置点击对话框外关闭对话框


        dialog_content.setText("其他设备登录，请重新登录");

        dialog_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //onRightClickListener.onRightClick(dialog);

                Intent intent = new Intent();
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);//或者使用Activity.startActivity(intent)

                intent.setClass(ctx, LoginActivity.class);
                ctx.startActivity(intent);


            }
        });
        dialog.show();
    }


    protected void finishRefresh() {
        smart.finishRefresh();
        smart.finishLoadmore();
    }


}
