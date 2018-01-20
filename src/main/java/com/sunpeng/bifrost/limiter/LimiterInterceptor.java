package com.sunpeng.bifrost.limiter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.util.concurrent.RateLimiter;
import com.sunpeng.bifrost.common.BiforstConstants;
import com.sunpeng.bifrost.common.PropertiesUtils;

/**
 * 
 * @author sunpeng
 *
 */
@Service
@Aspect
@EnableAspectJAutoProxy
public class LimiterInterceptor {

	@PostConstruct
	public void init() throws IOException {
		logger.info("#BIFROST# load limiter properties from path-[{}]", propertiesPath);
		Resource resource = new ClassPathResource(propertiesPath);

		if (resource.exists()) {
			Properties properties = new Properties();
			properties.load(resource.getInputStream());
			LimiterUtils.setPropertie(properties);
		} else {
			logger.warn(
					"#BIFROST# limiter properties is null, use default setting (default.isLimit=true, default.premitsPreSecond=1024) instead");
		}
	}

	@Around("@annotation(com.sunpeng.bifrost.limiter.Limiter)")
	public Object limit(final ProceedingJoinPoint jp) throws Throwable {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Class<?> clazz = jp.getTarget().getClass();
		Method method = clazz.getMethod(signature.getName(), signature.getParameterTypes());
		Class<?> returnType = method.getReturnType();

		Limiter annotation = method.getAnnotation(Limiter.class);
		String key = annotation.key(); // 方法名
		String code = annotation.code(); // 返回码
		String codeField = annotation.codeField(); // 返回码字段
		String message = annotation.message(); // 返回信息
		String messageField = annotation.messageField(); // 返回信息字段
		int permits = annotation.permits(); // 方法需要的令牌数
		if (permits < 1) {
			permits = 1;
		}
		if (StringUtils.isEmpty(key)) {
			key = clazz.getName() + "." + method.getName();
		}
		String limitType = annotation.limitType(); // 限流类型 WAIT/RETURN

		if (!LimiterUtils.isLimit(key)) {
			// 判断方法是否限流
			return jp.proceed();
		}

		RateLimiter limiter = LimiterUtils.getLimiter(key);

		if (BiforstConstants.LIMIT_METHOD_RETURN.equals(limitType)) { // 直接返回
			if (limiter.tryAcquire(permits)) {
				return jp.proceed();
			} else {
				final String _key = key;
				LimiterThreadPool.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						Object[] params = jp.getArgs();
						LimitedData data = new LimitedData();
						data.setKey(_key);
						data.setData(params);
						LimitedDataUtils.transferLimitedData(data);
					}
				});

				return getLimiterResponse(returnType, code, codeField, message, messageField);
			}
		} else if (BiforstConstants.LIMIT_METHOD_WAIT.equals(limitType)) { // 等待
			limiter.acquire(permits);
			return jp.proceed();
		} else {
			logger.warn("#BIFROST# unknow limit type-[{}]", limitType);
			return jp.proceed();
		}
	}

	private Object getLimiterResponse(Class<?> returnType, String code, String codeField, String message,
			String messageField) throws InstantiationException, IllegalAccessException {
		Object obj = returnType.newInstance();

		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(codeField)) {
			PropertiesUtils.setFieldValue(obj, codeField, code);
		}

		if (!StringUtils.isEmpty(message) && !StringUtils.isEmpty(messageField)) {
			PropertiesUtils.setFieldValue(obj, messageField, message);
		}

		return obj;
	}

	private final String propertiesPath = "props/limiter.properties";

	private Logger logger = LoggerFactory.getLogger(getClass());
}
