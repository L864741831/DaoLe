package com.tck.daole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tck.daole.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHloder> {
    private List<Map<Object, String>> list;
    private Context context;

    public ShopAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHloder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new MyViewHloder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHloder holder, final int position) {
        //holder.tv_item_shop_name.setText(list.get(position).get("name"));

        holder.iv_shop_jiaHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ll_shop_jiaJian.setVisibility(View.VISIBLE);
                holder.iv_shop_jiaHao.setVisibility(View.GONE);
            }
        });
        holder.iv_shop_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = holder.tv_shop_num.getText().toString().trim();
                int in = Integer.parseInt(num) - 1;
                if (in == 0) {
                    holder.ll_shop_jiaJian.setVisibility(View.GONE);
                    holder.iv_shop_jiaHao.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_shop_num.setText(String.valueOf(in));
                }
            }
        });
        holder.iv_shop_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = holder.tv_shop_num.getText().toString().trim();
                int in = Integer.parseInt(num) + 1;
                holder.tv_shop_num.setText(String.valueOf(in));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position);
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

    class MyViewHloder extends RecyclerView.ViewHolder {
        TextView tv_item_shop_name, tv_shop_num;
        ImageView iv_shop_jiaHao, iv_shop_jian, iv_shop_jia;
        LinearLayout ll_shop_jiaJian;

        public MyViewHloder(View itemView) {
            super(itemView);
            tv_item_shop_name = (TextView) itemView.findViewById(R.id.tv_item_shop_name);
            tv_shop_num = (TextView) itemView.findViewById(R.id.tv_shop_num);
            iv_shop_jiaHao = (ImageView) itemView.findViewById(R.id.iv_shop_jiaHao);
            iv_shop_jian = (ImageView) itemView.findViewById(R.id.iv_shop_jian);
            iv_shop_jia = (ImageView) itemView.findViewById(R.id.iv_shop_jia);
            ll_shop_jiaJian = (LinearLayout) itemView.findViewById(R.id.ll_shop_jiaJian);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
