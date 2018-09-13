package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

/**账户余额,余额宝信息*/
public class TbAlipayInfo2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TbAlipayInfo2Data data;

	public TbAlipayInfo2Data getData() {
		return data;
	}

	public void setData(TbAlipayInfo2Data data) {
		this.data = data;
	}
	
}
