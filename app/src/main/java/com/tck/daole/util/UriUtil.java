package com.tck.daole.util;

/**
 * kylin on 2017/12/12.
 */

public class UriUtil {

//    public static final String ip = "http://192.168.1.100:8080/DaoLe/";
    //public static final String ip = "http://192.168.1.168:8080/DaoLe/";

    public static final String ip = "http://192.168.1.101:8080/DaoLe/";

//    public static final String ip = "http://39.107.67.191/DaoLe/";



    //public static final String ip_login = ip + "action/basicMember/userLogin";//用户登录

    public static final String sendYzm = ip + "action/memberMemberApp/sendYzm";//获取验证码
    public static final String ip_register = ip + "action/memberMemberApp/addMember";//用户注册
    public static final String yzm_login = ip + "action/memberMemberApp/quickLogin";//用户验证码登录
    public static final String psd_login = ip + "action/memberMemberApp/login";//用户登录
    public static final String nickname_modify = ip + "action/memberMemberApp/updateNickName";//修改昵称
    public static final String select_sex = ip + "action/memberMemberApp/updateSex";//选择性别
    public static final String autograph_modify = ip + "action/memberMemberApp/updateSignature";//修改个性签名
    public static final String see_phone = ip + "action/memberMemberApp/selectPhone";//查看绑定手机号
    public static final String set_age = ip + "action/memberMemberApp/updateAge";//设置年龄
    public static final String see_balance = ip + "action/appTakeProject/getBalance";//查询余额
    public static final String my_posted = ip + "action/articlePublishApp/goodComment";//我的发布列表
    public static final String cash_coupon = ip + "action/couponCouponApp/acquireMN";//优惠券列表
    public static final String System_news = ip + "action/appNews/getNews";//新闻
    public static final String merchandise_msg = ip + "action/adminStoreCollectEnshrineApp/goods";//物品详情

    public static final String commodity = ip + "action/adminStoreCollectEnshrineApp/commodityEnshrine";//收藏、商品
    public static final String shop = ip + "action/adminStoreCollectEnshrineApp/storeEnshrine";//收藏、店铺

    public static final String address_list = ip + "action/addressAddressApp/addressList";//地址列表
    public static final String default_address = ip + "action/addressAddressApp/defaultAddress";//设置默认地址
    public static final String insert_address = ip + "action/addressAddressApp/insertAddress";//添加收货地址
    public static final String delete_address = ip + "action/addressAddressApp/deleteHouseRent";//删除收货地址
    public static final String modify_address = ip + "action/addressAddressApp/UpdateHouseRent";//修改收货地址
    public static final String shop_name = ip + "action/adminStoreCollectEnshrineApp/storeMessage";//门店内页


    public static final String goodComment = ip + "action/adminStoreCollectEnshrineApp/goodComment";//商品好评
    public static final String mediumComment = ip + "action/adminStoreCollectEnshrineApp/mediumComment";//商品中评
    public static final String badComment = ip + "action/adminStoreCollectEnshrineApp/badComment";//商品差评


    public static final String satisfaction = ip + "action/adminStoreCollectEnshrineApp/satisfaction";//店铺好评
    public static final String entire = ip + "action/adminStoreCollectEnshrineApp/entire";//店铺中评
    public static final String yawp = ip + "action/adminStoreCollectEnshrineApp/yawp";//店铺差评

    public static final String Shop_Information = ip + "action/adminStoreCollectEnshrineApp/storeMaterial";//店铺信息

    public static final String update_Head = ip + "action/memberMemberApp/userHeadUndo";//上传头像

    public static final String add_feed_back = ip + "action/SettingApp/addfeedback";//意见反馈



    public static final String update_Password = ip + "action/memberMemberApp/updatePassword";//忘记密码



    public static final String My_Posted_Information = ip + "action/articlePublishApp/member";//我的发布个人信息

    public static final String Add_Posted = ip + "action/articlePublishApp/addfeedback";//发布文章



    public static final String get_Column_Detail = ip + "action/articlePublishApp/columnDetail";//发布文章列表详情

    public static final String Add_EessList = ip + "action/articlePublishApp/addressList";//发布文章评论列表

    public static final String Send_Posted = ip + "action/articlePublishApp/send";//发送对文章的评论


    public static final String Get_User_Information = ip + "action/memberMemberApp/getUser";//获得用户信息


    public static final String appPay = ip + "action/payAlipay/appPay";//支付宝支付

