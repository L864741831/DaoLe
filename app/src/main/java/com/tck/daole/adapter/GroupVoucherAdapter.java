package com.tck.daole.adapter;

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
 * 购物券适配器
 */

public class GroupVoucherAdapter extends RecyclerView.Adapter<GroupVoucherAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public GroupVoucherAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_voucher, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String caiming = list.get(position).get("caiming");
        final String fenshu = list.get(position).get("fenshu");
        final String jiage = list.get(position).get("jiage");

        //                    holder.tv_shop_num.setText(String.valueOf(in));
        //        holder.tv_item_shop_name.setText(list.get(position).get("name"));
        holder.tv_caiming.setText(caiming);
        holder.tv_fenshu.setText(fenshu);
        holder.tv_jiage.setText(jiage);



        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position);
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
        TextView tv_caiming, tv_fenshu, tv_jiage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_caiming = (TextView) itemView.findViewById(R.id.tv_caiming);
            tv_fenshu = (TextView) itemView.findViewById(R.id.tv_fenshu);
            tv_jiage = (TextView) itemView.findViewById(R.id.tv_jiage);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
