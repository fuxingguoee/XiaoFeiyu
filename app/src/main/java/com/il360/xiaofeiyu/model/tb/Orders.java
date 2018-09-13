package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.List;
/**淘宝返回信息集合*/
public class Orders implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Order order;
	private List<OrderItem> orderItemList;
	private OrderLogistics orderLogistics;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public OrderLogistics getOrderLogistics() {
		return orderLogistics;
	}
	public void setOrderLogistics(OrderLogistics orderLogistics) {
		this.orderLogistics = orderLogistics;
	}

}
