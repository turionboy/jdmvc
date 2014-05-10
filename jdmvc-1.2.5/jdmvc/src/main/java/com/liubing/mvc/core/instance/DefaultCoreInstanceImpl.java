/**
 * 
 */
package com.liubing.mvc.core.instance;

/**
 * @author liubing1
 *
 */
public class DefaultCoreInstanceImpl implements ICoreInstance {

	
	
	public Object returnclass(String classname) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object returnclass(Class cls) throws Exception {
		// TODO Auto-generated method stub
		return cls.newInstance();
	}
}
