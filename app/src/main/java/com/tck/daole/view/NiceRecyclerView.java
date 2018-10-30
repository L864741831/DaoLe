package com.tck.daole.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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

public class NiceRecyclerView extends FrameLayout {
    public RecyclerView rv;
    public TextView tv;

    //RecyclerView 是listview还是gridview
    private ListDirection listDirection;
    //为空提示语第一次是否显示
    private boolean isFirstShowEmptyText;
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
    @SuppressLint("ResourceType")
    public NiceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceRecyclerView);

        rv=new RecyclerView(context);
//        rv.setBackgroundColor(getResources().getColor(R.color.white));
        LayoutParams lpRV=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rv.setLayoutParams(lpRV);

        tv=new TextView(context);
        tv.setGravity(Gravity.CENTER);
        LayoutParams lpTV=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv.setPadding(0,70,0,70);
        tv.setLayoutParams(lpTV);
//        Drawable drawable=getResources().getDrawable(R.mipmap.nodata_ico);
//        drawable.setBounds(0, 0, 200, 200);
//        tv.setCompoundDrawables(null,drawable,null,null);

        this.addView(rv);
        this.addView(tv);

        emptyText=typedArray.getString(R.styleable.NiceRecyclerView_emptyText);
        listDirection=ListDirection.fromStep(typedArray.getInt(R.styleable.NiceRecyclerView_listDirection,1));
        gridNum=typedArray.getInt(R.styleable.NiceRecyclerView_gridNum,1);
        isFirstShowEmptyText=typedArray.getBoolean(R.styleable.NiceRecyclerView_isFirstShowEmptyText,true);
        isShowEmptyText=typedArray.getBoolean(R.styleable.NiceRecyclerView_isShowEmptyText,false);

        setvisibility();

        if (listDirection==ListDirection.HORIZONTAL){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,gridNum);
            rv.setLayoutManager(gridLayoutManager);
        }else {
            LinearLayoutManager lm = new LinearLayoutManager(context);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(lm);
        }

        tv.setText(emptyText);
        isFirstShowEmptyText=true;
    }

    private void setvisibility() {
        if(isShowEmptyText&&isFirstShowEmptyText){
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
        adapter.registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer=new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            RecyclerView.Adapter adapter=getAdapter();
            if (adapter.getItemCount()==0&&isShowEmptyText){
                setShowEmptyText(true);
            }else {
                setShowEmptyText(false);
            }
        }
    };

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
