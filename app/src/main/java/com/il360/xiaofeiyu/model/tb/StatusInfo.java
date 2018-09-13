package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;

/**交易状态*/
public class StatusInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private List<StatusInfoOpreations> operations;
	private String text;
	private String type;
	private String url;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<StatusInfoOpreations> getOperations() {
		return operations;
	}
	public void setOperations(List<StatusInfoOpreations> operations) {
		this.operations = operations;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
