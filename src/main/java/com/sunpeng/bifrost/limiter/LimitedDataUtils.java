package com.sunpeng.bifrost.limiter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author sunpeng
 *
 */
public class LimitedDataUtils {

	/**
	 * 从队列中获取被限流的数据
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public static LimitedData getLimitData(long timeout, TimeUnit unit) throws InterruptedException {
		if (LIMITED_DATA == null) {
			synchronized (obj) {
				initlizeLimitedQueue();
			}
		}
		return LIMITED_DATA.poll(timeout, unit);
	}

	/**
	 * 保存 被限流的数据到LIMITED_DATA中
	 * 
	 * @param data
	 * @return
	 */
	static <T> boolean transferLimitedData(LimitedData data) {
		if (LIMITED_DATA == null) {
			synchronized (obj) {
				initlizeLimitedQueue();
			}
		}
		return LIMITED_DATA.offer(data);
	}

	/**
	 * 初始化 LIMITED_DATA
	 */
	private static void initlizeLimitedQueue() {
		if (LIMITED_DATA == null) {
			synchronized (obj) {
				LIMITED_DATA = new LinkedBlockingQueue<>(LimiterUtils.getQueueCapacity());
			}
		}
	}

	private static Object obj = new Object();

	private static BlockingQueue<LimitedData> LIMITED_DATA = null;
}
