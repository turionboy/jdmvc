package com.liubing.jdmvc.samples.interceptor;

import java.lang.reflect.Method;

import com.liubing.mvc.core.interceptor.DefaultInterceptor;




public class demoInterceptor implements DefaultInterceptor{


	public Boolean preInterceptor(Class<?> cls,String routepath, Method method, Object... params) {
		// TODO Auto-generated method stub
		return true;
	}

	public void afterInterceptor(Class<?> cls,String routepath, Method method, Object... params) {
		// TODO Auto-generated method stub
		
	}

}
