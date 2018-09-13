package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class MySwitch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String switchKey;
	private String switchValue;

	public String getSwitchKey() {
		return switchKey;
	}

	public void setSwitchKey(String switchKey) {
		this.switchKey = switchKey;
	}

	public String getSwitchValue() {
		return switchValue;
	}

	public void setSwitchValue(String switchValue) {
		this.switchValue = switchValue;
	}
}
