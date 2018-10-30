package com.tck.daole.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.DisplayUtils;
import com.tck.daole.util.DrawableUtils;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.MyHelper;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.FlowLayout;
import com.tck.daole.view.SpinerPopWindow;
import com.tck.daole.view.YhFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.view.ViewGroup.*;


/**
 * 搜索框页
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_fanhui;

    private YhFlowLayout flowLayout;
    private List<String> mDatas;

    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> list;

    private List<String> lishi_list;


    private TextView tvValue;


    private TextView clear_lishi, btn_add_random, btn_remove_all;


    FlowLayout flowlayout_lishi;

    String[] texts = new String[]{
            "乐拼购", "外卖", "景区订票", "超市",
            "生活充值", "同城在线", "钱包", "我的发布"
    };

    int length;

    private EditText search_huiche;

    private SQLiteDatabase db;
    private MyHelper helper;

    private int id = 1;

    Handler hd;

    @Override
    protected void initView(Bundle savedInstanceState) {

        helper = new MyHelper(SearchActivity.this, "searchpage.db", null, 1);
        //db = helper.getWritableDatabase();//返回一个SQLiteDatabase实例

        mDatas = new ArrayList<String>();
        lishi_list = new ArrayList<String>();
/*        mDatas.add("乐拼购");
        mDatas.add("外卖");
        mDatas.add("景区订票");
        mDatas.add("超市");
        mDatas.add("生活充值");
        mDatas.add("同城在线");
        mDatas.add("钱包");
        mDatas.add("我的发布");
        mDatas.add("团购");*/




        setContentView(R.layout.activity_search);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        length = texts.length;
        flowlayout_lishi = (FlowLayout) findViewById(R.id.flowlayout_lishi);


        findViewId();

        flowLayout = (YhFlowLayout) findViewById(R.id.flowlayout);
        flowLayout.setSpace(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        flowLayout.setPadding(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5),
                DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));

        //displayUI();


        search_huiche.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String str = search_huiche.getText().toString();
                    if(!str.equals("")){
                        search(str);

                    }
                    //Log.i("1245",str);

                    Intent intent_tuangou = new
                            Intent(SearchActivity.this, ShopSearchActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_tuangou.putExtra("type", "0");
                    intent_tuangou.putExtra("content", str);
                    startActivity(intent_tuangou);

                    search_huiche.setText("");

                }
                return false;
            }
        });


        hd = new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case Constant.Search:
                        displayUI();
                        break;
                    case Constant.Lishi_Search:

                        //lishi_list.get(0);

