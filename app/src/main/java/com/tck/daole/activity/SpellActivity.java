package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.adapter.SpellAdapter;
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
 * 乐拼购列表
 */
public class SpellActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private NiceRecyclerView rv_spell;
    private List<Map<Object, String>> list = new ArrayList<>();

    Handler hd;
    SpellAdapter adapter;

    private EditText et_search;

    @SuppressLint("HandlerLeak")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_spell);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }
        tv_left = findView(R.id.tv_left);
        tv_title = findView(R.id.tv_title);
        tv_right = findView(R.id.tv_right);
        ll_left = findView(R.id.ll_left);
        rv_spell = findView(R.id.rv_spell);

        et_search = findView(R.id.et_search);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    spellList(et_search.getText().toString().trim(), 0, 50);

                    //toast(et_search.getText().toString().trim());

                    et_search.setText("");


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

                Bundle b;
                b = msg.getData();
                String msgStr = b.getString("msg");


                switch (msg.what) {

                    case Constant.Spell_List_Error:
                        toast(msgStr);
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
        tv_title.setText("乐拼购");
        tv_right.setVisibility(View.GONE);

        spellList("", 0, 50);

        adapter = new SpellAdapter(list, SpellActivity.this);
        rv_spell.setAdapter(adapter);

        adapter.setOnItemClickListener(new SpellAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String oid = list.get(position).get("Oid");

                //startActivity(new Intent(SpellActivity.this, SpellParticularsActivity.class));//跳转拼单物品详情

                Intent intent = new
                        Intent(SpellActivity.this, SpellParticularsActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("oid", oid);
                startActivity(intent);

            }
        });

        //SpellList();

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


    //获得乐平能够列表
    public void spellList(final String search, final int index, final int num) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getSpellList(search, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取了拼购列表.成功", result);
                            Bean.SpellModel data = new Gson().fromJson(result, Bean.SpellModel.class);

                            if (data.getStatus() != 1) {
                                Bundle b = new Bundle();
                                b.putString("msg", data.getMessage());
                                Message msg = new Message();
                                msg.setData(b);
                                msg.what = Constant.Spell_List_Error;
                                hd.sendMessage(msg);

                            } else {
                                list.clear();
                                List<Bean.Spell> spell_list = data.list;
                                for (int i = 0; i < spell_list.size(); i++) {
                                    Map<Object, String> map = new HashMap<>();
                                    map.put("Oid", spell_list.get(i).getOid()); //	oid主键
                                    map.put("ProjectName", spell_list.get(i).getName()); //商品名称
                                    map.put("LoanAmounts", spell_list.get(i).getLoanAmounts()); //价格
                                    map.put("SaleNum", spell_list.get(i).getSaleNum()); //销量
                                    map.put("ThumPath", spell_list.get(i).ThumPath); //图片地址
                                    map.put("price", spell_list.get(i).price); //商品原价

                                    list.add(map);

                                }

                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取了拼购列表.异常", response);

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








/*    public void SpellList() {
       *//*
 * Get请求
 * 参数一：请求Url
 * 参数二：请求回调
 *//*
        HttpUtils.doGet(UriUtil.spell_list, new Callback() {

            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //toast(response.body().string());

                    String str = response.body().string();

                    Bean.SpellModel data = new Gson().fromJson(str, Bean.SpellModel.class);

                    Log.i("lepingou",str);

                    if(data.getStatus()!=1){
                        Bundle b = new Bundle();
                        b.putString("msg", data.getMessage());
                        Message msg = new Message();
                        msg.setData(b);
                        msg.what = Constant.Spell_List_Error;
                        hd.sendMessage(msg);

                    }else{
                        List<Bean.Spell> spell_list = data.getModel();
                        for (int i = 0; i < spell_list.size(); i++) {
                            Map<Object, String> map = new HashMap<>();
                            map.put("Oid", spell_list.get(i).getOid()); //	oid主键
                            map.put("ProjectName", spell_list.get(i).getName()); //商品名称
                            map.put("LoanAmounts", spell_list.get(i).getLoanAmounts()); //价格
                            map.put("SaleNum", spell_list.get(i).getSaleNum()); //销量
                            map.put("ThumPath", spell_list.get(i).ThumPath); //图片地址
                            map.put("price", spell_list.get(i).price); //商品原价

                            list.add(map);

                            Log.i("lepingou",spell_list.get(i).getProjectImage().getThumPath()+"\n"+spell_list.get(i).getName());
                        }

                        //Bundle b = new Bundle();
                        //b.putString("msg", result);
                        Message msg = new Message();
                        //msg.setData(b);
                        msg.what = Constant.Spell;
                        hd.sendMessage(msg);
                    }




                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }*/


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


/*        dialog_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        dialog_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //onRightClickListener.onRightClick(dialog);

                Intent intent = new Intent();
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);//或者使用Activity.startActivity(intent)

                intent.setClass(ctx, LoginActivity.class);
                ctx.startActivity(intent);

                //关闭adapter所在activity
                if (Activity.class.isInstance(ctx)) {
                    //关闭adapter所在activity
                    Activity activity = (Activity) ctx;
                    activity.finish();
                }

            }
        });
        dialog.show();
    }


}
