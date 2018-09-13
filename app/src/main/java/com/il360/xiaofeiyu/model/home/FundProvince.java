package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;
import java.util.List;

public class FundProvince implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer pid;
	private String pname;
	private List<FundCity> cityList;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public List<FundCity> getCityList() {
		return cityList;
	}
	public void setCityList(List<FundCity> cityList) {
		this.cityList = cityList;
	}

}
