package com.il360.xiaofeiyu.model.recovery;

import java.io.Serializable;

public class AssessDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Integer iphonetype;
	private Integer itemId;
	private Integer answerId;
	private Integer amount;
	private Integer lowAmount;
	private String itemDesc;
	private String answerDesc;
	private String type;
	
	private Boolean isCheck;//多选用到
	private Integer choicePosition;//单选用到

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIphonetype() {
		return iphonetype;
	}

	public void setIphonetype(Integer iphonetype) {
		this.iphonetype = iphonetype;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getLowAmount() {
		return lowAmount;
	}

	public void setLowAmount(Integer lowAmount) {
		this.lowAmount = lowAmount;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getAnswerDesc() {
		return answerDesc;
	}

	public void setAnswerDesc(String answerDesc) {
		this.answerDesc = answerDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean isCheck() {
		return isCheck;
	}

	public void setCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public Integer getChoicePosition() {
		return choicePosition;
	}

	public void setChoicePosition(Integer choicePosition) {
		this.choicePosition = choicePosition;
	}

}
