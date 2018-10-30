package com.tck.daole.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.OnlineAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.google.gson.Gson;


/**
 * 论坛贴吧页
 * 同城在线 1.2.3.4.5.6.7.8.9
 */

public class ForumActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_left;
    private TextView tv_right, tv_title;
    private ImageView iv_right;

    private NiceRecyclerView nrv_forum;
    private List<Map<String,Object >> list = new ArrayList<>();

    private EditText et_forun_search;       //搜索框


    Handler hd;
    OnlineAdapter adapter;

    private String add_type = "luntantieba";
    private String str_int_type = "1";



//    public String sp_token = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();

        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

/*                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");*/

                switch (msg.what) {
                    case Constant.Fosum:

                        adapter = new OnlineAdapter(list, ForumActivity.this);
                        nrv_forum.setAdapter(adapter);

                        adapter.setOnItemClickListener(new OnlineAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(int position) {
                                                              Intent intent = new
                                        Intent(ForumActivity.this, PostedParticularsActivity.class);  //跳转发布详情
                                //在Intent对象当中添加一个键值对
                                //list.get(position).get("publishdate")
                                intent.putExtra("articleId", (String)list.get(position).get("articleId"));
                                startActivity(intent);
                                //toast(list.get(position).get("articleId"));
                            }
                        });


                        break;

                    case Constant.Search_Forum_List:

                        adapter.notifyDataSetChanged();

                        break;


                }

            }
        };


    }

    public void findViewId() {
        ll_left = findView(R.id.ll_left);
        nrv_forum = findView(R.id.nrv_forum);

        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        iv_right = findView(R.id.iv_right);

        et_forun_search = findView(R.id.et_forun_search);

    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);

    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(ForumActivity.this, "token", "").toString();

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            String PostedType = intent.getStringExtra("PostedType");
            //toast(PostedType);
            //1招聘求职 2二手车辆 3二手交易 4家政服务 5房产交易 6更多选择 7旅游攻略 8论坛贴吧 9自驾游召集 10生活问答 11技巧分享 12唠一唠
            if (PostedType.equals("zhaopinqiuzhi") || PostedType.equals("1")) {
                tv_title.setText("招聘求职");
                add_type = "zhaopinqiuzhi";
                getNewForum("0", "9", "1");
                str_int_type = "1";
            } else if (PostedType.equals("ershoucheliang") || PostedType.equals("2")) {
                tv_title.setText("二手车辆");
                add_type = "ershoucheliang";
                getNewForum("0", "9", "2");
                str_int_type = "2";
            } else if (PostedType.equals("ershoujiaoyi") || PostedType.equals("3")) {
                tv_title.setText("二手交易");
                add_type = "ershoujiaoyi";
                getNewForum("0", "9", "3");
                str_int_type = "3";
            } else if (PostedType.equals("jiazhengfuwu") || PostedType.equals("4")) {
                tv_title.setText("家政服务");
                add_type = "jiazhengfuwu";
                getNewForum("0", "9", "4");
                str_int_type = "4";
            } else if (PostedType.equals("fangchanjiaoyi") || PostedType.equals("5")) {
                tv_title.setText("房产交易");
                add_type = "fangchanjiaoyi";
                getNewForum("0", "9", "5");
                str_int_type = "5";

            } else if (PostedType.equals("gengduoxuanze") || PostedType.equals("6")) {
                tv_title.setText("更多选择");
                add_type = "gengduoxuanze";
                getNewForum("0", "9", "6");
                str_int_type = "6";

            } else if (PostedType.equals("luntantieba") || PostedType.equals("8")) {
                tv_title.setText("论坛贴吧");
                add_type = "luntantieba";
                getNewForum("0", "9", "8");
                str_int_type ="8";

            } else if (PostedType.equals("lvyougonglue") || PostedType.equals("7")) {
                tv_title.setText("旅游攻略");
                add_type = "lvyougonglue";
                getNewForum("0", "9", "7");
                str_int_type = "7";

            } else if (PostedType.equals("zijiayouzhaoji") || PostedType.equals("9")) {
                tv_title.setText("自驾游召集");
                add_type = "zijiayouzhaoji";
                getNewForum("0", "9", "9");
                str_int_type = "9";

            }else if (PostedType.equals("shenghuowenda") || PostedType.equals("10")) {
                tv_title.setText("生活问答");
                add_type = "shenghuowenda";
                getNewForum("0", "9", "10");
                str_int_type = "10";

            }else if (PostedType.equals("jiqiaofenxiang") || PostedType.equals("11")) {
                tv_title.setText("技巧分享");
                add_type = "jiqiaofenxiang";
                getNewForum("0", "9", "11");
                str_int_type = "11";

            }else if (PostedType.equals("laoyilao") || PostedType.equals("12")) {
                tv_title.setText("唠一唠");
                add_type = "laoyilao";
                getNewForum("0", "9", "12");
                str_int_type = "12";

            }


            et_forun_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String str = et_forun_search.getText().toString();
                        //search(str);
                        //Log.i("1245",str);
                        getSearchForum("0","9",et_forun_search.getText().toString().trim(),str_int_type);
                        et_forun_search.setText("");

                        //隐藏软键盘，如果当前打开则隐藏，如果当前隐藏则打开
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return false;
                }
            });


        }


        tv_right.setVisibility(View.GONE);

        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.ic_fabu);


