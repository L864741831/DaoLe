package com.tck.daole.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tck.daole.R;
import com.tck.daole.base.BaseActivity;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.Constant;
import com.tck.daole.util.HttpUtils;
import com.tck.daole.util.StringUtil;
import com.tck.daole.util.UriUtil;

;import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 店铺信息
 */
public class StoreInformationActivity extends BaseActivity {
    private LinearLayout ll_back, ll_store_appraise;
    private ImageView iv_store_img;

    //店铺名、月售、介绍、地址、评价、信息、品类、营业时间
    private TextView tv_information_name, tv_information_orderNum, tv_information_storeDetail,
            tv_information_address, tv_information_adminsStoreStarlevel, tv_information_storeMessage,
            tv_information_storeType, tv_information_startTime_endTime;

    private RatingBar tv_information_starlevel; //评分条

    //电话
    private ImageView call_phone;

    Handler hd;

    private String adminsStoreId, name, storeDetail, logoImage, starlevel,
            orderNum, projectName, projectImage, address, adminsStoreStarlevel,
            storeMessage, storeType, startTime, endTime = "";

    private String phone = "10086";

    private String img_url_one,img_url_two,img_url_three,img_url_foue = "";
    private String tv_name_one,tv_name_two,tv_name_three,tv_name_foue = "";


    private ImageView img_store_one,img_store_two,img_store_three,img_store_foue;
    private TextView tv_store_one,tv_store_two,tv_store_three,tv_store_foue;
/*    {
        adminsStoreId=店铺ID;
        name=店铺名字;
        storeDetail=店铺详情;
        logoImage=店铺头像;
        starlevel=评论星级;

        orderNum=月售;
        projectName=商品名字;
        projectImage=商品图片;
        address=店铺地址;
        adminsStoreStarlevel=店铺评价;

        storeMessage=商家信息;
        storeType=商家类型;
        startTime=开门时间;
        endTime=打烊时间;
    }*/


    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_store_information);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.store_View).setVisibility(View.GONE);
        }

        ll_store_appraise = findView(R.id.ll_store_appraise);
        ll_back = findView(R.id.ll_back);
        iv_store_img = findView(R.id.iv_store_img);


        //店铺名、月售、介绍、地址、评价、信息、品类、营业时间
        tv_information_name = findView(R.id.tv_information_name);
        tv_information_orderNum = findView(R.id.tv_information_orderNum);
        tv_information_storeDetail = findView(R.id.tv_information_storeDetail);
        tv_information_address = findView(R.id.tv_information_address);
        tv_information_adminsStoreStarlevel = findView(R.id.tv_information_adminsStoreStarlevel);
        tv_information_storeMessage = findView(R.id.tv_information_storeMessage);
        tv_information_storeType = findView(R.id.tv_information_storeType);
        tv_information_startTime_endTime = findView(R.id.tv_information_startTime_endTime);

        tv_information_starlevel = findView(R.id.tv_information_starlevel);

        call_phone = findView(R.id.call_phone);





        img_store_one = findView(R.id.img_store_one);
        img_store_two = findView(R.id.img_store_two);
        img_store_three = findView(R.id.img_store_three);
        img_store_foue = findView(R.id.img_store_foue);
        tv_store_one = findView(R.id.tv_store_one);
        tv_store_two = findView(R.id.tv_store_two);
        tv_store_three = findView(R.id.tv_store_three);
        tv_store_foue = findView(R.id.tv_store_four);








        hd = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.Shop_Informmation:
                        //店铺名、月售、介绍、地址、评价、信息、品类、营业时间
                        tv_information_name.setText(name);
                                tv_information_orderNum.setText("月售"+orderNum);
                                tv_information_storeDetail.setText(storeDetail);
                                tv_information_address.setText(address);
                                tv_information_adminsStoreStarlevel.setText(adminsStoreStarlevel);
                                tv_information_storeMessage.setText(storeMessage);
                                tv_information_storeType.setText(storeType);
                                tv_information_startTime_endTime.setText(startTime+"到"+endTime);


/*                        final String evaluateIdstarlevel = list.get(position).get("evaluateIdstarlevel");
                        Float float_evaluateIdstarlevel=0f;
                        if (!StringUtil.isSpace(evaluateIdstarlevel)) {
                            float_evaluateIdstarlevel = Float.parseFloat(evaluateIdstarlevel);
                        }*/

                        Float float_evaluateIdstarlevel=0f;
                        if (!StringUtil.isSpace(starlevel)) {
                            float_evaluateIdstarlevel = Float.parseFloat(starlevel);
                        }
                        //星级
                        tv_information_starlevel.setRating(float_evaluateIdstarlevel);


                       tv_store_one.setText(tv_name_one);
                        tv_store_two.setText(tv_name_two);
                        tv_store_three.setText(tv_name_three);
                        tv_store_foue.setText(tv_name_foue);

                        //显示图片的配置
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.mipmap.def)//等待加载显示图片
                                .showImageOnFail(R.mipmap.def)//显示错误图片
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(UriUtil.ip+img_url_one, img_store_one, options);
                        ImageLoader.getInstance().displayImage(UriUtil.ip+img_url_two, img_store_two, options);
                        ImageLoader.getInstance().displayImage(UriUtil.ip+img_url_three, img_store_three, options);
                        ImageLoader.getInstance().displayImage(UriUtil.ip+img_url_foue, img_store_foue, options);

                        //头像图片
                        ImageLoader.getInstance().displayImage(UriUtil.ip+logoImage, iv_store_img, options);



