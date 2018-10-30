package com.tck.daole.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.activity.ElectricChargeActivity;
import com.tck.daole.activity.FuelChargeActivity;
import com.tck.daole.activity.GamesChargeActivity;
import com.tck.daole.activity.GamesSelectActivity;
import com.tck.daole.activity.GasChargeActivity;
import com.tck.daole.activity.LiuliangChargeActivity;
import com.tck.daole.activity.PhoneChargeActivity;
import com.tck.daole.activity.RechargeRecordActivity;
import com.tck.daole.activity.StoreInformationActivity;
import com.tck.daole.activity.WaterChargeActivity;
import com.tck.daole.thread.MUIToast;

/**
 * 生活充值Fragment
 */
public class RechargeFragment extends Fragment implements View.OnClickListener {
    private TextView tv_left, tv_title, tv_right;
    private LinearLayout ll_left;
    private LinearLayout ll_huaFei, ll_liuLiang, ll_youXi, ll_jiaYou,
            ll_shuiFei, ll_dianFei, ll_ranQi, ll_xiaoYuan, ll_gongJiao;
    private ImageView ll_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            view.findViewById(R.id.View).setVisibility(View.GONE);
        }
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View view) {
        tv_left = (TextView) view.findViewById(R.id.tv_left);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        ll_left = (LinearLayout) view.findViewById(R.id.ll_left);
        ll_huaFei = (LinearLayout) view.findViewById(R.id.ll_huaFei);
        ll_liuLiang = (LinearLayout) view.findViewById(R.id.ll_liuLiang);
        ll_youXi = (LinearLayout) view.findViewById(R.id.ll_youXi);
        ll_jiaYou = (LinearLayout) view.findViewById(R.id.ll_jiaYou);
        ll_shuiFei = (LinearLayout) view.findViewById(R.id.ll_shuiFei);
        ll_dianFei = (LinearLayout) view.findViewById(R.id.ll_dianFei);
        ll_ranQi = (LinearLayout) view.findViewById(R.id.ll_ranQi);
        ll_xiaoYuan = (LinearLayout) view.findViewById(R.id.ll_xiaoYuan);
        ll_gongJiao = (LinearLayout) view.findViewById(R.id.ll_gongJiao);
        ll_back = (ImageView) view.findViewById(R.id.ll_back);
    }

    private void initListener() {
        ll_left.setOnClickListener(this);
        ll_huaFei.setOnClickListener(this);
        ll_liuLiang.setOnClickListener(this);
        ll_youXi.setOnClickListener(this);
        ll_jiaYou.setOnClickListener(this);
        ll_shuiFei.setOnClickListener(this);
        ll_dianFei.setOnClickListener(this);
        ll_ranQi.setOnClickListener(this);
        ll_xiaoYuan.setOnClickListener(this);
        ll_gongJiao.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    private void initData() {
        ll_back.setVisibility(View.GONE);
        tv_left.setText("首页");
        tv_title.setText("生活充值");
        tv_right.setText("充值记录");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
                break;
            case R.id.ll_huaFei://话费充值
                startActivity(new Intent(getActivity(), PhoneChargeActivity.class));
                break;
            case R.id.ll_liuLiang://流量充值
                startActivity(new Intent(getActivity(), LiuliangChargeActivity.class));
                break;
            case R.id.ll_youXi://游戏卡充值
                startActivity(new Intent(getActivity(), GamesSelectActivity.class));
                break;
            case R.id.ll_jiaYou://加油卡充值
                startActivity(new Intent(getActivity(), FuelChargeActivity.class));
                break;
            case R.id.ll_shuiFei://水费
                startActivity(new Intent(getActivity(), WaterChargeActivity.class));
                break;
            case R.id.ll_dianFei://电费
                startActivity(new Intent(getActivity(), ElectricChargeActivity.class));
                break;
            case R.id.ll_ranQi://燃气费
                startActivity(new Intent(getActivity(), GasChargeActivity.class));
                break;
            case R.id.ll_xiaoYuan://校园卡充值
                MUIToast.show(getActivity(),"功能开发中，暂未开放");
                break;
            case R.id.ll_gongJiao://公交卡充值
                MUIToast.show(getActivity(),"功能开发中，暂未开放");
                break;
            case R.id.tv_right://充值记录
                startActivity(new Intent(getActivity(), RechargeRecordActivity.class));//充值记录页

                break;

    }
    }
}
