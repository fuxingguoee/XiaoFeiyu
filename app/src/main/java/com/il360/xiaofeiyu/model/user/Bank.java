package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

/**
 * Bank
 *
 */
public class Bank implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 银行编号 **/
	private String bankNo;
	/** 银行名称 **/
	private String bankName;
	/** 分行名称 **/
	private String branchName;
	/** 地区编码 **/
	private String areaName;
	/** 市名称 **/
	private String cityName;
	/** 省名称 **/
	private String provinceName;
	
	/** 银行代码 **/
	private String bankCode;

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankNo() {
		return this.bankNo;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
