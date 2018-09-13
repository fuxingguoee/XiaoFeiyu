package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;

public class CreditPenaltyfee implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**penaltyfeeid**/
	private Integer penaltyfeeid;
	/**credit_id**/
	private Integer creditId;
	/**增加或减免滞纳金金额**/
	private Integer amount;
	/**1:增加滞纳金，2:减免滞纳金**/
	private Integer status;
	/**creationtime**/
	private String creationtime;
	/**ext1**/
	private Integer ext1;
	/**ext2**/
	private String ext2;
	/**ext3**/
	private String ext3;
	/**新增滞纳金时间**/
	private String occurDay;
	
	public void setPenaltyfeeid(Integer penaltyfeeid) {
		this.penaltyfeeid = penaltyfeeid;
	}
	
	public Integer getPenaltyfeeid() {
		return this.penaltyfeeid;
	}
	
	public void setCreditId(Integer creditId) {
		this.creditId = creditId;
	}
	
	public Integer getCreditId() {
		return this.creditId;
	}
	
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public Integer getAmount() {
		return this.amount;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}
	
	public String getCreationtime() {
		return this.creationtime;
	}
	
	public void setExt1(Integer ext1) {
		this.ext1 = ext1;
	}
	
	public Integer getExt1() {
		return this.ext1;
	}
	
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	
	public String getExt2() {
		return this.ext2;
	}
	
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	
	public String getExt3() {
		return this.ext3;
	}

	public String getOccurDay() {
		return occurDay;
	}

	public void setOccurDay(String occurDay) {
		this.occurDay = occurDay;
	}
	
}
