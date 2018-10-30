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
 * Created by Administrator on 2017/12/16.
 */

public class MyPostedAdapter extends RecyclerView.Adapter<MyPostedAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public MyPostedAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_myposted, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //holder.tv_item_myPosted_time.setText((String) list.get(position).get("tiem"));

        String articleId = (String)list.get(position).get("articleId");
        String title = (String)list.get(position).get("title");
        String content = (String)list.get(position).get("content");
        String publishdate = (String)list.get(position).get("publishdate");
        String likenumber = (String)list.get(position).get("likenumber");
        String commentnumber = (String)list.get(position).get("commentnumber");

        holder.tv_item_myPosted_time.setText(publishdate);
        holder.tv_item_myPosted_title.setText(title);
        holder.tv_item_myPosted_content.setText(content);
        holder.tv_item_myPosted_likenumber.setText(likenumber);
        holder.tv_item_myPosted_commentnumber.setText(commentnumber);




/*        //时间、标题、内容、点赞数量、评论数量
        TextView tv_item_myPosted_time,tv_item_myPosted_title,tv_item_myPosted_content,tv_item_myPosted_likenumber,tv_item_myPosted_commentnumber;*/

/*        map.put("articleId", my_posted.get(i).getArticleId()); //	文章ID;
        map.put("title", my_posted.get(i).getTitle()); //	文章标题;
        map.put("content", my_posted.get(i).getContent()); //	文章内容;
        map.put("publishdate", my_posted.get(i).getPublishdate()); //	文章发表时间;
        map.put("likenumber", my_posted.get(i).getLikenumber()); //	文章点赞数量;
        map.put("commentnumber", my_posted.get(i).getCommentnumber()); //	文章评论数量;*/



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
        //时间、标题、内容、点赞数量、评论数量
        TextView tv_item_myPosted_time,tv_item_myPosted_title,tv_item_myPosted_content,tv_item_myPosted_likenumber,tv_item_myPosted_commentnumber;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_myPosted_time = (TextView) itemView.findViewById(R.id.tv_item_myPosted_time);
            tv_item_myPosted_title = (TextView) itemView.findViewById(R.id.tv_item_myPosted_title);
            tv_item_myPosted_content = (TextView) itemView.findViewById(R.id.tv_item_myPosted_content);
            tv_item_myPosted_likenumber = (TextView) itemView.findViewById(R.id.tv_item_myPosted_likenumber);
            tv_item_myPosted_commentnumber = (TextView) itemView.findViewById(R.id.tv_item_myPosted_commentnumber);

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
