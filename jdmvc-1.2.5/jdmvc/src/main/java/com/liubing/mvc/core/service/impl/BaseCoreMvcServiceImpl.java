package com.liubing.mvc.core.service.impl;

import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.http.HttpServletRequest;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.lang3.ClassUtils;

import com.liubing.mvc.core.common.RouteInfo;
import com.liubing.mvc.core.controller.annotation.FormParam;
import com.liubing.mvc.core.controller.annotation.MethodType;
import com.liubing.mvc.core.controller.annotation.QueryParam;
import com.liubing.mvc.core.controller.annotation.Route;
import com.liubing.mvc.core.controller.annotation.RouteParam;
import com.liubing.mvc.core.exception.IOEError;
import com.liubing.mvc.core.exception.SysError;
import com.liubing.mvc.core.exception.SystemException;
import com.liubing.mvc.core.instance.DefaultCoreInstanceImpl;
import com.liubing.mvc.core.instance.ICoreInstance;
import com.liubing.mvc.core.instance.SpringCoreInstanceImpl;
import com.liubing.mvc.core.interceptor.DefaultInterceptor;
import com.liubing.mvc.core.service.IBaseCoreMvcService;
import com.liubing.mvc.core.util.BeanMapUtil;
import com.liubing.mvc.core.util.MvcPageContextUtil;
import com.liubing.mvc.core.util.MvcPageUtil;
import com.liubing.mvc.core.util.ScanClassPathUtil;
import com.liubing.mvc.core.util.StringUtil;
/**
 * 
 * @author liubingsmile@gmail.com
 *
 */
public  class BaseCoreMvcServiceImpl implements IBaseCoreMvcService{
	
	
	//private static final Map<Class<?>, Method[]> CACHE = Collections.synchronizedMap(new WeakHashMap<Class<?>, Method[]>());
	public ConcurrentLinkedQueue<RouteInfo> getRouteInfos(String packageDir, String filters) {
		// TODO Auto-generated method stub
		//ConcurrentLinkedQueue<E>
		ConcurrentLinkedQueue<RouteInfo> routeInfos = new ConcurrentLinkedQueue<RouteInfo>();
		List<String> classFilters = new ArrayList<String>();
		classFilters.add(filters);
		// 创建一个扫描处理器，排除内部类 扫描符合条件的类
		ScanClassPathUtil handler = new ScanClassPathUtil(true,true,classFilters);
		// 递归扫描包 下符合自定义过滤规则的类
		Set<Class<?>> clazzList = handler.getPkgClassAll(packageDir,true);
		for(Class<?> clazz:clazzList){
			Method[] methods = clazz.getMethods();
			List<Method> methodList = new ArrayList<Method>();
			//循环获取所有的方法 除了 Result_JsonTxt或者Result_JsonFile方法
			for(int i = 0;i < methods.length;i ++ ){
				Method m = methods[i];
				if( !Modifier.isStatic(m.getModifiers())&&! "Result_JsonTxt".equalsIgnoreCase(m.getName()) && ! "Result_JsonFile".equalsIgnoreCase(m.getName()) && ! "ForwardPage".equalsIgnoreCase(m.getName())){
					methodList.add(m);
				}
			}
			for(Method method:methodList){
				if(method.isAnnotationPresent(Route.class)){
					RouteInfo routeInfo = new RouteInfo();
					Route pathMethod = method.getAnnotation(Route.class);
					if(null != pathMethod){
						routeInfo.setClazz(clazz.getName());
						routeInfo.setMethods(clazz.getMethods());
						routeInfo.setMethod(method.getName());
						boolean boo = clazz.isAnnotationPresent(Route.class);
						Route pathClazz = null;
						if(boo){
							pathClazz = clazz.getAnnotation(Route.class);
							String path = ((null != pathClazz)?MvcPageUtil.deleteRightBar(pathClazz.value()):"") + MvcPageUtil.deleteRightBar(pathMethod.value());
							routeInfo.setRoute(path.trim());
							routeInfo.setRouteLength(path.split("/").length);
							routeInfo.setInterceptor(pathClazz.cls());
						}
						
					}
					MethodType methodType = method.getAnnotation(MethodType.class);
					if(null != methodType){
						routeInfo.setCallMethod(methodType.type().toString());
						routeInfo.setReturnType(methodType.returnType().toString());
					}
					
					routeInfos.add(routeInfo);
				}
			}
		}
		return routeInfos;
	}

	

