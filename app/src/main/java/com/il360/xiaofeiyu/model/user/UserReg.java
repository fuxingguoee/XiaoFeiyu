package com.il360.xiaofeiyu.model.user;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.il360.xiaofeiyu.model.hua.OutUserBank;



/**
 * 实名认证信息
 * User: Wanghk
 * Date: 15-3-18
 * Time: 下午12:55
 */
public class UserReg implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /**主键**/
    private Integer userRegId;
    /**用户id**/
    private Integer userId;
    /**身份证正面图片url**/
    private byte[] identityUrl1;
    /**身份证反面图片url**/
    private byte[] identityUrl2;
    /**资质图片url**/
    private byte[] qualificationUrl;
    /**姓名**/
    private String realName;
    /**性别**/
    private Integer sex;
    /**出生年月**/
    private String birth;
    /**审核人id**/
    private Integer auditId;
    /**审核人名称**/
    private String auditor;
    /**审核时间**/
    private String auditTime;
    /**状态 0初始 1审核通过 -1审核打回**/
    private Integer status;
    /**创建时间**/
    private String createTime;
    /**更新时间**/
    private String updateTime;
    /**审核退回原因**/
    private String auditDesc;
    
    /**身份证**/
    private String idNo;
    /**身份证所在城市**/
    private String idCity;
    /**身份证地址**/
    private String idAddress;

    private String identityStr1;
    private String identityStr2;
    private String qualificationStr;
    
    private String collectionPwd;
    private String inviteCode;
    private List<OutUserBank> bankList;
    private Integer contactCount;//是否上传通讯录1是 2否
   
    private String serviceNumber;//手机服务号
	private String phone;
	
	private Integer relationCount;//父母配偶的手机号码是否上传 1是2否
	
	private Integer ext5;//是否通过审核：0未知状态 1过 2 不过 3目前还不支持，转人工，原来流程
	private Integer ext4;//0已经过期，需要抓取数据 1未过期不用抓取数据
	
	private Integer serial;//0打回或者为上传序列号 1已上传并不可修改
	
	private Integer appCount;//判断是否上传app 1是 2否
	
    /** 腾讯云身份证正面url**/
    private String identityPic1;
    /**腾讯云身份证反面url**/
    private String identityPic2;
    /** 腾讯云手持身份证图片url**/
    private String qualificationPic;
    /** 0已下载到本地1需要下载到本地**/
    private Integer picType;
    
	/**允许贷款额度**/
	private BigDecimal userAmount;
    
    
	public Integer getUserRegId() {
		return userRegId;
	}
	public void setUserRegId(Integer userRegId) {
		this.userRegId = userRegId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public byte[] getIdentityUrl1() {
		return identityUrl1;
	}
	public void setIdentityUrl1(byte[] identityUrl1) {
		this.identityUrl1 = identityUrl1;
	}
	public byte[] getIdentityUrl2() {
		return identityUrl2;
	}
	public void setIdentityUrl2(byte[] identityUrl2) {
		this.identityUrl2 = identityUrl2;
	}
	public byte[] getQualificationUrl() {
		return qualificationUrl;
	}
	public void setQualificationUrl(byte[] qualificationUrl) {
		this.qualificationUrl = qualificationUrl;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public Integer getAuditId() {
		return auditId;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getAuditDesc() {
		return auditDesc;
	}
	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getIdCity() {
		return idCity;
	}
	public void setIdCity(String idCity) {
		this.idCity = idCity;
	}
	public String getIdAddress() {
		return idAddress;
	}
	public void setIdAddress(String idAddress) {
		this.idAddress = idAddress;
	}
	public String getIdentityStr1() {
		return identityStr1;
	}
	public void setIdentityStr1(String identityStr1) {
		this.identityStr1 = identityStr1;
	}
	public String getIdentityStr2() {
		return identityStr2;
	}
	public void setIdentityStr2(String identityStr2) {
		this.identityStr2 = identityStr2;
	}
	public String getQualificationStr() {
		return qualificationStr;
	}
	public void setQualificationStr(String qualificationStr) {
		this.qualificationStr = qualificationStr;
	}
	public String getCollectionPwd() {
		return collectionPwd;
	}
	public void setCollectionPwd(String collectionPwd) {
		this.collectionPwd = collectionPwd;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public List<OutUserBank> getBankList() {
		return bankList;
	}
	public void setBankList(List<OutUserBank> bankList) {
		this.bankList = bankList;
	}
	public Integer getContactCount() {
		return contactCount;
	}
	public void setContactCount(Integer contactCount) {
		this.contactCount = contactCount;
	}
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getRelationCount() {
		return relationCount;
	}
	public void setRelationCount(Integer relationCount) {
		this.relationCount = relationCount;
	}
	public Integer getExt5() {
		return ext5;
	}
	public void setExt5(Integer ext5) {
		this.ext5 = ext5;
	}
	public Integer getExt4() {
		return ext4;
	}
	public void setExt4(Integer ext4) {
		this.ext4 = ext4;
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
	public String getIdentityPic1() {
		return identityPic1;
	}
	public void setIdentityPic1(String identityPic1) {
		this.identityPic1 = identityPic1;
	}
	public String getIdentityPic2() {
		return identityPic2;
	}
	public void setIdentityPic2(String identityPic2) {
		this.identityPic2 = identityPic2;
	}
	public String getQualificationPic() {
		return qualificationPic;
	}
	public void setQualificationPic(String qualificationPic) {
		this.qualificationPic = qualificationPic;
	}
	public Integer getPicType() {
		return picType;
	}
	public void setPicType(Integer picType) {
		this.picType = picType;
	}
	public BigDecimal getUserAmount() {
		return userAmount;
	}
	public void setUserAmount(BigDecimal userAmount) {
		this.userAmount = userAmount;
	}
}
