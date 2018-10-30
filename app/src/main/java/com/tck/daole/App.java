package com.tck.daole;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tck.daole.entity.Bean;
import com.tck.daole.util.SPUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.util.Map;

/**
 * kylin on 2017/12/12.
 */

public class App extends Application {
    public static Map<String, Long> map;

    public static String token="";

    public static String phone="";

    public static boolean islogin=false;
    public static Bean.Login login;

    @Override
    public void onCreate() {
//        Acp.getInstance(getApplicationContext()).request(new AcpOptions.Builder()
//                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)//, Manifest.permission.READ_PHONE_STATE、, Manifest.permission.SEND_SMS
//                /*以下为自定义提示语、按钮文字
//                .setDeniedMessage()
//                .setDeniedCloseBtn()
//                .setDeniedSettingBtn()
//                .setRationalMessage()
//                .setRationalBtn()*/
//                        .build(),
//                new AcpListener() {
//                    @Override
//                    public void onGranted() {
//
//                    }
//
//                    @Override
//                    public void onDenied(List<String> permissions) {
//                        MUIToast.show(getApplicationContext(),permissions.toString() + "权限拒绝");
//                    }
//                });
        super.onCreate();

        token= SPUtil.getData(this,"token","")+"";
        phone= SPUtil.getData(this,"phone","")+"";
        islogin= (boolean) SPUtil.getData(this,"islogin",false);
        login=new Bean.Login();

        //设置LOG开关，默认为false

        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5a6f00e6b27b0a3eb500016b", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "7297729fd0879f3e261e4f078f29bffa");
        //开启ShareSDK debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
        initImageLoader(getApplicationContext());
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.gray, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        PlatformConfig.setWeixin("wx1c817b693a63a6b7","68729cfb0dc6e0ec2fa7dcaffc67787e");
        PlatformConfig.setQQZone("1106636109","xKOohUulUP4XZAKI");
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        ImageLoader.getInstance().init(config.build());
    }
}
