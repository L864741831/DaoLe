package com.tck.daole.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tck.daole.R;


/*
 * kylin on 2017年12月11日16:04:04
 */

public class HorNiceRecyclerView extends FrameLayout {
    public RecyclerView rv;
    public TextView tv;

    //RecyclerView 是listview还是gridview
    private ListDirection listDirection;
    //为空提示语是否显示
    private boolean isShowEmptyText;
    //列表为空提示语
    private String emptyText;
    //gridview每行显示数量
    private int gridNum;

    /**
     * 只支持在xml文件定义
     * @param context 上下文activity
     * @param attrs 自定义属性
     */
    public HorNiceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceRecyclerView);

        rv=new RecyclerView(context);
        LayoutParams lpRV=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rv.setLayoutParams(lpRV);

        tv=new TextView(context);
        tv.setGravity(Gravity.CENTER);
        LayoutParams lpTV=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv.setPadding(0,100,0,100);
        tv.setLayoutParams(lpTV);

        this.addView(rv);
        this.addView(tv);

        emptyText=typedArray.getString(R.styleable.NiceRecyclerView_emptyText);
        listDirection=ListDirection.fromStep(typedArray.getInt(R.styleable.NiceRecyclerView_listDirection,0));
        gridNum=typedArray.getInt(R.styleable.NiceRecyclerView_gridNum,1);
        isShowEmptyText=typedArray.getBoolean(R.styleable.NiceRecyclerView_isShowEmptyText,false);

        setvisibility();

        if (listDirection==ListDirection.VERTICAL){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,gridNum);
            rv.setLayoutManager(gridLayoutManager);
        }else {
            LinearLayoutManager lm = new LinearLayoutManager(context);
            lm.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(lm);
        }

        tv.setText(emptyText);
    }

    private void setvisibility() {
        if(isShowEmptyText){
            rv.setVisibility(GONE);
            tv.setVisibility(VISIBLE);
        }else {
            rv.setVisibility(VISIBLE);
            tv.setVisibility(GONE);
        }
    }

    public RecyclerView.Adapter<?> getAdapter() {
        return rv.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter<?> adapter) {
        rv.setAdapter(adapter);
    }

    public boolean isShowEmptyText() {
        return isShowEmptyText;
    }

    public void setShowEmptyText(boolean showEmptyText) {
        isShowEmptyText = showEmptyText;
        setvisibility();
    }

    /**
     * RecyclerView 是listview还是gridview
     */
    private enum ListDirection {
        HORIZONTAL(0), VERTICAL(1);
        int step;

        ListDirection(int step) {
            this.step = step;
        }

        public static ListDirection fromStep(int step) {
            for (ListDirection f : values()) {
                if (f.step == step) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
