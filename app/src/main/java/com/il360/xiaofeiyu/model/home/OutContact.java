package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;
import java.util.List;

public class OutContact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Contact> list;
	private List<String> appList;

	public List<Contact> getList() {
		return list;
	}

	public void setList(List<Contact> list) {
		this.list = list;
	}

	public List<String> getAppList() {
		return appList;
	}

	public void setAppList(List<String> appList) {
		this.appList = appList;
	}

}
