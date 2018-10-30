package com.tck.daole.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.AddressBean;
import com.tck.daole.entity.Bean;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.PickAddressDialog;
import com.tck.daole.thread.PickAddressInterface;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.ACache;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.StringUtil;
import com.tck.daole.util.UtilImags;
import com.tck.daole.view.MyCountDownTimer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * 企业入驻页
 */

public class EnterpriseSettledActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;

    //公司简称、店铺名称、店铺地址、公司类型、营业执照号码
    private EditText et_enterprice_name, et_enterprice_shop_name, et_enterprice_shop_address, et_enterprice_shop_card;
    private TextView tv_enterprice_shop_type;
    //营业执照
    private ImageView img_enterprice_shop_card;
    //运营人名字、身份证、电话、验证码
    private EditText et_enterprice_people_name, et_enterprice_people_card, et_enterprice_people_phone, et_enterprice_people_YZM;
    //获取验证码按钮
    private TextView merchandise_yanzheng;
    //提交企业信息
    private Button et_enterprice_submit;

    ACache aCache;

    PopupWindow pop;
    LinearLayout ll_popup;

    public String image_address = "";

    public int int_shop_type = -1;
    public String[] arr_shop_type = {"超市", "美食团购", "美食外卖", "花之语", "休闲娱乐", "运动健康", "生活服务", "果蔬生鲜"};


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_enterprise_settled);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }


        findViewId();
    }

    public void findViewId() {
        ll_back = findView(R.id.ll_back);


        et_enterprice_name = findView(R.id.et_enterprice_name);
        et_enterprice_shop_name = findView(R.id.et_enterprice_shop_name);
        et_enterprice_shop_address = findView(R.id.et_enterprice_shop_address);

        tv_enterprice_shop_type = findView(R.id.tv_enterprice_shop_type);

        et_enterprice_shop_card = findView(R.id.et_enterprice_shop_card);


        img_enterprice_shop_card = findView(R.id.img_enterprice_shop_card);

        et_enterprice_people_name = findView(R.id.et_enterprice_people_name);
        et_enterprice_people_card = findView(R.id.et_enterprice_people_card);
        et_enterprice_people_phone = findView(R.id.et_enterprice_people_phone);
        et_enterprice_people_YZM = findView(R.id.et_enterprice_people_YZM);

        merchandise_yanzheng = findView(R.id.merchandise_yanzheng);

        et_enterprice_submit = findView(R.id.et_enterprice_submit);

    }

    @Override
    protected void initListener() {
        ll_back.setOnClickListener(this);
        img_enterprice_shop_card.setOnClickListener(this);

        tv_enterprice_shop_type.setOnClickListener(this);
        et_enterprice_submit.setOnClickListener(this);
        merchandise_yanzheng.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //aCache = ACache.get(EnterpriseSettledActivity.this);
        //img_enterprice_shop_card.setImageBitmap(aCache.getAsBitmap("myimg"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.img_enterprice_shop_card:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        EnterpriseSettledActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;


            case R.id.tv_enterprice_shop_type:      //选择店铺类型

                List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();
/*                for (int i = 1; i < 100; i++) {
                    AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                    age.setName("" + i);
                    age.setCode("" + i);
                    streetChildsBeans.add(age);
                }*/

                //public String[] arr_shop_type = {"超市","美食团购","美食外卖","花之语","休闲娱乐","运动健康","生活服务","果蔬生鲜"};


                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age.setName("超市");
                age.setCode("" + 1);
                streetChildsBeans.add(age);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age2 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age2.setName("美食团购");
                age2.setCode("" + 2);
                streetChildsBeans.add(age2);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age3 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age3.setName("美食外卖");
                age3.setCode("" + 2);
                streetChildsBeans.add(age3);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age4 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age4.setName("花之语");
                age4.setCode("" + 2);
                streetChildsBeans.add(age4);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age5 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age5.setName("休闲娱乐");
                age5.setCode("" + 2);
                streetChildsBeans.add(age5);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age6 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age6.setName("运动健康");
                age6.setCode("" + 2);
                streetChildsBeans.add(age6);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age7 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age7.setName("生活服务");
                age7.setCode("" + 2);
                streetChildsBeans.add(age7);

                AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age8 = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                age8.setName("果蔬生鲜");
                age8.setCode("" + 2);
                streetChildsBeans.add(age8);

                PickAddressDialog.showPickAddressDialog(EnterpriseSettledActivity.this, "2", streetChildsBeans);
                PickAddressDialog.setOnTopClicklistener(new PickAddressInterface() {
                    @Override
                    public void onOkClick(String mProvinceName, String mCityName, String mCountyName, String mCurrentName, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans) {
                        //Toast.makeText(AddAdressActivity.this, mCurrentName, Toast.LENGTH_SHORT).show();
                        //toast("更改年龄为"+mCurrentName);
                        //设置店铺类型
                        //setPersonalAge(App.token, mCurrentName);
                        //public String[] arr_shop_type = {"超市","美食团购","美食外卖","花之语","休闲娱乐","运动健康","生活服务","果蔬生鲜"};
                        //int_shop_type

                        tv_enterprice_shop_type.setText(mCurrentName);


                        int k = 0;//定义变量保存指定元素的下标
                        for (int i = 0; i < arr_shop_type.length; i++) {
                            if (arr_shop_type[i].equals(mCurrentName))//ffd 为你指定的元素
                            {
                                int_shop_type = i;
                                Log.i("店铺类型", (int_shop_type + 1) + "");
                                //System.out.println(int_shop_type);//打印出指定元素的下标

                            }
                        }
                        if (k == 0) {
                            System.out.println("您所指定的元素不存在");//指定元素不存在
                        }

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

                break;

            case R.id.merchandise_yanzheng:
                String str = et_enterprice_people_phone.getText().toString().trim();
                if (StringUtil.isSpace(str)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(str)){
                    toast("请输入正确的手机号");
                    return;
                }
                getYanZhengMa(str);
                break;



            case R.id.et_enterprice_submit:

/*                //公司简称、店铺名称、店铺地址、公司类型、营业执照号码
                private EditText et_enterprice_name, et_enterprice_shop_name, et_enterprice_shop_address, et_enterprice_shop_card;
                private TextView tv_enterprice_shop_type;
                //营业执照
                private ImageView img_enterprice_shop_card;
                //运营人名字、身份证、电话、验证码
                private EditText et_enterprice_people_name, et_enterprice_people_card, et_enterprice_people_phone, et_enterprice_people_YZM;
                //获取验证码按钮
                private TextView merchandise_yanzheng;
                //提交企业信息
                private Button et_enterprice_submit;

                ACache aCache;

                PopupWindow pop;
                LinearLayout ll_popup;

                public String image_address = "";

                public int int_shop_type = -1;*/

                String ShortName = et_enterprice_name.getText().toString(); //公司简称
                String storeName = et_enterprice_shop_name.getText().toString();//公司名称
                String storeAddress = et_enterprice_shop_address.getText().toString();  //店铺地址
                //int_shop_type         -1表示没选      传值时加一，因为取得数组下标          //店铺类型
                String businessLicenseNo = et_enterprice_shop_card.getText().toString().trim(); //营业执照号码
                //image_address 店铺执照图片地址 默认为空


                String operatorName = et_enterprice_people_name.getText().toString().trim();    //运营人
                String operatorIdCard = et_enterprice_people_card.getText().toString().trim();  //运营人身份整
                String  operatorPhone = et_enterprice_people_phone.getText().toString().trim(); //运营人电话
                String  inputYzm =et_enterprice_people_YZM.getText().toString().trim(); //验证码


                if (StringUtil.isSpace(ShortName)){
                    toast("请输入公司简称");
                    return;
                }
                if (StringUtil.isSpace(storeName)){
                    toast("请输入公司名称");
                    return;
                }
                if (StringUtil.isSpace(storeAddress)){
                    toast("请输入店铺地址");
                    return;
                }
                if (int_shop_type == -1){
                    toast("请选择店铺类型");
                    return;
                }
                if (StringUtil.isSpace(businessLicenseNo)){
                    toast("请输入营业执照号码");
                    return;
                }
                if(image_address.equals("")){
                    toast("请上传营业执照图片");
                    return;
                }
                if (StringUtil.isSpace(operatorName)){
                    toast("请输入运营人姓名");
                    return;
                }
                if (StringUtil.isSpace(operatorIdCard)){
                    toast("请输入运营人身份证号");
                    return;
                }
                String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
                if (operatorPhone.matches(REGEX_ID_CARD)){
                    toast("请输入正确的身份证号");
                    return;
                }
                if (StringUtil.isSpace(operatorPhone)){
                    toast("请输入运营人电话");
                    return;
                }
                if (!StringUtil.isPhone(operatorPhone)){
                    toast("请输入正确的运营人电话");
                    return;
                }
                if (StringUtil.isSpace(inputYzm)){
                    toast("请输入验证码");
                    return;
                }

                //toast("提交成功");


                /**
                 * 上传入驻信心
                 *
                 * 店铺公司简称
                 * 店铺名称
                 * 店铺地址
                 * 店铺类型|1.超市 2.美食团购 3.美食外卖 4.花之语 5.休闲娱乐 6.运动健康 7.生活服务 8.果蔬生鲜
                 * 营业执照号码
                 * 营业执照照片
                 * 运营人姓名
                 * 运营人身份证号码
                 * 运营人手机号
                 * 验证码
                 */

/*                String ShortName = et_enterprice_name.getText().toString(); //公司简称
                String storeName = et_enterprice_shop_name.getText().toString();//公司名称
                String storeAddress = et_enterprice_shop_address.getText().toString();  //店铺地址
                //int_shop_type         -1表示没选      传值时加一，因为取得数组下标          //店铺类型
                String businessLicenseNo = et_enterprice_shop_card.getText().toString().trim(); //营业执照号码
                //image_address 店铺执照图片地址 默认为空


                String operatorName = et_enterprice_people_name.getText().toString().trim();    //运营人
                String operatorIdCard = et_enterprice_people_card.getText().toString().trim();  //运营人身份整
                String  operatorPhone = et_enterprice_people_phone.getText().toString().trim(); //运营人电话
                String  inputYzm =et_enterprice_people_YZM.getText().toString().trim(); //验证码*/

                submitShop(ShortName,storeName,storeAddress,(int_shop_type+1)+"",businessLicenseNo,image_address,operatorName,operatorIdCard,operatorPhone,inputYzm);

                break;
        }
    }


    /****
     * 企业上传营业执照
     */
    public void showPopupWindow() {
        pop = new PopupWindow(EnterpriseSettledActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {   //调用系统相机


                ActivityCompat.requestPermissions(EnterpriseSettledActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 6);


                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {   //调用系统相册
/*                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, 2);*/

                ActivityCompat.requestPermissions(EnterpriseSettledActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, 7);


                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 6 && resultCode == Activity.RESULT_OK
                && null != data) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bmp = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            String filename = null;
            try {
                filename = UtilImags.SHOWFILEURL(EnterpriseSettledActivity.this) + "/" + name;
            } catch (IOException e) {
            }
            try {
                fout = new FileOutputStream(filename);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            } catch (FileNotFoundException e) {
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                }
            }
            //ZQ_round_image.setImageBitmap(bmp);
            String baseBim = bitmapToBase64(bmp);
            //Log.i("Personal====",baseBim);
            img_enterprice_shop_card.setImageBitmap(stringtoBitmap(baseBim));

            updateHeadv(baseBim, "JPEG");

        }
        if (requestCode == 7 && resultCode == Activity.RESULT_OK
                && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage,
                        filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Bitmap bmp = BitmapFactory.decodeFile(picturePath);
                // 获取图片并显示
                //ZQ_round_image.setImageBitmap(bmp);
                String baseBim = bitmapToBase64(bmp);
                //Log.i("Personal====",baseBim);
                img_enterprice_shop_card.setImageBitmap(stringtoBitmap(baseBim));

                updateHeadv(baseBim, "JPEG");

            } catch (Exception e) {
            }
        }
    }

    /*
    图片转banse64
     */
    public String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
    string转bitmap
     */
    public Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //上传营业执照
    public void updateHeadv(final String headImage, final String PicType) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.submitCard(headImage, PicType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            //Log.i("上传",headImage);
                            //Log.e("上传.成功", result);

                            Bean.ShopImagePath path_data = new Gson().fromJson(result, Bean.ShopImagePath.class);

                            if (path_data.status==0) {
                                toast(path_data.message);
                            }else{
                                image_address = path_data.Head;
                            }

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("上传.异常", response);

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            toast(R.string.system_busy);
        }
    }




    //获得验证码
    public void getYanZhengMa(final String phone) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.sendYzm(phone, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取验证码.成功", result);

                            MyCountDownTimer timer = new MyCountDownTimer(EnterpriseSettledActivity.this, merchandise_yanzheng, 60000, 1000);
                            timer.start();

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取验证码.异常", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            toast(R.string.system_busy);
        }
    }




    //提交入驻信息
    /**
     * 上传入驻信心
     *
     * 店铺公司简称
     * 店铺名称
     * 店铺地址
     * 店铺类型|1.超市 2.美食团购 3.美食外卖 4.花之语 5.休闲娱乐 6.运动健康 7.生活服务 8.果蔬生鲜
     * 营业执照号码
     * 营业执照照片
     * 运营人姓名
     * 运营人身份证号码
     * 运营人手机号
     * 验证码
     */
    public void submitShop(final String ShortName,final String storeName,final  String storeAddress,final String storeType,final String businessLicenseNo,final String storeImage,final String operatorName,final String operatorIdCard,final String operatorPhone,final String inputYzm) {
        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.submitShopInformation(ShortName,storeName,storeAddress,storeType,businessLicenseNo,storeImage,operatorName,operatorIdCard,operatorPhone,inputYzm, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("企业入驻.成功", result);
                            toast("企业入驻.成功");
                            finish();
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("企业入驻.成功", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            toast(R.string.system_busy);
        }
    }



}