    public static final String wxLogin = ip + "action/memberMemberApp/WpartyLogin";//微信登录

    public static final String wxbind_phone = ip + "action/memberMemberApp/lock";//微信第三方登录绑定手机号

    public static final String qqLogin = ip + "action/memberMemberApp/QpartyLogin";//qq登录

    public static final String qqbind_phone = ip + "action/memberMemberApp/Qlock";//qq第三方登录绑定手机号

    public static final String Get_Search_Page = ip + "action/apphomepage/getSearchPage";//热门搜索列表

    public static final String get_Business_Store_Commodity = ip + "action/appStore/getBusinessStoreCommodity";//获得 店铺类型|1.超市 2.美食团购 3.美食外卖 4.花之语 5.休闲娱乐 6.运动健康 7.生活服务 8.果蔬生鲜

    public static final String Get_Shop_Type = ip + "action/appStore/goodsCategory";//获得店铺信息和分类

    public static final String Get_Shop_Content = ip + "action/appStore/projectMessage";//获得店铺分类的商品列表

    public static final String Add_Type_Shop = ip + "action/appShoppingCart/addCommodityCart";//添加购物车

    public static final String Delete_Type_Shop = ip + "action/appShoppingCart/decreaseShoppingCart";//减少购物车

    public static final String Online_List = ip + "action/apponline/goodComment";//论坛贴吧、同城在线等等列表

    public static final String Online_search = ip + "action/apponline/seekStroe";//论坛贴吧、同城在线等等列表

    public static final String Get_Eess_List = ip + "action/apponline/addressList";//论坛贴吧、同城在线等等评论的列表

    public static final String Get_Eess_Comumn = ip + "action/apponline/columnDetail";//论坛贴吧、同城在线等等文章的详情

    public static final String Get_Shop_Cat_List = ip + "action/appShoppingCart/selectCommodityCart";//获得购物车列表

    public static final String Delete_Shop_Cat = ip + "action/appShoppingCart/CommodityCart";//删除购物车商品

    public static final String address_default = ip + "action/addressAddressApp/addressModel";//默认地址

    public static final String submit_order = ip + "action/takeOutOrder/addTakeOutOrder";//提交订单

    public static final String Order_List = ip + "action/appOrderMessage/goodComment";//订单列表分类

    public static final String Order_List23 = ip + "action/appOrderMessage/goodComment23";//订单列表--待接单-配送中

    public static final String Order_Lists = ip + "action/appOrderMessage/goodComments";//订单列表全部

    public static final String getOrderDetails = ip + "action/takeOutOrder/getOrderDetails";//订单详情

    public static final String cancelOrder = ip + "action/takeOutOrder/cancelOrder";//取消订单

    public static final String returnOrder = ip + "action/takeOutOrder/returnOrder";//订单退货接口

    public static final String confirmOrder = ip + "action/takeOutOrder/confirmOrder";//订单确认送达接口

    public static final String yuePayOrder = ip + "action/takeOutOrder/yuePayOrder";//余额支付订单

    public static final String Index_TuiJian = ip + "action/apphomepage/storeMessage";//首页推荐


    public static final String All_Online = ip + "action/apponline/onlineInterface";//同城全部

    public static final String user_HeadUndo = ip + "action/appJionUsBusinessCheck/userHeadUndo";//上传企业营业执照

    public static final String Submit_Shop_Information = ip + "action/appJionUsBusinessCheck/insertAddress";//上传企业营业信息

    public static final String Get_Spell_Information = ip + "action/Lepingou/projectMessage";//获得拼购商品详情

    public static final String On_Submit_Booking = ip + "action/appOrderMessage/addTakeOutOrder";//提交订票订单



    public static final String storeMaterial =ip +"action/adminStoreCollectEnshrineApp/goods";  //店铺商品内页




    public static final String appraise_goodevaluate = ip + "action/appProductCopy/goodevaluate";//好评
    public static final String appraise_middleevaluate = ip + "action/appProductCopy/middleevaluate";//中评
    public static final String appraise_badevaluate = ip + "action/appProductCopy/badevaluate";//差评


    //flower    leisure     motion      life        fruite
/*    public static final String appraise_flower = ip + "action/appFlower/seekStroe";//花之语列表
    public static final String appraise_leisure = ip + "action/appEntertainment/seekStroe";//休闲娱乐列表
    public static final String appraise_motion = ip + "action/appExercise/seekStroe";//运动健康列表
    public static final String appraise_life = ip + "action/appLivelihood/seekStroe";//生活服务列表
    public static final String appraise_fruite = ip + "action/appFruits/seekStroe";//生鲜果蔬列表*/








