
package com.liubing.mvc.core.cache.face;
/**
 * LRU 接口
 * @author liubingsmile@gmail.com
 *
 */
public interface ICache<KEY,VALUE> {
	void put ( KEY key, VALUE value );

	VALUE get ( KEY key );

	VALUE getSilent ( KEY key );

    void remove ( KEY key );

    int size ();

}
