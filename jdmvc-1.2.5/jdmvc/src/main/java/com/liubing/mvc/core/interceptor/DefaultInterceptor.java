package com.liubing.mvc.core.interceptor;

import java.lang.reflect.Method;

/**
 * 拦截器
 * 
 * @author liubingsmile@gmail.com
 * 
 */
public interface DefaultInterceptor {
	/**
	 * 方法开始前拦截
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param cls
	 * @param routepath
	 * @param method
	 * @param params
	 * @return
	 */
	public Boolean preInterceptor(Class<?> cls, String routepath,
			Method method, Object... params);

	/**
	 * 方法后开始拦截
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param cls
	 * @param method
	 * @param params
	 */
	public void afterInterceptor(Class<?> cls, String routepath, Method method,
			Object... params);
}
