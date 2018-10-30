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
 * Created by Administrator on 2017/12/14.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public BookingAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        String oid = list.get(position).get("oid"); //图片oid

        //景区：名称，开始结束时间，价格，销售量
        holder.tv_booking_name.setText(list.get(position).get("name"));
        holder.tv_booking_ctime_ftime.setText(list.get(position).get("ctime")+list.get(position).get("ftime"));
        holder.tv_booking_price.setText(list.get(position).get("price"));
        holder.tv_booking_sellNumber.setText(list.get(position).get("sellNumber"));


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip+list.get(position).get("thumPath"), holder.image_booking, options);



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
        ImageView image_booking;    //景区图片
        TextView tv_booking_name,tv_booking_ctime_ftime,tv_booking_price,tv_booking_sellNumber; //景区：名称，开始结束时间，价格，销售量

        public MyViewHolder(View itemView) {
            super(itemView);
            image_booking = (ImageView) itemView.findViewById(R.id.image_booking);
            tv_booking_name = (TextView) itemView.findViewById(R.id.tv_booking_name);
            tv_booking_ctime_ftime = (TextView) itemView.findViewById(R.id.tv_booking_ctime_ftime);
            tv_booking_price = (TextView) itemView.findViewById(R.id.tv_booking_price);
            tv_booking_sellNumber = (TextView) itemView.findViewById(R.id.tv_booking_sellNumber);

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
