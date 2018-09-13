package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class Advert implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**广告id**/
	private Integer advertId;
	/**标题**/
	private String title;
	/**图片url**/
	private String picUrl;
	/**详情url**/
	private String webUrl;
	/**类型**/
	private Integer type;
	/**状态1有效 -1无效**/
	private Integer status;
	/**创建时间**/
	private String createTime;
	/**修改时间**/
	private String updateTime;
	
	public void setAdvertId(Integer advertId) {
		this.advertId = advertId;
	}
	
	public Integer getAdvertId() {
		return this.advertId;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getPicUrl() {
		return this.picUrl;
	}
	
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	
	public String getWebUrl() {
		return this.webUrl;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateTime() {
		return this.createTime;
	}
	
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateTime() {
		return this.updateTime;
	}
}