    public static final String index_banner = ip + "action/apphomepage/getHomePage";//首页轮播图
    public static final String Journalism = ip + "action/apphomepage/getjournalism";//新闻\首页滚动播报
    public static final String spell_list = ip + "action/Lepingou/supermarket";//乐拼购商品列表
    //public static final String forum_list = ip + "action/appforum/getForum";//大爱旅行论坛贴吧列表
    public static final String forum_list = ip + "action/apponline/getArticleList";//同城在线文章
    public static final String luntan_list = ip + "action/appforum/getForum";//大爱旅行论坛贴吧


    public static final String booking_list = ip + "action/appscenicticket/getscenicticket";//大爱旅行景区订票列表
    public static final String booking_getDetail = ip + "action/appscenicticket/getDetail";//大爱旅行景区订票列表

    public static final String all_getDetail = ip + "action/appscenicticket/getWorldList";//大爱旅行全球签证办理列表

    public static final String Booking_Search = ip + "action/appscenicticket/getSearch";//景区列表搜索

    public static final String All_Search = ip + "action/appscenicticket/getWorld";//全球签证列表搜索

    public static final String Shoucang_Type = ip + "action/adminStoreCollectEnshrineApp/getCollectionOfShopsStatus";//获得店铺收藏状态

    public static final String Change_Type = ip + "action/appStoreCollection/addOrCancleShopCollection";//修改店铺收藏状态

    public static final String GET_Merchandise_Type = ip + "action/adminStoreCollectEnshrineApp/getCollectionOfGoodsStatus";//获得商品收藏状态

    public static final String Change_Merchandise_Type = ip + "action/appStoreCollection/addWishlist";//修改商品收藏状态


    public static final String Get_Spell_List = ip + "action/Lepingou/getLepinggouSearch";//获得乐拼购商品列表

    public static final String Get_Seek_Stroe = ip + "action/appEntertainment/seekStroe";//获得符合搜索条件的商家列表

    public static final String Get_All_Seek_Stroe = ip + "action/appSeek/seekStroe";//获得全部符合搜索条件的商家列表


    /**
     * 生活充值接口
     */
    public static final String phone1_charge = ip + "action/recharge_action/phoneOne";//话费充值--手机充值第一步
    public static final String phone2_charge = ip + "action/recharge_action/phoneTwo";//话费充值--手机充值第二步
    public static final String liuliang1_charge = ip + "action/recharge_action/flowOne";//流量充值--手机流量充值第一步
    public static final String liuliang2_charge = ip + "action/recharge_action/flowTwo";//流量充值--手机流量充值第二步
    public static final String games1_charge = ip + "action/recharge_action/gameOne";//游戏点卡充值--游戏充值第一步
    public static final String games2_charge = ip + "action/recharge_action/gameTwo";//游戏点卡充值--游戏充值第二步
    public static final String games3_charge = ip + "action/recharge_action/gameThree";//游戏点卡充值--游戏充值第二步
    public static final String fuel1_charge = ip + "action/recharge_action/refuelingCardOne";//加油卡充值--加油卡充值第一步
    public static final String fuel2_charge = ip + "action/recharge_action/refuelingCardTwo";//加油卡充值--加油卡充值第二步
    public static final String w_e_g1_charge = ip + "action/recharge_action/hydroelectricCoalOne";//水电煤费充值--水电煤费充值第一步
    public static final String w_e_g2_charge = ip + "action/recharge_action/hydroelectricCoalTwo";//水电煤费充值--水电煤费充值第二步
//    public static final String electric1_charge = ip + "action/recharge_action/phoneOne";//电费充值
//    public static final String electric2_charge = ip + "action/recharge_action/phoneOne";//电费充值
//    public static final String gas1_charge = ip + "action/recharge_action/phoneOne";//天然气充值
//    public static final String gas2_charge = ip + "action/recharge_action/phoneOne";//天然气充值

//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值
//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值
//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值
//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值
//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值
//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值
//    public static final String phone_charge = ip + "action/appscenicticket/getscenicticket";//充值







    //http://192.168.0.124:8080/DaoLe/action/appMember/registerMembers?phone=15868999998&password=123456

    //action/appMember/registerMembers
}
