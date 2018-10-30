package com.tck.daole.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */

public class ShopCat {
    public int status;
    public String message;
    public List<ShopName> list; //店铺列表

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ShopName> getList() {
        return list;
    }

    public void setList(List<ShopName> list) {
        this.list = list;
    }

    public static class ShopName {
        public String AdminStoreOid;    //店铺id
        public String name; //店铺名称

        public double lowestPrice;  //起送价格
        public double servicePrice; //配送价格

        public List<ShopContent> business;  //商品列表

        public ShopName(String AdminStoreOid,String name,List<ShopContent> business,double lowestPrice,double servicePrice){
            this.AdminStoreOid = AdminStoreOid;
            this.name = name;
            this.business = business;
            this.lowestPrice = lowestPrice;
            this.servicePrice = servicePrice;
        }
        
    }

    public static class ShopContent{
        public String oid;  //购物车oid
        public String projectOid;   //店铺oid
        public String takeProjectName; //name=商品名称
        public String mainImage;    //mainImage=商品照片
        public double price;   //price=价格
        public int purchaseNumber;  //purchaseNumber=购买数量
        public String detail;       //商品详情备注

        //1.商品oid   2.商品图片  3.商品名称  4.商品备注  5.商品数量  6.商品价格
        public ShopContent(String projectOid,String mainImage,String takeProjectName,String detail,int purchaseNumber,double price){
            this.projectOid = projectOid;
            this.mainImage = mainImage;
            this.takeProjectName = takeProjectName;
            this.detail = detail;
            this.purchaseNumber = purchaseNumber;
            this.price = price;

        }

    }
}

