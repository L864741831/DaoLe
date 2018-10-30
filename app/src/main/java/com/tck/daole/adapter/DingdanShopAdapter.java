package com.tck.daole.adapter;

/**
 * Created by Administrator on 2018/2/2.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tck.daole.R;

import java.util.List;
import java.util.Map;

/**
 * 订单商店名适配器.
 */

public class DingdanShopAdapter extends RecyclerView.Adapter<DingdanShopAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public DingdanShopAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_system_msg, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_news_title.setText((String) list.get(position).get("title"));
        holder.tv_news_text.setText((String) list.get(position).get("text"));


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
        TextView tv_news_title,tv_news_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
            tv_news_text = (TextView) itemView.findViewById(R.id.tv_news_text);

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
