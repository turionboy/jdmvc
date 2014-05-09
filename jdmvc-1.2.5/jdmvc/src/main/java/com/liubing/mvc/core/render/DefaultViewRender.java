/**   
* @Title: DefaultViewRender.java 
* @Package com.jd.mvc.core.render 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubingsmile@gmail.com	   
* @date 2014-5-5 上午11:43:49 
* @version V1.0   
*/ 
package com.liubing.mvc.core.render;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;

import com.alibaba.fastjson.JSONObject;
import com.liubing.mvc.core.util.MvcPageUtil;
/**
 * @author liubingsmile@gmail.com
 *
 */
public class DefaultViewRender implements ViewRender {

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#renderPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	@Override
	public void renderPage(HttpServletRequest request,
			HttpServletResponse response, String view) throws Exception {
		// TODO Auto-generated method stub
		MvcPageUtil.redirectPage(request, response, view, true);
	}

	/* (non-Javadoc)
	 * @see com.jd.mvc.core.render.ViewRender#renderResult(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	@Override
	public void renderResult(HttpServletRequest request,
			HttpServletResponse response, Object result,String returnType) throws Exception {
		
		//String jsonResult="";
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
	 * @see com.jd.mvc.core.render.ViewRender#init()
	 */
	@Override
	public void init(ServletContext servletContext) {
		// TODO Auto-generated method stub
		
	}

}
