package com.tck.daole.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.activity.AddAdressActivity;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.UriUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 地址管理适配器
 */

public class AddressAdminAdapter extends RecyclerView.Adapter<AddressAdminAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    private int currentCheck = -1;

    public void setCurrentCheck(int currentCheck) {
        this.currentCheck = currentCheck;
    }

    public int getCurrentCheck() {
        return currentCheck;
    }

    public AddressAdminAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //final String state = list.get(position).get("state");
        final String people = (String) list.get(position).get("people");    //联系人名字
        final String phone = (String) list.get(position).get("phone");  //联系人电话
        final String province = (String) list.get(position).get("province");    //省
        final String city = (String) list.get(position).get("city");    //市
        final String district = (String) list.get(position).get("district");    //区
        final String street = (String) list.get(position).get("street");    //街道
        final String address = (String) list.get(position).get("address");  //地址
        final String defaultAddress = (String) list.get(position).get("defaultAddress");  //是否地址 0是 1不是


        holder.address_list_name.setText(people);
        holder.address_list_phone.setText(phone);
        holder.address_list_address.setText(province + city + district + street + address);

        /*
     memberId=用户ID
 	 nickName=用户呢称
 	 addressId=地址ID
 	 people=地址联系人
 	 phone=联系人电话

 	 province=联系人省
 	 city=联系人市
 	 district=联系人区
 	 street=联系人街道
 	 address=联系人地址

 	 defaultAddress=是否为默认地址 0是 1不是

                */



/*        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position);
            }
        });*/


        final String token = (String) list.get(position).get("token");  //token用于修改默认地址
        final String addressId = (String) list.get(position).get("addressId"); //	地址ID
        final String memberId = (String) list.get(position).get("memberId");  //	用户ID


        //在adapter跳转activity
        //编辑地址
        holder.tv_bianji_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);//或者使用Activity.startActivity(intent)
                intent.putExtra("addresstype", "modifyaddress");

                intent.putExtra("addressoId", addressId);
                intent.putExtra("people", people);
                intent.putExtra("phone", phone);
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("district", district);
                intent.putExtra("street", street);
                intent.putExtra("address", address);


                intent.setClass(context, AddAdressActivity.class);
                context.startActivity(intent);


                //关闭adapter所在activity
                if (Activity.class.isInstance(context)) {
                    //关闭adapter所在activity
                    Activity activity = (Activity) context;
                    activity.finish();
                }

            }
        });

/**
 * 列表中redio单选
 */
        boolean check = false;
        if (list.get(position).get("check") != null) {
            check = (boolean) list.get(position).get("check");
        }
        holder.address_check_moren.setChecked(check);

        if (holder.address_check_moren.isChecked()) {
            holder.address_check_moren.setClickable(false);
            holder.address_check_moren.setEnabled(false);
        } else {
            holder.address_check_moren.setClickable(true);
            holder.address_check_moren.setEnabled(true);
        }

        //直接在adapter单独设置控件点击事件
        holder.address_check_moren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("=======", "ischeck");

                //设置默认地址
                setDefaultAddress(token, addressId, memberId);

                boolean isChecked = (boolean) list.get(position).get("check");
                clearCheck();
                list.get(position).put("check", !isChecked);
                notifyDataSetChanged();
                if (isChecked) {//选中变未选中
                    currentCheck = -1;
                } else {//未选中变选中
                    currentCheck = position;
                }


            }
        });


        //删除收货地址
        holder.tv_delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                if(defaultAddress.equals("1")||position!=0){
                    list.get(position-1).put("defaultAddress", "1");
                    notifyDataSetChanged();
                }*/

                deleteAddress(token, addressId);
                list.remove(position);
                notifyDataSetChanged();

            }
        });


        holder.ll_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,"返回地址",Toast.LENGTH_SHORT).show();


/*                final String people = (String) list.get(position).get("people");    //联系人名字
                final String phone = (String) list.get(position).get("phone");  //联系人电话
                final String province = (String) list.get(position).get("province");    //省
                final String city = (String) list.get(position).get("city");    //市
                final String district = (String) list.get(position).get("district");    //区
                final String street = (String) list.get(position).get("street");    //街道
                final String address = (String) list.get(position).get("address");  //地址
                final String defaultAddress = (String) list.get(position).get("defaultAddress");  //是否地址 0是 1不是*/




                //关闭adapter所在activity
                if (Activity.class.isInstance(context)) {
                    //关闭adapter所在activity
                    Activity activity = (Activity) context;

                    //返回结果
                    Intent i = new Intent();
                    i.putExtra("addressId", addressId);
                    i.putExtra("people", people);
                    i.putExtra("phone", phone);
                    i.putExtra("province", province);
                    i.putExtra("city", city);
                    i.putExtra("district", district);
                    i.putExtra("street", street);
                    i.putExtra("address", address);

                    activity.setResult(4, i);
                    activity.finish();
                }


            }

/*                Intent intent = new
                        Intent(context,SpellOrderActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("intentaddress","addressadapter");
                context.startActivity(intent);

                //关闭adapter所在activity
                if(Activity.class.isInstance(context))
                {
                    //关闭adapter所在activity
                    Activity activity = (Activity)context;
                    activity.finish();
                }
            }*/


        });


    }

    public void clearCheck() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("check", false);
        }
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
        //LinearLayout ll_index_isDpCs;
        TextView address_list_name, address_list_phone, address_list_address;//姓名、电话、地址
        CheckBox address_check_moren;
        TextView tv_delete_address, tv_bianji_address; //删除地址\编辑地址

        LinearLayout ll_select_address;


        public MyViewHolder(View itemView) {
            super(itemView);
            //ll_index_isDpCs = (LinearLayout) itemView.findViewById(R.id.ll_index_isDpCs);
            address_list_name = (TextView) itemView.findViewById(R.id.address_list_name);
            address_check_moren = (CheckBox) itemView.findViewById(R.id.address_check_moren);
            address_list_phone = (TextView) itemView.findViewById(R.id.address_list_phone);
            address_list_address = (TextView) itemView.findViewById(R.id.address_list_address);
            tv_delete_address = (TextView) itemView.findViewById(R.id.tv_delete_address);
            tv_bianji_address = (TextView) itemView.findViewById(R.id.tv_bianji_address);

            ll_select_address = (LinearLayout) itemView.findViewById(R.id.ll_select_address);


        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * @param token
     * @param addressOid
     * @param memberOid  设置默认地址
     */
    public void setDefaultAddress(final String token, final String addressOid, final String memberOid) {
        /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("address.oid", addressOid);
        map.put("address.member.oid", memberOid);

        HttpUtils.doPost(UriUtil.default_address, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("55555", token + "\n" + addressOid + "\n" + memberOid);
                Log.i("55555", response.body().string());
            }
        });
    }


    /**
     * 删除地址
     */
    public void deleteAddress(final String token, final String addressOid) {
        /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("address.oid", addressOid);

        HttpUtils.doPost(UriUtil.delete_address, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("55555", response.body().string());
            }
        });
    }

}
