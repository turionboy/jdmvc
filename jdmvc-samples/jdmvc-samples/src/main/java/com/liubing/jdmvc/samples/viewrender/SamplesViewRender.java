/**   
* @Title: SamplesViewRender.java 
* @Package com.jd.jdmvc.samples.viewrender 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubingsmile@gmail.com	   
* @date 2014-5-7 上午11:29:56 
* @version V1.0   
*/ 
package com.liubing.jdmvc.samples.viewrender;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.liubing.mvc.core.render.ViewRender;

/**
 * @author liubingsmile@gmail.com
 *
 */
public class SamplesViewRender implements ViewRender {

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#renderPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void renderPage(HttpServletRequest request,
			HttpServletResponse response, String view) throws Exception {
		// TODO Auto-generated method stub
		request.getRequestDispatcher(view).include(request, response);
	}

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#renderResult(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void renderResult(HttpServletRequest request,
			HttpServletResponse response, Object result, String returnType)
			throws Exception {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.setContentType("text/html;charset=UTF-8");
		String jsonResult="";
		if(returnType.equalsIgnoreCase("JSON")){
			jsonResult=JSONObject.toJSONString(result);
		}else{
			jsonResult=result+"";
		}
		PrintWriter out =response.getWriter();
		out.print(jsonResult);
	}

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#init(javax.servlet.ServletContext)
	 */
	public void init(ServletContext servletContext) {
		// TODO Auto-generated method stub

	}

}
