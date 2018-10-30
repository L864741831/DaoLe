package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.MainActivity;
import com.tck.daole.R;
import com.tck.daole.adapter.PostedParticularsAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.StringUtil;
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

/**
 * 文章发布详情
 */
public class PostedParticularsActivity extends BaseActivity {
    private LinearLayout ll_back;
    private NiceRecyclerView rv_posted;

//    private String sp_token = "";
    String articleId = "1";

    private ImageView img_posted_particulars_head;  //头像

    CheckBox img_posted_particulars_shoucang;   //\收藏
    //当页标题、昵称、时间、标题、
    private TextView title_posted_particulars_title, tv_posted_particulars_nickname, tv_posted_particulars_date, tv_posted_particulars_title, tv_posted_particulars_content, tv_posted_particulars_kanguo, tv_posted_particulars_pinglunshuliang;

    private EditText et_posted_particulars_input;   //输入评论
    private TextView tv_posted_particulars_send;    //发送评论


    private String memberId, nickName, head, title, content, publishdate, pageview,  likeStatus = "";

    private String commentnumber= "0";
    Handler hd;

    PostedParticularsAdapter adapter;   //评论适配器

    private String dianzan = "0";   //.1点过赞，0没有点过赞;

    private List<Map<String, Object>> list = new ArrayList<>();


    private String[] dianzan_url = {UriUtil.ip+"action/articlePublishApp/praise",UriUtil.ip+"action/articlePublishApp/noPraise"}; //点赞、取消点赞
    private String dianzan_url_zhuangtai = dianzan_url[0];


    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_posted_particulars);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);
        rv_posted = findView(R.id.rv_posted);


