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
 * Created by Administrator on 2018/1/28.
 */

/*
文章评论列表adapter
 */

public class PostedParticularsAdapter extends RecyclerView.Adapter<PostedParticularsAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public PostedParticularsAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_posted_particulars, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_posted_particulars_nickName.setText((String) list.get(position).get("nickName"));
        holder.tv_posted_particulars_content.setText(" : "+(String) list.get(position).get("content"));


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
        TextView tv_posted_particulars_nickName,tv_posted_particulars_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_posted_particulars_nickName = (TextView) itemView.findViewById(R.id.tv_posted_particulars_nickName);
            tv_posted_particulars_content = (TextView) itemView.findViewById(R.id.tv_posted_particulars_content);

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
