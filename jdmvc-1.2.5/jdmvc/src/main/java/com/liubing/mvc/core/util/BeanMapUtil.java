/**   
 * @Title: BeanMapUtil.java 
 * @Package com.jd.mvc.core.util 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author liubingsmile@gmail.com	   
 * @date 2014-5-7 下午2:32:35 
 * @version V1.0   
 */
package com.liubing.mvc.core.util;

import java.util.Map;

import net.sf.cglib.beans.BeanMap;

/**
 * @author liubingsmile@gmail.com
 * 
 */
public class BeanMapUtil {
	/**
	 * 
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:javabean transform map
	 * </p>
	 * 
	 * @param object
	 * @return
	 */
	public static Map<String, Object> convertBeanToMap(Object object) {
		return BeanMap.create(object);
	}

	/**
	 * 
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:javabean transform map
	 * </p>
	 * 
	 * @param object
	 * @param result
	 * @return
	 */
	public static Object convertMapToBean(Object object,
			Map<String, Object> result) {
		Map map = BeanMap.create(object);
		for (String key : result.keySet()) {
			if (map.containsKey(key)
					&& !StringUtil.isNullOrEmpty(result.get(key))) {
				map.put(key, result.get(key));
			}
		}
		return map;
	}

	public static Object convertStringMapToBean(Object object,
			Map<String, String> result) {
		Map map = BeanMap.create(object);
		for (String key : result.keySet()) {
			if (map.containsKey(key)
					&& !StringUtil.isNullOrEmpty(result.get(key))) {
				map.put(key, result.get(key));
			}
		}
		return map;
	}
}