	public boolean isImplementsEasyPageListener(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	public ConcurrentHashMap<String, Object> isRouteInfos(ConcurrentLinkedQueue<RouteInfo> routeInfos,
			String route, String methodType) throws Exception{
		// TODO Auto-generated method stub
		ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<String,Object>();
		String inUrl = MvcPageUtil.deleteRightBar(route);
		String[] inUrls = inUrl.split("/");
		int inUrlsLen = inUrls.length;
		for(RouteInfo routeInfo:routeInfos){
			if(!routeInfo.getRoute().contains("{")&&!routeInfo.getRoute().contains("}")){
				if(routeInfo.getRoute().equals(inUrl)&&routeInfo.getCallMethod().equalsIgnoreCase(methodType)){//没有URL作为参数匹配
					map.put("isRouteInfos",true);
					map.put("routeInfo",routeInfo);
					return map;
				}
			}else{
				if(routeInfo.getRoute().contains("{")&&routeInfo.getRoute().contains("}")&&routeInfo.getRouteLength() == inUrlsLen&&routeInfo.getCallMethod().equalsIgnoreCase(methodType)){
					String[] mUrls = MvcPageUtil.deleteRightBar(routeInfo.getRoute()).split("/");
					Map<String, Object> params=new HashMap<String, Object>();
					List<String> results=new ArrayList<String>();
					for(int i=0;i<mUrls.length;i++){
						if(!mUrls[i].equals(inUrls[i])){
							if(mUrls[i].contains(".")&&mUrls[i].contains("{")&&mUrls[i].contains("}")){//{demo}.html
								String value=inUrls[i].substring(0, inUrls[i].indexOf("."));
								String key=mUrls[i].substring(mUrls[i].indexOf("{")+1, mUrls[i].indexOf("}"));
								params.put(key, value);
							}else if(!mUrls[i].contains(".")&&mUrls[i].contains("{")&&mUrls[i].contains("}")){
								params.put(mUrls[i].substring(1, mUrls[i].length()-1),MvcPageUtil.deleteBigBrackets(inUrls[i]));
							}
							
						}else{
							results.add(mUrls[i]);
						}
					}
					if(results.size()+params.keySet().size()==mUrls.length){
						routeInfo.setParams(params);
						map.put("isRouteInfos",true);
						map.put("routeInfo",routeInfo);
						return map;
					}	
				}
			}
			
		}
		map.put("isRouteInfos",false);
		map.put("routeInfo",null);
		return map;
	}
	
	
	public Map<String,String> getPutParams(HttpServletRequest request,String charset){
		InputStreamReader in = null;
		try{
			//request.getParameterMap()
			in = new InputStreamReader(request.getInputStream(),charset);
		}catch(Exception e){
			// e.printStackTrace();
			throw SystemException.unchecked(e, IOEError.IOE_ERROR);
		}
		String urlParam = MvcPageUtil.getServletInputStream(in);
		return MvcPageUtil.getUrlParamByKey(urlParam);
	}

	
	public Object methodInvoke(RouteInfo routeInfo, boolean isSupportSpring)
			throws Exception {
		// TODO Auto-generated method stub
		MvcPageContextUtil easyPageContext = new MvcPageContextUtil();
		HttpServletRequest request = easyPageContext.getRequest();
		String method = request.getMethod();
		String charset = request.getCharacterEncoding();
		Map<String,String> multipartParams = null;
		Map<String,String> putParams = null;
		Object obj = null;
		try{
			boolean isMultipartRequest = MvcPageUtil.isMultipartRequest(request);
			if(isMultipartRequest){
				multipartParams = MvcPageUtil.getMultipartParams(request,charset);
			}
			if("put".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)){
				if(isMultipartRequest){
					putParams = multipartParams;
				}else{
					putParams = getPutParams(request,charset);
				}
			}
			Class<?> clazz = MvcPageUtil.getClassByClassLoader(MvcPageUtil.getClassLoader(),routeInfo.getClazz());
			Method[] mds = routeInfo.getMethods();
			for(int i = 0;i < mds.length;i ++ ){
				Method md = mds[i];
				if(routeInfo.getMethod().equals(md.getName())){
					Class<?>[] mdTypes = md.getParameterTypes();
					Annotation[][] annoParams = md.getParameterAnnotations();
					Object[] objs = new Object[annoParams.length];
					for(int j = 0;j < annoParams.length;j ++ ){
						Annotation annotation = annoParams[j][0];
						if("FormParam".equals(annotation.annotationType().getSimpleName())){
							FormParam param = (FormParam) annotation;
							String pmType = mdTypes[j].getName();
							Map<String, String> params=new HashMap<String, String>();
							if(isMultipartRequest){
								params.putAll(multipartParams);
								//throw new Exception("the jdmvc do not support uploadfile");
							}
							if("put".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)){
								params=putParams;
							}else{
								Enumeration enumeration = request.getParameterNames();
								while(enumeration.hasMoreElements()){
									String key=(String) enumeration.nextElement();
									params.put(key, request.getParameter(key));
								}
							}
							if(ClassUtils.isAssignable(mdTypes[j], Map.class)){
								objs[j] =params ;
							}else if(mdTypes[j].isArray()){
								Object[] object=new Object[params.keySet().size()];
								int k=0;
								for(String key:params.keySet()){
									object[k]=params.get(key);
									k++;
								}
								objs[j]=object;
							}else if(ClassUtils.isAssignable(mdTypes[j], Collection.class)){
								List result=new LinkedList();
								for(String key:params.keySet()){
									result.add(params.get(key));
								}
								objs[j]=result;
//								throw new Exception("RouteParam时，java 基本类型不能作为参数"+pmType);
							}else if(mdTypes[j].isPrimitive() || mdTypes[j].getName().startsWith("java.")){
								if(params.containsKey(param.value())){
									objs[j] = MvcPageUtil.createObjectByParamType(pmType,params.get(param.value())+"");
								}
							}else{//javabean
								objs[j]=mdTypes[j].newInstance();
								BeanMapUtil.convertStringMapToBean(objs[j], params);
							}
						}else if("QueryParam".equals(annotation.annotationType().getSimpleName())){
							QueryParam param = (QueryParam) annotation;
							String pmType = mdTypes[j].getName();
							Map<String, String> params=new HashMap<String, String>();
							if("put".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)){//两种方式
								params=putParams;
							}else{
								Enumeration enumeration = request.getParameterNames();
								while(enumeration.hasMoreElements()){
									String key=(String) enumeration.nextElement();
									params.put(key, request.getParameter(key));
								}
							}
							if(ClassUtils.isAssignable(mdTypes[j], Map.class)){
								objs[j] =params ;
							}else if(mdTypes[j].isArray()){
								Object[] object=new Object[params.keySet().size()];
								int k=0;
								for(String key:params.keySet()){
									object[k]=params.get(key);
									k++;
								}
								objs[j]=object;
							}else if(ClassUtils.isAssignable(mdTypes[j], Collection.class)){
								List result=new LinkedList();
								for(String key:params.keySet()){
									result.add(params.get(key));
								}
								objs[j]=result;
//								throw new Exception("RouteParam时，java 基本类型不能作为参数"+pmType);
							}else if(mdTypes[j].isPrimitive() || mdTypes[j].getName().startsWith("java.")){
								if(params.containsKey(param.value())){
									objs[j] = MvcPageUtil.createObjectByParamType(pmType,params.get(param.value())+"");
								}
							}else{//javabean
								objs[j]=mdTypes[j].newInstance();
								BeanMapUtil.convertStringMapToBean(objs[j], params);
							}
						}else if("RouteParam".equals(annotation.annotationType().getSimpleName())){
							RouteParam param = (RouteParam) annotation;
							String pmType = mdTypes[j].getName();
							if(ClassUtils.isAssignable(mdTypes[j], Map.class)){
								objs[j] =routeInfo.getParams() ;
							}else if(mdTypes[j].isArray()){
								Map<String, Object> params=routeInfo.getParams();
								Object[] object=new Object[params.keySet().size()];
								int k=0;
								for(String key:params.keySet()){
									object[k]=params.get(key);
									k++;
								}
								objs[j]=object;
							}else if(ClassUtils.isAssignable(mdTypes[j], Collection.class)){
								Map<String, Object> params=routeInfo.getParams();
								List result=new LinkedList();
								for(String key:params.keySet()){
									result.add(params.get(key));
								}
								objs[j]=result;
//								throw new Exception("RouteParam时，java 基本类型不能作为参数"+pmType);
							}else if(mdTypes[j].isPrimitive() || mdTypes[j].getName().startsWith("java.")){
								Map<String, Object> params=routeInfo.getParams();
								if(params.containsKey(param.value())){
									objs[j] = MvcPageUtil.createObjectByParamType(pmType,routeInfo.getParams().get(param.value())+"");
								}
							}else{//javabean
								objs[j]=mdTypes[j].newInstance();
								BeanMapUtil.convertMapToBean(objs[j], routeInfo.getParams());
							}
						}
					}
					Object o = null;
					if(!StringUtil.isNullOrEmpty(isSupportSpring)&&isSupportSpring){
						try{
							ICoreInstance coreInstance=new SpringCoreInstanceImpl();
							String clsname=clazz.getSimpleName().substring(0, 1).toLowerCase()+clazz.getSimpleName().substring(1);
							o = coreInstance.returnclass(clsname);
						}catch(Exception e){
							throw SystemException.unchecked(e, SysError.ClassNotFound_ERROR);
						}
					}else{
						try{
							ICoreInstance coreInstance=new DefaultCoreInstanceImpl();
							o = coreInstance.returnclass(clazz);
						}catch(Exception e){
							throw SystemException.unchecked(e, SysError.ClassNotFound_ERROR);
						}
					}
					Boolean  flag=true;
					Class<?> interceptorclass=routeInfo.getInterceptor();
					if(!StringUtil.isNullOrEmpty(interceptorclass)&&interceptorclass.newInstance() instanceof DefaultInterceptor){
						DefaultInterceptor defaultInterceptot=(DefaultInterceptor) interceptorclass.newInstance();
						if(objs.length>0){
							flag=defaultInterceptot.preInterceptor(clazz,routeInfo.getRoute(), md, objs);
						}else{
							flag=defaultInterceptot.preInterceptor(clazz,routeInfo.getRoute(), md, null);
						}	
					}//开始前拦截
					if(flag){//通过
						try{
							md.setAccessible(true);
							if(objs.length>0){						
								obj=md.invoke(o, objs);//执行
							}else{
								obj=md.invoke(o, null);
							}
						}catch(InvocationTargetException e){
							throw SystemException.unchecked(e, SysError.InvocationTarget_ERROR);
							//throw new Exception(e.getTargetException().getMessage());
						} catch (Throwable e) {
							throw SystemException.unchecked(e);
						}
						
					}
					if(!StringUtil.isNullOrEmpty(interceptorclass)&&interceptorclass.newInstance() instanceof DefaultInterceptor){
						DefaultInterceptor defaultInterceptot=(DefaultInterceptor) interceptorclass.newInstance();
						if(objs.length>0){
							defaultInterceptot.afterInterceptor(clazz,routeInfo.getRoute(), md, objs);
						}else{
							defaultInterceptot.afterInterceptor(clazz,routeInfo.getRoute(), md, null);
						}	
					}//方法后拦截
					break;
				}
			}
		}catch(Exception e){
			throw SystemException.unchecked(e, SysError.MvcPage_ERROR);
		}
		return obj;
	}


}
