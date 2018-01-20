package com.sunpeng.bifrost.limiter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 
 * @author sunpeng
 *
 */
public class LimiterUtils {

	/**
	 * 设置限流属性
	 * 
	 * 
	 * @param _properties
	 */
	static void setPropertie(Properties _properties) {
		getProperties().putAll(_properties);
	}

	/**
	 * 设置限流属性
	 * 
	 * @param key
	 * @param value
	 */
	public static void setPropertie(String key, String value) {
		getProperties().setProperty(key, value);
		// 更新
		String _key = key.replace(IS_LIMIT_FIELD_SUFFIX, "").replace(PREMITS_PER_SECOND_FIELD_SUFFIX, "");
		limiters.remove(_key);
	}

	/**
	 * 获取限流器
	 * 
	 * @param key
	 * @return
	 */
	public static RateLimiter getLimiter(String key) {
		RateLimiter limiter = limiters.get(key);

		if (limiter == null) {
			synchronized (obj) {
				limiter = limiters.get(key);
				if (limiter == null) {
					limiter = RateLimiter.create(getPremitsPreSecond(key));
					limiters.put(key, limiter);
				}
			}
		}

		return limiter;
	}

	/**
	 * 更新限流器每秒钟令牌数
	 * 
	 * @param key
	 * @param permitsPerSecond
	 */
	static void setLimiterRate(String key, double permitsPerSecond) {
		RateLimiter limiter = limiters.get(key);
		if (limiter == null) {
			limiter = getLimiter(key);
		}
		limiter.setRate(permitsPerSecond);
	}

	/**
	 * 获取队列初始大小, 默认为1024
	 * 
	 * @return
	 */
	static int getQueueCapacity() {
		String capacity = getProperties().getProperty(QUEUE_CAPACITY_FIELD, DEFAULT_QUEUE_CAPACITY);
		return Integer.parseInt(capacity);
	}

	/**
	 * 判断接口是否需要限流
	 * 
	 * @param key
	 * @return
	 */
	static boolean isLimit(String key) {
		String isLimitKey = key + IS_LIMIT_FIELD_SUFFIX;
		String _isLimit = getProperties().getProperty(isLimitKey, DEFAULT_IS_LIMIT_FIELD);
		return Boolean.valueOf(_isLimit);
	}

	/**
	 * 获取方法限流器的初始每秒钟令牌数
	 * 
	 * @param key
	 * @return
	 */
	private static int getPremitsPreSecond(String key) {
		String premitsPreSecondKey = key + PREMITS_PER_SECOND_FIELD_SUFFIX;
		String _premitsPreSecond = getProperties().getProperty(premitsPreSecondKey, DEFAULT_PREMITS_PER_SECOND_FIELD);
		return Integer.parseInt(_premitsPreSecond);
	}

	/**
	 * 获取配置
	 * 
	 * <p>
	 * default.isLimit=true
	 * </p>
	 * <p>
	 * default.premitsPreSecond=1024
	 * </p>
	 * 
	 * @return
	 */
	private static Properties getProperties() {
		if (properties == null || properties.size() == 0) {
			synchronized (obj) {
				if (properties == null || properties.size() == 0) {
					properties = new Properties();
					properties.setProperty(DEFAULT_IS_LIMIT_FIELD, DEFAULT_IS_LIMIT);
					properties.setProperty(DEFAULT_PREMITS_PER_SECOND_FIELD, DEFAULT_PREMITS_PER_SECOND);
				}
			}
		}
		return properties;
	}

	/**
	 * 限流配置
	 * 
	 * <p>
	 * default.isLimit 默认是否限流 key.isLimit key指定的方法是否限流
	 * </p>
	 * <p>
	 * default.premitsPreSecond 默认每秒钟令牌数 key.premitsPreSecond key指定的方法每秒钟令牌数
	 * </p>
	 */
	private static Properties properties;

	private static final String IS_LIMIT_FIELD_SUFFIX = ".isLimit";
	private static final String PREMITS_PER_SECOND_FIELD_SUFFIX = ".premitsPreSecond";
	private static final String DEFAULT_IS_LIMIT_FIELD = "default" + IS_LIMIT_FIELD_SUFFIX;
	private static final String DEFAULT_IS_LIMIT = "true";
	private static final String DEFAULT_PREMITS_PER_SECOND_FIELD = "default" + PREMITS_PER_SECOND_FIELD_SUFFIX;
	private static final String DEFAULT_PREMITS_PER_SECOND = "1024";
	private static final String QUEUE_CAPACITY_FIELD = "queue.capacity";
	private static final String DEFAULT_QUEUE_CAPACITY = "1024";

	private static Object obj = new Object();

	private static Map<String, RateLimiter> limiters = new HashMap<String, RateLimiter>();
}
