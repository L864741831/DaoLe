package com.tck.daole.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tck.daole.R;
import com.tck.daole.thread.HttpInterface;

/**
 * kylin on 2017/12/12.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public Activity activity;
    private RelativeLayout view;
    private FrameLayout container;
    protected SmartRefreshLayout smart;
    protected HttpInterface httpInterface;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        httpInterface=new HttpInterface(activity);
        if (inflater != null) {
            view = (RelativeLayout)inflater.inflate(R.layout.fragment_base, null);
            container = (FrameLayout) view.findViewById(R.id.container);
            smart = (SmartRefreshLayout) view.findViewById(R.id.smart);
//            smart.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
            smart.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
            initView(view);
            initListener();
            initData();
            return view;
        }
        return new View(activity);
    }

    /**
     * 获得布局id
     */
    protected void setContainer(int layoutId){
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(layoutId,container,true);
        }
    }

    /**
     * 初始化控件
     */
    protected abstract void initView(View view);

    protected abstract void initData();

    /**
     * 时间监听
     */
    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }

    /**
     * 弹吐司
     */
    public void toast(Object message) {
        Toast toast = Toast.makeText(activity, message + "", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * findViewById
     */
    protected <T extends View> T findView(int id) {
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(id);
    }

    protected void finishRefresh(){
        smart.finishRefresh();
        smart.finishLoadmore();
    }
}
