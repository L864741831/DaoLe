package com.tck.daole.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.activity.LoginActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.StringUtil;
import com.tck.daole.util.UriUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * kylin on 2017/12/15.
 */

public class ShopTypeAdapter extends RecyclerView.Adapter<ShopTypeAdapter.MyViewHloder> {
    private List<Map<Object, String>> list;
    private Context context;
//    private String sp_token = "";

    Handler hd;

    public ShopTypeAdapter(List<Map<Object, String>> list, Context context,Handler hd) {
        this.list = list;
        this.context = context;
        this.hd = hd;
    }

    @Override
    public MyViewHloder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_type, parent, false);

        return new MyViewHloder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHloder holder, final int position) {


        //holder.tv_item_shop_name.setText(list.get(position).get("name"));

/*        map.put("takeProjectId", shop_type_list.get(i).getTakeProjectId()); //	商品ID

        map.put("name", shop_type_list.get(i).getName()); //商品名字
        map.put("detail", shop_type_list.get(i).getDetail()); //商品详情
        map.put("saleNum", shop_type_list.get(i).getSaleNum()); //商品销量
        map.put("price", shop_type_list.get(i).getPrice()); //商品单价
        map.put("projectImage", shop_type_list.get(i).getProjectImage()); //商品分类图片*/


/*        ImageView img_shop_content_projectImage;    //商品图片
        TextView tv_shop_content_name,tv_shop_content_detail,tv_shop_content_saleNum,tv_shop_content_price;//名称，介绍，月售，价钱*/

        String takeProjectId = list.get(position).get("takeProjectId"); //商品id
        String num = list.get(position).get("cartNum"); //商品已选数量
        if (StringUtil.isSpace(num)) {
            num = "0";
        }
        String projectImage = list.get(position).get("projectImage");
        String name = list.get(position).get("name");
        String detail = list.get(position).get("detail");
        String saleNum = list.get(position).get("saleNum");
        String price = list.get(position).get("price");
        String title = list.get(position).get("title");



        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                .showImageOnFail(R.mipmap.def)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip + projectImage, holder.img_shop_content_projectImage, options);

        holder.tv_shop_content_name.setText(name);
        holder.tv_shop_content_detail.setText(title);
        holder.tv_shop_content_saleNum.setText(saleNum);
        holder.tv_shop_content_price.setText(price);
        holder.tv_shop_content_num.setText(num);

        //int int_sum = Integer.parseInt(num);

        holder.tv_shop_content_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_sum = holder.tv_shop_content_num.getText().toString();
                int int_sum = Integer.parseInt(str_sum);
                if (int_sum == 0) {
                    int_sum = 0;
                } else {
                    int_sum = int_sum - 1;


                    String take_Project_Id = list.get(position).get("takeProjectId");
                    if (App.islogin) {
                        //Toast.makeText(context,"加一个商品",Toast.LENGTH_SHORT).show();
                        deleteShop(App.token, take_Project_Id, "1");
                        //addShop(sp_token,"1","1");
                        //Log.i("ShopTypeAdapter",take_Project_Id);

                        Bundle b = new Bundle();
                        b.putString("count_price", list.get(position).get("price"));
                        Message msg = new Message();
                        msg.setData(b);
                        msg.what = Constant.Jian_Count_Price;
                        hd.sendMessage(msg);

                    } else {
                        Intent intent = new
                                Intent(context, LoginActivity.class);
                        //在Intent对象当中添加一个键值对
                        //intent.putExtra("key","value");
                        context.startActivity(intent);
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                    }



                }
                holder.tv_shop_content_num.setText(String.valueOf(int_sum));

//                sp_token = SPUtil.getData(context, "token",
//                        "").toString();




            }
        });

        holder.tv_shop_content_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_sum = holder.tv_shop_content_num.getText().toString();
                int int_sum = Integer.parseInt(str_sum);
                int_sum = int_sum + 1;
                holder.tv_shop_content_num.setText(String.valueOf(int_sum));


