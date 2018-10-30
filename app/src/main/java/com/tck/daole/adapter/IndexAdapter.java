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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.util.StringUtil;
import com.tck.daole.util.UriUtil;

import java.util.List;
import java.util.Map;

/**
 * 首页列表适配器
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public IndexAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_index_listview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String state = list.get(position).get("state");
        switch (state) {
            case "1":
                holder.ll_index_isDpCs.setVisibility(View.VISIBLE);
                holder.ll_index_isTj.setVisibility(View.GONE);
                holder.ll_index_isMd.setVisibility(View.GONE);


/*                map.put("stoeId", list_store.get(i).stoeId); //	店铺ID
                map.put("logoImage", list_store.get(i).logoImage); //	店铺z图片
                map.put("name", list_store.get(i).name); //	店铺名字
                map.put("evaluateIdstarlevel", list_store.get(i).evaluateIdstarlevel); //	店铺星级
                map.put("storeAddress", list_store.get(i).storeAddress); //	店铺收藏ID
                map.put("jvLi", list_store.get(i).jvLi); //	店铺距离
                                                map.put("storeAddress", list_store.get(i).storeAddress); //	店铺收藏ID
                */

                final String logoImage = list.get(position).get("logoImage");
                final String name = list.get(position).get("name");

                final String evaluateIdstarlevel = list.get(position).get("evaluateIdstarlevel");
                Float float_evaluateIdstarlevel=0f;
                if (!StringUtil.isSpace(evaluateIdstarlevel)) {
                    float_evaluateIdstarlevel = Float.parseFloat(evaluateIdstarlevel);
                }

                final String storeAddress = list.get(position).get("storeAddress");
                final String jvLi = list.get(position).get("jvLi");


                //显示图片的配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                        .showImageOnFail(R.mipmap.def)//显示错误图片
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                ImageLoader.getInstance().displayImage(UriUtil.ip + logoImage, holder.iv_image_index, options);


                holder.index_shop_name.setText(name);
                holder.ratingBar.setRating(float_evaluateIdstarlevel);
                holder.index_str_ratingbar.setText(evaluateIdstarlevel);
                holder.index_shop_address.setText(storeAddress);
                holder.index_shop_jili.setText(jvLi);

                break;
            case "2":

                final String oid = list.get(position).get("oid");   //	oid主键
                final String title = list.get(position).get("title");   //文章标题
                final String publishdate = list.get(position).get("publishdate");   //发布时间
                final String pageview = list.get(position).get("pageview"); //浏览量

                holder.tv_forum_title.setText(title);
                holder.tv_forum_date.setText(publishdate);
                holder.tv_forum_pageview.setText(pageview+"人看过");


                holder.tv_content_index.setVisibility(View.GONE);
                holder.iv_image_index.setVisibility(View.GONE);
                holder.ll_index_isDpCs.setVisibility(View.GONE);
                holder.ll_index_isTj.setVisibility(View.VISIBLE);
                holder.ll_index_isMd.setVisibility(View.GONE);
                break;
            case "3":
                holder.ll_index_isDpCs.setVisibility(View.GONE);
                holder.ll_index_isTj.setVisibility(View.GONE);
                holder.ll_index_isMd.setVisibility(View.VISIBLE);
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
        LinearLayout ll_index_isDpCs, ll_index_isTj, ll_index_isMd;
        ImageView iv_image_index;
        TextView tv_content_index;
        TextView tv_forum_title,tv_forum_date,tv_forum_pageview;    //文章：标题，发布时间，阅读量

        TextView index_shop_name;   //店铺名
        RatingBar ratingBar;
        TextView  index_str_ratingbar;
        TextView  index_shop_address,index_shop_jili;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_index_isDpCs = (LinearLayout) itemView.findViewById(R.id.ll_index_isDpCs);
            ll_index_isTj = (LinearLayout) itemView.findViewById(R.id.ll_index_isTj);
            ll_index_isMd = (LinearLayout) itemView.findViewById(R.id.ll_index_isMd);

            iv_image_index = (ImageView) itemView.findViewById(R.id.iv_image_index);
            tv_content_index = (TextView) itemView.findViewById(R.id.tv_content_index);

            tv_forum_title = (TextView) itemView.findViewById(R.id.tv_forum_title);
            tv_forum_date = (TextView) itemView.findViewById(R.id.tv_forum_date);
            tv_forum_pageview = (TextView) itemView.findViewById(R.id.tv_forum_pageview);

            index_shop_name = (TextView) itemView.findViewById(R.id.index_shop_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            index_str_ratingbar = (TextView) itemView.findViewById(R.id.index_str_ratingbar);
            index_shop_address = (TextView) itemView.findViewById(R.id.index_shop_address);
            index_shop_jili = (TextView) itemView.findViewById(R.id.index_shop_jili);
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
