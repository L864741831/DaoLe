package com.tck.daole.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.util.SPUtil;


public class SystemSetActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RelativeLayout rl_edition_update;
    private RelativeLayout rl_clean_cache;
    private RelativeLayout rl_opinion_feedback;
    private RelativeLayout rl_modify_password;
    private RelativeLayout rl_about_reach;
    private RelativeLayout rl_contact_we;
    private Button exit_login;

    PopupWindow pop;
    LinearLayout ll_popup;

//    String sp_token = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_set);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
    }

    public void findViewId() {
        ll_back = findView(R.id.ll_back);
        rl_edition_update = (RelativeLayout) findViewById(R.id.rl_edition_update);
        rl_clean_cache = (RelativeLayout) findViewById(R.id.rl_clean_cache);
        rl_opinion_feedback = (RelativeLayout) findViewById(R.id.rl_opinion_feedback);
        rl_modify_password = (RelativeLayout) findViewById(R.id.rl_modify_password);
        rl_about_reach = (RelativeLayout) findViewById(R.id.rl_about_reach);
        rl_contact_we = (RelativeLayout) findViewById(R.id.rl_contact_we);
        exit_login = (Button) findViewById(R.id.exit_login);
    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        rl_edition_update.setOnClickListener(this);
        rl_clean_cache.setOnClickListener(this);
        rl_opinion_feedback.setOnClickListener(this);
        rl_modify_password.setOnClickListener(this);
        rl_about_reach.setOnClickListener(this);
        rl_contact_we.setOnClickListener(this);
        exit_login.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(SystemSetActivity.this, "token", "").toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_edition_update:
                //finish();
                break;
            case R.id.rl_clean_cache:
                //finish();
                break;
            case R.id.rl_opinion_feedback:
                startActivity(new Intent(SystemSetActivity.this, OpinionActivity.class));   //跳转意见反馈页
                break;
            case R.id.rl_modify_password:
                if(!App.islogin){
                    toast("请先登录");
                    startActivity(new Intent(SystemSetActivity.this, LoginActivity.class));   //跳转登录页
                    return;
                }
                startActivity(new Intent(SystemSetActivity.this, ModifyPasswordActivity.class));   //跳转修改密码页
                break;
            case R.id.rl_about_reach:
                startActivity(new Intent(SystemSetActivity.this, AboutArrive.class));   //跳转关于到了
                break;
            case R.id.rl_contact_we:
                //finish();
                break;
            case R.id.exit_login:

                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        SystemSetActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);


/*                SPUtil.saveData(SystemSetActivity.this,"token","");
                startActivity(new Intent(SystemSetActivity.this, PersonalCenterActivity.class));
                String msg_token = SPUtil.getData(SystemSetActivity.this,"token","").toString();
                finish();
                toast("已退出当前账号" + msg_token);*/
                break;
        }
    }


    /****
     * 退出登录
     */
    public void showPopupWindow() {
        pop = new PopupWindow(SystemSetActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_exit_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);




        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_exit);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {   //退出账号

                SPUtil.saveData(SystemSetActivity.this, "token", "");
                startActivity(new Intent(SystemSetActivity.this, PersonalCenterActivity.class));
//                SPUtil.saveData(BindPhoneActivity.this,"token",token);
                SPUtil.saveData(SystemSetActivity.this,"islogin",false);
                App.token="";
                App.islogin=false;
//                String msg_token = SPUtil.getData(SystemSetActivity.this, "token", "").toString();
                finish();
//                toast("已退出当前账号" + msg_token);

                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

    }

}
