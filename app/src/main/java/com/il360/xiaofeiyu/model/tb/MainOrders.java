package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;
/**订单返回数据中的交易数据*/
public class MainOrders implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String extra;
	private String id;//订单号	
	private List<MainOperation> operations;//交易操作
	private OrderInfo orderInfo;//创建日期
	private PayInfo payInfo;//实付款(那一列信息)
	private Seller seller;//卖家信息
	private StatusInfo statusInfo;//交易状态(那一列信息)
	private String stepPayList;//有定金的情况
	private List<SubOrders> subOrders;//宝贝信息
	
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<MainOperation> getOperations() {
		return operations;
	}
	public void setOperations(List<MainOperation> operations) {
		this.operations = operations;
	}
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public PayInfo getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(PayInfo payInfo) {
		this.payInfo = payInfo;
	}
	public Seller getSeller() {
		return seller;
	}
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	public StatusInfo getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(StatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}
	public String getStepPayList() {
		return stepPayList;
	}
	public void setStepPayList(String stepPayList) {
		this.stepPayList = stepPayList;
	}
	public List<SubOrders> getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(List<SubOrders> subOrders) {
		this.subOrders = subOrders;
	}
	
	
}
