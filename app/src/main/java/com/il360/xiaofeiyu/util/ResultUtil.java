package com.il360.xiaofeiyu.util;

import com.il360.xiaofeiyu.model.home.MyResult;
import com.il360.xiaofeiyu.model.home.MyReturnResult;

public class ResultUtil {

	public static String isOutTime(String result) {
		MyResult myResult;
		try {
			myResult = FastJsonUtils.getSingleBean(result, MyResult.class);
			if (myResult.getCode() == 1) {
				if(myResult.getResult() != null){
					MyReturnResult myReturnResult =  FastJsonUtils.getSingleBean(myResult.getResult().toString(), MyReturnResult.class);
					if (myReturnResult.getReturnCode() != null && myReturnResult.getReturnCode() == 213) {
						return myReturnResult.getReturnMessage();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
