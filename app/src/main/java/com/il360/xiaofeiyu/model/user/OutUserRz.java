package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

public class OutUserRz implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户id**/
	private Integer userId;
	/**实名认证 0未认证 1认证通过 -1认证不通过 2待审核**/
	private Integer nameRz;
	/**运营商认证0 未认证 1认证通过 -1 认证不通过 2等待风控结果 3等待人工审核**/
	private Integer phoneRz;
	/**支付宝认证0未认证 1认证通过 -1 认证不通过 2等待风控结果**/
	private Integer zfbRz;
	/**银行卡认证0未认证 1认证通过 -1 认证不通过 2待审核**/
	private Integer bankRz;
	
	private Integer contactCount;//是否上传通讯录1是 2否
	private Integer relationCount;//父母配偶的手机号码是否上传 1是2否
	
	private Integer serial;//0打回或者为上传序列号 1已上传并不可修改
	
	private Integer appCount;//判断是否上传app 1是 2否
	
	private String nameDesc;//实名认证理由
	private String name;//姓名
	private String idNo;//身份证号
	private Integer hbAmount;//用户花呗可贷金额
	
	/**芝麻信用评分 0未获取 1已获取**/  
	private Integer zmxyRz;
	/**淘宝认证0未认证 1认证通过 -1 认证不通过 2等待风控结果**/
	private Integer taobaoRz;
	/**京东认证0未认证 1认证通过 -1 认证不通过 2等待风控结果**/
	private Integer jdRz;
	
	private Integer payPwd;//1已设置支付密码2未设置支付密码


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getNameRz() {
		return nameRz;
	}

	public void setNameRz(Integer nameRz) {
		this.nameRz = nameRz;
	}

	public Integer getPhoneRz() {
		return phoneRz;
	}

	public void setPhoneRz(Integer phoneRz) {
		this.phoneRz = phoneRz;
	}

	public Integer getZfbRz() {
		return zfbRz;
	}

	public void setZfbRz(Integer zfbRz) {
		this.zfbRz = zfbRz;
	}

	public Integer getBankRz() {
		return bankRz;
	}

	public void setBankRz(Integer bankRz) {
		this.bankRz = bankRz;
	}

	public Integer getContactCount() {
		return contactCount;
	}

	public void setContactCount(Integer contactCount) {
		this.contactCount = contactCount;
	}

	public Integer getRelationCount() {
		return relationCount;
	}

	public void setRelationCount(Integer relationCount) {
		this.relationCount = relationCount;
	}

	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}

	public Integer getAppCount() {
		return appCount;
	}

	public void setAppCount(Integer appCount) {
		this.appCount = appCount;
	}

	public String getNameDesc() {
		return nameDesc;
	}

	public void setNameDesc(String nameDesc) {
		this.nameDesc = nameDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Integer getHbAmount() {
		return hbAmount;
	}

	public void setHbAmount(Integer hbAmount) {
		this.hbAmount = hbAmount;
	}

	public Integer getZmxyRz() {
		return zmxyRz;
	}

	public void setZmxyRz(Integer zmxyRz) {
		this.zmxyRz = zmxyRz;
	}

	public Integer getTaobaoRz() {
		return taobaoRz;
	}

	public void setTaobaoRz(Integer taobaoRz) {
		this.taobaoRz = taobaoRz;
	}

	public Integer getJdRz() {
		return jdRz;
	}

	public void setJdRz(Integer jdRz) {
		this.jdRz = jdRz;
	}

	public Integer getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(Integer payPwd) {
		this.payPwd = payPwd;
	}
	
	
}
