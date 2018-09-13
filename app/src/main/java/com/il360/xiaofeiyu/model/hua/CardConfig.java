package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;

/**
 * 卡头服务关联配置 User: Wanghk Date: 15-9-7 Time: 下午2:06
 */
public class CardConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 配置id **/
	private Integer cardConfigId;
	/** 属性分组 **/
	private String configGroup;
	/** 属性key **/
	private String configName;
	/** 属性值 **/
	private String configValue;

	public CardConfig() {
	}

	public CardConfig(String configGroup, String configName) {
		this.configGroup = configGroup;
		this.configName = configName;
	}

	public Integer getCardConfigId() {
		return cardConfigId;
	}

	public void setCardConfigId(Integer cardConfigId) {
		this.cardConfigId = cardConfigId;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
}
