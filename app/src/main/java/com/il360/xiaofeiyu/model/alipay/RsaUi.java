package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;
import java.util.Date;

public class RsaUi implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**id**/
	private Integer id;
	/**支付宝ua**/
	private String rsaUi;
	/**新增时间**/
	private Date createTime;
	/**支付宝token**/
	private String rdsToken;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRsaUi(String rsaUi) {
		this.rsaUi = rsaUi;
	}
	
	public String getRsaUi() {
		return this.rsaUi;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}

	public String getRdsToken() {
		return rdsToken;
	}

	public void setRdsToken(String rdsToken) {
		this.rdsToken = rdsToken;
	}
	
}
