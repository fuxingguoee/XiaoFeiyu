package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;

public class BankInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String signId;//绑定关系id
	private String linkUrl;
	private String channelType;
	private String providerDesc;
	private String level;
	private String providerUserId;
	private String bankProductType;//(目前和cardType返回一样)
	private String cardType;//卡类型（CC=信用卡，DC=储蓄卡）
	private String cardTypeName;//卡类型名称
	private String providerUserName;///银行卡尾号
	private String showUserName;//持卡人
	private String providerName; //银行名称
	private String providerTel;//银行电话
	private Object providerType;//银行类型
	private String providerId;//银行编号
	private String partnerId;//银行ID（ABC,CMB...）
	private String logoLarge;//银行logo 大
	private String logoMiddle;//银行logo 中
	private String logoMini;//银行logo 小
	private String encryptProviderUserId;//绑定银行卡用户id（加密过的）
	private String encryptCardNo;//银行卡号（加密过的）
	
	public String getProviderUserName() {
		return providerUserName;
	}
	public void setProviderUserName(String providerUserName) {
		this.providerUserName = providerUserName;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public Object getProviderType() {
		return providerType;
	}
	public void setProviderType(Object providerType) {
		this.providerType = providerType;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getProviderDesc() {
		return providerDesc;
	}
	public void setProviderDesc(String providerDesc) {
		this.providerDesc = providerDesc;
	}
	public String getLogoLarge() {
		return logoLarge;
	}
	public void setLogoLarge(String logoLarge) {
		this.logoLarge = logoLarge;
	}
	public String getBankProductType() {
		return bankProductType;
	}
	public void setBankProductType(String bankProductType) {
		this.bankProductType = bankProductType;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getProviderUserId() {
		return providerUserId;
	}
	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getLogoMiddle() {
		return logoMiddle;
	}
	public void setLogoMiddle(String logoMiddle) {
		this.logoMiddle = logoMiddle;
	}
	public String getProviderTel() {
		return providerTel;
	}
	public void setProviderTel(String providerTel) {
		this.providerTel = providerTel;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getShowUserName() {
		return showUserName;
	}
	public void setShowUserName(String showUserName) {
		this.showUserName = showUserName;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getLogoMini() {
		return logoMini;
	}
	public void setLogoMini(String logoMini) {
		this.logoMini = logoMini;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getEncryptProviderUserId() {
		return encryptProviderUserId;
	}
	public void setEncryptProviderUserId(String encryptProviderUserId) {
		this.encryptProviderUserId = encryptProviderUserId;
	}
	public String getEncryptCardNo() {
		return encryptCardNo;
	}
	public void setEncryptCardNo(String encryptCardNo) {
		this.encryptCardNo = encryptCardNo;
	}
	
}
