package com.il360.xiaofeiyu.model;

public enum RetCodeEnum {
	
	SUCCESS(1, "成功"),
	INVALID_REQUEST(1000, "无效的请求"),
	MISSING_PARAMETERS(1001, "缺少请求参数"),
	USER_NOT_EXIST(1002, "用户不存在"),
	PHONE_NUM_NOT_SUPPORT(1003, "暂不支持此号码"),
	REQUEST_BEFORE_FAIL(1004, "请求发起前失败"),
	
	LOGIN_FAILURE(1020, "登录失败，请重试"),
	WRONG_PASSWORD(1021, "登录密码错误"),
	LOGIN_CHECK_FAIL(1022, "登录校验失败，请重试"),
	LOGIN_FAIL_FOR_BUSY(1023, "系统忙登录失败，请重试"),
	UNABLE_GET_PHONE_LOCATION(1024, "无法获取号码所在地"),
	ACCOUNT_NOT_EXIST(1027, "请确认帐号是否正确或者已开通公积金缴费"),
	
	INPUT_PIC_CODE(2001, "请输入图片验证码"),
	INPUT_SMS_CODE(2002, "请输入短信验证码"),
	INPUT_4_PIC_CODE(2003, "请输入图中4位黑色验证码"),
	NULL_CODE(2004, "验证码不能为空"),
	PIC_CODE_LEN_WRONG(2005, "验证码长度错误"),
	WRONG_CODE(2006, "验证码错误，请重新输入"),
	SMS_CODE_FAIL(2007, "发送短信验证码失败"),
	PIC_CODE_FAIL(2008, "获取图片验证码失败"),
	INPUT_USER_NAME(2009, "请输入真实姓名"),
	INPUT_ID_NUM(2010, "请输入身份证号"),
	INPUT_SMS_PIC_CODE(2011, "请输入短信和图片验证码"),
	CHECK_REALNAME_FAIL(2012,"实名制验证失败"),
	CHECK_REALNAME_FAIL_2(2013, "实名制验证失败，请查验短信以及其他信息"),
	GET_SMS_DOBULE_FAIL(2014, "短信验证码已经获取，不能重复申请短信验证码"),
	SMS_EXPIRE(2015, "短信随机码不正确或已过期，请重新获取"),
	INPUT_LAST_TIME_SMS(2016, "请输入最后一次接收到的短信验证码"),
	
	GET_BILL_SUCCESS(3000,"获取账单成功"),
	GET_BASICINFO_SUCCESS(3001,"获取用户基本信息成功"),
	BASIC_INFO_FAIL(3020, "获取基础信息失败"),
	QUERY_COND_FAIL(3021, "获取查询条件失败"),
	GET_FLOW_FAIL(3022, "获取流量信息失败"),
	GET_MONTH_BILL_FAIL(3023, "获取月账单失败"),
	GET_BASICINFO_FAIL(3024,"获取个人信息失败"),
	GET_ACCOUNTBALANCE_FAIL(3025,"获取余额及话费失败"),
	GET_CALL_DETAIL_FAIL(3026, "获取通话详单失败"),
	REQ_OVER_LIMIT(3027, "业务访问次数超限"),
	REQ_TIME_OUT(3028, "业务请求超时"),
	GET_BILL_FAIL(3029,"获取账单失败"),
	GET_USINGBUS_FAIL(3030,"获取在用业务失败"),
	SERVER_IS_BUSY(3031, "服务器繁忙请稍后再试"),
	OUT_OF_SERVICE(3032, "抱歉，每月1到3号为月结出账期，暂不能为您提供服务"),
	
	GET_GJJ_DETALL_FAIL(3100, "获取公积金详单失败"),
	GET_GJJ_LOAN_FAIL(3101, "获取公积金贷款进度失败"),
	GET_GJJ_LOAN_PLAN_FAIL(3102, "获取公积金贷款信息失败"),
	
	CACHE_NOT_EXIST(8000, "缓存数据不存在"),
	
	UNKNOWN_ERROR(9999, "未知交易错误");
	
	
	RetCodeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private Integer code;
	private String desc;

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

	public static RetCodeEnum convertTo(Integer code) {
		for (RetCodeEnum codeEnum : RetCodeEnum.values()) {
			if (codeEnum.getCode().equals(code)) {
				return codeEnum;
			}
		}
		return null;
	}


}
