package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;
/**淘宝1订单详情接收类*/
public class Taobao1Detials implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Taobao1DeliveryInfo deliveryInfo;//订单信息1
	private String mainOrder;//订单信息2
	private String orderBar;
	private List<String> crubms;
	private List<String> operationsGuide;
	private String customService;
	private String detialExtra;
	
	public Taobao1DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}
	public void setDeliveryInfo(Taobao1DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}
	public String getMainOrder() {
		return mainOrder;
	}
	public void setMainOrder(String mainOrder) {
		this.mainOrder = mainOrder;
	}
	public String getOrderBar() {
		return orderBar;
	}
	public void setOrderBar(String orderBar) {
		this.orderBar = orderBar;
	}
	public List<String> getCrubms() {
		return crubms;
	}
	public void setCrubms(List<String> crubms) {
		this.crubms = crubms;
	}
	public List<String> getOperationsGuide() {
		return operationsGuide;
	}
	public void setOperationsGuide(List<String> operationsGuide) {
		this.operationsGuide = operationsGuide;
	}
	public String getCustomService() {
		return customService;
	}
	public void setCustomService(String customService) {
		this.customService = customService;
	}
	public String getDetialExtra() {
		return detialExtra;
	}
	public void setDetialExtra(String detialExtra) {
		this.detialExtra = detialExtra;
	}
	
	

}
