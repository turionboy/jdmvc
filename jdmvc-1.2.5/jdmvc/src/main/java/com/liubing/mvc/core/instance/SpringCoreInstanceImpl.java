/**
 * 
 */
package com.liubing.mvc.core.instance;

import com.liubing.mvc.core.util.SpringContextUtil;

/**
 * @author liubing1
 *
 */
public class SpringCoreInstanceImpl implements ICoreInstance {

	public Object returnclass(String classname) throws Exception {
		// TODO Auto-generated method stub
		return SpringContextUtil.getBean(classname);
	}

	public Object returnclass(Class cls) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}
