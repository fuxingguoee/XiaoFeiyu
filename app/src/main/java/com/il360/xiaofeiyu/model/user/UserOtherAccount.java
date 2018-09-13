package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

public class UserOtherAccount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**3运营商4网银**/
	private Integer assId;
	/**用户**/
	private Integer userId;
	/**帐户类型 1:运营商 2:工资账单 3:汽车按揭账单 4:房屋按揭账单 5:银行流水账单 6:社保账单 7:蚂蚁花呗/支付宝 8:京东白条**/
	private Integer accountType;
	/**账号**/
	private String accountName;
	/**密码**/
	private String accountPwd;
	/**网银银行**/
	private String accountBelong;
	/**审核状态**/
	private Integer status;
	/**是否自动**/
	private Boolean isAuto;
	/**备注**/
	private String remark;
	/**创建时间**/
	private String createTime;
	/**更新时间**/
	private String updateTime;
	/**分控有效时间**/
	private String userDateValidate;
	
	/**信用卡账户名**/
	private String creditName;
	/**信用卡账户密码**/
	private String creditPwd;
	/**信用卡时填所属银行**/
	private String creditBelong;
	
	private String bizExt1;
	private String bizExt2;
	private String bizExt3;
	private String bizExt4;
	
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	
	public Integer getAccountType() {
		return this.accountType;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
	
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
	
	public String getAccountPwd() {
		return this.accountPwd;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public Boolean getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(Boolean isAuto) {
		this.isAuto = isAuto;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAccountBelong() {
		return accountBelong;
	}

	public void setAccountBelong(String accountBelong) {
		this.accountBelong = accountBelong;
	}

	public Integer getAssId() {
		return assId;
	}

	public void setAssId(Integer assId) {
		this.assId = assId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getBizExt1() {
		return bizExt1;
	}

	public void setBizExt1(String bizExt1) {
		this.bizExt1 = bizExt1;
	}

	public String getBizExt2() {
		return bizExt2;
	}

	public void setBizExt2(String bizExt2) {
		this.bizExt2 = bizExt2;
	}

	public String getBizExt3() {
		return bizExt3;
	}

	public void setBizExt3(String bizExt3) {
		this.bizExt3 = bizExt3;
	}

	public String getBizExt4() {
		return bizExt4;
	}

	public void setBizExt4(String bizExt4) {
		this.bizExt4 = bizExt4;
	}

	public String getUserDateValidate() {
		return userDateValidate;
	}

	public void setUserDateValidate(String userDateValidate) {
		this.userDateValidate = userDateValidate;
	}

	public String getCreditName() {
		return creditName;
	}

	public void setCreditName(String creditName) {
		this.creditName = creditName;
	}

	public String getCreditPwd() {
		return creditPwd;
	}

	public void setCreditPwd(String creditPwd) {
		this.creditPwd = creditPwd;
	}

	public String getCreditBelong() {
		return creditBelong;
	}

	public void setCreditBelong(String creditBelong) {
		this.creditBelong = creditBelong;
	}
}
