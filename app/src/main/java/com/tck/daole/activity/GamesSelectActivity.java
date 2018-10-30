package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.sidebar.CharacterParser;
import com.tck.daole.sidebar.Games;
import com.tck.daole.sidebar.SC_CityRecyclerAdapter;
import com.tck.daole.sidebar.SideBar;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;

public class GamesSelectActivity extends BaseActivity {
    private RecyclerView sortListView;
    private SideBar sideBar;
    private LinearLayout back;
    private SC_CityRecyclerAdapter adapter;
//    private ClearEditText mClearEditText;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private GamesComparator gamesComparator;

//    /**
//     * 汉字转换成拼音的类
//     */
//    private CharacterParser characterParser;
    private List<Games> SourceDateList=new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_games_select);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.store_View).setVisibility(View.GONE);
        }
        sideBar = (SideBar) findViewById(R.id.sidebar);
//        TextView dialog = ;
        back = (LinearLayout) findViewById(R.id.ll_back);
        sortListView = (RecyclerView) findViewById(R.id.citys);
        sideBar.setTextView((TextView) findViewById(R.id.contact_dialog));
//        mClearEditText = (ClearEditText) findViewById(R.id.words);
//        characterParser=new CharacterParser();
        gamesComparator=new GamesComparator();
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        httpInterface.games1_charge(new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                Log.e("onSuccess", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            String sub_code=jsonObject.optJSONObject("model").optString("error_response");
                            if (status==1 && StringUtil.isSpace(sub_code)){
                                JSONArray array=jsonObject.optJSONObject("model").optJSONObject("game_list_response").optJSONObject("games").optJSONArray("game");
                                getListData(array);
                            }else {
                                toast("充值失败，请重试");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            toast("充值失败，请重试");
                        }
                    }
                });
            }

            @Override
            public void onFail(String response) {

                //toast(response+"");
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

    private void getListData(JSONArray array) {
        SourceDateList = filledData(array);
        adapter = new SC_CityRecyclerAdapter(GamesSelectActivity.this, SourceDateList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GamesSelectActivity.this);
        sortListView.setLayoutManager(linearLayoutManager);
        sortListView.setAdapter(adapter);

        adapter.setOnCityClickListener(new SC_CityRecyclerAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name,int position) {
                Log.e("MainActivity", "onCityClick:" + name);
                Intent intent=new Intent(GamesSelectActivity.this,GamesChargeActivity.class);
                intent.putExtra("gameId",name);
                startActivityForResult(intent,666);
            }
        });

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s);
                if (position != -1) {
//                    mRecyCity.scrollToPosition(position);
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
    }

    /**
     * 为ListView填充数据
     */
    private List<Games> filledData(JSONArray data){
        //optJSONObject(0).optString("itemId")
        List<Games> mSortList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            //汉字转换成拼音
//            String pinyin = characterParser.getSelling(data.optJSONObject(i).optString("pinyin"));
            Games city = new Games(data.optJSONObject(i).optString("gameName"),
                    data.optJSONObject(i).optString("pinyin"),
                    data.optJSONObject(i).optString("gameId"));
            mSortList.add(city);
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, gamesComparator);
        return mSortList;
    }

    /**
     * a-z排序
     */
    public static class GamesComparator implements Comparator<Games> {
        @Override
        public int compare(Games lhs, Games rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
//
//    /**
//     * 根据输入框中的值来过滤数据并更新ListView
//     */
//    private void filterData(String filterStr){
//        List<Games> filterDateList = new ArrayList<>();
//
//        if(TextUtils.isEmpty(filterStr)){
//            filterDateList = SourceDateList;
//        }else{
//            filterDateList.clear();
//            for(Games sortModel : SourceDateList){
//                String name = sortModel.getName();
//                if(name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr)){
//                    filterDateList.add(sortModel);
//                }
//            }
//        }
//
//        // 根据a-z进行排序
//        Collections.sort(filterDateList, gamesComparator);
//        adapter.updateListView(filterDateList);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==666&&resultCode==888){
            finish();
        }
    }
}
