package com.tck.daole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tck.daole.R;

import java.util.List;
import java.util.Map;

/**
 * 商品分类适配器.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public SortAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sort_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //holder.tv_news_title.setText((String) list.get(position).get("title"));
        //holder.tv_sort.setText("数据后Aug会哦是");


        //Log.i("123",list.get(position).get("GoodsCategoryName"));

        holder.tv_sort.setText((String) list.get(position).get("GoodsCategoryName"));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sort;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_sort = (TextView) itemView.findViewById(R.id.tv_sort);

        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
