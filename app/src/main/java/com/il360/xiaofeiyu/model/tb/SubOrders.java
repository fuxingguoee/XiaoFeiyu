package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;
/**商品信息*/
public class SubOrders implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private ItemInfo itemInfo;//商品
	private List<SubOperation> operations;//子菜单操作
	private PriceInfo priceInfo;//单价和原价
	private String quantity;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ItemInfo getItemInfo() {
		return itemInfo;
	}
	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}
	public List<SubOperation> getOperations() {
		return operations;
	}
	public void setOperations(List<SubOperation> operations) {
		this.operations = operations;
	}
	public PriceInfo getPriceInfo() {
		return priceInfo;
	}
	public void setPriceInfo(PriceInfo priceInfo) {
		this.priceInfo = priceInfo;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}



}
