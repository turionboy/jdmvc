package com.liubing.mvc.core.exception;

public enum SysError implements ErrorCode {
    //@formatter:off
	ClassNotFound_ERROR,
	FOUNDNOPATH_ERROR,
	Runtime_ERROR,
	UnsupportedEncoding_ERROR,
	Parse_ERROR,
	REFLECT_ERROR,
	MvcPage_ERROR,
	PARAM_ERROR,
	InvocationTarget_ERROR,
	RouteParam_ERROR
    //@formatter:on
}
