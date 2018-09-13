package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

public class ValueData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String isLogin;
	private String taoScore;
	
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
	public String getTaoScore() {
		return taoScore;
	}
	public void setTaoScore(String taoScore) {
		this.taoScore = taoScore;
	}
	
	

}
