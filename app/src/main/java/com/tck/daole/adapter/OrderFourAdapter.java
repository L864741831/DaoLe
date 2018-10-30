package com.tck.daole.adapter;

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

public class OrderFourAdapter extends RecyclerView.Adapter<OrderFourAdapter.MyViewHolder> {
    private List<OrderList.Shop> shop_list;
    private Context context;

    public OrderFourAdapter(List<OrderList.Shop> shop_list, Context context) {
        this.shop_list = shop_list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_shop, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        //shop_list.get(position).TakeOutOrderProject;

        OrderContentAdapter adapter = new OrderContentAdapter(shop_list.get(position).TakeOutOrderProject, context);
        holder.rv_shop_content.setAdapter(adapter);



/*        holder.tv_news_title.setText((String) list.get(position).get("title"));
        holder.tv_news_text.setText((String) list.get(position).get("text"));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });*/


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
        /*        TextView tv_news_title,tv_news_text;*/
        NiceRecyclerView rv_shop_content;

        public MyViewHolder(View itemView) {
            super(itemView);
/*            tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
            tv_news_text = (TextView) itemView.findViewById(R.id.tv_news_text);*/
            rv_shop_content = (NiceRecyclerView) itemView.findViewById(R.id.rv_shop_content);

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
