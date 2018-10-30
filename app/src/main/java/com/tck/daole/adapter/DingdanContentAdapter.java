package com.tck.daole.adapter;

/**
 * Created by Administrator on 2018/2/2.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.util.UriUtil;

import java.util.List;
import java.util.Map;

/**
 * 订单商店商品内容适配器.
 */

public class DingdanContentAdapter extends RecyclerView.Adapter<DingdanContentAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public DingdanContentAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dingdan_content, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //holder.tv_news_title.setText((String) list.get(position).get("title"));

/*        map.put("projectOid", ShopContent_list.get(i).projectOid); //店铺oid
        map.put("takeProjectName", ShopContent_list.get(i).takeProjectName); //name=商品名称
        map.put("mainImage", ShopContent_list.get(i).mainImage); //mainImage=商品照片
        map.put("price", ShopContent_list.get(i).price); //price=价格
        map.put("purchaseNumber", ShopContent_list.get(i).purchaseNumber); //purchaseNumber=购买数量
        map.put("detail", ShopContent_list.get(i).detail); //商品详情备注*/

/*        ImageView img_order_mainImage;
        TextView tv_order_content_name,tv_order_content_detail,tv_order_content_purchaseNumber,tv_order_content_price;*/

String mainImage = (String )list.get(position).get("mainImage");
       String takeProjectName = (String )list.get(position).get("takeProjectName");
         String detail = (String )list.get(position).get("detail");
        int purchaseNumber = (int)list.get(position).get("purchaseNumber");
        double price = (double)list.get(position).get("price");


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
        holder.tv_order_content_purchaseNumber.setText(purchaseNumber+"");
        holder.tv_order_content_price.setText(price+"");

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
        //TextView tv_news_title,tv_news_text;
        ImageView img_order_mainImage;

        TextView tv_order_content_name,tv_order_content_detail,tv_order_content_purchaseNumber,tv_order_content_price;




        public MyViewHolder(View itemView) {
            super(itemView);
            //tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
            img_order_mainImage = (ImageView) itemView.findViewById(R.id.img_order_mainImage);

            tv_order_content_name = (TextView) itemView.findViewById(R.id.tv_order_content_name);
            tv_order_content_detail = (TextView) itemView.findViewById(R.id.tv_order_content_detail);
            tv_order_content_purchaseNumber = (TextView) itemView.findViewById(R.id.tv_order_content_purchaseNumber);
            tv_order_content_price = (TextView) itemView.findViewById(R.id.tv_order_content_price);


            //Log.i("显示一个商品","+++------");

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