/*        for (int i = 0; i < 5; i++) {
            Map<Object, String> map1 = new HashMap();
            map1.put("state", "2");
            list.add(map1);
        }
        IndexAdapter adapter = new IndexAdapter(list, ForumActivity.this);
        nrv_forum.setAdapter(adapter);

        adapter.setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            public void onItemClickListener(int position,String state) {

                startActivity(new Intent(ForumActivity.this, PostedParticularsActivity.class));//跳转文章详情

            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.iv_right:
                //toast(add_type);

                if (!App.islogin) {
                    toast("请先登录");
                    startActivity(new Intent(ForumActivity.this, LoginActivity.class));
                } else {
                    Intent intent = new
                            Intent(ForumActivity.this, AddMyPostedActivity.class);   //发布文章
                    //在Intent对象当中添加一个键值对
                    intent.putExtra("addtype", add_type);
                    startActivity(intent);
                    finish();
                }


                break;

        }
    }




    public void getSearchForum(final String index, final String num,final String searchText, final String articletype) {
        Map<String, String> map = new HashMap<>();
        map.put("page.index", index);
        map.put("page.num", num);
        map.put("searchText", searchText);
        map.put("articletype", articletype);

        HttpUtils.doPost(UriUtil.Online_search, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("ForumActivity",response.body().string());

                //Log.e("onResponse",response.body().string()+"");

String str = response.body().string();

                Log.i("onResponse",str);


                Bean.OnlineModel data = new Gson().fromJson(str,
                        Bean.OnlineModel.class);
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

                list.clear();

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
                    map.put("commentnumber",online_list.get(i).getCommentnumber()); //	=文章评论数量;

                    Log.i("ForumActivity",online_list.get(i).getTitle());


                    list.add(map);
                }

                //Bundle b = new Bundle();
                //b.putString("msg", result);
               Message msg = new Message();
                //msg.setData(b);
                msg.what = Constant.Search_Forum_List;
                hd.sendMessage(msg);
            }
        });
    }





    public void getNewForum(final String index, final String num, final String articletype) {
        Map<String, String> map = new HashMap<>();
        map.put("page.index", index);
        map.put("page.num", num);
        map.put("articletype", articletype);

        HttpUtils.doPost(UriUtil.Online_List, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("ForumActivity",response.body().string());

                String str = response.body().string();
                Log.i("ForumActivity",str);


                Bean.OnlineModel data = new Gson().fromJson(str,
                        Bean.OnlineModel.class);

                if(data.status==1) {


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
                        map.put("commentnumber", online_list.get(i).getCommentnumber()); //	=文章评论数量;

                        list.add(map);
                    }

                    //Bundle b = new Bundle();
                    //b.putString("msg", result);
                    Message msg = new Message();
                    //msg.setData(b);
                    msg.what = Constant.Fosum;
                    hd.sendMessage(msg);
                }

            }
        });
    }


}
