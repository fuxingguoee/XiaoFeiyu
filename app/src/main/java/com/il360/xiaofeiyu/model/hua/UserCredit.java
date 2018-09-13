package com.il360.xiaofeiyu.model.hua;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class UserCredit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/** creditId **/
	private Integer creditId;
	/** 用户id **/
	private Integer userId;
	/** 借款金额 **/
	private Integer amount;
	/** 开始日期 **/
	private Date startday;
	/** 结束日期 **/
	private Date endday;
	/** 0：正常,1:逾期未还，2逾期还清，3：违约，4贷款中， 5贷款拒绝，6.等待下款 ，7：续借 ，8：人工贷款申请，9:贷款申请中 **/
	/** 0：正常,1:逾期未还，2逾期还清，3：违约，4贷款中， 5等待审核，7:贷款拒绝 ，8:等待下款 ， 12：续借 **/
	private Integer status;
	/** 0:结束，关闭.2，贷款中。9申请贷款中 **/
	/** 0:结束，关闭. 1:未关闭，2，贷款中。3,，贷款被拒绝而结束。9申请贷款中 **/
	private Integer close;
	/** modifier **/
	private String modifier;
	/** modifytime **/
	private String modifytime;
	/** 手机类型 **/
	private Integer phonetype;
	/** 手续费 **/
	private BigDecimal fee;
	/** 提交状态 1已绑定iCloud **/
	private Integer submitStatus;
	/** 拒绝理由 **/
	private String ext2;
	/** 贷款状态 **/
	private String chstatus;
	private String chclose;
	/** 手机类型 **/
	private String chphonetype;
	/** Icloud账号 **/
	private String appleid;
	/** 密码 **/
	private String applepwd;
	/** 贷款类型 1不抵押手机 2抵押手机 **/
	private Integer type;
	
	private String frmstartday;
	private String frmendday;
	private Long createTime;

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public Integer getCreditId() {
		return creditId;
	}

	public void setCreditId(Integer creditId) {
		this.creditId = creditId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getStartday() {
		return startday;
	}

	public void setStartday(Date startday) {
		this.startday = startday;
	}

	public Date getEndday() {
		return endday;
	}

	public void setEndday(Date endday) {
		this.endday = endday;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getClose() {
		return close;
	}

	public void setClose(Integer close) {
		this.close = close;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public Integer getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(Integer phonetype) {
		this.phonetype = phonetype;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Integer getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getChstatus() {
		return chstatus;
	}

	public void setChstatus(String chstatus) {
		this.chstatus = chstatus;
	}

	public String getChclose() {
		return chclose;
	}

	public void setChclose(String chclose) {
		this.chclose = chclose;
	}

	public String getChphonetype() {
		return chphonetype;
	}

	public void setChphonetype(String chphonetype) {
		this.chphonetype = chphonetype;
	}

	public String getAppleid() {
		return appleid;
	}

	public void setAppleid(String appleid) {
		this.appleid = appleid;
	}

	public String getApplepwd() {
		return applepwd;
	}

	public void setApplepwd(String applepwd) {
		this.applepwd = applepwd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFrmendday() {
		return frmendday;
	}

	public void setFrmendday(String frmendday) {
		this.frmendday = frmendday;
	}

	public String getFrmstartday() {
		return frmstartday;
	}

	public void setFrmstartday(String frmstartday) {
		this.frmstartday = frmstartday;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