/*                           private String img_url_one,img_url_two,img_url_three,img_url_foue = "";
    private String tv_name_one,tv_name_two,tv_name_three,tv_name_foue = "";


                        private ImageView img_store_one,img_store_two,img_store_three,img_store_foue;
                        private TextView tv_store_one,tv_store_two,tv_store_three,tv_store_foue;*/

/*                        adminsStoreId = Shop_Information.getAdminsStoreId();//=店铺ID;
                        name = Shop_Information.getName();//=店铺名字;
                        storeDetail = Shop_Information.getStoreDetail();//=店铺详情;
                        logoImage = Shop_Information.getLogoImage();//=店铺头像;
                        starlevel = Shop_Information.getStarlevel();//=评论星级;
                        orderNum = Shop_Information.orderNum;//=月售;
                        projectName = Shop_Information.getProjectName();//=商品名字;
                        projectImage = Shop_Information.getProjectImage();//=商品图片;
                        address = Shop_Information.getAddress();//=店铺地址;
                        adminsStoreStarlevel = Shop_Information.getAdminsStoreStarlevel();//=店铺评价;
                        storeMessage = Shop_Information.getStoreMessage();//=商家信息;
                        storeType = Shop_Information.getStoreType();//=商家类型;
                        startTime = Shop_Information.getStartTime();//=开门时间;
                        endTime = Shop_Information.getEndTime();//=打烊时间;*/

                        break;

                }

            }
        };









    }

    @Override
    protected void initListener() {
        ll_store_appraise.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        call_phone.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            String oid = intent.getStringExtra("oid");
            getShopInformation(oid);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.call_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
                break;
/*            case R.id.ll_store_appraise:
                startActivity(new Intent(this, ShopAppraiseActivity.class));//跳转商家评价
                break;*/
        }
    }


    public void getShopInformation(final String oid) {
    /*
 * Post请求
 * 参数一：请求Url
 * 参数二：请求的键值对
 * 参数三：请求回调
 */
        Map<String, String> map = new HashMap<>();
        map.put("adminStore.oid", oid);

        HttpUtils.doPost(UriUtil.Shop_Information, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("11111",response.body().string());

                Bean.ShopInformationModel data = new Gson().fromJson(response.body().string(), Bean.ShopInformationModel.class);

                List<Bean.ShopInformationImageList> Shop_InformationImage_List = data.getList();
                Bean.ShopInformation Shop_Information = data.getModel();

                adminsStoreId = Shop_Information.getAdminsStoreId();//=店铺ID;
                name = Shop_Information.getName();//=店铺名字;
                storeDetail = Shop_Information.getStoreDetail();//=店铺详情;
                logoImage = Shop_Information.getLogoImage();//=店铺头像;
                starlevel = Shop_Information.getStarlevel();//=评论星级;
                orderNum = Shop_Information.getOrderNum();//=月售;
                projectName = Shop_Information.getProjectName();//=商品名字;
                projectImage = Shop_Information.getProjectImage();//=商品图片;
                address = Shop_Information.getAddress();//=店铺地址;
                adminsStoreStarlevel = Shop_Information.getAdminsStoreStarlevel();//=店铺评价;
                storeMessage = Shop_Information.getStoreMessage();//=商家信息;
                storeType = Shop_Information.getStoreType();//=商家类型;
                startTime = Shop_Information.getStartTime();//=开门时间;
                endTime = Shop_Information.getEndTime();//=打烊时间;

                phone = Shop_Information.getTel();

                if(Shop_InformationImage_List.size()>4){
                    img_url_one = Shop_InformationImage_List.get(0).getProjectImage();
                    img_url_two = Shop_InformationImage_List.get(1).getProjectImage();
                    img_url_three = Shop_InformationImage_List.get(2).getProjectImage();
                    img_url_foue = Shop_InformationImage_List.get(3).getProjectImage();

                    tv_name_one = Shop_InformationImage_List.get(0).getProjectName();
                    tv_name_two = Shop_InformationImage_List.get(1).getProjectName();
                    tv_name_three = Shop_InformationImage_List.get(2).getProjectName();
                    tv_name_foue = Shop_InformationImage_List.get(3).getProjectName();
                }

                Message msg = new Message();
                msg.what = Constant.Shop_Informmation;
                hd.sendMessage(msg);





            /*
            /*    {
        adminsStoreId=店铺ID;
        name=店铺名字;
        storeDetail=店铺详情;
        logoImage=店铺头像;
        starlevel=评论星级;

        orderNum=月售;
        projectName=商品名字;
        projectImage=商品图片;
        address=店铺地址;
        adminsStoreStarlevel=店铺评价;

        storeMessage=商家信息;
        storeType=商家类型;
        startTime=开门时间;
        endTime=打烊时间;
    }*/


            }
        });

    }


}
