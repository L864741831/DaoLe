package com.tck.daole.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.entity.OrderList;
import com.tck.daole.entity.StoreInfo;
import com.tck.daole.view.NiceRecyclerView;

import java.util.List;
import java.util.Map;

/**
 * 订单页适配器
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<OrderList.Shop> shop_list;
    private Context context;

    public OrderAdapter(List<OrderList.Shop> shop_list, Context context) {
        this.shop_list = shop_list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_shop, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        List<OrderList.TakeOutOrderProject> projects=shop_list.get(position).TakeOutOrderProject;
        OrderContentAdapter adapter = new OrderContentAdapter(projects, context);
        holder.rv_shop_content.setAdapter(adapter);
//        holder.tv_order_shop_delete.setVisibility(View.GONE);
//        holder.tv_order_shop_again.setVisibility(View.GONE);
        holder.tv_order_shop_name.setText(shop_list.get(position).adminStoreName);

        int count=0;
        for (int i = 0; i < projects.size(); i++) {
            count+=projects.get(i).orderProjectProjectNum;
        }
        //共1件商品 合计:¥110.00 (含运费¥10.00)
        //订单状态： 1待付款 2待接单 3待送达 4已送达 5退款退货 6订单取消
        //商品分类：1.乐拼购商品 2.景区订票(发短信) 3.全球签证办理 4.美食团购(发短信) 5.外卖商品
        holder.tv_order_shop_count_price.setText("共"+count+"件商品 合计:¥"+shop_list.get(position).payMoney+"(含运费¥"+shop_list.get(position).servicePrice+")");
        int state=Integer.parseInt(shop_list.get(position).orderState);
        int type=shop_list.get(position).orderType;
        switch (state){
            case 1:
                holder.tv_order_shop_status.setText("未付款");
                holder.tv_order_shop_delete.setText("取消订单");
                holder.tv_order_shop_delete.setVisibility(View.VISIBLE);
                holder.tv_order_shop_ok.setVisibility(View.VISIBLE);
                holder.tv_order_shop_ok.setText("去付款");
                break;
            case 2:
                if (type==5) {
                    holder.tv_order_shop_status.setText("待送货");
                    holder.tv_order_shop_delete.setText("取消订单");
                    holder.tv_order_shop_delete.setVisibility(View.VISIBLE);
                    holder.tv_order_shop_ok.setText("");
                    holder.tv_order_shop_ok.setVisibility(View.GONE);
                }else if (type==1||type==3){
                    holder.tv_order_shop_status.setText("待平台发货");
                    holder.tv_order_shop_delete.setText("取消订单");
                    holder.tv_order_shop_delete.setVisibility(View.VISIBLE);
                    holder.tv_order_shop_ok.setText("");
                    holder.tv_order_shop_ok.setVisibility(View.GONE);
                }else if (type==2){
                    holder.tv_order_shop_status.setText("待验票");
                    holder.tv_order_shop_delete.setText("");
                    holder.tv_order_shop_delete.setVisibility(View.GONE);
                    holder.tv_order_shop_ok.setText("");
                    holder.tv_order_shop_ok.setVisibility(View.GONE);
                }else if (type==4){
                    holder.tv_order_shop_status.setText("待消费");
                    holder.tv_order_shop_delete.setText("");
                    holder.tv_order_shop_delete.setVisibility(View.GONE);
                    holder.tv_order_shop_ok.setText("");
                    holder.tv_order_shop_ok.setVisibility(View.GONE);
                }
                break;
            case 3:
                holder.tv_order_shop_status.setText("送货中");
                holder.tv_order_shop_delete.setText("催单");
                holder.tv_order_shop_delete.setVisibility(View.VISIBLE);
                holder.tv_order_shop_ok.setText("确认送达");
                holder.tv_order_shop_ok.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.tv_order_shop_status.setText("已送达");
                holder.tv_order_shop_delete.setText("退款退货");
                holder.tv_order_shop_delete.setVisibility(View.VISIBLE);
                holder.tv_order_shop_ok.setText("");
                holder.tv_order_shop_ok.setVisibility(View.GONE);
                break;
            case 5:
                holder.tv_order_shop_status.setText("退货退款");
                holder.tv_order_shop_delete.setText("");
                holder.tv_order_shop_ok.setText("");
                holder.tv_order_shop_delete.setVisibility(View.GONE);
                holder.tv_order_shop_ok.setVisibility(View.GONE);
                break;
            case 6:
                holder.tv_order_shop_status.setText("订单取消");
                holder.tv_order_shop_delete.setText("");
                holder.tv_order_shop_delete.setVisibility(View.GONE);
                holder.tv_order_shop_ok.setText("");
                holder.tv_order_shop_ok.setVisibility(View.GONE);
                break;
        }

        adapter.setOnItemClickListener(new OrderContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int projectPosition) {
                onItemClickListener.onProjectItemClick(position,projectPosition);
            }
        });

//        if (shop_list.get(position).orderType==5) {
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
//        }
        holder.tv_order_shop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onJuJueClick(position);
            }
        });
        holder.tv_order_shop_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onJieShouClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (shop_list == null) {
            return 0;
        } else {
            return shop_list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        NiceRecyclerView rv_shop_content;
        View content;

        TextView tv_order_shop_name,tv_order_shop_status;
        TextView tv_order_shop_count_price;
        TextView tv_order_shop_delete,tv_order_shop_exit,tv_order_shop_again,tv_order_shop_ok;

        public MyViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            rv_shop_content = (NiceRecyclerView) itemView.findViewById(R.id.rv_shop_content);

            tv_order_shop_name = (TextView) itemView.findViewById(R.id.tv_order_shop_name);
            tv_order_shop_status = (TextView) itemView.findViewById(R.id.tv_order_shop_status);
            tv_order_shop_count_price = (TextView) itemView.findViewById(R.id.tv_order_shop_count_price);
            tv_order_shop_delete = (TextView) itemView.findViewById(R.id.tv_order_shop_delete);
            tv_order_shop_exit = (TextView) itemView.findViewById(R.id.tv_order_shop_exit);
            tv_order_shop_again = (TextView) itemView.findViewById(R.id.tv_order_shop_again);
            tv_order_shop_ok = (TextView) itemView.findViewById(R.id.tv_order_shop_ok);



        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onProjectItemClick(int position,int projectPosition);
        void onJuJueClick(int position);
        void onJieShouClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
