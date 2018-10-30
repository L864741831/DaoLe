package com.tck.daole.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.util.UriUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

/**
 * 收藏列表适配器
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public CollectionAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String state = list.get(position).get("state");

/*        ImageView item_collection_shangpin__dianpu_item;    //店铺或商品图片
        TextView item_collection_shangpin_jieshao,item_collection_shangpin_yueshou,item_collection_shangpin_jiage;  //介绍，月售，价格*/

/*        map.put("drtail", commodity_list.get(i).getDrtail()); //	商品介绍
        map.put("saleNum", commodity_list.get(i).getSaleNum()); //	商品销量
        map.put("loanAmounts", commodity_list.get(i).getLoanAmounts()); //	商品价格
        map.put("thumPath", commodity_list.get(i).getThumPath()); //	商品图片*/

        switch (state) {
            case "1":
                final String name = list.get(position).get("name"); //名称
                final String starlevel =  list.get(position).get("starlevel");
                final Float float_starlevel = Float.parseFloat(starlevel);  //星级
                final String address = list.get(position).get("address");   //店铺地址
                final String jvli = list.get(position).get("jvLi");   //距离



                holder.ll_collection_dianpu.setVisibility(View.VISIBLE);
                holder.ll_collection_shangpin.setVisibility(View.GONE);
                holder.item_collection_dianpu_name.setText(name);
                holder.ratingBar.setRating(float_starlevel);
                holder.item_collection_dianpu_address.setText(address);
                holder.item_collection_dianpu_juli.setText(jvli);
                holder.tv_ratingBar_shop.setText(starlevel);




/*
                map.put("memberId", shop_list.get(i).getMemberId()); //	用户ID
                map.put("oid", shop_list.get(i).getOid()); //	店铺收藏ID
                map.put("name", shop_list.get(i).getName()); //	店铺名称
                map.put("address", shop_list.get(i).getAddress()); //	店铺地址
                map.put("thumPath", shop_list.get(i).getThumPath()); //	店铺图片
                map.put("starlevel", shop_list.get(i).getStarlevel()); //	店铺评价星级
                map.put("jvli", shop_list.get(i).getJvli()); //	距离
*/


                break;
            case "2":
                final String projectName = list.get(position).get("projectName");
                holder.ll_collection_dianpu.setVisibility(View.GONE);
                holder.ll_collection_shangpin.setVisibility(View.VISIBLE);
                holder.item_collection_shangpin_name.setText(projectName);

                holder.item_collection_shangpin_jieshao.setText(list.get(position).get("drtail"));//	商品介绍
                holder.item_collection_shangpin_yueshou.setText("月售 "+list.get(position).get("saleNum")); //	商品销量
                holder.item_collection_shangpin_jiage.setText("¥"+list.get(position).get("loanAmounts")); //	商品价格

                //显示图片的配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                        .showImageOnFail(R.mipmap.def)//显示错误图片
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                ImageLoader.getInstance().displayImage(UriUtil.ip+list.get(position).get("thumPath"), holder.item_collection_shangpin__dianpu_item, options);

                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position,state);
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
        LinearLayout ll_collection_dianpu,ll_collection_shangpin;
        TextView item_collection_dianpu_name,item_collection_shangpin_name;
        ImageView item_collection_shangpin__dianpu_item;    //店铺或商品图片
        TextView item_collection_shangpin_jieshao,item_collection_shangpin_yueshou,item_collection_shangpin_jiage;  //介绍，月售，价格

        TextView item_collection_dianpu_address,item_collection_dianpu_juli,tv_ratingBar_shop;//店铺地址、距离\星级汉字

        RatingBar ratingBar;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_collection_dianpu = (LinearLayout) itemView.findViewById(R.id.ll_collection_dianpu);
            ll_collection_shangpin = (LinearLayout) itemView.findViewById(R.id.ll_collection_shangpin);
            item_collection_dianpu_name = (TextView) itemView.findViewById(R.id.item_collection_dianpu_name);
            item_collection_shangpin_name = (TextView) itemView.findViewById(R.id.item_collection_shangpin_name);

            item_collection_shangpin__dianpu_item = (ImageView) itemView.findViewById(R.id.item_collection_shangpin__dianpu_item);

            item_collection_shangpin_jieshao = (TextView) itemView.findViewById(R.id.item_collection_shangpin_jieshao);
            item_collection_shangpin_yueshou = (TextView) itemView.findViewById(R.id.item_collection_shangpin_yueshou);
            item_collection_shangpin_jiage = (TextView) itemView.findViewById(R.id.item_collection_shangpin_jiage);

            ratingBar  = (RatingBar) itemView.findViewById(R.id.ratingBar); //星级
            item_collection_dianpu_address = (TextView) itemView.findViewById(R.id.item_collection_dianpu_address);
            item_collection_dianpu_juli = (TextView) itemView.findViewById(R.id.item_collection_dianpu_juli);
            tv_ratingBar_shop  = (TextView) itemView.findViewById(R.id.tv_ratingBar_shop);




        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position,String state);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
