package com.il360.xiaofeiyu.model;

import java.io.Serializable;

/**
 * 上传文件 User: Wanghk Date: 14-8-25 Time: 上午8:46
 */
public class UploadFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 图片说明 **/
	private String imgName;
	/** 图片Base64字符串 **/
	private String imgData;
	/** 图片后缀 **/
	private String imgEnd;
	/** 用户登录是否超时验证 **/
	private String token;
	/** Android和IOS区别 **/
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImgEnd() {
		return imgEnd;
	}

	public void setImgEnd(String imgEnd) {
		this.imgEnd = imgEnd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgData() {
		return imgData;
	}

	public void setImgData(String imgData) {
		this.imgData = imgData;
	}
}
