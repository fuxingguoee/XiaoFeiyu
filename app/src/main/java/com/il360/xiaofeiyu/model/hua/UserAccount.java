package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserAccount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**id**/
	private Integer id;
	/**用户id**/
	private Integer userId;
	/**账号**/
	private String accountNumber;
	/**密码**/
	private String accountPwd;
	/**1花呗认证2学信网认证**/
	private Integer type;
	/**创建时间**/
	private Date createTime;
	/**审核员**/
	private String auditor;
	/**审核时间**/
	private Date auditorTime;
	/**0待审核；-2审核打回(不能提交)；-1审核打回(能提交)；1已审核**/
	private Integer status;
	/**理由**/
	private String auditDesc;
	/**可贷款额度**/
	private BigDecimal amount;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
	
	public String getAccountPwd() {
		return this.accountPwd;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}
	
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	
	public String getAuditor() {
		return this.auditor;
	}
	
	public void setAuditorTime(Date auditorTime) {
		this.auditorTime = auditorTime;
	}
	
	public Date getAuditorTime() {
		return this.auditorTime;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}
	
	public String getAuditDesc() {
		return this.auditDesc;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
}
