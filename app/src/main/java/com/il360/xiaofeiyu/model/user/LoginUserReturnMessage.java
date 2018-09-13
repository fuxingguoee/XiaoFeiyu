package com.il360.xiaofeiyu.model.user;


/**
 * 服务器返回的JSON result的实体类 {code:1,
 * result:{returnCode:102,returnMessage:"密码错误",returnResult:{......}}}
 * 
 * @author steven
 */
public class LoginUserReturnMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8154479688054714276L;
	/** 结果ID **/
	private int returnCode;
	/** 结果消息 **/
	private String returnMessage;
	
	private User returnResult;

	public User getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(User returnResult) {
		this.returnResult = returnResult;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}



}
