package com.tck.daole.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.UriUtil;
import com.tck.daole.util.UtilFileDB;
import com.tck.daole.util.UtilImags;
import com.tck.daole.view.ZQRoundOvalImageView;
import com.google.gson.Gson;

import okhttp3.Call;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity {
    private LinearLayout ll_back;
    private TextView tv_login, tv_register;
    private RelativeLayout rl_nick_name, rl_sex_dialog, rl_autograph, rl_personal_age;//see_phone,

    private TextView tv_nickname_show, tv_sex_show, tv_age_show, tv_signature_show,phone_show;

    //    private String sp_token = "";
    private String sex = "男";

    private String phone = "";


    String URL = "url";
    ZQRoundOvalImageView ZQ_round_image;
    PopupWindow pop;
    LinearLayout ll_popup;
    String urlsf = "";
    int img = 1;
    ACache aCache;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_center);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        findViewId();
        initImageView();
    }

    private void initImageView() {
        aCache = ACache.get(PersonalCenterActivity.this);
        ZQ_round_image = (ZQRoundOvalImageView) findViewById(R.id.ZQ_round_image);
        ZQ_round_image.setOnClickListener(this);
    }

//    private void initImageData() {
//        if (UtilFileDB.SELETEFile(aCache, "stscimage") != null) {
//            if (aCache.getAsBitmap("myimg") == null) {
//                getImage(UriUtil.ip + App.login.head);
//            } else {
//                ZQ_round_image.setImageBitmap(aCache.getAsBitmap("myimg"));
//            }
//        }
//    }


    public void getImage(String url) {
        OkHttpUtils.get().url(url).tag(this).build().connTimeOut(20000)
                .readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {

                    public void onError(Call call, Exception e) {

                    }

                    public void onResponse(Bitmap bitmap) {

                    }

                    public void onError(Call call, Exception e, int id) {
                    }

                    public void onResponse(Bitmap bitmap, int id) {
                        ZQ_round_image.setImageBitmap(bitmap);
                        aCache.put("myimg", bitmap);
                    }
                });
    }


    public void findViewId() {
        ll_back = findView(R.id.ll_back);
        tv_login = findView(R.id.tv_login);
        tv_register = findView(R.id.tv_register);
        rl_nick_name = findView(R.id.rl_nick_name);
        rl_sex_dialog = findView(R.id.rl_sex_dialog);
        rl_autograph = findView(R.id.rl_autograph);
//        see_phone = findView(R.id.see_phone);
        rl_personal_age = findView(R.id.rl_personal_age);

        //tv_nickname_show,tv_sex_show,tv_age_show,tv_signature_show;

        tv_nickname_show = findView(R.id.tv_nickname_show);
        tv_sex_show = findView(R.id.tv_sex_show);
        tv_age_show = findView(R.id.tv_age_show);
        tv_signature_show = findView(R.id.tv_signature_show);
        phone_show = findView(R.id.phone_show);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.islogin) {
            tv_nickname_show.setText(App.login.nickName);
            tv_sex_show.setText(App.login.sex);
            tv_age_show.setText(App.login.age);
            tv_signature_show.setText(App.login.Signature);
            phone_show.setText(App.login.phone);
            initHead();
        }
        else {
            ZQ_round_image.setImageResource(R.mipmap.touxiang);
        }
    }

    private void initHead() {
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)//等待加载显示图片
                .showImageOnFail(R.mipmap.touxiang)//显示错误图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(UriUtil.ip + App.login.head, ZQ_round_image, options);
    }

    @Override
    protected void initListener() {
        tv_login.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        rl_nick_name.setOnClickListener(this);
        rl_sex_dialog.setOnClickListener(this);
        rl_autograph.setOnClickListener(this);
//        see_phone.setOnClickListener(this);
        rl_personal_age.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        sp_token = SPUtil.getData(PersonalCenterActivity.this, "token", "").toString();
        if (App.islogin) {
            tv_login.setVisibility(View.GONE);
            tv_register.setVisibility(View.GONE);

            //getUserInformation(sp_token);
        } else {
            startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_login: //登录
/*                String msg_token = SPUtil.getData(PersonalCenterActivity.this,"token","").toString();
                if(msg_token.equals("")){*/
                startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
/*                }else{
                    toast("您已登录，无需再次登录");
                }*/
                break;
            case R.id.tv_register:  //注册
/*                String token = SPUtil.getData(PersonalCenterActivity.this,"token","").toString();
                if(token.equals("")){*/
                startActivity(new Intent(PersonalCenterActivity.this, RegisterActivity.class));
/*                }else{
                    toast("您已登录，无需注册");
                }*/
                break;
            case R.id.rl_nick_name: //昵称
                if (!App.islogin) {
                    toast("请先登录");
                    startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(PersonalCenterActivity.this, NickNameActivity.class));
                }
                break;
            case R.id.rl_sex_dialog: //性别弹出框
                if (!App.islogin) {
                    toast("请先登录");
                    startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
                } else {
                    selectSexDialog(PersonalCenterActivity.this);
                }
                break;
            case R.id.rl_autograph: //个性签名
                if (!App.islogin) {
                    toast("请先登录");
                    startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(PersonalCenterActivity.this, AutographActivity.class));
                }
                break;
