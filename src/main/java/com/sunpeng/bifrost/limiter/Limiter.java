package com.sunpeng.bifrost.limiter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;

import com.sunpeng.bifrost.common.BiforstConstants;

/**
 * 
 * @author sunpeng
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
@Order
public @interface Limiter {

	/**
	 * 接口名
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * 方法需要的令牌数
	 * 
	 * @return
	 */
	int permits() default 1;

	/**
	 * 限流处理方式
	 * <p>
	 * 取值为{@code BiforstConstants.LIMIT_METHOD_WAIT}和{@code BiforstConstants.LIMIT_METHOD_RETURN},
	 * 等待或直接返回
	 * </p>
	 * 
	 * @return
	 */
	String limitType() default BiforstConstants.LIMIT_METHOD_RETURN;

	/**
	 * 限流返回值
	 * <p>
	 * 限流方式{@code limitMethod()}为{@code BiforstConstants.LIMIT_METHOD_WAIT}时,
	 * 当队列超过{@code size()}时返回;
	 * </p>
	 * <p>
	 * 限流方式{@code limitMethod()}为{@code BiforstConstants.LIMIT_METHOD_RETURN}时,
	 * 处理方式为限流时返回
	 * </p>
	 * 
	 * @return
	 */
	String code();

	/**
	 * 返回码字段
	 * <p>
	 * 如果限流则把限流返回码设置到这个字段
	 * </p>
	 * 
	 * @return
	 */
	String codeField() default "code";

	/**
	 * 限流返回信息
	 * <p>
	 * 限流方式{@code limitMethod()}为{@code BiforstConstants.LIMIT_METHOD_WAIT}时,
	 * 当队列超过{@code size()}时返回;
	 * </p>
	 * <p>
	 * 限流方式{@code limitMethod()}为{@code BiforstConstants.LIMIT_METHOD_RETURN}时,
	 * 处理方式为限流时返回
	 * </p>
	 * 
	 * @return
	 */
	String message();

	/**
	 * 返回信息字段
	 * <p>
	 * 如果限流则把限流返回信息设置到这个字段
	 * </p>
	 * 
	 * @return
	 */
	String messageField() default "message";
}
