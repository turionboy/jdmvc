package com.liubing.mvc.core.servlet;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.liubing.mvc.core.cache.face.ICache;
import com.liubing.mvc.core.cache.face.impl.CacheImpl;
import com.liubing.mvc.core.common.RouteInfo;
import com.liubing.mvc.core.render.DefaultViewRender;
import com.liubing.mvc.core.render.ViewRender;
import com.liubing.mvc.core.service.IBaseCoreMvcService;
import com.liubing.mvc.core.service.impl.BaseCoreMvcServiceImpl;
import com.liubing.mvc.core.util.MvcPageContextUtil;
import com.liubing.mvc.core.util.MvcPageUtil;
import com.liubing.mvc.core.util.StringUtil;
import com.liubing.mvc.core.util.WebUtil;
/**
 * 框架核心控制层
 * Servlet implementation class CoreServlert
 */
public   class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//private Log logger=LogFactory.getLog(getClass());
     
    private static ICache<String,ConcurrentLinkedQueue<RouteInfo> > cache;
    
    private static ViewRender viewRender;
     /**
 	 * 项目包的根路径
 	 */
 	@SuppressWarnings("unused")
	private String rootPkg = "";

 	/**
 	 * Controller 过滤
 	 */
 	@SuppressWarnings("unused")
	private String controllerFilters = "";
 	
 	private static Boolean isSupportSpring;
 	/**
 	 * 路由集合
 	 */
 	private  ConcurrentLinkedQueue<RouteInfo> routeInfos;
   
    /**
     * MVC 核心核心控制层，
     * 判断http是ajax请求，那么直接print参数
     * 如果不是直接跳转
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
    		throws ServletException, IOException {
    	// TODO Auto-generated method stub
		// 缓存处理
		MvcPageContextUtil.threadLocalRequest.set(req);
		MvcPageContextUtil.threadLocalResponse.set(res);
    	String method = req.getMethod().toLowerCase();
		String pathInfo = req.getServletPath();
		IBaseCoreMvcService coreMvcService=new BaseCoreMvcServiceImpl();
		routeInfos=cache.getSilent("mvc_roteinfo");
		Object result="";
		try {
		ConcurrentHashMap<String,Object> map = coreMvcService.isRouteInfos(routeInfos,pathInfo,method);
		
		if(Boolean.parseBoolean(map.get("isRouteInfos").toString())){
			RouteInfo routeInfo = (RouteInfo) map.get("routeInfo");
			result=(String) coreMvcService.methodInvoke(routeInfo,isSupportSpring);	
			if(MvcPageUtil.isAjaxRequest(MvcPageContextUtil.getRequest())&&!StringUtil.isNullOrEmpty(result)){//ajax请求
				viewRender.renderResult(MvcPageContextUtil.getRequest(), MvcPageContextUtil.getResponse(), result,routeInfo.getReturnType());
			}else if(!StringUtil.isNullOrEmpty(result)){//页面跳转
				viewRender.renderPage(MvcPageContextUtil.getRequest(), MvcPageContextUtil.getResponse(), result+"");
			}
		  }
		} catch (Exception e) {
			 handleActionMethodException(req, res, e);
		}finally{
			MvcPageContextUtil.threadLocalRequest.remove();
			MvcPageContextUtil.threadLocalResponse.remove();
		}
		
    }
	
	/**
	 * 初始化加载
	 */
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		//logger.info("jdmvc服务开始启动-----------------");
		String  rootPkg=getInitParameter("rootPkg");
		String  controllerFilters=getInitParameter("controllerFilters");
		isSupportSpring=Boolean.parseBoolean(getInitParameter("isSupportSpring"));
		if(StringUtil.isNullOrEmpty(getInitParameter("viewRender"))){
			viewRender =new DefaultViewRender();
		}else{
			Object bean = null;
	        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	        try {
	            Class<?> clazz = classLoader.loadClass(getInitParameter("viewRender"));
	            bean = clazz.newInstance();
	            if(bean instanceof ViewRender){
		        	viewRender =(ViewRender) bean;
		        }else{
		        	viewRender =new DefaultViewRender();
		        }
	        } catch (Exception e) {
	           // logger.error(e.getMessage());
	        }
	        
		}
		viewRender.init(getServletContext());
		IBaseCoreMvcService coreMvcService=new BaseCoreMvcServiceImpl();
		routeInfos=coreMvcService.getRouteInfos(rootPkg, controllerFilters);
		cache=new CacheImpl<String, ConcurrentLinkedQueue<RouteInfo>>(routeInfos.size());
		cache.put("mvc_roteinfo", routeInfos);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	private void handleActionMethodException(HttpServletRequest request, HttpServletResponse response, Exception e) {
            // 若为认证异常，则分两种情况进行处理
            if (WebUtil.isAJAX(request)) {
                // 若为 AJAX 请求，则发送 403 错误
                WebUtil.sendError(403, response,e.getMessage());
                request.setAttribute("javax.servlet.error.exception", e);
            } else {
                // 否则重定向到首页
                WebUtil.redirectRequest(request.getContextPath() + "/", response);
            }
        
    }
}
