package com.liubing.mvc.core.interceptor;

import java.lang.reflect.Method;
public  class DefaultInterceptorImpl implements DefaultInterceptor {
	
	@Override
	public Boolean preInterceptor(Class<?> cls, String routepath,
			Method method, Object... params) {
		// TODO Auto-generated method stub
		//logger.info("访问路径:"+routepath);
		return true;
	}

	@Override
	public void afterInterceptor(Class<?> cls, String routepath, Method method,
			Object... params) {
		// TODO Auto-generated method stub
		
	}

}
