package com.liubing.mvc.core.common;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * 路由的封装
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class RouteInfo implements Serializable {
	private String route;
	private int routeLength;
	private String clazz;
	private String method;
	private String callMethod;
	private Object[] objs;
	private Map<String, Object> params;
	private Class<?> interceptor;
	private Method[] methods;
	private String returnType;

	public RouteInfo() {

	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public int getRouteLength() {
		return routeLength;
	}

	public void setRouteLength(int routeLength) {
		this.routeLength = routeLength;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getObjs() {
		return objs;
	}

	public void setObjs(Object[] objs) {
		this.objs = objs;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getCallMethod() {
		return callMethod;
	}

	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}

	public Class<?> getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Class<?> interceptor) {
		this.interceptor = interceptor;
	}

	public Method[] getMethods() {
		return methods;
	}

	public void setMethods(Method[] methods) {
		this.methods = methods;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

}
