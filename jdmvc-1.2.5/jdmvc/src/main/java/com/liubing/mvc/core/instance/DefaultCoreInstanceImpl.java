/**
 * 
 */
package com.liubing.mvc.core.instance;

/**
 * @author liubing1
 *
 */
public class DefaultCoreInstanceImpl implements ICoreInstance {

	
	@Override
	public Object returnclass(String classname) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object returnclass(Class cls) throws Exception {
		// TODO Auto-generated method stub
		return cls.newInstance();
	}
}
