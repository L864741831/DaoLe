package com.tck.daole.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tck.daole.MainActivity;
import com.tck.daole.R;
import com.tck.daole.activity.ForumActivity;
import com.tck.daole.activity.MyPostedActivity;
import com.tck.daole.activity.PostedParticularsActivity;
import com.tck.daole.activity.SpellParticularsActivity;
import com.tck.daole.adapter.IndexAdapter;
import com.tck.daole.adapter.OnlineAdapter;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;
import com.google.gson.Gson;
import com.tck.daole.view.ObservableScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 同城在线Fragment
 */
public class OnlineFragment extends Fragment implements View.OnClickListener,NoticeDialogFragment.NoticeDialogListener,ObservableScrollView.OnObservableScrollViewListener{
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private ImageView iv_right;
    private NiceRecyclerView rv_online;
    private List<Map<String, Object>> list = new ArrayList<>();
    private ImageView ll_back;
    private RadioButton redio_one,redio_two,redio_three;

    //招聘求职，二手车辆、二手交易、家政服务、房产交易、更多选择
    private LinearLayout zhaopinqiuzhi,ershoucheliang,ershoujiaoyi,jiazhengfuwu,fangchanjiaoyi,gengduoxuanze;


    Handler hd;
    OnlineAdapter adapter;


    private int index = 0, index2 = 15;
    private int num = 100;

    protected SmartRefreshLayout smart;

    LinearLayout ll_type_height;    //上边分类布局
    LinearLayout ll_title_height;   //标题栏和状态栏
    ObservableScrollView sv_online_content; //scrollview

    private int mHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online, container, false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            view.findViewById(R.id.View).setVisibility(View.GONE);
        }

/*        getForum();*/

        initView(view);

        smart =(SmartRefreshLayout) view.findViewById(R.id.smart);
//            smart.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        smart.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多


        smart.setEnableRefresh(true);
        smart.setEnableLoadmore(true);


        initListener();
        initData();




        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

