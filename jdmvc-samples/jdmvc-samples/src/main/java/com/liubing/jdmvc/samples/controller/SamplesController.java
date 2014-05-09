/**   
* @Title: SamplesController.java 
* @Package com.jd.jdmvc.samples.controller 
* @Description: jdmvc 测试
* @author liubingsmile@gmail.com	   
* @date 2014-5-7 上午11:26:35 
* @version V1.0   
*/ 
package com.liubing.jdmvc.samples.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.liubing.jdmvc.samples.entity.DemoEntity;
import com.liubing.jdmvc.samples.interceptor.demoInterceptor;
import com.liubing.mvc.core.controller.annotation.FormParam;
import com.liubing.mvc.core.controller.annotation.MethodType;
import com.liubing.mvc.core.controller.annotation.QueryParam;
import com.liubing.mvc.core.controller.annotation.Route;
import com.liubing.mvc.core.controller.annotation.RouteParam;
import com.liubing.mvc.core.controller.annotation.MethodType.mType;
import com.liubing.mvc.core.controller.annotation.MethodType.returnType;
import com.liubing.mvc.core.util.BeanMapUtil;
import com.liubing.mvc.core.util.MvcPageContextUtil;
import com.liubing.mvc.core.util.MvcPageUtil;

/**
 * @author liubingsmile@gmail.com
 *
 */
