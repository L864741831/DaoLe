package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * 邀请好友页
 */


public class InvitationFriendActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_back;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invitation_friend);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
    }

    public void findViewId(){
        ll_back = findView(R.id.ll_back);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;

            case R.id.img1:
                share(R.id.img1);
                break;

            case R.id.img2:
                share(R.id.img2);
                break;
            case R.id.img3:
                share(R.id.img3);
                break;
            case R.id.img4:
                share(R.id.img4);
                break;
        }
    }

    private void share(int id){
        switch (id){
            case R.id.img1:
                new ShareAction(this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//微信
                        .withText("hello")//分享内容
                        .setCallback(umShareListener)//回调监听器
                        .share();
                break;
            case R.id.img2:
                new ShareAction(this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//微信朋友圈
                        .withText("hello")//分享内容
                        .setCallback(umShareListener)//回调监听器
                        .share();
                break;
            case R.id.img3:
                UMImage image = new UMImage(InvitationFriendActivity.this, R.mipmap.logo_zanding);//资源文件
                new ShareAction(this)
                        .setPlatform(SHARE_MEDIA.QQ)//qq
                        .withText("hello")//分享内容
                        .withMedia(image)
                        .setCallback(umShareListener)//回调监听器
                        .share();
                break;
            case R.id.img4:
                image = new UMImage(InvitationFriendActivity.this, R.mipmap.logo_zanding);//资源文件
                new ShareAction(this)
                        .setPlatform(SHARE_MEDIA.QZONE)//qq空间
                        .withText("hello")//分享内容
                        .withMedia(image)
                        .setCallback(umShareListener)//回调监听器
                        .share();
        }
    }

    UMShareListener umShareListener=new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
