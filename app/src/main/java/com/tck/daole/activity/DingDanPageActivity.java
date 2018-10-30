package com.tck.daole.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.adapter.MFragmentPagerAdapter;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.fragment.DingDanFiveFragment;
import com.tck.daole.fragment.DingDanFourFragment;
import com.tck.daole.fragment.DingDanOneFragment;
import com.tck.daole.fragment.DingDanThreeFragment;
import com.tck.daole.fragment.DingDanTwoFragment;

import java.util.ArrayList;


public class DingDanPageActivity extends BaseActivity implements View.OnClickListener {

    //实现Tab滑动效果
    private ViewPager mViewPager;
    //动画图片
    private ImageView cursor;
    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_there;
    private int position_four;


    //动画图片宽度
    private int bmpW;
    //当前页卡编号
    private int currIndex = 0;
    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //管理Fragment
    private FragmentManager fragmentManager;
    public Context context;


    private LinearLayout ll_back;
//    String sp_token = "";

    //待付款
    private TextView tv_order_daifukuan;
    //待收货
    private TextView tv_order_daishouhuo;
    //已收货
    private TextView tv_order_yishouhuo;
    //退款退货
    private TextView tv_order_tuikuantuihuo;
    //全部订单
    private TextView tv_order_tquanbudingdan;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ding_dan_page);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        context = this;
        findViewId();

        //初始化ImageView
        InitImageView();

        //初始化Fragment
        InitFragment();

        //初始化ViewPager
        InitViewPager();
    }

    public void findViewId() {
        ll_back = findView(R.id.ll_back);


        //待付款
        tv_order_daifukuan = (TextView) findViewById(R.id.tv_order_daifukuan);
        //待收货
        tv_order_daishouhuo = (TextView) findViewById(R.id.tv_order_daishouhuo);
        //已收货
        tv_order_yishouhuo = (TextView) findViewById(R.id.tv_order_yishouhuo);
        //退款退货
        tv_order_tuikuantuihuo = (TextView) findViewById(R.id.tv_order_tuikuantuihuo);
        //全部订单
        tv_order_tquanbudingdan = (TextView) findViewById(R.id.tv_order_tquanbudingdan);


    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);

        //添加点击事件
        tv_order_daifukuan.setOnClickListener(new MyOnClickListener(0));
        tv_order_daishouhuo.setOnClickListener(new MyOnClickListener(1));
        tv_order_yishouhuo.setOnClickListener(new MyOnClickListener(2));
        tv_order_tuikuantuihuo.setOnClickListener(new MyOnClickListener(3));
        tv_order_tquanbudingdan.setOnClickListener(new MyOnClickListener(4));


    }


    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;

        bmpW = (screenW / 5);

        //设置动画图片宽度
        setBmpW(cursor, bmpW);
        offset = 0;

        //动画图片偏移量赋值
        position_one = (int) (screenW / 5.0);
        position_two = position_one * 2;
        position_there = position_one * 3;
        position_four = position_one * 4;


    }


    /**
     * 设置动画图片宽度
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }


    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new DingDanOneFragment());
        fragmentArrayList.add(new DingDanTwoFragment());
        fragmentArrayList.add(new DingDanThreeFragment());
        fragmentArrayList.add(new DingDanFourFragment());
        fragmentArrayList.add(new DingDanFiveFragment());

        fragmentManager = getSupportFragmentManager();

    }


    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存2个页面
        //mViewPager.setOffscreenPageLimit(0);

        //设置默认打开第一页
        //mViewPager.setCurrentItem(0);

        mViewPager.setOffscreenPageLimit(0);

        //setOffscreenPageLimit

        //将顶部文字恢复默认值
        resetTextViewTextColor();
        tv_order_daifukuan.setTextColor(getResources().getColor(R.color.orange));

        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    /**
     * 将顶部文字恢复默认值
     */
    private void resetTextViewTextColor() {


        tv_order_daifukuan.setTextColor(getResources().getColor(R.color.hintColor));
        tv_order_daishouhuo.setTextColor(getResources().getColor(R.color.hintColor));
        tv_order_yishouhuo.setTextColor(getResources().getColor(R.color.hintColor));
        tv_order_tuikuantuihuo.setTextColor(getResources().getColor(R.color.hintColor));
        tv_order_tquanbudingdan.setTextColor(getResources().getColor(R.color.hintColor));

    }


    /**
     * 头标点击监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {







                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_order_daifukuan.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 2) {//从页卡1跳转转到页卡3
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_order_daifukuan.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 3) {//从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_there, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_order_daifukuan.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 4) {//从页卡1跳转转到页卡5
                        animation = new TranslateAnimation(position_four, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_order_daifukuan.setTextColor(getResources().getColor(R.color.orange));
                    }
                    break;


                /*                //添加点击事件
                tv_order_daifukuan.setOnClickListener(new MyOnClickListener(0));
                tv_order_daishouhuo.setOnClickListener(new MyOnClickListener(1));
                tv_order_yishouhuo.setOnClickListener(new MyOnClickListener(2));
                tv_order_tuikuantuihuo.setOnClickListener(new MyOnClickListener(3));
                tv_order_tquanbudingdan.setOnClickListener(new MyOnClickListener(4));*/


                //当前为页卡2
                case 1:
                    //从页卡1跳转转到页卡0
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_order_daishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 2) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_two,position_one , 0, 0);
                        resetTextViewTextColor();
                        tv_order_daishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 3) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_there, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_order_daishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 4) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_four,position_one , 0, 0);
                        resetTextViewTextColor();
                        tv_order_daishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    }
                    break;


                //当前为页卡2
                case 2:
                    //从页卡1跳转转到页卡0
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_order_yishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 1) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_order_yishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 3) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_there, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_order_yishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 4) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_four, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_order_yishouhuo.setTextColor(getResources().getColor(R.color.orange));
                    }
                    break;

                //当前为页卡2
                case 3:
                    //从页卡1跳转转到页卡0
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_there, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tuikuantuihuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 1) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_there, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tuikuantuihuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 2) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_two, position_there, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tuikuantuihuo.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 4) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_four, position_there, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tuikuantuihuo.setTextColor(getResources().getColor(R.color.orange));
                    }
                    break;

                //当前为页卡3
                case 4:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tquanbudingdan.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 1) {//从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_one, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tquanbudingdan.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 2) {//从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_two, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tquanbudingdan.setTextColor(getResources().getColor(R.color.orange));
                    } else if (currIndex == 3) {//从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_there, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_order_tquanbudingdan.setTextColor(getResources().getColor(R.color.orange));
                    }
                    break;
            }
            currIndex = position;

            if (animation != null) {
                animation.setFillAfter(true);// true:图片停在动画结束位置
                animation.setDuration(300);
            }
            cursor.startAnimation(animation);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(DingDanPageActivity.this, "token", "").toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }


}
