package com.sunpeng.bifrost.common;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class PropertiesUtils {

	/**
	 * 设置对象字段值
	 * <p>
	 * 设置obj对象field属性的值为value
	 * </p>
	 * 
	 * @param obj
	 * @param field
	 * @param value
	 */
	public static <T> void setFieldValue(Object obj, String field, T value) {
		if (obj == null) {
			return;
		}

		if (Map.class.isAssignableFrom(obj.getClass())) {
			@SuppressWarnings("unchecked")
			Map<String, T> map = (Map<String, T>) obj;
			map.put(field, value);
		} else {
			BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
			beanWrapper.setPropertyValue(field, value);
		}
	}
}
