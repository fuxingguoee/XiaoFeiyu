package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户id**/
	private Integer userId;
	/**登录帐号**/
	private String loginName;
	/**密码**/
	private String loginPwd;
	/**真实姓名**/
	private String userName;
	/**手机号码**/
	private String phone;
	/**有效状态**/
	private Integer status;	
	/**注册类型 0普通 1认证用户**/
	private Integer regType;
	/**注册时间**/
	private String regTime;
	/**性别 1：男 0女**/
	private Boolean sex;
	/**手机IMEI码,ip或其他**/
	private String lastTag;
	/**最后登录时间**/
	private String lastLogin;
	/**手机服务号**/
	private String serviceNumber;
	/**app用户的友盟设备号**/
	private String deviceTokens;
	/**过期时间**/
	private String expiredate;
	/**0已经过期，需要抓取数据 1未过期不用抓取数据**/
	private Integer ext4;
	/**是否通过审核：0未知状态 1过 2 不过 3目前还不支持，转人工，原来流程**/
	private Integer ext5;

	
	private String token;
	   /**用户头像url**/
    private String userPic;

    private Long lastTime;
    
    private Integer regStatus;//审核状态
    
    private String auditTime;//审核时间
    
    private String auditor;//认证人
    private String startTime;//时间
    private String endTime;//时间
    private String createTime;//时间
    
    private Integer creditId;
    private Integer amount;
    private String startday;
    private String endday;
    private Integer phonetype;
    private String bankno;
    private String bankname;
    private String branchno;
    private String branchname;
    
    private Integer close;
    private String idno;
    
    private Integer submitStatus;//贷款记录用户提交的状态
	private String agentNo;
	
	private String txyUserPic;//腾讯云头像地址
	
	private Integer hbAmount;//花呗可贷金额
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRegType() {
		return regType;
	}
	public void setRegType(Integer regType) {
		this.regType = regType;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public String getLastTag() {
		return lastTag;
	}
	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public String getDeviceTokens() {
		return deviceTokens;
	}
	public void setDeviceTokens(String deviceTokens) {
		this.deviceTokens = deviceTokens;
	}
	public String getExpiredate() {
		return expiredate;
	}
	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}
	public Integer getExt4() {
		return ext4;
	}
	public void setExt4(Integer ext4) {
		this.ext4 = ext4;
	}
	public Integer getExt5() {
		return ext5;
	}
	public void setExt5(Integer ext5) {
		this.ext5 = ext5;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public Long getLastTime() {
		return lastTime;
	}
	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
	public Integer getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(Integer regStatus) {
		this.regStatus = regStatus;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getCreditId() {
		return creditId;
	}
	public void setCreditId(Integer creditId) {
		this.creditId = creditId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getStartday() {
		return startday;
	}
	public void setStartday(String startday) {
		this.startday = startday;
	}
	public String getEndday() {
		return endday;
	}
	public void setEndday(String endday) {
		this.endday = endday;
	}
	public Integer getPhonetype() {
		return phonetype;
	}
	public void setPhonetype(Integer phonetype) {
		this.phonetype = phonetype;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBranchno() {
		return branchno;
	}
	public void setBranchno(String branchno) {
		this.branchno = branchno;
	}
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
	public Integer getClose() {
		return close;
	}
	public void setClose(Integer close) {
		this.close = close;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public Integer getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getTxyUserPic() {
		return txyUserPic;
	}
	public void setTxyUserPic(String txyUserPic) {
		this.txyUserPic = txyUserPic;
	}
	public Integer getHbAmount() {
		return hbAmount;
	}
	public void setHbAmount(Integer hbAmount) {
		this.hbAmount = hbAmount;
	}
	
}