/*        private ImageView img_posted_particulars_head,img_posted_particulars_shoucang;  //头像\收藏
        //当页标题、昵称、时间、标题、
        private TextView title_posted_particulars_title,tv_posted_particulars_nickname,tv_posted_particulars_date,
                tv_posted_particulars_title,tv_posted_particulars_content,tv_posted_particulars_kanguo,tv_posted_particulars_pinglunshuliang;*/

        img_posted_particulars_head = findView(R.id.img_posted_particulars_head);
        img_posted_particulars_shoucang = findView(R.id.img_posted_particulars_shoucang);

        title_posted_particulars_title = findView(R.id.title_posted_particulars_title);
        tv_posted_particulars_nickname = findView(R.id.tv_posted_particulars_nickname);
        tv_posted_particulars_date = findView(R.id.tv_posted_particulars_date);
        tv_posted_particulars_title = findView(R.id.tv_posted_particulars_title);
        tv_posted_particulars_content = findView(R.id.tv_posted_particulars_content);
        tv_posted_particulars_kanguo = findView(R.id.tv_posted_particulars_kanguo);
        tv_posted_particulars_pinglunshuliang = findView(R.id.tv_posted_particulars_pinglunshuliang);

        et_posted_particulars_input = findView(R.id.et_posted_particulars_input);
        tv_posted_particulars_send = findView(R.id.tv_posted_particulars_send);

               /*
                memberId=文章发表人ID;
	 nickName=文章发表人昵称;
	 head=文章发表人头像;
	 title=文章标题;
	 content=文章内容;
	 publishdate=文章发表时间;
	 pageview=浏览次数;
	 commentnumber=评论个数;
	 likeStatus=是否点赞.1点过赞，0没有点过赞;
                 */


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.Posted_Particulars:

                        title_posted_particulars_title.setText(title);

                        //显示图片的配置
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                                .showImageOnFail(R.mipmap.def)//显示错误图片
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(UriUtil.ip + head, img_posted_particulars_head, options);

                        tv_posted_particulars_nickname.setText(nickName);

                        tv_posted_particulars_date.setText(publishdate);

                        tv_posted_particulars_title.setText(title);

                        tv_posted_particulars_content.setText(content);

                        tv_posted_particulars_kanguo.setText("阅读量"+pageview);

                        tv_posted_particulars_pinglunshuliang.setText("评论("+commentnumber+")");

                        if(likeStatus.equals("1")){
                            img_posted_particulars_shoucang.setChecked(true);
                            dianzan = "1";  //第一次点赞

                        }else{
                            img_posted_particulars_shoucang.setChecked(false);
                            dianzan = "0";  //第一次是未点赞
                        }

                        break;


                    case Constant.Posted_Particulars_List:
                        adapter.notifyDataSetChanged();

                        break;

                    case Constant.Posted_Notify_List:
                        adapter.notifyDataSetChanged();
                        break;


                }

            }
        };


    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        tv_posted_particulars_send.setOnClickListener(this);
        img_posted_particulars_shoucang.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new PostedParticularsAdapter(list, PostedParticularsActivity.this);
        rv_posted.setAdapter(adapter);
        adapter.setOnItemClickListener(new PostedParticularsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
//        sp_token = SPUtil.getData(PostedParticularsActivity.this, "token", "").toString();

        //rv_posted.setAdapter();
        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            articleId = intent.getStringExtra("articleId");
//            toast(articleId);

            if(App.islogin){
                columnDetail(App.token, articleId);  //获得文章详情
                addEessList(App.token, articleId);   //获取评论列表
            }else{
                startActivity(new Intent(PostedParticularsActivity.this, LoginActivity.class));
                finish();
            }



        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_posted_particulars_send:

                //sp_token
                //articleId
                String posted_content = et_posted_particulars_input.getText().toString().trim();
                String msg_memberId = SPUtil.getData(PostedParticularsActivity.this,"memberId","").toString();
                String msg_nickName = SPUtil.getData(PostedParticularsActivity.this,"nickName","").toString();


                if (StringUtil.isSpace(posted_content)){
                    toast("请输入评论内容");
                    return;
                }

                //toast(sp_token+"\n"+msg_memberId+"\n"+articleId+"\n"+posted_content+"\n"+articleId+"\n"+msg_memberId);

                sendPosted(App.token,msg_memberId,articleId,posted_content,articleId,msg_memberId,msg_nickName);

                if (StringUtil.isSpace(commentnumber)){
                    commentnumber="0";
                }

                tv_posted_particulars_pinglunshuliang.setText("评论("+(Integer.valueOf(commentnumber)+1)+")");

                et_posted_particulars_input.setText("");
                //toast("发布成功");
                break;

            case R.id.img_posted_particulars_shoucang:

                //.1点过赞，0没有点过赞;

                if(dianzan.equals("1")){    //如果点赞乐
                    img_posted_particulars_shoucang.setChecked(false);      //变为未点赞
                    dianzan = "0";  ////变为未点赞
                    dianzan_url_zhuangtai = dianzan_url[1];   //使用取消点赞接口

                    //toast(dianzan_url_zhuangtai);

                    String dianzan_memberId = SPUtil.getData(PostedParticularsActivity.this,"memberId","").toString();
                    praise(App.token,articleId,dianzan_memberId,dianzan_url_zhuangtai);

                }else{  //如果未点赞
                    img_posted_particulars_shoucang.setChecked(true);   //变为点赞
                    dianzan = "1";  //变为已点赞
                    dianzan_url_zhuangtai = dianzan_url[0];   //使用点赞接口

                    //toast(dianzan_url_zhuangtai);

                    String dianzan_memberId = SPUtil.getData(PostedParticularsActivity.this,"memberId","").toString();
                    praise(App.token,articleId,dianzan_memberId,dianzan_url_zhuangtai);

                }

                break;

        }
    }





public void praise(final String token,final String article,final String memberId,final String dianzan_url){
    Map<String,String> map = new HashMap<>();
    map.put("token",token);
    map.put("article.oid",article);
    map.put("article.memberId.oid",memberId);


    HttpUtils.doPost(dianzan_url, map, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("147",response.body().string());

        }
    });
}











