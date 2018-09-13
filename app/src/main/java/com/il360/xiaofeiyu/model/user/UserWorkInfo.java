package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

public class UserWorkInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户**/
	private Integer userId;
	/**工作地址**/
	private String workAddress;
	/**工作单位**/
	private String workName;
	/**工作岗位**/
	private String workStation;
	/**备注**/
	private String remark;
	
	/**工作电话**/
	private String workPhone;
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	
	public String getWorkAddress() {
		return this.workAddress;
	}
	
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	
	public String getWorkName() {
		return this.workName;
	}
	
	public void setWorkStation(String workStation) {
		this.workStation = workStation;
	}
	
	public String getWorkStation() {
		return this.workStation;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
}
