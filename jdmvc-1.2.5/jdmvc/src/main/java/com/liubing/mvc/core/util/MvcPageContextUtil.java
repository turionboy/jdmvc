package com.liubing.mvc.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * MVC 框架核心 上下文
 * 
 * @author liubing
 * 
 */
public class MvcPageContextUtil {

	public static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();
	public static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();

	/**
	 * 
	 * <br/>
	 * Description:获取 request
	 * 
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return threadLocalRequest.get();
	}

	/**
	 * 
	 * <br/>
	 * Description:获取 response
	 * 
	 * 
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return threadLocalResponse.get();
	}

	/**
	 * 
	 * <br/>
	 * Description:获取 session
	 * 
	 * 
	 * @return
	 */
	public HttpSession getSession() {
		return threadLocalRequest.get().getSession();
	}

}
