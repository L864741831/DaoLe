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
 * Created by Administrator on 2017/12/16.
 */

public class SpellParticularsAdapter extends RecyclerView.Adapter<SpellParticularsAdapter.MyViewHoldre> {
    private List<Map<String, Object>> list;
    private Context context;

    public SpellParticularsAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHoldre onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spell_particulars, parent, false);
        return new MyViewHoldre(view);
    }

    @Override
    public void onBindViewHolder(MyViewHoldre holder, final int position) {
        holder.tv_item_particulars_name.setText((String) list.get(position).get("name"));

        holder.tv_spell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpellClickListener.onSpellClick(position);
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

    class MyViewHoldre extends RecyclerView.ViewHolder {
        TextView tv_item_particulars_name, tv_spell;

        public MyViewHoldre(View itemView) {
            super(itemView);
            tv_item_particulars_name = (TextView) itemView.findViewById(R.id.tv_item_particulars_name);
            tv_spell = (TextView) itemView.findViewById(R.id.tv_spell);
        }
    }

    private OnSpellClickListener onSpellClickListener;

    public interface OnSpellClickListener {
        void onSpellClick(int position);
    }

    public void setOnSpellClickListener(OnSpellClickListener onSpellClickListener) {
        this.onSpellClickListener = onSpellClickListener;
    }
}
