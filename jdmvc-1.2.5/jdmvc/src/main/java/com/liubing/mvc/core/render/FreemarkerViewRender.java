/**   
* @Title: FreemarkerViewRender.java 
* @Package com.jd.mvc.core.render 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubingsmile@gmail.com	   
* @date 2014-5-7 下午3:53:10 
* @version V1.0   
*/ 
package com.liubing.mvc.core.render;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.liubing.mvc.core.util.MvcFreemarkerContextUtil;
import com.liubing.mvc.core.util.MvcPageUtil;
import com.liubing.mvc.core.util.StringUtil;

import freemarker.template.Configuration;
/**
 * @author liubingsmile@gmail.com
 *
 */
public class FreemarkerViewRender implements ViewRender {
	private Configuration cfg;
	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#renderPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void renderPage(HttpServletRequest request,
			HttpServletResponse response, String view) throws Exception {
		// TODO Auto-generated method stub
		MvcPageUtil.resultHTMLToString(response, view);
	}

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#renderResult(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void renderResult(HttpServletRequest request,
			HttpServletResponse response, Object result, String returnType)
			throws Exception {
		// TODO Auto-generated method stub
		if(returnType.equalsIgnoreCase("json")||returnType.equalsIgnoreCase("jsonp")){
			if(result.getClass().isPrimitive() || result.getClass().getName().startsWith("java.")){//java 基本类型
				//jsonResult=JSONObject.toJSONString(result);
				String jsonResult=result+"";
				MvcPageUtil.resultJsonToString(response, jsonResult);
			}else{//javabean 、map 、collection
				String jsonResult=JSONObject.toJSONString(result);
				MvcPageUtil.resultJsonToString(response, jsonResult);
			}
		}else if(returnType.equalsIgnoreCase("xml")){
			String jsonResult=result+"";
			MvcPageUtil.resultXMLToString(response, jsonResult);
		}else{
			String jsonResult=result+"";
			MvcPageUtil.resultHTMLToString(response, jsonResult);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#init(javax.servlet.ServletContext)
	 */
	public void init(ServletContext servletContext) {
		// TODO Auto-generated method stub
		cfg = new Configuration();
		if(!StringUtil.isNullOrEmpty(servletContext.getInitParameter("templates"))){
			cfg.setServletContextForTemplateLoading(servletContext, servletContext.getInitParameter("templates"));
		}else{
			cfg.setServletContextForTemplateLoading(servletContext, "WEB-INF/templates");
		}
		MvcFreemarkerContextUtil.cfg.set(cfg);
	}

}
