package com.tck.daole;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tck.daole.base.BaseActivity;
import com.tck.daole.fragment.IndexFragment;
import com.tck.daole.fragment.MeFragment;
import com.tck.daole.fragment.OnlineFragment;
import com.tck.daole.fragment.RechargeFragment;
import com.tck.daole.util.UserUtil;


/**
 * 首页
 */

public class MainActivity extends BaseActivity {
    private Fragment indexFragment, rechargeFragment, onlineFragment, meFragment;
    private RadioButton tabIndex, tabRecharge, tabOnline, tabMe;
    private RadioGroup radioGroup;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        radioGroup = findView(R.id.radioGroup);
        tabIndex = findView(R.id.rb_index);
        tabRecharge = findView(R.id.rb_recharge);
        tabOnline = findView(R.id.rb_online);
        tabMe = findView(R.id.rb_me);

        new UserUtil(this,httpInterface).getUserInformation(App.token);

    }

    @Override
    protected void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_index:
                        setSelect(0);
                        break;
                    case R.id.rb_recharge:
                        setSelect(1);
                        break;
                    case R.id.rb_online:
                        setSelect(2);
                        break;
                    case R.id.rb_me:
                        setSelect(3);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

        tabIndex.setChecked(true);
        setSelect(0);


    }

    public void setSelect(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);//先隐藏所有界面
        switch (i) {
            case 0:
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.main_content, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }
                break;
            case 1:
                if (rechargeFragment == null) {
                    rechargeFragment = new RechargeFragment();
                    transaction.add(R.id.main_content, rechargeFragment);
                } else {
                    transaction.show(rechargeFragment);
                }
                break;
            case 2:
 /*               if (onlineFragment == null) {*/
                    onlineFragment = new OnlineFragment();
                    transaction.add(R.id.main_content, onlineFragment);
/*                } else {
                    transaction.show(onlineFragment);
                }*/
                break;
            case 3:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.main_content, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                break;
        }
        transaction.commit();
    }

    //用于隐藏界面
    private void hideFragment(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (rechargeFragment != null) {
            transaction.hide(rechargeFragment);
        }
        if (onlineFragment != null) {
            transaction.hide(onlineFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
    }
}
