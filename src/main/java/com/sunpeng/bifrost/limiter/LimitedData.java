package com.sunpeng.bifrost.limiter;

/**
 * 
 * @author sunpeng
 *
 */
public class LimitedData {

	/**
	 * 限流方法key
	 */
	private String key;

	/**
	 * 方法限流数据
	 */
	private Object[] data;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

}
