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
import com.tck.daole.util.UriUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ShopAppraiseAdapter extends RecyclerView.Adapter<ShopAppraiseAdapter.MyViewHolder> {
    private List<Map<Object, String>> list;
    private Context context;

    public ShopAppraiseAdapter(List<Map<Object, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_appraise, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String state = list.get(position).get("state");

        String name = list.get(position).get("name");   //名称
        String evaluatetime = list.get(position).get("evaluatetime");   //评论日期
        String starlevel = list.get(position).get("starlevel"); //评论星级
        Float ration_float = Float.parseFloat(starlevel);   //星级
        String content = list.get(position).get("content"); //内容
        String head = list.get(position).get("head"); //评论人头像相对地址

        holder.tv_item_shop_name.setText(name);
        holder.tv_shop_riqi.setText(evaluatetime);
        holder.tv_shop_ratingBar.setRating(ration_float);
        holder.tv_shop_content.setText(content);


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip+head, holder.tv_shop_head, options);


/*        tv_item_shop_name = (TextView) itemView.findViewById(R.id.tv_item_shop_name);
        ll_grid_img = (LinearLayout) itemView.findViewById(R.id.ll_grid_img);

        tv_shop_riqi = (TextView) itemView.findViewById(R.id.tv_shop_riqi);
        tv_shop_ratingBar = (RatingBar) itemView.findViewById(R.id.tv_shop_ratingBar);
        tv_shop_content = (TextView) itemView.findViewById(R.id.tv_shop_content);
        tv_shop_head = (ImageView) itemView.findViewById(R.id.tv_shop_head);*/


/*        map.put("adminsStoreId", comment_list.get(i).getAdminsStoreId()); //	店铺ID
        map.put("name", comment_list.get(i).getName()); //店铺名字;
        map.put("cryptonym", comment_list.get(i).getCryptonym()); //=是否匿名（匿名返回“匿名用户”，不匿名返回“用户nickName”）;
        map.put("head", comment_list.get(i).getHead()); //评论人头像;
        map.put("starlevel", comment_list.get(i).getStarlevel()); //评论星级;
        map.put("evaluatetime", comment_list.get(i).getEvaluatetime()); //=评论时间;
        map.put("content", comment_list.get(i).getContent()); //=评论内容;
        map.put("commentLevel", comment_list.get(i).getCommentLevel()); //=评论等级;*/


        if (state.equals("1")) {
            holder.ll_grid_img.setVisibility(View.GONE);
        } else {
            holder.ll_grid_img.setVisibility(View.VISIBLE);
        }


/*        String ration_str = list.get(position).get("starlevel");
        Float ration_float = Float.parseFloat(ration_str);*/

        //holder.tv_shop_riqi.setText(list.get(position).get("evaluatetime"));

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
        TextView tv_item_shop_name,tv_shop_riqi,tv_shop_content;
        LinearLayout ll_grid_img;
        ImageView tv_shop_head;
        RatingBar tv_shop_ratingBar;



        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_shop_name = (TextView) itemView.findViewById(R.id.tv_item_shop_name);
            ll_grid_img = (LinearLayout) itemView.findViewById(R.id.ll_grid_img);

            tv_shop_riqi = (TextView) itemView.findViewById(R.id.tv_shop_riqi);
            tv_shop_ratingBar = (RatingBar) itemView.findViewById(R.id.tv_shop_ratingBar);
            tv_shop_content = (TextView) itemView.findViewById(R.id.tv_shop_content);
            tv_shop_head = (ImageView) itemView.findViewById(R.id.tv_shop_head);

        }
    }
}
