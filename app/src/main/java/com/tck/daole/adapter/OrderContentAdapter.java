package com.tck.daole.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.entity.OrderList;
import com.tck.daole.util.UriUtil;

import java.util.List;

/**
 * 订单页商品适配器
 */

public class OrderContentAdapter extends RecyclerView.Adapter<OrderContentAdapter.MyViewHolder> {
    private List<OrderList.TakeOutOrderProject> content_list;
    private Context context;

    OrderContentAdapter(List<OrderList.TakeOutOrderProject> content_list, Context context) {
        this.content_list = content_list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_content, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        OrderList.TakeOutOrderProject project=content_list.get(position);
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip + project.orderProjectProjectImgae,holder.iv_adapter_list_pic, options);

        holder.tv_buy_num.setText("x"+project.orderProjectProjectNum);
        holder.tv_intro.setText(""+project.orderProjectName);
        holder.tv_price.setText("￥ "+project.orderProjectprice);
        holder.tv_color_size.setText(""+project.orderProjectdetail);

        holder.layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (content_list == null) {
            return 0;
        } else {
            return content_list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_adapter_list_pic;
        TextView tv_intro,tv_color_size,tv_price,tv_buy_num;
        LinearLayout layer;



        public MyViewHolder(View itemView) {
            super(itemView);
            iv_adapter_list_pic = (ImageView) itemView.findViewById(R.id.iv_adapter_list_pic);
            tv_intro = (TextView) itemView.findViewById(R.id.tv_intro);
            tv_color_size = (TextView) itemView.findViewById(R.id.tv_color_size);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_buy_num = (TextView) itemView.findViewById(R.id.tv_buy_num);
            layer = (LinearLayout) itemView.findViewById(R.id.layer);

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
