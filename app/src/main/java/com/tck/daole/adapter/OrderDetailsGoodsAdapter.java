package com.tck.daole.adapter;

/*
 * kylin on 2018/2/2.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.entity.OrderDetails;
import com.tck.daole.util.UriUtil;

import java.util.List;

/**
 * 订单商店商品内容适配器.
 */

public class OrderDetailsGoodsAdapter extends RecyclerView.Adapter<OrderDetailsGoodsAdapter.MyViewHolder> {
    private List<OrderDetails.Goods> list;
    private Context context;

    public OrderDetailsGoodsAdapter(List<OrderDetails.Goods> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dingdan_content, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String mainImage =list.get(position).imagePath;
        String takeProjectName = list.get(position).name;
        String detail = list.get(position).detail;
        int purchaseNumber = list.get(position).num;
        double price = (double)list.get(position).price;


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip+mainImage, holder.img_order_mainImage, options);

        holder.tv_order_content_name.setText(takeProjectName);
        holder.tv_order_content_detail.setText(detail);
        holder.tv_order_content_purchaseNumber.setText("x"+purchaseNumber);
        holder.tv_order_content_price.setText("¥"+price);

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
        ImageView img_order_mainImage;
        TextView tv_order_content_name,tv_order_content_detail,tv_order_content_purchaseNumber,tv_order_content_price;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_order_mainImage = (ImageView) itemView.findViewById(R.id.img_order_mainImage);

            tv_order_content_name = (TextView) itemView.findViewById(R.id.tv_order_content_name);
            tv_order_content_detail = (TextView) itemView.findViewById(R.id.tv_order_content_detail);
            tv_order_content_purchaseNumber = (TextView) itemView.findViewById(R.id.tv_order_content_purchaseNumber);
            tv_order_content_price = (TextView) itemView.findViewById(R.id.tv_order_content_price);
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