public void sendPosted(final String token,final String memberId,final String articleId,final String content,final String article,final String member,final String nickName){

    Map<String,String> map = new HashMap<>();
    map.put("token",token);
    map.put("articleComment.memberId.oid",memberId);
    map.put("articleComment.articleId.oid",articleId);
    map.put("articleComment.content",content);
    map.put("article.oid",article);
    //map.put("article.memberId.oid",member);

    //Log.i("1111",token+"\n"+memberId+"\n"+articleId+"\n"+content);


    HttpUtils.doPost(UriUtil.Send_Posted, map, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("1111", response.body().string());


/*            Map<String, Object> map = new HashMap<>();
            map.put("nickName",nickName); //用户呢称
            map.put("content", content); //评论内容
            list.add(map);*/

            addEessList(App.token, articleId);   //获取评论列表


            Message msg = new Message();
            msg.what = Constant.Posted_Notify_List;
            hd.sendMessage(msg);



        }
    });

    }











    /*
    文章详情

     */
    public void columnDetail(final String token, final String oid) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("article.oid", oid);

        HttpUtils.doPost(UriUtil.get_Column_Detail, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("1211", response.body().string());


                Bean.PostedModel data = new Gson().fromJson(response.body().string(),
                        Bean.PostedModel.class);
                Bean.Posted posted = data.getModel();

                /*
                memberId=文章发表人ID;
	 nickName=文章发表人昵称;
	 head=文章发表人头像;
	 title=文章标题;
	 content=文章内容;
	 publishdate=文章发表时间;
	 pageview=浏览次数;
	 commentnumber=评论个数;
	 likeStatus=是否点赞.1点过赞，0没有点过赞;
                 */

                //private String memberId,nickName,head,title,content,publishdate,pageview,commentnumber,likeStatus = "";

                memberId = posted.getMemberId();
                nickName = posted.getNickName();
                head = posted.getHead();
                title = posted.getTitle();
                content = posted.getContent();
                publishdate = posted.getPublishdate();
                pageview = posted.getPageview();
                commentnumber = posted.getCommentnumber();
                likeStatus = posted.getLikeStatus();

/*
 SPUtil.saveData(PostedParticularsActivity.this,"memberId",memberId);
                String msg_memberId = SPUtil.getData(PostedParticularsActivity.this,"memberId","").toString();
                */

                SPUtil.saveData(PostedParticularsActivity.this,"memberId",memberId);
                SPUtil.saveData(PostedParticularsActivity.this,"nickName",nickName);

                //String msg_memberId = SPUtil.getData(PostedParticularsActivity.this,"memberId","").toString();

                //Log.i("1211",msg_memberId+"\n"+nickName+"\n"+head);


                Message msg = new Message();
                msg.what = Constant.Posted_Particulars;
                hd.sendMessage(msg);


                //Log.i("1211",memberId+"\n"+nickName+"\n"+head);
            }
        });
    }





    //获得评论列表
    public void addEessList(final String token, final String oid) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("article.oid", oid);

        HttpUtils.doPost(UriUtil.Add_EessList, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("1221", response.body().string());


                Bean.PostedParticularsList data = new Gson().fromJson(response.body().string(), Bean.PostedParticularsList.class);
                List<Bean.PostedParticulars> posted_particulars_list = data.getList();

                list.clear();
                for (int i = 0; i < posted_particulars_list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("articleId", posted_particulars_list.get(i).getArticleId()); //	文章ID
                    map.put("articlecommentId", posted_particulars_list.get(i).getArticlecommentId()); //文章评论ID
                    map.put("nickName", posted_particulars_list.get(i).getNickName()); //用户呢称
                    map.put("content", posted_particulars_list.get(i).getContent()); //评论内容
                    list.add(map);
                }

                //Log.i("1221", posted_particulars_list.get(0).getContent());

                Message msg = new Message();
                msg.what = Constant.Posted_Particulars_List;
                hd.sendMessage(msg);


            }
        });
    }






}
