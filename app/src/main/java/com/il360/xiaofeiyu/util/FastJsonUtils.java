package com.il360.xiaofeiyu.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import android.text.TextUtils;

public class FastJsonUtils {
	/**
	 * 功能描述：把JSON数据转换成普通字符串列表
	 * 
	 * @param jsonData
	 *            JSON数据
	 * @return
	 * @throws Exception
	 */
	public static List<String> getStringList(String jsonData) throws Exception {
		return JSON.parseArray(jsonData, String.class);
	}

	/**
	 * 功能描述：把JSON数据转换成指定的java对象
	 * 
	 * @param jsonData
	 *            JSON数据
	 * @param clazz
	 *            指定的java对象
	 * @return
	 * @throws Exception
	 */
	public static <T> T getSingleBean(String jsonData, Class<T> clazz) throws Exception {
		return JSON.parseObject(jsonData, clazz);
	}

	/**
	 * 功能描述：把java对象转换成JSON数据
	 * 
	 * @param object
	 *            java对象
	 * @return
	 */
	public static String getJsonString(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * 功能描述：把JSON数据转换成指定的java对象列表
	 * 
	 * @param jsonData
	 *            JSON数据
	 * @param clazz
	 *            指定的java对象
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getBeanList(String jsonData, Class<T> clazz) throws Exception {
		return JSON.parseArray(jsonData, clazz);
	}

	/**
	 * 功能描述：把JSON数据转换成较为复杂的java对象列表
	 * 
	 * @param jsonData
	 *            JSON数据
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getBeanMapList(String jsonData) throws Exception {
		return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	public static float StringToFloat(String str) {

		if (TextUtils.isEmpty(str)) {
			return 0;
		}
		try {
			return Float.valueOf(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
