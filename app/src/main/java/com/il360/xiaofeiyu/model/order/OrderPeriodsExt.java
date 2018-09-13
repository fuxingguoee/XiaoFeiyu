package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.math.BigDecimal;
public class OrderPeriodsExt implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**分期id**/
    private Long fqId;
    /**商品订单号**/
    private String orderNo;
    /**期数**/
    private Integer number;
    /**本期本金 元**/
    private BigDecimal amount;
    /**本期利息*/
    private BigDecimal fee;
    /**到期时间**/
    private String expireTime;
    /**用户id**/
    private Integer userId;
    /**状态0未还款1已还款**/
    private Integer status;
    /**逾期费用 元**/
    private BigDecimal overdueFee;
    /**还款时间**/
    private String payTime;
    /**创建时间**/
    private String createTime;

    public void setFqId(Long fqId) {
        this.fqId = fqId;
    }

    public Long getFqId() {
        return this.fqId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setOverdueFee(BigDecimal overdueFee) {
        this.overdueFee = overdueFee;
    }

    public BigDecimal getOverdueFee() {
        return this.overdueFee;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
