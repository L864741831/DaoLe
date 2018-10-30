package com.tck.daole.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.util.UriUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

/**
 * kylin on 2017/12/14.
 */

public class SpellAdapter extends RecyclerView.Adapter<SpellAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public SpellAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_spell_name.setText(list.get(position).get("ProjectName"));
        holder.tv_spell_loanAmounts.setText("¥"+list.get(position).get("price"));
        holder.tv_spell_saleNum.setText("已售"+list.get(position).get("SaleNum"));

        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip+list.get(position).get("ThumPath"), holder.image_spell_path, options);



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
        ImageView image_spell_path;
        TextView tv_spell_name,tv_spell_loanAmounts,tv_spell_saleNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            image_spell_path = (ImageView) itemView.findViewById(R.id.image_spell_path);
            tv_spell_name = (TextView) itemView.findViewById(R.id.tv_spell_name);
            tv_spell_loanAmounts = (TextView) itemView.findViewById(R.id.tv_spell_loanAmounts);
            tv_spell_saleNum = (TextView) itemView.findViewById(R.id.tv_spell_saleNum);

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
