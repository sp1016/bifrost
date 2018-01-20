package com.sunpeng.bifrost.limiter;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * @author sunpeng
 *
 */
public class LimiterThreadPool {

	public static ThreadPoolTaskExecutor getExecutor() {
		if (executor == null) {
			synchronized (obj) {
				executor = new ThreadPoolTaskExecutor();
				executor.setCorePoolSize(1);
				executor.setMaxPoolSize(2);
				executor.setQueueCapacity(100);
			}
		}

		return executor;
	}

	private static Object obj = new Object();

	private static ThreadPoolTaskExecutor executor = null;
}
