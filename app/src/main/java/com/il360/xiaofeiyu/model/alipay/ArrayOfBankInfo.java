package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;
import java.util.List;

public class ArrayOfBankInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<BankInfo> results;
	private String stat;

	public List<BankInfo> getResults() {
		return results;
	}

	public void setResults(List<BankInfo> results) {
		this.results = results;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

}
