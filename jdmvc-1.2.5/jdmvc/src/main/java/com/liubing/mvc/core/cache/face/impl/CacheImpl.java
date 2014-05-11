package com.liubing.mvc.core.cache.face.impl;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.liubing.mvc.core.cache.face.ICache;

/**
 * LRU CACHE 实现
 * 
 * @author liubingsmile@gmail.com
 * 
 * @param <KEY>
 * @param <VALUE>
 */
public class CacheImpl<KEY, VALUE> implements ICache<KEY, VALUE> {

	private final ReentrantLock lock = new ReentrantLock();

	private final ConcurrentHashMap<KEY, VALUE> map = new ConcurrentHashMap<KEY, VALUE>();
	private final LinkedList<KEY> queue = new LinkedList<KEY>();
	private final int limit;

	public CacheImpl(int limit) {
		this.limit = limit;
	}

	public void put(KEY key, VALUE value) {
		VALUE oldValue = map.put(key, value);
		if (oldValue != null) {
			removeThenAddKey(key);
		} else {
			addKey(key);
		}
		if (map.size() > limit) {
			map.remove(removeLast());
		}
	}

	public VALUE get(KEY key) {
		removeThenAddKey(key);
		return map.get(key);
	}

	private void addKey(KEY key) {
		lock.lock();
		try {
			queue.addFirst(key);
		} finally {
			lock.unlock();
		}

	}

	private KEY removeLast() {
		lock.lock();
		try {
			final KEY removedKey = queue.removeLast();
			return removedKey;
		} finally {
			lock.unlock();
		}
	}

	private void removeThenAddKey(KEY key) {
		lock.lock();
		try {
			queue.removeFirstOccurrence(key);
			queue.addFirst(key);
		} finally {
			lock.unlock();
		}

	}

	private void removeFirstOccurrence(KEY key) {
		lock.lock();
		try {
			queue.removeFirstOccurrence(key);
		} finally {
			lock.unlock();
		}

	}

	public VALUE getSilent(KEY key) {
		return map.get(key);
	}

	public void remove(KEY key) {
		removeFirstOccurrence(key);
		map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public String toString() {
		return map.toString();
	}

}