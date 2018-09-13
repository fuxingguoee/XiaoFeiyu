package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserRecovery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	private String assessmentDetails;//评估详情
	private String recoveryType;//回收方式 默认快递
	private String phone;//联系电话
	private BigDecimal amount;//估值金额
	private String phoneName;//手机名称
	private String recoveryId;//回收id
	private String createTime;//下单时间
	private Integer status;//状态0已下单1已收货2已完成评估3待打款4已完成交易5已寄回-1取消订单
	private String expressNo;//快递单号
	private String expressCompany;//快递公司
	private String expressNo2;//退回快递单号
	private String expressCompany2;//退回快递公司
	private String orderNo;//订单号
	private String ext1;
	private String ext2;
	
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getAssessmentDetails() {
		return assessmentDetails;
	}
	public void setAssessmentDetails(String assessmentDetails) {
		this.assessmentDetails = assessmentDetails;
	}
	public String getRecoveryType() {
		return recoveryType;
	}
	public void setRecoveryType(String recoveryType) {
		this.recoveryType = recoveryType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPhoneName() {
		return phoneName;
	}
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	public String getRecoveryId() {
		return recoveryId;
	}
	public void setRecoveryId(String recoveryId) {
		this.recoveryId = recoveryId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getExpressCompany() {
		return expressCompany;
	}
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}
	public String getExpressNo2() {
		return expressNo2;
	}
	public void setExpressNo2(String expressNo2) {
		this.expressNo2 = expressNo2;
	}
	public String getExpressCompany2() {
		return expressCompany2;
	}
	public void setExpressCompany2(String expressCompany2) {
		this.expressCompany2 = expressCompany2;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
	
}
