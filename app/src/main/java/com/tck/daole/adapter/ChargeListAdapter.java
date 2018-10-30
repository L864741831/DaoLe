package com.tck.daole.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tck.daole.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * kylin on 2017/7/25.
 */

public class ChargeListAdapter extends RecyclerView.Adapter<ChargeListAdapter.ViewHolder>{

    private int resourceId;
    private List<Map<String,Object>> data = new ArrayList<>();
    private int mSelectedPos = 0;//实现单选  方法二，变量保存当前选中的position

    public interface onItemClickListener{
        void onClick(View view, int position);
    }

    private onItemClickListener onItemClickListener;

    public  void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener  = listener;
    }

    public ChargeListAdapter(int resourceId, List<Map<String,Object>> data) {//,RecyclerView type1,
        this.resourceId = resourceId;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.checkedTv.setText((String)data.get(position).get("itemName"));
        holder.checkedTv.setChecked((boolean)data.get(position).get("check"));
        holder.checkedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).put("check",false);
                }
                mSelectedPos = position;
                data.get(mSelectedPos).put("check",true);
                holder.checkedTv.setChecked(true);
                notifyDataSetChanged();
                onItemClickListener.onClick(holder.checkedTv,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkedTv;

        public ViewHolder(View itemView) {
            super(itemView);
            checkedTv = (CheckBox)itemView.findViewById(R.id.item);
        }
    }

}
