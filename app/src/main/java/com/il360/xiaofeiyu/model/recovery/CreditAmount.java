package com.il360.xiaofeiyu.model.recovery;

import java.io.Serializable;

public class CreditAmount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer creditid;
	private Integer iphonetype;
	private Integer amount;
	private String creationtime;
	private String creator;
	private String phoneGeneration;
	
	private String recoveryPic;//手机图片
	private Integer status;//1回收0不回收

	public Integer getCreditid() {
		return creditid;
	}

	public void setCreditid(Integer creditid) {
		this.creditid = creditid;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPhoneGeneration() {
		return phoneGeneration;
	}

	public void setPhoneGeneration(String phoneGeneration) {
		this.phoneGeneration = phoneGeneration;
	}

	public Integer getIphonetype() {
		return iphonetype;
	}

	public void setIphonetype(Integer iphonetype) {
		this.iphonetype = iphonetype;
	}

	public String getRecoveryPic() {
		return recoveryPic;
	}

	public void setRecoveryPic(String recoveryPic) {
		this.recoveryPic = recoveryPic;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
