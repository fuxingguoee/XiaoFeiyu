package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;

public class Stages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;//租赁套餐id
	private Integer stagesNumber;//天数
	private String stagesRate;//优惠 万分比 1=0.01%
	private Integer status;//0普通
	private String create_time;//创建时间
	private String update_time;//更新时间
	private String creater;//创建者
	private String updater;//修改者
	private String ext1;//套餐钱
	
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStagesNumber() {
		return stagesNumber;
	}
	public void setStagesNumber(Integer stagesNumber) {
		this.stagesNumber = stagesNumber;
	}
	public String getStagesRate() {
		return stagesRate;
	}
	public void setStagesRate(String stagesRate) {
		this.stagesRate = stagesRate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	
	
}