//                sp_token = SPUtil.getData(context, "token",
//                        "").toString();
                //String shop_type_id = list.get(position).get("adminsStoreId"); //商品id

                String take_Project_Id = list.get(position).get("takeProjectId");
                if (App.islogin) {
                    //Toast.makeText(context,"加一个商品",Toast.LENGTH_SHORT).show();
                    addShop(App.token, take_Project_Id, "1");


                    Bundle b = new Bundle();
                    b.putString("count_price", list.get(position).get("price"));
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = Constant.Add_Count_Price;
                    hd.sendMessage(msg);


                    //addShop(sp_token,"1","1");
                    //Log.i("ShopTypeAdapter",take_Project_Id);
                } else {
                    Intent intent = new
                            Intent(context, LoginActivity.class);
                    //在Intent对象当中添加一个键值对
                    //intent.putExtra("key","value");
                    context.startActivity(intent);
                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
        //TextView tv_item_shop_name, tv_shop_num;
        ImageView img_shop_content_projectImage, tv_shop_content_jian, tv_shop_content_jia;    //商品图片、加、减
        TextView tv_shop_content_name, tv_shop_content_detail, tv_shop_content_saleNum, tv_shop_content_price, tv_shop_content_num;//名称，介绍，月售，价钱


        public MyViewHloder(View itemView) {
            super(itemView);
            //tv_item_shop_name = (TextView) itemView.findViewById(R.id.tv_item_shop_name);
            img_shop_content_projectImage = (ImageView) itemView.findViewById(R.id.img_shop_content_projectImage);
            tv_shop_content_jian = (ImageView) itemView.findViewById(R.id.tv_shop_content_jian);
            tv_shop_content_jia = (ImageView) itemView.findViewById(R.id.tv_shop_content_jia);


            tv_shop_content_name = (TextView) itemView.findViewById(R.id.tv_shop_content_name);
            tv_shop_content_detail = (TextView) itemView.findViewById(R.id.tv_shop_content_detail);
            tv_shop_content_saleNum = (TextView) itemView.findViewById(R.id.tv_shop_content_saleNum);
            tv_shop_content_price = (TextView) itemView.findViewById(R.id.tv_shop_content_price);
            tv_shop_content_num = (TextView) itemView.findViewById(R.id.tv_shop_content_num);

        }
    }


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //添加商品
    public void addShop(final String token, final String take_Project_Id, final String num) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                //Thread.sleep(3000);//休眠3秒
                /**
                 * 要执行的操作
                 */

                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("commodityOid", take_Project_Id);
                map.put("shoppingCart.num", num);


                HttpUtils.doPost(UriUtil.Add_Type_Shop, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.i("ShopTypeAdapter",response.body().string());

                        Bean data = new Gson().fromJson(response.body().string(),
                                Bean.class);
                        int status = data.getStatus();
                        if (status == 9) {
                            Log.i("ShopTypeAdapter", data.getMessage());
                        } else {
                            Log.i("ShopTypeAdapter", data.getMessage());
                        }
                    }
                });

            }
        }.start();

    }


    //减少商品
    public void deleteShop(final String token, final String take_Project_Id, final String num) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                //Thread.sleep(3000);//休眠3秒
                /**
                 * 要执行的操作
                 */

                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("commodityOid", take_Project_Id);
                map.put("shoppingCart.num", num);


                HttpUtils.doPost(UriUtil.Delete_Type_Shop, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.i("ShopTypeAdapter",response.body().string());

                        Bean data = new Gson().fromJson(response.body().string(),
                                Bean.class);
                        int status = data.getStatus();
                        if (status == 9) {
                            Log.i("ShopTypeAdapter", data.getMessage());
                        } else {
                            Log.i("ShopTypeAdapter", data.getMessage());
                        }
                    }
                });

            }
        }.start();

    }


}