//            case R.id.see_phone: //查看手机号弹出框
//                if (!App.islogin) {
//                    toast("请先登录");
//                    startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
//                } else {
//                    showPhone(App.token);
//                }
//                break;
            case R.id.ZQ_round_image:
                if (!App.islogin) {
                    toast("请先登录");
                    startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
                } else {
                    showPopupWindow();
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            PersonalCenterActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                }
                break;

            case R.id.rl_personal_age: //滚动选择年龄
                if (!App.islogin) {
                    toast("请先登录");
                    startActivity(new Intent(PersonalCenterActivity.this, LoginActivity.class));
                } else {

                    List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();

                    for (int i = 1; i < 100; i++) {
                        AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean age = new AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean();
                        age.setName("" + i);
                        age.setCode("" + i);
                        streetChildsBeans.add(age);
                    }

                    PickAddressDialog.showPickAddressDialog(PersonalCenterActivity.this, "2", streetChildsBeans);
                    PickAddressDialog.setOnTopClicklistener(new PickAddressInterface() {
                        @Override
                        public void onOkClick(String mProvinceName, String mCityName, String mCountyName, String mCurrentName, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans) {
                            //Toast.makeText(AddAdressActivity.this, mCurrentName, Toast.LENGTH_SHORT).show();
                            //toast("更改年龄为"+mCurrentName);
                            setPersonalAge(App.token, mCurrentName);
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });

                }
                break;

        }
    }

    /****
     * 头像提示框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(PersonalCenterActivity.this);
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

                Acp.getInstance(getApplicationContext()).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)//, Manifest.permission.READ_PHONE_STATE、, Manifest.permission.SEND_SMS
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                try {
                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    toast("已授权调用相机，请重新操作");
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                toast(permissions.toString() + "权限拒绝");
                            }
                        });

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
                Acp.getInstance(getApplicationContext()).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)//, Manifest.permission.READ_PHONE_STATE、, Manifest.permission.SEND_SMS
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                try {
                                    Intent picture = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(picture, 2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    toast("已授权访问本机文件，请重新操作");
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                toast(permissions.toString() + "权限拒绝");
                            }
                        });

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
        if (requestCode == 1 && resultCode == Activity.RESULT_OK
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
                filename = UtilImags.SHOWFILEURL(PersonalCenterActivity.this) + "/" + name;
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
            ZQ_round_image.setImageBitmap(stringtoBitmap(baseBim));

            updateHeadv(App.token, baseBim, "JPEG");

        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK
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
                ZQ_round_image.setImageBitmap(stringtoBitmap(baseBim));

                updateHeadv(App.token, baseBim, "JPEG");

            } catch (Exception e) {
            }
        }
//        if (requestCode==66&&resultCode==88){
//            tv_nickname_show.setText(data.getStringExtra("nickName"));
//        }
//        if (requestCode==666&&resultCode==888) {
//            tv_signature_show.setText(data.getStringExtra("signature"));
//        }
    }


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

    public void selectSexDialog(final Activity ctx) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_sex, null);
        //       TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
//
        RelativeLayout rl_man = (RelativeLayout) view.findViewById(R.id.rl_man);
        RelativeLayout rl_woman = (RelativeLayout) view.findViewById(R.id.rl_woman);


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
        builder.setView(view);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        dialog.setCanceledOnTouchOutside(true); //设置点击对话框外关闭对话框


        rl_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSex(App.token, "男");
                dialog.dismiss();
            }
        });

        rl_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSex(App.token, "女");
                dialog.dismiss();
            }
        });

 /*       ll_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();
    }


    //上传头像
    public void updateHeadv(final String token, final String headImage, final String picType) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.setUpdateHead(token, headImage, picType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {

//                            Log.e("上传.成功", result);
//
//                            Bean data = new Gson().fromJson(result, Bean.class);
//                            toast(data.getMessage());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("修改.成功", result);
                                    Bean data = new Gson().fromJson(result, Bean.class);
                                    if (data.status == 1) {
                                        toast("头像修改成功！");
                                        App.login.head=data.Head;
                                        initHead();
                                    } else {
                                        toast(data.getMessage());
                                    }
                                }
                            });

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("上传.异常", response);
                            Bean data = new Gson().fromJson(response, Bean.class);
                            toast(data.getMessage());
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


    //查看手机号对话框
    public void seePhoneDialog(final Activity ctx) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_see_phone, null);

        TextView tv_see_phone = (TextView) view.findViewById(R.id.tv_see_phone);


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
        builder.setView(view);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        dialog.setCanceledOnTouchOutside(true); //设置点击对话框外关闭对话框
        ;
        tv_see_phone.setText(phone);

 /*       ll_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();
    }


    //查看绑定手机号
    public String showPhone(final String token) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.seePhone(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {


                            Log.e("查看.成功", result);


                            Bean.UserModels data = new Gson().fromJson(result, Bean.UserModels.class);
                            phone = data.getPhone();

//                            toast(phone+"\n"+token+"\n"+data.getMessage());

                            seePhoneDialog(PersonalCenterActivity.this);

                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("查看.异常", response);

                            Bean.UserModels data = new Gson().fromJson(response, Bean.UserModels.class);
                            toast(data.getMessage());

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
            return phone;
        } else {
            toast(R.string.system_busy);
            return phone;
        }
    }


    //选择性别
    public void selectSex(final String token, final String sex) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.sexSelect(token, sex, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("修改.成功", result);
                                    Bean.UserModels data = new Gson().fromJson(result, Bean.UserModels.class);
                                    if (data.status == 1) {
                                        toast("修改成功！");
                                        tv_sex_show.setText(sex);
                                        App.login.sex = sex;
                                    } else {
                                        toast(data.getMessage());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("修改.异常", response);

                            Bean.UserModels data = new Gson().fromJson(response, Bean.UserModels.class);
                            toast(data.getMessage());

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


    //设置年龄
    public void setPersonalAge(final String token, final String age) {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.setAge(token, age, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("修改.成功", result);
                                    Bean.UserModels data = new Gson().fromJson(result, Bean.UserModels.class);
                                    if (data.status == 1) {
                                        toast("修改成功！");
                                        tv_age_show.setText(age);
                                        App.login.age = age;
                                    } else {
                                        toast(data.getMessage());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("设置.异常", response);

                            Bean.UserModels data = new Gson().fromJson(response, Bean.UserModels.class);
                            toast(data.getMessage());

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
