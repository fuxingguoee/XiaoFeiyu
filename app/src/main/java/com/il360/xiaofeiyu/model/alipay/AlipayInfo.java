package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlipayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<BillList> billList = new ArrayList<BillList>();
	private AliUserInfo userInfo;
	private List<AliBank> bankList;

	public List<BillList> getBillList() {
		return billList;
	}

	public void setBillList(List<BillList> billList) {
		this.billList = billList;
	}

	public AliUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(AliUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public List<AliBank> getBankList() {
		return bankList;
	}

	public void setBankList(List<AliBank> bankList) {
		this.bankList = bankList;
	}

}
