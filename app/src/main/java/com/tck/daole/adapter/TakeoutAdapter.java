package com.tck.daole.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * 超市页列表适配器
 */

public class TakeoutAdapter extends RecyclerView.Adapter<TakeoutAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public TakeoutAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_takeout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position);
            }
        });


/*                             map.put("oid", chaoshi_list.get(i).getOid()); //	店铺oid
                        map.put("logoImage", chaoshi_list.get(i).getLogoImage()); //店铺图片
                        map.put("name", chaoshi_list.get(i).getName()); //店铺名称
                        map.put("evaluateIdstarlevel", chaoshi_list.get(i).getEvaluateIdstarlevel()); //=店铺星级
                        map.put("sellnumber", chaoshi_list.get(i).getSellnumber()); //月售
                        map.put("address", chaoshi_list.get(i).getAddress()); //地址
                        map.put("jvLi", chaoshi_list.get(i).getJvLi()); //距离
                        */

/*        ImageView public_image; //公共显示图片
        TextView tv_takeout_name; //超市名称
        RatingBar ratingBar;//超市星级
        TextView evaluateIdstarlevel_and_sellnumber;    //星级和月售
        TextView tv_takeout_address;    //超市地址
        TextView tv_takeout_juli;   //超市距离*/

        String logoImage =  list.get(position).get("logoImage")+"";//公共显示图片
        String name =  list.get(position).get("name")+""; //超市名称
        String evaluateIdstarlevel =  list.get(position).get("evaluateIdstarlevel")+"";//=店铺星级
        Float float_evaluateIdstarlevel=0f;
        if (!StringUtil.isSpace(evaluateIdstarlevel)) {
            float_evaluateIdstarlevel = Float.parseFloat(evaluateIdstarlevel);
        }
        String sellnumber = list.get(position).get("sellnumber")+""; //月售
        String address =  list.get(position).get("address")+"";//地址
        String jvLi =  list.get(position).get("jvLi")+"";//距离


        //holder.tv_takeout_name.setText((String) list.get(position).get("name"));
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(UriUtil.ip + logoImage, holder.public_image, options);

        holder.tv_takeout_name.setText(name);
        holder.ratingBar.setRating(float_evaluateIdstarlevel);
        holder.evaluateIdstarlevel_and_sellnumber.setText("  " + evaluateIdstarlevel + "  " + sellnumber);
        holder.tv_takeout_address.setText(address);
        holder.tv_takeout_juli.setText(jvLi);


        String TuPian =  list.get(position).get("TuPian")+"";
        //boolean WaiMai = (boolean) list.get(position).get("WaiMai");
        //1.不显示图片 2.显示图片
        if (TuPian.equals("1")) {
            holder.item_linear.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        } else {
            holder.item_linear.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);


/*            ImageView img_takeout_projectImage_one,img_takeout_projectImage_two,img_takeout_projectImage_there; //商品图片
            TextView tv_takeout_projectName_one,tv_takeout_projectName_two,tv_takeout_projectName_there;    //商品名称
            TextView tv_takeout_loanAmounts_one,tv_takeout_loanAmounts_two,tv_takeout_loanAmounts_there;    //商品价格*/

            String projectImage0 =  list.get(position).get("projectImage0")+"";
            String projectImage1 =  list.get(position).get("projectImage1")+"";
            String projectImage2 =  list.get(position).get("projectImage2")+"";

            String projectName0 =  list.get(position).get("projectName0")+"";
            String projectName1 =  list.get(position).get("projectName1")+"";
            String projectName2 =  list.get(position).get("projectName2")+"";

            String loanAmounts0 =  list.get(position).get("loanAmounts0")+"";
            String loanAmounts1 =  list.get(position).get("loanAmounts1")+"";
            String loanAmounts2 =  list.get(position).get("loanAmounts2")+"";





            holder.tv_takeout_projectName_one.setText(projectName0);
            holder.tv_takeout_projectName_two.setText(projectName1);
            holder.tv_takeout_projectName_there.setText(projectName2);



            holder.tv_takeout_loanAmounts_one.setText(loanAmounts0);
            holder.tv_takeout_loanAmounts_two.setText(loanAmounts1);
            holder.tv_takeout_loanAmounts_there.setText(loanAmounts2);



            ImageLoader.getInstance().displayImage(UriUtil.ip + projectImage0, holder.img_takeout_projectImage_one, options);
            ImageLoader.getInstance().displayImage(UriUtil.ip + projectImage1, holder.img_takeout_projectImage_two, options);
            ImageLoader.getInstance().displayImage(UriUtil.ip + projectImage2, holder.img_takeout_projectImage_there, options);

            //Log.i("147",projectName3);

        }
        //true.外卖页面 false.团购页面
