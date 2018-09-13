package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

/**
 * User: Wanghk
 * Date: 16-4-25
 * Time: 下午3:52
 */
public class OutUserAddress implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键**/
    private Integer addressId;
	/**用户id**/
	private Integer userId;
    /**邮寄地址**/
    private String deliverAddress;
    /**邮寄手机号码**/
    private String deliverPhone;
    /**收件人姓名**/
    private String deliverName;
    /**是否默认 1默认 0自然顺序**/
    private String isDefault;
    /**创建时间**/
    private String createTime;
    /**type1实物 2虚拟**/
    private Integer type;

    public OutUserAddress() {
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getDeliverPhone() {
        return deliverPhone;
    }

    public void setDeliverPhone(String deliverPhone) {
        this.deliverPhone = deliverPhone;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String aDefault) {
        isDefault = aDefault;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
