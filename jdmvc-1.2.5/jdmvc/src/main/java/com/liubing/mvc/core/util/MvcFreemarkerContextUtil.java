/**   
* @Title: MvcFreemarkerContextUtil.java 
* @Package com.jd.mvc.core.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubingsmile@gmail.com	   
* @date 2014-5-7 下午3:58:28 
* @version V1.0   
*/ 
package com.liubing.mvc.core.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import freemarker.template.Configuration;

/**
 * @author liubingsmile@gmail.com
 *
 */
public class MvcFreemarkerContextUtil {
	public static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();
	public static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();
	
	public static ThreadLocal<Configuration> cfg=new ThreadLocal<Configuration>();
	
	/**
	 * 
	 * <br/>Description:获取 request
	 * 
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		return threadLocalRequest.get();
	}

	/**
	 * 
	 * <br/>Description:获取 response
	 * 
	 * 
	 * @return
	 */
	public static HttpServletResponse getResponse(){
		return threadLocalResponse.get();
	}

	/**
	 * 
	 * <br/>Description:获取 session
	 * 
	 * 
	 * @return
	 */
	public HttpSession getSession(){
		return threadLocalRequest.get().getSession();
	}
	
	/**
	 * @return the cfg
	 */
	public static ThreadLocal<Configuration> getCfg() {//freemarker 配置
		return cfg;
	}

	
	
}
