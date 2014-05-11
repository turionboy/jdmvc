/**   
 * @Title: IBaseCoreMvcService.java 
 * @Package com.jd.mvc.core.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author liubingsmile@gmail.com	   
 * @date 2014-4-25 下午11:45:41 
 * @version V1.0   
 */
package com.liubing.mvc.core.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.liubing.mvc.core.common.RouteInfo;

/**
 * @author liubingsmile@gmail.com
 * 
 */
public interface IBaseCoreMvcService {
	/**
	 * 
	 * <br/>
	 * Description:获取路由信息集合
	 * 
	 * @param packageDir
	 * @param filters
	 * @return
	 */
	public ConcurrentLinkedQueue<RouteInfo> getRouteInfos(String packageDir,
			String filters);

	/**
	 * 
	 * <br/>
	 * Description:判断传入类是否实现的EasyPageListener接口
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isImplementsEasyPageListener(Class<?> clazz);

	/**
	 * 
	 * <br/>
	 * Description:判断请求地址是否包含在路由表内，有则返回对应的实体
	 * <p>
	 * Author:Eric Shi/史丙利
	 * </p>
	 * 
	 * @param routeInfos
	 * @param route
	 * @param methodType
	 * @return
	 */
	public ConcurrentHashMap<String, Object> isRouteInfos(
			ConcurrentLinkedQueue<RouteInfo> routeInfos, String route,
			String methodType) throws Exception;

	/**
	 * 
	 * <br/>
	 * Description:根据参数执行对应的方法
	 * <p>
	 * Author:Eric Shi/史丙利
	 * </p>
	 * 
	 * @param routeInfo
	 * @param listenerBeans
	 * @param isSupportSpring
	 * @return
	 * @throws ControllerException
	 * @throws OperateSpringBeanException
	 * @throws ListenerException
	 * @throws ConversionTypeException
	 * @throws UnknownException
	 */
	public Object methodInvoke(RouteInfo routeInfo, boolean isSupportSpring)
			throws Exception;

}
