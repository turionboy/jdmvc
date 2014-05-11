package com.liubing.mvc.core.controller.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author liubingsmile@gmail.com
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MethodType {
	public enum mType {
		get, post, put, delete, head
	};

	public mType type() default mType.get;

	public enum returnType {
		JSON, String, XML, JSONP
	}

	public returnType returnType() default returnType.String;
}
