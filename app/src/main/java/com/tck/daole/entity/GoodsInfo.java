package com.tck.daole.entity;

/**
 * 商品信息
 */
public class GoodsInfo {
    protected String Id;
    protected String name;
    protected boolean isChoosed;
    private String imageUrl;
    private String desc;
    private double price;
    private int count;
    private int position;// 绝对位置，只在ListView构造的购物车中，在删除时有效
    private String color;
    private String size;
    private int goodsImg;
    private double discountPrice;

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(int goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public GoodsInfo(String id, String name, String desc, double price, int count, String color,
                     String size, int goodsImg, double discountPrice) {
        Id = id;    //购物车可以删除的id
        this.name = name;       // 商品详情的id
        this.desc = desc;//名称
        this.price = price;//价格
        this.count = count; //商品数量
        this.color=color;       //这个当做图片地址
        this.size=size; //这个当做备注
        this.goodsImg=goodsImg;
        this.discountPrice=discountPrice;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