/*                       for(String sear:lishi_list){
                            Log.i("1214",sear);
                            //getSearch(sear);
                        }*/

         /*               for (int i = 0; i < lishi_list.size(); i++) {

                        }*/
                        Bundle b;
                        b = msg.getData();
                        String content = b.getString("content");
                        getSearch(content);

                        break;

                }

            }
        };


    }


    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.mipmap.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            tvValue.setText(list.get(position));
            //toast("点击了:" + list.get(position));

        }
    };


    public void findViewId() {
        rl_fanhui = findView(R.id.rl_fanhui);

        btn_add_random = findView(R.id.btn_add_random);
        btn_remove_all = findView(R.id.btn_remove_all);

        search_huiche = findView(R.id.search_huiche);

        btn_add_random.setVisibility(View.GONE);

        tvValue = (TextView) findViewById(R.id.tv_value);

    }

    @Override
    protected void initListener() {
        rl_fanhui.setOnClickListener(this);

        btn_add_random.setOnClickListener(this);
        btn_remove_all.setOnClickListener(this);
        tvValue.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        //获得历史搜索
        select();

        //获得热门搜索
        getSearchPage();

        list = new ArrayList<String>();
/*        for (int i = 0; i < 1; i++) {*/
        //list.add("天津" + i);
        list.add("商家");
        list.add("商品");
/*        }*/

        mSpinerPopWindow = new SpinerPopWindow<String>(this, list, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fanhui:
                finish();
                break;

            case R.id.btn_add_random:

                break;
            case R.id.btn_remove_all:
                flowlayout_lishi.removeAllViews();
                delete();
                break;
            case R.id.tv_value:
                mSpinerPopWindow.setWidth(tvValue.getWidth());
                mSpinerPopWindow.showAsDropDown(tvValue);
                setTextImage(R.mipmap.icon_down);
                break;

        }
    }

    private void displayUI() {
        for (int i = 0; i < mDatas.size(); i++) {
            final String data = mDatas.get(i);
            TextView tv = new TextView(this);
            tv.setText(data);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setTextColor(Color.parseColor("#999999"));
            tv.setGravity(Gravity.CENTER);
            int paddingy = DisplayUtils.dp2Px(this, 5);
            int paddingx = DisplayUtils.dp2Px(this, 6);
            tv.setPadding(paddingx, paddingy, paddingx, paddingy);
            tv.setClickable(false);

            int shape = GradientDrawable.RECTANGLE;
            int radius = DisplayUtils.dp2Px(this, 4);
            int strokeWeight = DisplayUtils.dp2Px(this, 1);
            int stokeColor = getResources().getColor(R.color.text_color_gray);
            int stokeColor2 = getResources().getColor(R.color.orange);

            GradientDrawable normalBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor, Color.WHITE);
            GradientDrawable pressedBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor2, getResources().getColor(R.color.orange));
            StateListDrawable selector = DrawableUtils.getSelector(normalBg, pressedBg);
            tv.setBackgroundDrawable(selector);
            ColorStateList colorStateList = DrawableUtils.getColorSelector(getResources().getColor(R.color.text_color_gray), getResources().getColor(R.color.white));
            tv.setTextColor(colorStateList);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //Log.i("1211", data);

                    Intent intent_tuangou = new
                            Intent(SearchActivity.this, ShopSearchActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent_tuangou.putExtra("type", "0");
                    intent_tuangou.putExtra("content", data);
                    startActivity(intent_tuangou);


                }
            });
            flowLayout.addView(tv);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public void getSearch(final String str) {
        int ranHeight = dip2px(this, 30);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);
        lp.setMargins(dip2px(this, 3), 0, dip2px(this, 5), 0);
        TextView tv = new TextView(this);
        tv.setPadding(dip2px(this, 6), 0, dip2px(this, 6), 0);
        tv.setTextColor(Color.parseColor("#999999"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        int index = (int) (Math.random() * length);
        //tv.setText(texts[index]);
        tv.setText(str);
        //search_huiche.setText("");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setLines(1);
        //tv.setBackgroundResource(R.mipmap.def);

        int shape = GradientDrawable.RECTANGLE;
        int radius = DisplayUtils.dp2Px(this, 4);
        int strokeWeight = DisplayUtils.dp2Px(this, 1);
        int stokeColor = getResources().getColor(R.color.text_color_gray);
        int stokeColor2 = getResources().getColor(R.color.orange);
        GradientDrawable normalBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor, Color.WHITE);
        GradientDrawable pressedBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor2, getResources().getColor(R.color.orange));
        StateListDrawable selector = DrawableUtils.getSelector(normalBg, pressedBg);

        tv.setBackgroundDrawable(selector);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_tuangou = new
                        Intent(SearchActivity.this, ShopSearchActivity.class);
                //在Intent对象当中添加一个键值对
                intent_tuangou.putExtra("type", "0");
                intent_tuangou.putExtra("content", str);
                startActivity(intent_tuangou);

            }
        });
        flowlayout_lishi.addView(tv, lp);
    }


    //清空数据
    public void delete(){

        db = helper.getWritableDatabase();//返回一个SQLiteDatabase实例

        db.execSQL("delete from search");

        db.close();
    }



    public void search(final String str) {
        int ranHeight = dip2px(this, 30);
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, ranHeight);
        lp.setMargins(dip2px(this, 3), 0, dip2px(this, 5), 0);
        TextView tv = new TextView(this);
        tv.setPadding(dip2px(this, 6), 0, dip2px(this, 6), 0);
        tv.setTextColor(Color.parseColor("#999999"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        int index = (int) (Math.random() * length);
        tv.setText(str);


        db = helper.getWritableDatabase();//返回一个SQLiteDatabase实例

        // 创建ContentValues对象
        ContentValues values1 = new ContentValues();

        // 向该对象中插入键值对
        values1.put("content", str);

        // 调用insert()方法将数据插入到数据库当中
        db.insert("search", null, values1);
        // sqliteDatabase.execSQL("insert into user (id,name) values (1,'carson')");
        //关闭数据库
        db.close();

        //id = id + 1;

        Log.i("1211", "插入成功");


        search_huiche.setText("");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setLines(1);
        //tv.setBackgroundResource(R.mipmap.def);

        int shape = GradientDrawable.RECTANGLE;
        int radius = DisplayUtils.dp2Px(this, 4);
        int strokeWeight = DisplayUtils.dp2Px(this, 1);
        int stokeColor = getResources().getColor(R.color.text_color_gray);
        int stokeColor2 = getResources().getColor(R.color.orange);
        GradientDrawable normalBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor, Color.WHITE);
        GradientDrawable pressedBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor2, getResources().getColor(R.color.orange));
        StateListDrawable selector = DrawableUtils.getSelector(normalBg, pressedBg);

        tv.setBackgroundDrawable(selector);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent_tuangou = new
                        Intent(SearchActivity.this, ShopSearchActivity.class);
                //在Intent对象当中添加一个键值对
                intent_tuangou.putExtra("type", "0");
                intent_tuangou.putExtra("content", str);
                startActivity(intent_tuangou);


            }
        });
        flowlayout_lishi.addView(tv, lp);
    }


    /**
     * 给TextView右边设置图片
     *
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvValue.setCompoundDrawables(null, null, drawable, null);
    }


    public void getSearchPage() {
        HttpUtils.doGet(UriUtil.Get_Search_Page, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //tv.setText(response.body().string());
                    //Log.i("1211",response.body().string());


                    Bean.SearchPageModel data = new Gson().fromJson(response.body().string(), Bean.SearchPageModel.class);
                    List<Bean.SearchPage> search_page_list = data.getModel();

                    for (int i = 0; i < search_page_list.size(); i++) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("Oid", search_page_list.get(i).getOid()); //	oid主键
                        //map.put("ProjectName", search_page_list.get(i).getContent()); //商品名称
                        mDatas.add(search_page_list.get(i).getContent());

                    }

                    Message msg = new Message();
                    msg.what = Constant.Search;
                    hd.sendMessage(msg);


                    //displayUI();

                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });

    }


    public void select() {


        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //Thread.sleep(3000);//休眠3秒
                    db = helper.getWritableDatabase();//返回一个SQLiteDatabase实例
                    Cursor cursor = db.query("search", new String[]{"content"}, null, null, null, null, null);
                    String content = null;
                    while (cursor.moveToNext()) {
                        content = cursor.getString(cursor.getColumnIndex("content"));
                        //search(content);
                        Log.i("1234", content);
                        lishi_list.add(content);
                        //getSearch(content);

                        Bundle b = new Bundle();
                        b.putString("content", content);
                        Message msg = new Message();
                        msg.setData(b);
                        msg.what = Constant.Lishi_Search;
                        hd.sendMessage(msg);

                        Thread.sleep(100);

                    }
                    cursor.close();
                    db.close();

                    //Thread.sleep(3000);//休眠3秒


/*                    Message msg = new Message();
                    msg.what = Constant.Lishi_Search;
                    hd.sendMessage(msg);*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }


}
