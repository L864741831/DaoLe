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
 * 定位列表适配器
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public LocationAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String address = list.get(position).get("address");
        final String name = list.get(position).get("name");
        final String phone = list.get(position).get("phone");

        holder.tv_address.setText(address);
        holder.tv_name.setText(name);
        holder.tv_phone.setText(phone);

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
        TextView tv_address, tv_name, tv_phone;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_name = (TextView) itemView.findViewById(R.id.tv_add_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
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
