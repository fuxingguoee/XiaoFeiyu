package com.il360.xiaofeiyu.model.goods;

import java.io.Serializable;
import java.util.List;

public class ArrayOfAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AttributeType> goodsType;
	private List<AttributeColour> GoodsColour;
	private List<AttributeVersion> goodsVersion;
	private List<Goods> goodsStock;

	public List<AttributeType> getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(List<AttributeType> goodsType) {
		this.goodsType = goodsType;
	}

	public List<AttributeColour> getGoodsColour() {
		return GoodsColour;
	}

	public void setGoodsColour(List<AttributeColour> goodsColour) {
		GoodsColour = goodsColour;
	}

	public List<AttributeVersion> getGoodsVersion() {
		return goodsVersion;
	}

	public void setGoodsVersion(List<AttributeVersion> goodsVersion) {
		this.goodsVersion = goodsVersion;
	}

	public List<Goods> getGoodsStock() {
		return goodsStock;
	}

	public void setGoodsStock(List<Goods> goodsStock) {
		this.goodsStock = goodsStock;
	}

}