/*                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");*/

                switch (msg.what) {

                      case Constant.All_Online:

                          adapter.notifyDataSetChanged();


                        break;

                }

            }
        };


        return view;

    }

    private void initView(View view) {
        tv_left = (TextView) view.findViewById(R.id.tv_left);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        ll_left = (LinearLayout) view.findViewById(R.id.ll_left);
        rv_online = (NiceRecyclerView) view.findViewById(R.id.rv_online);
        ll_back = (ImageView) view.findViewById(R.id.ll_back);
        redio_one = (RadioButton) view.findViewById(R.id.redio_one);
        redio_two = (RadioButton) view.findViewById(R.id.redio_two);
        redio_three = (RadioButton) view.findViewById(R.id.redio_three);


        zhaopinqiuzhi = (LinearLayout) view.findViewById(R.id.zhaopinqiuzhi);
                ershoucheliang = (LinearLayout) view.findViewById(R.id.ershoucheliang);
                ershoujiaoyi = (LinearLayout) view.findViewById(R.id.ershoujiaoyi);
                jiazhengfuwu = (LinearLayout) view.findViewById(R.id.jiazhengfuwu);
                fangchanjiaoyi = (LinearLayout) view.findViewById(R.id.fangchanjiaoyi);
                gengduoxuanze = (LinearLayout) view.findViewById(R.id.gengduoxuanze);

        ll_type_height = (LinearLayout) view.findViewById(R.id.ll_type_height);
        ll_title_height = (LinearLayout) view.findViewById(R.id.ll_title_height);
        sv_online_content =(ObservableScrollView) view.findViewById(R.id.sv_online_content);


        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = ll_type_height.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                ll_type_height.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = ll_type_height.getHeight() - ll_title_height.getHeight();//这里取的高度应该为图片的高度-标题栏
                //注册滑动监听
                //sv_online_content.setOnObservableScrollViewListener(getActivity());
            }
        });

    }



    /**
     * 获取ObservableScrollView的滑动数据
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //顶部图处于最顶部，标题栏透明
            //header_spell.setBackgroundColor(Color.argb(0, 250, 128, 10));
        } else if (t > 0 && t < mHeight) {
            //滑动过程中，渐变
            float scale = (float) t / mHeight;//算出滑动距离比例
            float alpha = (255 * scale);//得到透明度
            //header_spell.setBackgroundColor(Color.argb((int) alpha, 250, 128, 10));
        } else {
            //过顶部图区域，标题栏定色
            //header_spell.setBackgroundColor(Color.argb(255, 250, 128, 10));
            Log.i("11111","禁止华东");
        }
    }

    private void initListener() {
        iv_right.setOnClickListener(this);

        zhaopinqiuzhi.setOnClickListener(this);
                ershoucheliang.setOnClickListener(this);
                ershoujiaoyi.setOnClickListener(this);
                jiazhengfuwu.setOnClickListener(this);
                fangchanjiaoyi.setOnClickListener(this);
                gengduoxuanze.setOnClickListener(this);

        redio_one.setOnClickListener(this);
        redio_two.setOnClickListener(this);
        redio_three.setOnClickListener(this);


        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                index2 = num;
                getNewForum(String.valueOf(index),String.valueOf(index2), "0");



            }
        });

        smart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                index2 += num;
                getNewForum(String.valueOf(index),String.valueOf(index2), "0");

            }
        });


    }

    private void initData() {

        adapter = new OnlineAdapter(list, getActivity());
        rv_online.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnlineAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new
                        Intent(getActivity(), PostedParticularsActivity.class);  //跳转发布详情
                //在Intent对象当中添加一个键值对
                //list.get(position).get("publishdate")
                intent.putExtra("articleId", (String)list.get(position).get("articleId"));
                startActivity(intent);
                //toast(list.get(position).get("articleId"));
            }
        });

        //redio_one.setChecked(true);

        ll_back.setVisibility(View.GONE);
        tv_left.setText("首页");
        tv_title.setText("同城在线");
        tv_right.setVisibility(View.GONE);
        //发布分类删除了
        iv_right.setVisibility(View.GONE);
        iv_right.setImageResource(R.mipmap.ic_fabu);

        //获得总分类
        getNewForum(String.valueOf(index),String.valueOf(index2), "0");


 /*       for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap();
            map1.put("state", "2");
            list.add(map1);
        }
        IndexAdapter adapter = new IndexAdapter(list, getActivity());
        rv_online.setAdapter(adapter);

        adapter.setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            public void onItemClickListener(int position,String state) {
 //               Toast.makeText(getActivity(), state, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getActivity(), PostedParticularsActivity.class));//跳转文章详情


            }
        });*/
    }


    //创建对话框
    public void confirmFireMissiles() {
        DialogFragment newFragment = new ReleaseFragment();
        newFragment.show(getFragmentManager(),"missiles");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_right://
                confirmFireMissiles();
                break;

            case R.id.zhaopinqiuzhi://招聘求职
                Intent intent = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("PostedType","zhaopinqiuzhi");
                startActivity(intent);

                break;
            case R.id.ershoucheliang://二手车辆
                Intent intent1 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent1.putExtra("PostedType","ershoucheliang");
                startActivity(intent1);
                break;
            case R.id.ershoujiaoyi://二手交易
                Intent intent2 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent2.putExtra("PostedType","ershoujiaoyi");
                startActivity(intent2);
                break;
            case R.id.jiazhengfuwu://家政服务
                Intent intent3 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent3.putExtra("PostedType","jiazhengfuwu");
                startActivity(intent3);
                break;
            case R.id.fangchanjiaoyi://房产交易
                Intent intent4 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent4.putExtra("PostedType","fangchanjiaoyi");
                startActivity(intent4);
                break;
            case R.id.gengduoxuanze://更多选择
                Intent intent5 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent5.putExtra("PostedType","gengduoxuanze");
                startActivity(intent5);
                break;


            case R.id.redio_one://生活问答
                Intent intent6 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent6.putExtra("PostedType","shenghuowenda");
                startActivity(intent6);
                break;

            case R.id.redio_two://技巧分享
                Intent intent7 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent7.putExtra("PostedType","jiqiaofenxiang");
                startActivity(intent7);
                break;

            case R.id.redio_three://唠一唠
                Intent intent8 = new
                        Intent(getActivity(),ForumActivity.class);
                //在Intent对象当中添加一个键值对
                intent8.putExtra("PostedType","laoyilao");
                startActivity(intent8);
                break;

        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }






    public void getNewForum(final String index, final String num, final String articletype) {
        Map<String, String> map = new HashMap<>();
        map.put("page.index", index);
        map.put("page.num", num);
        map.put("articletype", articletype);

        HttpUtils.doPost(UriUtil.All_Online, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("ForumActivity",response.body().string());


                Bean.OnlineModel data = new Gson().fromJson(response.body().string(),
                        Bean.OnlineModel.class);


        if(data.status==1){

                List<Bean.Online> online_list = data.getList();

/*                public String articleId;//=文章ID;
                public String title;//=文章标题;
                public String content;//=文章内容;
                public String publishdate;//=文章发表时间;
                public String likenumber;//=文章点赞数量;
                public String likeStatus;//=是否点赞.1点过赞，0没有点过赞;
                public String articletype;//=文章类型：1招聘求职 2二手车辆 3二手交易 4家政服务 5房产交易 6更多选择 7旅游攻略 8论坛贴吧 9自驾游召集 10生活问答 11技巧分享 12唠一唠;
                public String pageview;//=文章浏览数量;
                public String commentnumber;//=文章评论数量;*/


            if (!smart.isLoading()) {
                list.clear();
            }

                for (int i = 0; i < online_list.size(); i++) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("articleId", online_list.get(i).getArticleId()); //	=文章ID;
                    map.put("title", online_list.get(i).getTitle()); //	=文章标题;
                    map.put("content", online_list.get(i).getContent()); //=文章内容;
                    map.put("publishdate", online_list.get(i).getPublishdate()); //	=文章发表时间;
                    map.put("likenumber", online_list.get(i).getLikenumber()); //	=文章点赞数量;
                    map.put("likeStatus", online_list.get(i).getLikeStatus()); //	=是否点赞.1点过赞，0没有点过赞;
                    map.put("articletype", online_list.get(i).getArticletype()); //=文章类型：1招聘求职 2二手车辆 3二手交易 4家政服务 5房产交易 6更多选择 7旅游攻略 8论坛贴吧 9自驾游召集 10生活问答 11技巧分享 12唠一唠;
                    map.put("pageview", online_list.get(i).getPageview()); //	=文章浏览数量;
                    map.put("commentnumber",  online_list.get(i).getCommentnumber()); //	=文章评论数量;

                    list.add(map);
                }

            finishRefresh();


            //Bundle b = new Bundle();
            //b.putString("msg", result);
            Message msg = new Message();
            //msg.setData(b);
            msg.what = Constant.All_Online;
            hd.sendMessage(msg);


        }




            }
        });
    }


    protected void finishRefresh() {
        smart.finishRefresh();
        smart.finishLoadmore();
    }


}
