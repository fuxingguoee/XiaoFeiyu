package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;
/**订单返回数据的所有数据*/
public class ConsumeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String error;
//	private String extra;
	private List<MainOrders> mainOrders;//主订单
	private InfoPage page;
	private String query;
	private List<Tab> tabs;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
//	public String getExtra() {
//		return extra;
//	}
//	public void setExtra(String extra) {
//		this.extra = extra;
//	}
	public List<MainOrders> getMainOrders() {
		return mainOrders;
	}
	public void setMainOrders(List<MainOrders> mainOrders) {
		this.mainOrders = mainOrders;
	}
	public InfoPage getPage() {
		return page;
	}
	public void setPage(InfoPage page) {
		this.page = page;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<Tab> getTabs() {
		return tabs;
	}
	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}
	
	
}