@Route("/demo")
public class SamplesController{
	/**
	 * demo 不带参数
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo.html </p> 
	* @throws Exception
	 */
	@Route(value="/demo.html")
	@MethodType(type=mType.get)
	public void demo() throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, "1111");
	}
	/**
	 * demo2 第二种方式
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo3/demo20.html </p> 
	* @param demo2
	* @throws Exception
	 */
	@Route(value="/{demo2}/demo20.html")
	@MethodType(type=mType.get)
	public void demo20(@RouteParam("demo2") Map<String, Object> demo2) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, demo2.get("demo2")+"");
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo3/demo21.html </p> 
	* @param demo2
	* @throws Exception
	 */
	@Route(value="/{demo2}/demo21.html")
	@MethodType(type=mType.get,returnType=returnType.JSON)
	public void demo21(@RouteParam("demo2") String demo2) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, demo2);
	}
	
	/**
	 *
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo3/demo22.html </p> 
	* @param demo2
	* @throws Exception
	 */
	@Route(value="/{demo1}/demo22.html")
	@MethodType(type=mType.get,returnType=returnType.JSON)
	public void demo22(@RouteParam("demo2") DemoEntity demo2) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, demo2.getDemo1());
	}
	/**
	 *
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo99.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo99.html")
	@MethodType(type=mType.get)
	public void demo99(@QueryParam("demo") String demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, demo);
	}
	
	/**
	 *
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo991.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo991.html")
	@MethodType(type=mType.get)
	public void demo991(@QueryParam("demo") Map<String, Object>map) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, map.get("demo")+"");
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo992.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo992.html")
	@MethodType(type=mType.get)
	public void demo992(@QueryParam("demo") DemoEntity demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, demo.getDemo());
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo993.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo993.html")
	@MethodType(type=mType.get)
	public void demo993(@QueryParam("demo") Object[] demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultJsonToString(response, demo.length+"");
	}
	
	
	/**
	 * 
	* <p>Title:@RouteParam参数类型是:map </p> 
	* <p>URL: http://127.0.0.1:8080/jdmvc-samples/demo/demo12/sa/demo6.html</p> 
	* @param demo4
	* @throws Exception
	 */
	@Route(value="/demo12/{demo}/{demo1}.html",cls=demoInterceptor.class)
	@MethodType(type=mType.get)
	public void demo12(@RouteParam("demo") Map<String, Object> demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo.get("demo1")+"");
	}
	/**
	 * 
	* <p>Title:@RouteParam参数类型是:javabean </p> 
	* <p>URL: http://127.0.0.1:8080/jdmvc-samples/demo/demo4/sa/demo6.html</p> 
	* @param demo4
	* @throws Exception
	 */
	@Route(value="/demo4/{demo}/{demo1}.html",cls=demoInterceptor.class)
	@MethodType(type=mType.get)
	public void demo4(@RouteParam("demo") DemoEntity demoEntity) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demoEntity.getDemo());
	}
	
	/**
	 * 
	* <p>Title: @RouteParam参数类型是:java基本类型</p> 
	* <p>URL: http://127.0.0.1:8080/jdmvc-samples/demo/demo14/sa/demo16.html</p> 
	* @param demo4
	* @throws Exception
	 */
	@Route(value="/demo14/{demo}/{demo1}.html",cls=demoInterceptor.class)
	@MethodType(type=mType.get)
	public void demo14(@RouteParam("demo") String demo, @RouteParam("demo1") String demo1) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo);
	}
	
	/**
	 * 
	* <p>Title: @RouteParam参数类型是:List</p> 
	* <p>URL: http://127.0.0.1:8080/jdmvc-samples/demo/demo17/sa/demo16.html</p> 
	* @param demo4
	* @throws Exception
	 */
	@Route(value="/demo17/{demo}/{demo1}.html",cls=demoInterceptor.class)
	@MethodType(type=mType.get)
	public void demo17(@RouteParam("demo") List<String> demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo.size()+"");
	}
	/**
	 * 
	* <p>Title: @RouteParam参数类型是:Array,并且只能是obejct[]</p> 
	* <p>URL: http://127.0.0.1:8080/jdmvc-samples/demo/demo18/sa/demo16.html</p> 
	* @param demo4
	* @throws Exception
	 */
	@Route(value="/demo18/{demo}/{demo1}.html",cls=demoInterceptor.class)
	@MethodType(type=mType.get)
	public void demo18(@RouteParam("demo") Object[] demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo.length+"");
	}
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo7.html?demo=3&demo1=4 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo7.html")
	@MethodType(type=mType.get)
	public void demo7(@QueryParam("demo") Map<String,Object> demo) throws Exception{
		DemoEntity demoEntity=new DemoEntity();
		BeanMapUtil.convertMapToBean(demoEntity, demo);
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demoEntity.getDemo());
		//return demo.get("demo")+"";
	}
	/**
	 * 第7种方式
	* <p>Title: </p> 
	* <p>Description:URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo10.html?demo=10 </p> 
	* @param demo
	* @return
	* @throws Exception
	 */
	@Route(value="/demo10.html")
	@MethodType(type=mType.get)
	public String demo10(@QueryParam("demo") String demo) throws Exception{
		//HttpServletResponse response=MvcPageContextUtil.getResponse();
		return "/index.jsp";
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo110.html?demo=10 </p> 
	* @param demo
	* @return
	* @throws Exception
	 */
	@Route(value="/demo110.html")
	@MethodType(type=mType.post)
	public String demo110(@QueryParam("demo") String demo) throws Exception{
		//HttpServletResponse response=MvcPageContextUtil.getResponse();
		return "/index.jsp";
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo33.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo33.html")
	@MethodType(type=mType.get)
	public void demo33(@FormParam("demo") String demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo);
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo331.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo331.html")
	@MethodType(type=mType.get)
	public void demo331(@FormParam("demo") Map<String, Object> demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo.get("demo")+"");
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo332.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo332.html")
	@MethodType(type=mType.get)
	public void demo332(@FormParam("demo") DemoEntity demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo.getDemo());
	}
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>URL:http://127.0.0.1:8080/jdmvc-samples/demo/demo333.html?demo=3 </p> 
	* @param demo
	* @throws Exception
	 */
	@Route(value="/demo333.html")
	@MethodType(type=mType.get)
	public void demo333(@FormParam("demo") Object[] demo) throws Exception{
		HttpServletResponse response=MvcPageContextUtil.getResponse();
		MvcPageUtil.resultHTMLToString(response, demo.length+"");
	}
}
