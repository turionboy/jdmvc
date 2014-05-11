/**   
 * @Title: ViewRender.java 
 * @Package com.jd.mvc.core.render 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author liubingsmile@gmail.com	   
 * @date 2014-5-5 上午9:17:31 
 * @version V1.0   
 */
package com.liubing.mvc.core.render;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liubingsmile@gmail.com
 * 
 */
public interface ViewRender {
	/**
	 * 页面跳转
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws Throwable
	 */
	public void renderPage(HttpServletRequest request,
			HttpServletResponse response, String view) throws Exception;

	/**
	 * 
	 * <p>
	 * Title: ajax 请求
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param result
	 * @param returnType
	 *            返回类型，json，String json需要格式化，系统默认提供fastjson方式
	 * @throws Exception
	 */
	public void renderResult(HttpServletRequest request,
			HttpServletResponse response, Object result, String returnType)
			throws Exception;

	/**
	 * 
	 * <p>
	 * Title: 初始化
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param servletContext
	 */
	public void init(ServletContext servletContext);
}
