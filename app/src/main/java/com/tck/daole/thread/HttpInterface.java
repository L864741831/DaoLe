package com.tck.daole.thread;

import android.content.Context;
import android.util.Log;

import com.tck.daole.util.UriUtil;

/**
 * kylin on 2017/12/12.
 */

public class HttpInterface {
    private Context context;
    public LoadingDialog loadingDialog;

    public HttpInterface(Context context) {
        this.context = context;
        if (loadingDialog == null) {
            Log.e("httpInterface", "new LoadingDialog");
            loadingDialog = new LoadingDialog(context);
            //loadingDialog.setHint("playing。。。");
        }
    }

    /**
     * 获取验证码
     */
    public void sendYzm(String phone, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.sendYzm);
        try {
            userClient.AddParam("model.phone", phone);  //手机号
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户注册
     */
    public void register(String phone, String yanzhengma, String password, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_register);
        try {
            userClient.AddParam("model.phone", phone);  //手机号
            userClient.AddParam("inputYzm", yanzhengma);    //验证码
            userClient.AddParam("model.password", password);    //密码

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户验证码登录
     */
    public void YzmLogin(String phone, String yanzhengma, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.yzm_login);
        try {
            userClient.AddParam("model.phone", phone);  //手机号
            userClient.AddParam("inputYzm", yanzhengma);    //验证码

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户密码登录
     */
    public void psdLogin(String phone, String password, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.psd_login);
        try {
            userClient.AddParam("model.phone", phone);  //手机号
            userClient.AddParam("model.password", password);    //密码

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改昵称
     */
    public void modifyNickName(String token, String nick_name, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.nickname_modify);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.nickName", nick_name);   //昵称

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 选择性别
     */
    public void sexSelect(String token, String sex, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.select_sex);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.sex", sex);  //性别

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改个性签名
     */
    public void modifyAutograph(String token, String autograph, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.autograph_modify);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.signature", autograph);  //个性签名

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置年龄
     */
    public void setAge(String token, String age, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.set_age);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.age", age);  //年龄

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 查看绑定手机号
     */
    public void seePhone(String token, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.see_phone);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 首页轮番图
     */
    public void indexBanner(MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.index_banner);
        try {
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 首页滚动播报
     */
    public void getJournalism(MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.Journalism);
        try {
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 乐拼购商品列表
     */
    public void getSpellList(MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.spell_list);
        try {
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 大爱旅行论坛贴吧列表
     */
    public void getForumlist(MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.forum_list);
        try {
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 查询账户余额
     */
    public void seeBalance(String token, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.see_balance);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 上传头像
     */
    public void setUpdateHead(String token,String headImage,String picType, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.update_Head);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("headImage", headImage);
            userClient.AddParam("PicType", picType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    /**
     * 意见反馈
     */
    public void addfeedback(String token,String phone,String content, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.add_feed_back);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("feedBack.phone", phone);
            userClient.AddParam("feedBack.content", content);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









    /**
     * 忘记密码
     */
    public void updatePassword(String inputYzm,String phone,String password, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.update_Password);
        try {
            userClient.AddParam("inputYzm", inputYzm);
            userClient.AddParam("model.phone", phone);
            userClient.AddParam("model.password", password);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 我的发布个人信息
     */
    public void getMyPostedInformation(String token,String articleId, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.My_Posted_Information);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("articleId", articleId);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    /**
     * 发布文章
     */
    public void addMyPosted(String token,String title,String articletype,String content, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Add_Posted);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("article.title", title);
            userClient.AddParam("article.articletype", articletype);
            userClient.AddParam("article.content", content);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * 发布文章详情
     */
    public void getColumnDetail(String token,String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.get_Column_Detail);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("article.oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * 获得用户信息
     */
    public void getUser(String token, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Get_User_Information);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝支付
     */
    public void alipaypay(String out_trade_no, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.appPay);
        try {
            userClient.AddParam("out_trade_no", out_trade_no);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信登录
     */
    public void wxLogin(String openid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.wxLogin);
        try {
            userClient.AddParam("model.wechaOpenid", openid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方登录绑定手机号
     */
    public void bindPhone(String inputYzm,String phone,String wechaOpenid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.wxbind_phone);
        try {
            userClient.AddParam("inputYzm", inputYzm);
            userClient.AddParam("model.wechaOpenid", wechaOpenid);
            userClient.AddParam("model.phone", phone);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * qq登录
     */
    public void qqLogin(String openid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.qqLogin);
        try {
            userClient.AddParam("model.qqOpenid", openid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * qq第三方登录绑定手机号
     */
    public void qqbindPhone(String inputYzm,String phone,String wechaOpenid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.qqbind_phone);
        try {
            userClient.AddParam("inputYzm", inputYzm);
            userClient.AddParam("model.qqOpenid", wechaOpenid);
            userClient.AddParam("model.phone", phone);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得超市店铺列表
     */
    public void getMarketList(String dimensionality, String longitude,String titleState,String index,String num,String storeType,String twoType,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.get_Business_Store_Commodity);
        try {
            userClient.AddParam("dimensionality", dimensionality);
            userClient.AddParam("longitude", longitude);
            userClient.AddParam("titleState", titleState);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("adminStore.storeType", storeType);
            userClient.AddParam("adminStore.twoType", twoType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 话费充值--手机充值第一步
     */
    public void phone1_charge(String phone,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.phone1_charge);
        try {
            userClient.AddParam("phone", phone);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 话费充值--手机充值第二步
     */
    public void phone2_charge(String phone, String amount,String itemId,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.phone2_charge);
        try {
            userClient.AddParam("phone", phone);
            userClient.AddParam("amount", amount);
            userClient.AddParam("itemId", itemId);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 流量充值--流量充值第一步
     */
    public void liuliang1_charge(String phone, String amount,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.liuliang1_charge);
        try {
            userClient.AddParam("phone", phone);
            userClient.AddParam("flowOne", amount);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 流量充值--流量充值第二步
     */
    public void liuliang2_charge(String phone, String amount,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.liuliang2_charge);
        try {
            userClient.AddParam("phone", phone);
            userClient.AddParam("itemId", amount);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 游戏卡充值--游戏卡充值第一步
     */
    public void games1_charge(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.games1_charge);
        try {
//            userClient.AddParam("phone", phone);
//            userClient.AddParam("itemId", amount);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 游戏卡充值--游戏卡充值第二步
     */
    public void games2_charge(String gameId,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.games2_charge);
        try {
            userClient.AddParam("gameId", gameId);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 游戏卡充值--游戏卡充值第三步
     */
    public void games3_charge(String itemNum,String cardNumber,String itemId,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.games3_charge);
        try {
            userClient.AddParam("itemNum", itemNum);
            userClient.AddParam("cardNumber", cardNumber);
            userClient.AddParam("itemId", itemId);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加油卡充值--加油卡充值第一步
     */
    public void fuel1_charge(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.fuel1_charge);
        try {
//            userClient.AddParam("itemNum", itemNum);
//            userClient.AddParam("cardNumber", cardNumber);
//            userClient.AddParam("itemId", itemId);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加油卡充值--加油卡充值第二步
     */
    public void fuel2_charge(String phone,String name,String cardNumber,String itemId,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.fuel2_charge);
        try {
            userClient.AddParam("phone", phone);
            userClient.AddParam("name", name);
            userClient.AddParam("cardNumber", cardNumber);
            userClient.AddParam("itemId", itemId);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 水电煤充值--水电煤充值第一步
     */
    public void w_e_g1_charge(String city,String projectId,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.w_e_g1_charge);
        try {
            userClient.AddParam("city", city);
            userClient.AddParam("projectId", projectId);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 水电煤充值--水电煤充值第二步
     */
    public void w_e_g2_charge(String amount,String cardNumber,String itemId,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.w_e_g2_charge);
        try {
            userClient.AddParam("amount", amount);
            userClient.AddParam("cardNumber", cardNumber);
            userClient.AddParam("itemId", itemId);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取购物车列表
     */
    public void getShopCatList(String token, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Get_Shop_Cat_List);
        try {
            userClient.AddParam("token", token);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取景区订票详情
     */
    public void booking_getDetail(String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.booking_getDetail);
        try {
            userClient.AddParam("takeProject.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除购物车商品
     */
    public void deleteShop(String token,String orderOid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Delete_Shop_Cat);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("shopcarOid", orderOid);  //商品id

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 提交订单
     */
    public void submitOrder(String token,String orderStr,String type, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.submit_order);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("param", orderStr);  //json订单
            userClient.AddParam("type", type);  //json订单

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得订单列表-分类
     */
    public void getOrderList(String token,String index,String num,String orderState ,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Order_List);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("orderState", orderState);  //订单状态： 1待付款 2待接单 3待送达 4已送达 5退款退货


            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得订单列表-分类
     */
    public void getOrderList23(String token,String index,String num,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Order_List23);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得订单列表-全部
     */
    public void getOrderLists(String token,String index,String num,MApiResultCallback callback) {//,String orderState
        UserClient userClient = new UserClient(UriUtil.Order_Lists);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得订单详情
     */
    public void getOrderDetails(String token,String oid,MApiResultCallback callback) {//,String orderState
        UserClient userClient = new UserClient(UriUtil.getOrderDetails);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     */
    public void cancelOrder(String token,String oid,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.cancelOrder);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单退货接口
     */
    public void returnOrder(String token,String oid,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.returnOrder);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单确认送达接口
     */
    public void confirmOrder(String token,String oid,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.confirmOrder);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 余额支付订单
     */
    public void yuePayOrder(String token,String oid,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.yuePayOrder);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获得首页推荐
     */
    public void getTuiJian(String dimensionality,String longitude,String lTakeProjecttype,String wTakeProjecttype ,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Index_TuiJian);
        try {
            userClient.AddParam("dimensionality", dimensionality);
            userClient.AddParam("longitude", longitude);
            userClient.AddParam("lTakeProjecttype", lTakeProjecttype);
            userClient.AddParam("wTakeProjecttype", wTakeProjecttype);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 上传营业执照
     */
    public void submitCard(String headImage,String PicType, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.user_HeadUndo);
        try {
            userClient.AddParam("headImage", headImage);
            userClient.AddParam("PicType", PicType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
    public void submitShopInformation(String ShortName,String storeName, String storeAddress,String storeType,String businessLicenseNo,String storeImage,String operatorName,String operatorIdCard,String operatorPhone,String inputYzm,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Submit_Shop_Information);
        try {
            userClient.AddParam("businessCheck.companyShortName", ShortName);
            userClient.AddParam("businessCheck.storeName", storeName);
            userClient.AddParam("businessCheck.storeAddress", storeAddress);
            userClient.AddParam("businessCheck.storeType", storeType);
            userClient.AddParam("businessCheck.businessLicenseNo", businessLicenseNo);
            userClient.AddParam("businessCheck.storeImage", storeImage);
            userClient.AddParam("businessCheck.operatorName", operatorName);
            userClient.AddParam("businessCheck.operatorIdCard", operatorIdCard);
            userClient.AddParam("businessCheck.operatorPhone", operatorPhone);
            userClient.AddParam("inputYzm", inputYzm);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 获得拼购详情
     */
    public void getSpellData(String index,String num,String coommenLevel,String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Get_Spell_Information);
        try {
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("coommenLevel", coommenLevel);
            userClient.AddParam("takeProject.oid", oid);


            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 提交订票订单
     */
    public void submitBooking(String token,String param,String type, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.On_Submit_Booking);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("param", param);
            userClient.AddParam("type", type);  //json订单

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 景区列表搜索
     */
    public void getBookingSearch(String index,String num,String search, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Booking_Search);
        try {
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("search", search);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 全球签证列表搜索
     */
    public void getAllSearch(String index,String num,String search, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.All_Search);
        try {
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("search", search);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * 获得店铺收藏状态
     */
    public void getShoucangType(String token,String adminStoreId, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Shoucang_Type);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("evaluate_adminStoreId_oid", adminStoreId);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 改变店铺收藏状态
     */
    public void changeShoucangType(String token,String storeOid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Change_Type);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("storeOid", storeOid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取商品收藏状态
     */
    public void getMerchandiseType(String token,String projectId, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.GET_Merchandise_Type);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("evaluate_projectId_oid", projectId);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 修改商品收藏状态
     */
    public void changeMerchandiseType(String token,String storeOid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Change_Merchandise_Type);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("storeOid", storeOid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 获得我的文章发布列表
     */
    public void getMyPostedList(String token, int index, int num, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.my_posted);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", String.valueOf(index));
            userClient.AddParam("page.num", String.valueOf(num));

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获得了拼购商品列表
     */
    public void getSpellList(String search, int index, int num, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Get_Spell_List);
        try {
            userClient.AddParam("search", search);
            userClient.AddParam("page.index", String.valueOf(index));
            userClient.AddParam("page.num", String.valueOf(num));

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * 获得符合搜索条件的商家列表
     */
    public void getSeekStroe(String index, String num,String searchText,double dimensionality,double longitude,int storeType, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Get_Seek_Stroe);
        try {
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("searchText",searchText);
            userClient.AddParam("dimensionality", String.valueOf(dimensionality));
            userClient.AddParam("longitude", String.valueOf(longitude));
            userClient.AddParam("adminStore.storeType", String.valueOf(storeType));

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * 获得全部符合搜索条件的商家列表
     */
    public void getAllSeekStroe(String index, String num,String searchText,double dimensionality,double longitude, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.Get_All_Seek_Stroe);
        try {
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("searchText",searchText);
            userClient.AddParam("dimensionality", String.valueOf(dimensionality));
            userClient.AddParam("longitude", String.valueOf(longitude));

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
