package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
/**商品ID、标题等信息*/
public class ItemInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String itemUrl;
	private String pic;
	private String serviceIcons;
	private String skuId;
	private String skuText;
	private String snapUrl;
	private String title;
	private String xtCurrent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItemUrl() {
		return itemUrl;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getServiceIcons() {
		return serviceIcons;
	}
	public void setServiceIcons(String serviceIcons) {
		this.serviceIcons = serviceIcons;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuText() {
		return skuText;
	}
	public void setSkuText(String skuText) {
		this.skuText = skuText;
	}
	public String getSnapUrl() {
		return snapUrl;
	}
	public void setSnapUrl(String snapUrl) {
		this.snapUrl = snapUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getXtCurrent() {
		return xtCurrent;
	}
	public void setXtCurrent(String xtCurrent) {
		this.xtCurrent = xtCurrent;
	}
	
	

}
