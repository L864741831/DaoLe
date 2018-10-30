package com.tck.daole.entity;

import java.util.List;

/**
 * kylin on 2018/2/4.
 */

public class OrderDetails {
    public int status;
    public String message;
    public TakeOutOrder models;


    public class TakeOutOrder {
        public Shop shop;
        public int orderState;
        public Address address;
        public String oid;
        public List<Goods> goods;
        public String orderNum;
        public String payDate;
        public String submitTime;
        public String remarks;
        public String payMoney;
        public String payType;
        public int type;
        public int yujishouru;
    }

    public class Shop {
        public String address;
        public String oid;
        public String name;
        public String dimensionality;
        public String longitude;
    }

    public class Address {
        public String phone;
        public String address;
        public String oid;
        public String people;
    }

    public class Goods {
        public String title;
        public String detail;
        public int num;
        public int price;
        public String imagePath;
        public String name;
        public String poid;
    }
}


