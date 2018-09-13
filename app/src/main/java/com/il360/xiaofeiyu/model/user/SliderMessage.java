package com.il360.xiaofeiyu.model.user;


/**
 * 服务器返回的JSON result的实体类 {code:1,
 * result:{returnCode:102,returnMessage:"密码错误",returnResult:{......}}}
 * 
 * @author steven
 */
public class SliderMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8154479688054714276L;
	/** 结果ID **/
	private int returnCode;
	/** 结果消息 **/
	private String desc;

	private SliderInfo sliderInfo;


	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public SliderInfo getSliderInfo() {
		return sliderInfo;
	}

	public void setSliderInfo(SliderInfo sliderInfo) {
		this.sliderInfo = sliderInfo;
	}

}
