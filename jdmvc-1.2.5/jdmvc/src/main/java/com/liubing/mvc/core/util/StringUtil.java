package com.liubing.mvc.core.util;

public class StringUtil {
	public static boolean isNullOrEmpty(Object paramObject) {
		return (paramObject == null)
				|| ("".equals(paramObject.toString()))
				|| (paramObject.equals("null") || paramObject.toString().trim()
						.equals(""));
	}

	public static String toString(Object paramObject) {
		if (paramObject == null)
			return "null";
		return paramObject.toString();
	}
}
