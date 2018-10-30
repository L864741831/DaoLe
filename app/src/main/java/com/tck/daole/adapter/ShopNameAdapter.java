package com.tck.daole.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tck.daole.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.util.UriUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/27.
 * 商品推荐横向列表
 */

public class ShopNameAdapter extends RecyclerView.Adapter<ShopNameAdapter.MyViewHloder> {
    private List<Map<Object, String>> list;
    private Context context;

    public ShopNameAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHloder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_name, parent, false);
        return new MyViewHloder(view);
    }


    /*
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(datas[position]);
    }
     */
    @Override
    public void onBindViewHolder(final MyViewHloder holder, final int position) {
        //加粗
        holder.tv_xianjia.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        //加删除线
        holder.tv_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        String projectThumPath = (String) list.get(position).get("projectThumPath");
        String projectName = (String) list.get(position).get("projectName");
        String projectLoanAmounts = (String) list.get(position).get("projectLoanAmounts");
        //String yuanjia = (String) list.get(position).get("yuanjia");

/*        map2.put("projectName", shopName_list.get(i).projectName);
        map2.put("projectThumPath", shopName_list.get(i).projectThumPath);
        map2.put("projectLoanAmounts", shopName_list.get(i).projectLoanAmounts);*/


        holder.tv_yifen.setText(projectName);
        holder.tv_xianjia.setText(projectLoanAmounts);
        //holder.tv_yuanjia.setText(yuanjia);


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip+projectThumPath, holder.img_shangpin, options);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position);
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

    class MyViewHloder extends RecyclerView.ViewHolder {
        TextView tv_yifen, tv_xianjia,tv_yuanjia;
        ImageView img_shangpin;

        public MyViewHloder(View itemView) {
            super(itemView);
            tv_yifen = (TextView) itemView.findViewById(R.id.tv_yifen);
            tv_xianjia = (TextView) itemView.findViewById(R.id.tv_xianjia);
            tv_yuanjia = (TextView) itemView.findViewById(R.id.tv_yuanjia);
            img_shangpin = (ImageView) itemView.findViewById(R.id.img_shangpin);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
