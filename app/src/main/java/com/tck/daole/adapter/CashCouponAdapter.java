package com.tck.daole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tck.daole.R;

import java.util.List;
import java.util.Map;

/**
 * 代金券适配器.
 */

public class CashCouponAdapter extends RecyclerView.Adapter<CashCouponAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public CashCouponAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cash_coupon, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_cash_jine.setText((String) list.get(position).get("money"));   //金额
        holder.tv_cash_manjian.setText("满"+(String) list.get(position).get("fullCutMoney")+"可用");    //满减金额
        holder.tv_cash_mingcheng.setText((String) list.get(position).get("cpName"));  //名称
        holder.tv_cash_zhuanxiang.setText((String) list.get(position).get("cpDescribe")); //专享
        holder.tv_cash_youxiaoqi.setText((String) list.get(position).get("effectiveTime"));  //有效期
        holder.tv_cash_jine_two.setText((String) list.get(position).get("money"));   //金额
        holder.tv_cash_manjian_two.setText("满"+(String) list.get(position).get("fullCutMoney")+"可用");  //满减金额




        //金额、满减、名称、专享、有效期、金额2、有效期2
        //TextView tv_cash_jine,tv_cash_manjian,tv_cash_mingcheng,tv_cash_zhuanxiang,tv_cash_youxiaoqi,tv_cash_jine_two,tv_cash_youxiaoqi_two;

/*        map1.put("couponId", coupon_list.get(i).getCouponId()); //	優惠券couponId
        map1.put("cpName", coupon_list.get(i).getCpName()); //	優惠券名稱
        map1.put("cpDescribe", coupon_list.get(i).getCpDescribe()); //	描述
        map1.put("money", coupon_list.get(i).getMoney()); //	抵扣金額
        map1.put("fullCutMoney", coupon_list.get(i).getFullCutMoney()); //	滿減金額
        map1.put("status", coupon_list.get(i).getStatus()); //	登录状态
        map1.put("effectiveTime", coupon_list.get(i).getEffectiveTime()); //	有效期*/



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

        //金额、满减、名称、专享、有效期、金额2、有效期2
        TextView tv_cash_jine,tv_cash_manjian,tv_cash_mingcheng,tv_cash_zhuanxiang,tv_cash_youxiaoqi,tv_cash_jine_two,tv_cash_manjian_two;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_cash_jine = (TextView) itemView.findViewById(R.id.tv_cash_jine);
            tv_cash_manjian = (TextView) itemView.findViewById(R.id.tv_cash_manjian);
            tv_cash_mingcheng = (TextView) itemView.findViewById(R.id.tv_cash_mingcheng);
            tv_cash_zhuanxiang = (TextView) itemView.findViewById(R.id.tv_cash_zhuanxiang);
            tv_cash_youxiaoqi = (TextView) itemView.findViewById(R.id.tv_cash_youxiaoqi);
            tv_cash_jine_two = (TextView) itemView.findViewById(R.id.tv_cash_jine_two);
            tv_cash_manjian_two = (TextView) itemView.findViewById(R.id.tv_cash_manjian_two);

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
