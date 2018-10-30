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
 * 同城列表适配器
 */

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public OnlineAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_online_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //holder.ll_index_isDpCs.setVisibility(View.VISIBLE);

        //标题、发布时间、阅读量、点赞量、评论量
        //TextView tv_online_title,tv_online_publishdate,tv_online_pageview,tv_online_likenumber,tv_online_commentnumber;

 /*       map.put("articleId", online_list.get(i).getArticleId()); //	=文章ID;
        map.put("title", online_list.get(i).getTitle()); //	=文章标题;
        map.put("content", online_list.get(i).getContent()); //=文章内容;
        map.put("publishdate", online_list.get(i).getPublishdate()); //	=文章发表时间;
        map.put("likenumber", online_list.get(i).getLikenumber()); //	=文章点赞数量;
        map.put("likeStatus", online_list.get(i).getLikeStatus()); //	=是否点赞.1点过赞，0没有点过赞;
        map.put("articletype", online_list.get(i).getArticletype()); //=文章类型：1招聘求职 2二手车辆 3二手交易 4家政服务 5房产交易 6更多选择 7旅游攻略 8论坛贴吧 9自驾游召集 10生活问答 11技巧分享 12唠一唠;
        map.put("pageview", online_list.get(i).getPageview()); //	=文章浏览数量;
        map.put("commentnumber", online_list.get(i).getCommentnumber()); //	=文章评论数量;*/

        String title = (String)list.get(position).get("title");
        String publishdate = (String)list.get(position).get("publishdate");
        int pageview = (int)list.get(position).get("pageview");
        int likenumber = (int)list.get(position).get("likenumber");
        int commentnumber =(int) list.get(position).get("commentnumber");

        holder.tv_online_title.setText(title);
        holder.tv_online_publishdate.setText(publishdate);
        holder.tv_online_pageview.setText(pageview+"人看过");
        holder.tv_online_likenumber.setText(likenumber+"");
        holder.tv_online_commentnumber.setText(commentnumber+"");



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position);
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
        //TextView tv_content_index;\
        //标题、发布时间、阅读量、点赞量、评论量
        TextView tv_online_title,tv_online_publishdate,tv_online_pageview,tv_online_likenumber,tv_online_commentnumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            //ll_index_isDpCs = (LinearLayout) itemView.findViewById(R.id.ll_index_isDpCs);
            tv_online_title = (TextView) itemView.findViewById(R.id.tv_online_title);
            tv_online_publishdate = (TextView) itemView.findViewById(R.id.tv_online_publishdate);
            tv_online_pageview = (TextView) itemView.findViewById(R.id.tv_online_pageview);
            tv_online_likenumber = (TextView) itemView.findViewById(R.id.tv_online_likenumber);
            tv_online_commentnumber = (TextView) itemView.findViewById(R.id.tv_online_commentnumber);



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
