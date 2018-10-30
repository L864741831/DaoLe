package com.tck.daole.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.adapter.LocationAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.view.NiceRecyclerView;
import com.tck.daole.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 定位
 */

public class LocationActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> list;
    private TextView tvValue;


    private NiceRecyclerView location_recyclerview;
    private List<Map<Object, String>> locationlist = new ArrayList<>();
    private LocationAdapter locationadapter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_location);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        findViewId();
        initListener();
        initData();

        mSpinerPopWindow = new SpinerPopWindow<String>(this, list,itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
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
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            tvValue.setText(list.get(position));
            //toast("点击了:" + list.get(position));
        }
    };



    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvValue.setCompoundDrawables(null, null, drawable, null);
    }
    public void findViewId(){

        ll_back = findView(R.id.ll_back);
        tvValue = (TextView) findViewById(R.id.tv_value);

        location_recyclerview = findView(R.id.rv_location);

    }

    @Override
    protected void initListener() {

        ll_back.setOnClickListener(this);
        tvValue.setOnClickListener(this);
    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add("天津" + i);
        }


        ShopData("哪个市哪个区");
        locationadapter = new LocationAdapter(locationlist, this);
        location_recyclerview.setAdapter(locationadapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_value:
                mSpinerPopWindow.setWidth(tvValue.getWidth());
                mSpinerPopWindow.showAsDropDown(tvValue);
                setTextImage(R.mipmap.icon_down);
                break;
        }
    }

    private void ShopData(String str) {
        locationlist.clear();
        for (int i = 0; i < 15; i++) {
            Map<Object, String> map1 = new HashMap<>();
            map1.put("address", str);
            map1.put("name", "刘女士");
            map1.put("phone", "15625896542");
            locationlist.add(map1);
        }
    }

}