/*        if (WaiMai) {
            holder.ll_img_WaiMai.setVisibility(View.GONE);
            holder.ll_tv_Juan.setVisibility(View.VISIBLE);
        } else {
            holder.ll_img_WaiMai.setVisibility(View.VISIBLE);
            holder.ll_tv_Juan.setVisibility(View.GONE);
        }*/
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
        LinearLayout item_linear;   //显示图片布局
        /*        LinearLayout ll_img_WaiMai, ll_tv_Juan;*/
        View view;//显示和图片的分割线

        ImageView public_image; //公共显示图片
        TextView tv_takeout_name; //超市名称
        RatingBar ratingBar;//超市星级
        TextView evaluateIdstarlevel_and_sellnumber;    //星级和月售
        TextView tv_takeout_address;    //超市地址
        TextView tv_takeout_juli;   //超市距离

        ImageView img_takeout_projectImage_one, img_takeout_projectImage_two, img_takeout_projectImage_there; //商品图片
        TextView tv_takeout_projectName_one, tv_takeout_projectName_two, tv_takeout_projectName_there;    //商品名称
        TextView tv_takeout_loanAmounts_one, tv_takeout_loanAmounts_two, tv_takeout_loanAmounts_there;    //商品价格

        public MyViewHolder(View itemView) {
            super(itemView);
            public_image = (ImageView) itemView.findViewById(R.id.public_image);   //公共显示图片
            tv_takeout_name = (TextView) itemView.findViewById(R.id.tv_takeout_name);   //超市名称
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);   //超市星级
            evaluateIdstarlevel_and_sellnumber = (TextView) itemView.findViewById(R.id.evaluateIdstarlevel_and_sellnumber);   //星级和月售
            tv_takeout_address = (TextView) itemView.findViewById(R.id.tv_takeout_address);   //超市地址
            tv_takeout_juli = (TextView) itemView.findViewById(R.id.tv_takeout_juli);   //超市距离

            view = itemView.findViewById(R.id.view);    //显示和图片的分割线
            item_linear = (LinearLayout) itemView.findViewById(R.id.item_linear);   //显示图片布局
/*            ll_img_WaiMai = (LinearLayout) itemView.findViewById(R.id.ll_img_WaiMai);
            ll_tv_Juan = (LinearLayout) itemView.findViewById(R.id.ll_tv_Juan);*/

            img_takeout_projectImage_one = (ImageView) itemView.findViewById(R.id.img_takeout_projectImage_one);
            img_takeout_projectImage_two = (ImageView) itemView.findViewById(R.id.img_takeout_projectImage_two);
            img_takeout_projectImage_there = (ImageView) itemView.findViewById(R.id.img_takeout_projectImage_there);

            tv_takeout_projectName_one = (TextView) itemView.findViewById(R.id.tv_takeout_projectName_one);
            tv_takeout_projectName_two = (TextView) itemView.findViewById(R.id.tv_takeout_projectName_two);
            tv_takeout_projectName_there = (TextView) itemView.findViewById(R.id.tv_takeout_projectName_there);

            tv_takeout_loanAmounts_one = (TextView) itemView.findViewById(R.id.tv_takeout_loanAmounts_one);
            tv_takeout_loanAmounts_two = (TextView) itemView.findViewById(R.id.tv_takeout_loanAmounts_two);
            tv_takeout_loanAmounts_there = (TextView) itemView.findViewById(R.id.tv_takeout_loanAmounts_there);


        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
