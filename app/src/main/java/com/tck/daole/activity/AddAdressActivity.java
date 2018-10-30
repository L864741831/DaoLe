package com.tck.daole.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.AddressBean;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.DataHelper;
import com.tck.daole.thread.PickAddressDialog;
import com.tck.daole.thread.PickAddressInterface;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.StringUtil;
import com.tck.daole.util.UriUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AddAdressActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RelativeLayout rl_diqu, rl_jiedao;
    private TextView tv_diqu, tv_jiedao, address_title;

    private Button btn_add_address;
    private EditText et_address_people, et_address_phone, et_address_xiangxi_address;


    private List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();
    private List<AddressBean> addressBeen;

//    private String sp_token = "";

    private String addresstype = "";

    private String addressoId = "1";

    private String  people, phone, address = "";   //信息   状态 0正常 9删除、收货人、电话、、详细地址、

    private int status = 1;
    //省、市、区、街道
    private String province = "河南省";
    private String city = "郑州市";
    private String district = "中原区";
    private String street = "秦岭路街道";

    private String statu = "";
    private String message = "";

    Handler hd;

    private String btn_type = "0";  //0为修改地址 1为添加地址

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_adress);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            addresstype = intent.getStringExtra("addresstype");
        }

        findViewId();


        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.Add_Address:
                        Toast.makeText(AddAdressActivity.this, "添加新地址成功", Toast.LENGTH_SHORT).show();
                        ;
                        startActivity(new Intent(AddAdressActivity.this, AddressCollectionActivity.class));//添加地址
                        finish();
                        break;
                    case Constant.Modify_Address:
                        Toast.makeText(AddAdressActivity.this, "修改地址成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddAdressActivity.this, AddressCollectionActivity.class));//修改地址
                        finish();
                        break;



                }

            }
        };


    }

    public void findViewId() {
        ll_back = findView(R.id.ll_back);
        rl_diqu = findView(R.id.rl_diqu);
        rl_jiedao = findView(R.id.rl_jiedao);
        tv_diqu = findView(R.id.tv_diqu);
        tv_jiedao = findView(R.id.tv_jiedao);
        address_title = findView(R.id.address_title);
        btn_add_address = findView(R.id.btn_add_address);

        et_address_people = findView(R.id.et_address_people);
        et_address_phone = findView(R.id.et_address_phone);
        et_address_xiangxi_address = findView(R.id.et_address_xiangxi_address);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        rl_diqu.setOnClickListener(this);
        rl_jiedao.setOnClickListener(this);
        btn_add_address.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(AddAdressActivity.this, "token", "").toString();

        if (addresstype.equals("addaddress")) {
            address_title.setText("添加新地址");

            btn_type = "1";

        }
        if (addresstype.equals("modifyaddress")) {
            address_title.setText("修改地址");

            btn_type = "0";

            Intent intent1 = getIntent();
            //从Intent当中根据key取得value
            if (intent1 != null) {
                addressoId = intent1.getStringExtra("addressoId");
                people = intent1.getStringExtra("people");
                phone = intent1.getStringExtra("phone");
                province = intent1.getStringExtra("province");
                city = intent1.getStringExtra("city");
                district = intent1.getStringExtra("district");
                street = intent1.getStringExtra("street");
                address = intent1.getStringExtra("address");

                //toast(addressoId+"\n"+people+"\n"+phone+"\n"+province+"\n"+city+"\n"+district+"\n"+street+"\n"+address+"\n");

                et_address_people.setText(people);
                et_address_phone.setText(phone);
                et_address_xiangxi_address.setText(address);

                tv_diqu.setText(province + city + district);
                tv_jiedao.setText(street);
            }

/*            intent.putExtra("addressoId", addressId);
            intent.putExtra("people", people);
            intent.putExtra("phone", phone);
            intent.putExtra("province", province);
            intent.putExtra("city", city);
            intent.putExtra("district", district);
            intent.putExtra("street", street);
            intent.putExtra("address", address);*/

        }

        //getAddress(sp_token);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_add_address:

                // et_address_people,et_address_phone,et_address_xiangxi_address;

                String people = et_address_people.getText().toString().trim();
                String phone = et_address_phone.getText().toString().trim();
                String address = et_address_xiangxi_address.getText().toString().trim();


                if (people.equals("")) {
                    toast("请输入收货人名称");
                } else if (phone.equals("")) {
                    toast("请输入收货人联系方式");
                } else if (StringUtil.isSpace(phone)) {
                    toast("请输入手机号");
                } else if (!StringUtil.isPhone(phone)) {
                    toast("请输入正确的手机号");
                } else if (address.equals("")) {
                    toast("请输入收货人详细地址");
                } else {
                    if (btn_type.equals("1")) {
                        insertAddress(App.token, "0", people, phone, province, city, district, street, address);
                    }else{
                        //toast("修改地址");
                        modifyAddress(App.token, addressoId, people, phone, province, city, district, street, address);
                    }
                }

                break;

            case R.id.rl_diqu:
                /**
                 * 先设置不可点击
                 */
                rl_diqu.setEnabled(false);
                PickAddressDialog.showPickAddressDialog(AddAdressActivity.this, "1", streetChildsBeans);
                addressBeen = DataHelper.getAddress(this);
                PickAddressDialog.setData(addressBeen);
                /**
                 * 数据加载完成设置可点击
                 */
                rl_diqu.setEnabled(true);
                PickAddressDialog.setOnTopClicklistener(new PickAddressInterface() {
                    @Override
                    public void onOkClick(String mProvinceName, String mCityName, String mCountyName, String mCurrentName, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans) {
                        //Toast.makeText(AddAdressActivity.this, mProvinceName + mCityName + mCountyName, Toast.LENGTH_SHORT).show();
                        tv_diqu.setText(mProvinceName + mCityName + mCountyName);
                        streetChildsBeans = beans;

                        province = mProvinceName;
                        city = mCityName;
                        String district = mCountyName;
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;
            case R.id.rl_jiedao:
                /**
                 * 先设置不可点击
                 */
                rl_jiedao.setEnabled(false);
                if (streetChildsBeans.size() == 0) {
                    toast("请先选择地区再选择街道");
                    rl_jiedao.setEnabled(true);
                    return;
                }
                PickAddressDialog.showPickAddressDialog(AddAdressActivity.this, "2", streetChildsBeans);
                /**
                 * 数据加载完成设置可点击
                 */
                rl_jiedao.setEnabled(true);
                PickAddressDialog.setOnTopClicklistener(new PickAddressInterface() {
                    @Override
                    public void onOkClick(String mProvinceName, String mCityName, String mCountyName, String mCurrentName, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans) {
                        //Toast.makeText(AddAdressActivity.this, mCurrentName, Toast.LENGTH_SHORT).show();
                        tv_jiedao.setText(mCurrentName);
                        String street = mCurrentName;

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;
        }
    }


    //private String status,people,phone,province,city,district,street,address = "";   //信息状态 0正常 9删除、收货人、电话、省、市、区、街道、详细地址、

    public void insertAddress(final String token, final String statu, final String
            people, final String phone, final String province, final String city, final String
                                      district, final String street, final String address) {

/*        toast(token + "\n" + status + "\n" + people + "\n" + phone + "\n" + province + "\n" + city
                + "\n" + district + "\n" + street + "\n" + address + "\n");*/

        Map<String, String> map = new HashMap<>();

        map.put("token", token);
        map.put("address.status", statu);
        map.put("address.people", people);
        map.put("address.phone", phone);
        map.put("address.province", province);
        map.put("address.city", city);
        map.put("address.district", district);
        map.put("address.street", street);
        map.put("address.address", address);


        HttpUtils.doPost(UriUtil.insert_address, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("77777",response.body().string());

                Bean data = new Gson().fromJson(response.body().string(), Bean.class);
                status = data.getStatus();

                if (status==1) {


                    Message msg = new Message();
                    msg.what = Constant.Add_Address;
                    hd.sendMessage(msg);

                }

            }
        });

    }


    /**
     *
     * @param token
     * @param addressoid
     * @param people
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param street
     * @param address
     *
     * 修改地址信息
     */
    public void modifyAddress(final String token, final String addressoid, final String
            people, final String phone, final String province, final String city, final String
                                      district, final String street, final String address) {


        Map<String, String> map = new HashMap<>();

        map.put("token", token);
        map.put("address.oid", addressoid);
        map.put("address.people", people);
        map.put("address.phone", phone);
        map.put("address.province", province);
        map.put("address.city", city);
        map.put("address.district", district);
        map.put("address.street", street);
        map.put("address.address", address);


        HttpUtils.doPost(UriUtil.modify_address, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("88888",response.body().string());

                Bean data = new Gson().fromJson(response.body().string(), Bean.class);
                status = data.getStatus();

                if (status==1) {

                    Message msg = new Message();
                    msg.what = Constant.Modify_Address;
                    hd.sendMessage(msg);

                }

            }
        });

    }


}
