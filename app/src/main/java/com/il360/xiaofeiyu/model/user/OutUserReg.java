package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.util.Date;

public class OutUserReg implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	/** 用户id **/
	private String token;
	/** 身份证正面图片url1 **/
	private String identityUrl1;
	/** 身份证反面 **/
	private String identityUrl2;
	/** 手持身份证照片 **/
	private String qualificationUrl;
	/** 姓名 **/
	private String name;
	/** 手机号码 **/
	private String phone;
	/** 来源 **/
	private String type;
	/** 审核次数 **/
	private Integer count;
	/****/
	private Integer regId;
	/** 身份证 **/
	private String idNo;
	/** 所在城市 **/
	private String idCity;
	/** 身份证地址 **/
	private String idAddress;
	/** 性别0女 1男 **/
	private Integer sex;
	/** 出生年月 **/
	private Date birth;
	/** 学历 **/
	private String education;
	/** 婚姻状况 **/
	private String marriage;
	
    /** 腾讯云身份证正面url**/
    private String identityPic1;
    /**腾讯云身份证反面url**/
    private String identityPic2;
    /** 腾讯云手持身份证图片url**/
    private String qualificationPic;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIdentityUrl1() {
		return identityUrl1;
	}

	public void setIdentityUrl1(String identityUrl1) {
		this.identityUrl1 = identityUrl1;
	}

	public String getIdentityUrl2() {
		return identityUrl2;
	}

	public void setIdentityUrl2(String identityUrl2) {
		this.identityUrl2 = identityUrl2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	public String getQualificationUrl() {
		return qualificationUrl;
	}

	public void setQualificationUrl(String qualificationUrl) {
		this.qualificationUrl = qualificationUrl;
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
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

}
