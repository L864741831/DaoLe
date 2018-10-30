package com.tck.daole.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.tck.daole.adapter.SystemMsgAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.NiceRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tck.daole.R;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 系统消息
 */
public class SystemMsgActivity extends BaseActivity {
    private LinearLayout ll_back;
    private NiceRecyclerView rv_msg;
    private List<Map<String, Object>> list = new ArrayList<>();

    Handler hd;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_msg);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        ll_back = findView(R.id.ll_back);
        rv_msg = findView(R.id.rv_msg);


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

/*                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");*/


                switch (msg.what) {
                    case Constant.News:
                        SystemMsgAdapter adapter = new SystemMsgAdapter(list, SystemMsgActivity.this);
                        rv_msg.setAdapter(adapter);
                        adapter.setOnItemClickListener(new SystemMsgAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                //startActivity(new Intent(SystemMsgActivity.this,PostedParticularsActivity.class));//跳转发布详情
                            }
                        });

                        break;

                }

            }
        };

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {


        getNews();

/*        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", "2017-11-1" + i);
            list.add(map);
        }*/
/*        SystemMsgAdapter adapter = new SystemMsgAdapter(list, this);
        rv_msg.setAdapter(adapter);
        adapter.setOnItemClickListener(new SystemMsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //startActivity(new Intent(SystemMsgActivity.this,PostedParticularsActivity.class));//跳转发布详情
            }
        });*/
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




    //系统消息
    public void getNews(){
       /*
 * Get请求
 * 参数一：请求Url
 * 参数二：请求回调
 */
        HttpUtils.doGet(UriUtil.System_news, new Callback() {

            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    //Log.i("======",response.body().string());

                    String json_str = "{\"status\":1,\"message\":\"获取成功!\",\"model\":[{\"oid\":\"1\",\"title\":\"lol\",\"text\":\"lol是拳头公司开发\"},{\"oid\":\"2\",\"title\":\"dnf\",\"text\":\"dnf是一个黄了10年的游戏\"},{\"oid\":\"3\",\"title\":\"cf\",\"text\":\"腾讯已经放弃的游戏\"},{\"oid\":\"4\",\"title\":\"fff\",\"text\":\"FFF团\"}]}";

                   Bean.NewsModel data = new Gson().fromJson(json_str, Bean.NewsModel.class);
                    List<Bean.News> news_list = data.getModel();

                    for (int i = 0; i < news_list.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("oid", news_list.get(i).getOid());
                        map.put("title", news_list.get(i).getTitle());
                        map.put("text", news_list.get(i).getText());
                        list.add(map);
                    }


                   Message msg = new Message();
                    msg.what = Constant.News;
                    hd.sendMessage(msg);

                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }



}
