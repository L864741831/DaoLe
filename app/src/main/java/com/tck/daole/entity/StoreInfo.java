package com.tck.daole.entity;

/**
 * 店铺信息
 */
public class StoreInfo
{
	protected String Id;
	protected String name;
	protected boolean isChoosed;
    private boolean isEdtor;
	public double lowestPrice;	//起送价格
	public double servicePrice;	//配送价格



	public boolean isEdtor() {
		return isEdtor;
	}

	public void setIsEdtor(boolean isEdtor) {
		this.isEdtor = isEdtor;
	}

	public StoreInfo(String id, String name,double lowestPrice,double servicePrice) {
		Id = id;
		this.name = name;
		this.lowestPrice = lowestPrice;
		this.servicePrice = servicePrice;

	}

	public double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
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
}
