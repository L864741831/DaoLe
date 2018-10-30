package com.tck.daole.entity;

import java.util.List;

/**
 * kylin on 2018/2/4.
 */

public class OrderList {
    public int status;
    public String message;
    public List<Shop> list;

    public static class Shop {
        public double payMoney;
        public String orderState;
        public List<TakeOutOrderProject> TakeOutOrderProject;
        public String adminStoreName;
        public String adminStoreId;
        public Double servicePrice;
        public int orderType;
        public String orderId;

        public Shop(String orderId,String adminStoreId,String adminStoreName,List<TakeOutOrderProject> TakeOutOrderProject,double payMoney){
            this.orderId = orderId;
            this.adminStoreId = adminStoreId;
            this.adminStoreName = adminStoreName;
            this.TakeOutOrderProject = TakeOutOrderProject;
            this.payMoney = payMoney;
        }
    }

    public class TakeOutOrderProject {
        public String orderProjectId;  //订单商品ID;
        public String orderProjectName;  //订单商品名字;
        public String orderProjectTitle;  //=订单商品标题
        public String orderProjectdetail;  //订单商品详情;
        public double orderProjectprice;  //=订单商品价格;
        public int orderProjectProjectNum;  //=订单商品购买数量
        public String orderProjectProjectImgae;  //=订单商品图片
//    public class Content {
//        public String orderProjectId;  //订单商品ID;
//        public String orderProjectName;  //订单商品名字;
//        public String orderProjectTitle;  //=订单商品标题
//        public String orderProjectdetail;  //订单商品详情;
//        public double orderProjectprice;  //=订单商品价格;
//        public int orderProjectProjectNum;  //=订单商品购买数量
//        public String orderProjectProjectImgae;  //=订单商品图片

    }
}


