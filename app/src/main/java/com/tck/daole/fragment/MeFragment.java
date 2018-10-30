package com.tck.daole.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.activity.CashCouponActivity;
import com.tck.daole.activity.DingDanPageActivity;
import com.tck.daole.activity.EnterpriseSettledActivity;
import com.tck.daole.activity.InvitationFriendActivity;
import com.tck.daole.activity.LoginActivity;
import com.tck.daole.activity.MyCollectionActivity;
import com.tck.daole.activity.MyPostedActivity;
import com.tck.daole.activity.PersonalCenterActivity;
import com.tck.daole.activity.PurseActivity;
import com.tck.daole.activity.SystemSetActivity;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.view.ZQRoundOvalImageView;

/**
 * 我的Fragment
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_PersonalCenter, ll_MyPosted, ll_Order, ll_Purse,
            ll_Coupons, ll_Collect, ll_Merchant, ll_Invite, ll_Setting;
    private RelativeLayout rl_PersonalCenter,msg;
    private TextView name;
    private TextView address;
    private TextView sex;
    private ZQRoundOvalImageView iv_img;

//    private String sp_token = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            view.findViewById(R.id.View).setVisibility(View.GONE);
        }
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View view) {
        rl_PersonalCenter = (RelativeLayout) view.findViewById(R.id.rl_PersonalCenter);
        msg = (RelativeLayout) view.findViewById(R.id.msg);
        ll_PersonalCenter = (LinearLayout) view.findViewById(R.id.ll_PersonalCenter);
        ll_MyPosted = (LinearLayout) view.findViewById(R.id.ll_MyPosted);
        ll_Order = (LinearLayout) view.findViewById(R.id.ll_Order);
        ll_Purse = (LinearLayout) view.findViewById(R.id.ll_Purse);
        ll_Coupons = (LinearLayout) view.findViewById(R.id.ll_Coupons);
        ll_Collect = (LinearLayout) view.findViewById(R.id.ll_Collect);
        ll_Merchant = (LinearLayout) view.findViewById(R.id.ll_Merchant);
        ll_Invite = (LinearLayout) view.findViewById(R.id.ll_Invite);
        ll_Setting = (LinearLayout) view.findViewById(R.id.ll_Setting);
        name = (TextView) view.findViewById(R.id.name);
        address = (TextView) view.findViewById(R.id.address);
        sex = (TextView) view.findViewById(R.id.sex);
        iv_img = (ZQRoundOvalImageView) view.findViewById(R.id.iv_img);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.islogin) {
            name.setText(App.login.nickName);
            address.setText("");
            sex.setText(App.login.sex);
            msg.setVisibility(View.GONE);
            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.touxiang)//等待加载显示图片
                    .showImageOnFail(R.mipmap.touxiang)//显示错误图片
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoader.getInstance().displayImage(UriUtil.ip + App.login.head, iv_img, options);

        }else {
            name.setText("未登录");
            msg.setVisibility(View.GONE);
            iv_img.setImageResource(R.mipmap.touxiang);
        }
    }

    private void initListener() {
        rl_PersonalCenter.setOnClickListener(this);
        ll_PersonalCenter.setOnClickListener(this);
        ll_MyPosted.setOnClickListener(this);
        ll_Order.setOnClickListener(this);
        ll_Purse.setOnClickListener(this);
        ll_Coupons.setOnClickListener(this);
        ll_Collect.setOnClickListener(this);
        ll_Merchant.setOnClickListener(this);
        ll_Invite.setOnClickListener(this);
        ll_Setting.setOnClickListener(this);
    }

    private void initData() {
//        sp_token = SPUtil.getData(getActivity(), "token", "").toString();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_PersonalCenter://进入个人中心
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), PersonalCenterActivity.class));//跳转个人中心
                }
                break;
            case R.id.ll_PersonalCenter://个人中心
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), PersonalCenterActivity.class));//跳转个人中心
                }
                break;
            case R.id.ll_MyPosted://我的发布
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), MyPostedActivity.class));//跳转我的发布
                }
                break;
            case R.id.ll_Order://订单
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), DingDanPageActivity.class));//跳转订单页
                }
                break;
            case R.id.ll_Purse://钱包
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    startActivity(new Intent(getActivity(), PurseActivity.class));//跳转到钱包
                }
                break;
            case R.id.ll_Coupons://优惠券
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    startActivity(new Intent(getActivity(), CashCouponActivity.class));//跳转到代金券
                }
                break;
            case R.id.ll_Collect://收藏
                if(!App.islogin){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    startActivity(new Intent(getActivity(), MyCollectionActivity.class));//跳转我的收藏
                }
                break;
            case R.id.ll_Merchant://商家加盟
                startActivity(new Intent(getActivity(), EnterpriseSettledActivity.class));//跳转商家加盟
                break;
            case R.id.ll_Invite://邀请好友
                startActivity(new Intent(getActivity(), InvitationFriendActivity.class));//跳转邀请好友
                break;
            case R.id.ll_Setting://设置
                startActivity(new Intent(getActivity(), SystemSetActivity.class));//跳转设置
                break;
        }
    }



















}
