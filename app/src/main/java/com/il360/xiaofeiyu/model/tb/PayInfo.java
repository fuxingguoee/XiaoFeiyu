package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;


public class PayInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String actualFee;
	private List<Icon> icons;
	private String postFees;
	private String postType;
	
	public String getActualFee() {
		return actualFee;
	}
	public void setActualFee(String actualFee) {
		this.actualFee = actualFee;
	}
	public String getPostFees() {
		return postFees;
	}
	public List<Icon> getIcons() {
		return icons;
	}
	public void setIcons(List<Icon> icons) {
		this.icons = icons;
	}
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public void setPostFees(String postFees) {
		this.postFees = postFees;
	}
	
	

}
