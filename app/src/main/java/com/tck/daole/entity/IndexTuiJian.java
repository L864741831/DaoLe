package com.tck.daole.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/4.
 */

public class IndexTuiJian {
    public int status;
    public String message;
    public TuiJianModel model;


    public class TuiJianModel {
        public List<PinGou> lepingou;
        public List<WaiMai> waimai;
        public List<Store> store;
    }

    public class PinGou {
        public String takeProjectId;
        public String takeProjectName;
        public String title;
        public String detail;
        public double price;
        public String saleNum;
        public String showhomepage;
        public String sort;
        public String type;
        public String projectImage;
    }

    public class WaiMai {
        public String adminStoreId; //商品所属店铺id
        public String takeProjectId;        //商品id
        public String takeProjectName;
        public String title;
        public String detail;
        public double price;
        public String saleNum;
        public String showhomepage;
        public String sort;
        public String type;
        public String projectImage;
    }

    public class Store {
        public String stoeId;   //店铺id
        public String logoImage;
        public String evaluateIdstarlevel;  //星级
        public String sellnumber;
        public String name;
        public double lowestPrice;
        public double servicePrice;
        public String jvLi;
        public String storeAddress; //店址
    }


}
