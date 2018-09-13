package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

public class InfoPage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String currentPage;
	private String pageSize;
	private String prefetchCount;
	private String queryForTitle;
	private String totalNumber;
	private String totalPage;
	
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPrefetchCount() {
		return prefetchCount;
	}
	public void setPrefetchCount(String prefetchCount) {
		this.prefetchCount = prefetchCount;
	}
	public String getQueryForTitle() {
		return queryForTitle;
	}
	public void setQueryForTitle(String queryForTitle) {
		this.queryForTitle = queryForTitle;
	}
	public String getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	
	

}
