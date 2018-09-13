package com.il360.xiaofeiyu.model.goods;

import java.io.Serializable;
import java.util.List;

public class ArrayOfGoodsExt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String desc;
	private List<GoodsExt> result;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<GoodsExt> getResult() {
		return result;
	}

	public void setResult(List<GoodsExt> result) {
		this.result = result;
	}

}
