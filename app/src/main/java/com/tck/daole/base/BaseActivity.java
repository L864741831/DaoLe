package com.tck.daole.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tck.daole.thread.HttpInterface;

/**
 * kylin on 2017/12/12.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected HttpInterface httpInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpInterface = new HttpInterface(this);

        initView(savedInstanceState);
        initListener();
        initData();
    }

    /**
     * 初始化控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 时间监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void onClick(View v) {

    }

    /**
     * 弹吐司
     */
    public void toast(Object message) {
        Toast toast = Toast.makeText(this, message + "", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * findViewById
     */
    public <T extends View> T findView(int resId) {
        return (T) super.findViewById(resId);
    }
}
