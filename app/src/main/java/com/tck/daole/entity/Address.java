package com.tck.daole.entity;

/**
 * Created by Administrator on 2018/1/20.
 */

/**
 * 收货地址
 */
public class Address {
    public String oid;    //oid主键
    public String people;   //收货人
    public boolean isChecked; //每条item的状态

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
