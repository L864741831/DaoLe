package com.tck.daole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tck.daole.R;
import com.tck.daole.util.ImageLoadUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SpellParticularsImgAdapter extends RecyclerView.Adapter<SpellParticularsImgAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public SpellParticularsImgAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spell_particulars_img, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoadUtil.showImage((String) list.get(position).get("imger"), holder.iv_spell_item);
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
        ImageView iv_spell_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_spell_item = (ImageView) itemView.findViewById(R.id.iv_spell_item);
        }
    }
}
