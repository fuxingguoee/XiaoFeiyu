package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;
import java.util.List;

public class ArrayOfSwitch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String desc;
	private List<MySwitch> switchConfigs;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<MySwitch> getSwitchConfigs() {
		return switchConfigs;
	}

	public void setSwitchConfigs(List<MySwitch> switchConfigs) {
		this.switchConfigs = switchConfigs;
	}
}
