package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.Date;

/** 订单创建时间 */
public class OrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String archive;
	private String b2C;//
	private String createDay;// 创建日期
	private Date createTime;// 创建时间
	private String id;

	public String getArchive() {
		return archive;
	}

	public void setArchive(String archive) {
		this.archive = archive;
	}

	public String getB2C() {
		return b2C;
	}

	public void setB2C(String b2c) {
		b2C = b2c;
	}

	public String getCreateDay() {
		return createDay;
	}

	public void setCreateDay(String createDay) {
		this.createDay = createDay;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
