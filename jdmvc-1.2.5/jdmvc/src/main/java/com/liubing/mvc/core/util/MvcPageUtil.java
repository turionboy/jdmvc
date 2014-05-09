package com.liubing.mvc.core.util;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.liubing.mvc.core.exception.ConversionError;
import com.liubing.mvc.core.exception.IOEError;
import com.liubing.mvc.core.exception.SysError;
import com.liubing.mvc.core.exception.SystemException;
import com.liubing.mvc.core.exception.UnknownError;
import com.liubing.mvc.core.exception.UploadFileExceptionError;
/**
 * MVC 框架工具类
 * @author Administrator
 *
 */
public class MvcPageUtil{
	private static final Logger logger = Logger.getLogger(MvcPageUtil.class);
	/**
	 * 
	 * <br/>Description:根据key取出Map里对应的value
	 * 
	 * 
	 * @param maps
	 * @param key
	 * @return
	 */
	public static String getMapValue(List<ConcurrentHashMap<String,String>> maps,String key){
		String val = null;
		for(Map<String,String> m:maps){
			String v = m.get(key);
			if(null != v && ! "".equalsIgnoreCase(v) && ! "null".equalsIgnoreCase(v)){
				val = v;
				break;
			}
		}
		return val;
	}

	/**
	 * 
	 * <br/>Description:返回当前Request请求IP
	 * 
	 * @param req
	 * @return
	 */
	public static String getRealIp(HttpServletRequest req){
		String lastLoginIP = req.getHeader("x-forwarded-for");
		if(lastLoginIP == null || lastLoginIP.length() == 0 || "unknown".equalsIgnoreCase(lastLoginIP)){
			lastLoginIP = req.getHeader("Proxy-Client-IP");
		}
		if(lastLoginIP == null || lastLoginIP.length() == 0 || "unknown".equalsIgnoreCase(lastLoginIP)){
			lastLoginIP = req.getHeader("WL-Proxy-Client-IP");
		}
		if(lastLoginIP == null || lastLoginIP.length() == 0 || "unknown".equalsIgnoreCase(lastLoginIP)){
			lastLoginIP = req.getRemoteAddr();
		}
		return lastLoginIP;
	}

	/**
	 * 
	 * <br/>Description:生成UUID
	 * 
	 * @return
	 */
	public static String generateUUID(){
		String uuid = UUID.randomUUID().toString().replace("-","").toUpperCase();
		return uuid;
	}

	/**
	 * 
	 * <br/>Description:根据当前时间返一个long字符串
	 
	 * 
	 * @return
	 */
	public static Long generateLong(){
		Long l = new Date().getTime();
		return l;
	}

	/**
	 * 
	 * <br/>Description:返回 HttpSession
	 * 
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		return session;
	}

	/**
	 * 
	 * <br/>Description:将一个对象放入Session
	 * 
	 * @param sessionName
	 * @param obj
	 * @param request
	 * @return
	 */
	public static boolean addSession(String sessionName,Object obj,HttpServletRequest request){
		boolean boo = false;
		try{
			HttpSession session = getSession(request);
			session.setAttribute(sessionName,obj);
			boo = true;
		}catch(Exception e){
			boo = false;
		}

		return boo;
	}

	/**
	 * 
	 * <br/>Description:设置P3P，以完成跨域请求
	 * 
	 * @param response
	 */
	public static void setResponseHeaderP3P(HttpServletResponse response){
		response.setHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
	}

	/**
	 * 
	 * <br/>Description:添加 Cookie
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 * @param path
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,Integer maxAge,String path){
		Cookie cookie = new Cookie(name,value);

		if( ! (null == path) || ! "".equalsIgnoreCase(path)){
			cookie.setPath(path);
		}else{
			cookie.setPath("/");
		}

		if(null != maxAge){
			cookie.setMaxAge(maxAge.intValue() * 60); // 单位为秒所以* 60 以便按分钟设值
		}

		cookie.setSecure(true);

		response.addCookie(cookie);
	}

	public static void addHttpsCookie(HttpServletResponse response,String name,String value,Integer maxAge,String path){
		Cookie cookie = new Cookie(name,value);

		if( ! (null == path) || ! "".equalsIgnoreCase(path)){
			cookie.setPath(path);
		}else{
			cookie.setPath("/");
		}

		if(null != maxAge){
			cookie.setMaxAge(maxAge.intValue() * 60); // 单位为秒所以* 60 以便按分钟设值
		}

		cookie.setSecure(true);

		response.addCookie(cookie);
	}

	public static void addCookie(HttpServletResponse response,String name,String value){
		Cookie cookie = new Cookie(name,value);

		cookie.setPath("/");

		cookie.setMaxAge( - 1);

		response.addCookie(cookie);
	}

	/**
	 * 
	 * <br/>Description:添加 SSO Cookie maxAge为两周
	 * 
	 * @param response
	 * @param cookieMsg
	 */
	public static void addSSOInfo(HttpServletResponse response,String cookieMsg){
		setResponseHeaderP3P(response);
		addCookie(response,"_ticket",cookieMsg,60 * 60 * 24 * 14,"/");
	}

	/**
	 * 
	 * <br/>Description:添加 SSO Cookie 并指定时间
	 * 
	 * @param response
	 * @param cookieMsg
	 * @param cookieMaxAge
	 */
	public static void addSSOInfo(HttpServletResponse response,String cookieMsg,Integer cookieMaxAge){
		setResponseHeaderP3P(response);
		addCookie(response,"_ticket",cookieMsg,cookieMaxAge,"/");
	}

	/**
	 * 
	 * <br/>Description:根据名字获取cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String cookieName){
		Map<String,Cookie> cookieMap = getCookieMap(request);
		if(cookieMap.containsKey(cookieName)){
			Cookie cookie = (Cookie) cookieMap.get(cookieName);
			return cookie;
		}else{
			return null;
		}
	}

	/**
	 * 
	 * <br/>Description:将cookie封装到Map里面并返回
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String,Cookie> getCookieMap(HttpServletRequest request){
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		Cookie[] cookies = getCooikes(request);
		if(null != cookies){
			for(Cookie cookie:cookies){
				cookieMap.put(cookie.getName(),cookie);
			}
		}
		return cookieMap;
	}

	/**
	 * 
	 * <br/>Description:返回Cookie集合
	 * 
	 * @param request
	 * @return
	 */
	public static Cookie[] getCooikes(HttpServletRequest request){
		return request.getCookies();
	}

	/**
	 * 
	 * <br/>Description:更新Cookie值
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 */
	public static void updateCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue){
		Cookie[] cookies = getCooikes(request);
		if(cookies.length > 1){
			for(int i = 0;i < cookies.length;i ++ ){
				if(cookies[i].getName().equalsIgnoreCase(cookieName)){
					cookies[i].setValue(cookieValue);
					response.addCookie(cookies[i]);
					break;
				}
			}
		}
	}

	/**
	 * 
	 * <br/>Description:删除指定Cookie
	 
	 * 
	 * @param response
	 * @param cookieName
	 * @param path
	 * @param domain
	 */
	public static void deleteCookie(HttpServletResponse response,String cookieName,String path,String domain){
		addCookie(response,cookieName,"",0,path);
		// ProjectTools.addCookie(response,cookieName,"",0,path,domain);
	}

	/**
	 * 
	 * <br/>Description:页面跳转
	 * 
	 * @param request
	 * @param response
	 * @param path
	 * @param isRequestDispatcher
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void redirectPage(HttpServletRequest request,HttpServletResponse response,String path,boolean isRequestDispatcher) throws IOException,ServletException{
		if(isRequestDispatcher){
			request.getRequestDispatcher(path).forward(request,response);
			// RequestDispatcher rd= request.getRequestDispatcher(path);
		}else{
			response.sendRedirect(path);
		}
	}

	/**
	 * 
	 * <br/>Description:返回JSON结果
	 
	 * 
	 * @param response
	 * @param str
	 */
	public static void resultJsonToString(HttpServletResponse response,String str){
		try{
			
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", - 1);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(str);
			out.close();
		}catch(IOException e){
			throw SystemException.unchecked(e, IOEError.IOE_ERROR);
			//logger.error("返回JSON结果失败。");
			//System.out.println();
		}
	}

	/**
	 * 
	 * <br/>Description:返回JSON结果
	 
	 * 
	 * @param response
	 * @param str
	 */
	public static void resultHTMLToString(HttpServletResponse response,String str){
		try{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(str);
			out.close();
		}catch(IOException e){
			throw SystemException.unchecked(e, IOEError.IOE_ERROR);
			//System.out.println("返回HTML结果失败。");
		}
	}

	/**
	 * 
	 * <br/>Description:返回XML结果
	 
	 * 
	 * @param response
	 * @param str
	 */
	public static void resultXMLToString(HttpServletResponse response,String str){
		try{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.setContentType("text/xml; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(str);
			out.close();
		}catch(IOException e){
			throw SystemException.unchecked(e, IOEError.IOE_ERROR);
			//System.out.println("返回HTML结果失败。");
		}
	}

	/**
	 * 
	 * <br/>Description:返回DataGrid数据
	 
	 * 
	 * @param total
	 * @param jsonArrayRows
	 * @return
	 */
	public static String toJQeryEasyUIDataGrid(int total,String jsonArrayRows){
		StringBuffer sb = new StringBuffer();

		sb.append("{").append("\"").append("total").append("\"").append(":");
		sb.append(total).append(",");
		sb.append("\"").append("data").append("\"").append(":");
		sb.append(jsonArrayRows);
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 
	 * <br/>Description:将字符串进行URLEncoder编码
	 
	 * 
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str){
		String encodeStr = null;
		try{
			encodeStr = URLEncoder.encode(str,"UTF-8");
		}catch(UnsupportedEncodingException e){
			throw SystemException.unchecked(e, SysError.UnsupportedEncoding_ERROR);
		}
		return encodeStr;
	}

	/**
	 * 
	 * <br/>Description:将字符串进行URLDecoder解码
	 
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecoder(String str){
		String decoderStr = null;
		try{
			decoderStr = URLDecoder.decode(str,"UTF-8");
		}catch(UnsupportedEncodingException e){
			throw SystemException.unchecked(e, SysError.UnsupportedEncoding_ERROR);

			//System.out.println("字符串解码失败。");
		}
		return decoderStr;
	}

	/**
	 * 
	 * <br/>Description:将字符串进行URLDecoder解码并转换成UTF-8编码
	 
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecoder(String str,String charset){
		String decoderStr = null;
		try{
			decoderStr = new String(URLDecoder.decode(str,charset).getBytes("ISO-8859-1"),charset);
		}catch(UnsupportedEncodingException e){
			throw SystemException.unchecked(e, SysError.UnsupportedEncoding_ERROR);
			//System.out.println("字符串解码失败。");
		}
		return decoderStr;
	}

	/**
	 * 
	 * <br/>Description:读取一个文本文件
	 
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String getFile(String filePath) throws IOException{
		try{
			StringBuffer sb = new StringBuffer();
			if(isEmptyFile(filePath)){
				InputStream is = new FileInputStream(filePath);

				String line;

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				line = reader.readLine();
				while(line != null){
					sb.append(line);
					sb.append("\n");
					line = reader.readLine();
				}
				is.close();
			}else{
				sb.append("无法获取文件！").append("\n").append("Can not get file!");
			}
			return sb.toString();
		}catch(IOException e){
			throw SystemException.unchecked(e, IOEError.IOE_ERROR);
		}
	}

	/**
	 * 
	 * <br/>Description:重命名文件
	 
	 * 
	 * @param filePath
	 * @param newFileName
	 */
	public static void renameFile(String filePath,String newFileName){

		int ll = filePath.lastIndexOf("/");

		String strPath = filePath.substring(0,ll + 1);
		// String strFileName=filePath.substring(ll+1,filePath.length());

		File oldFile = new File(filePath);
		File newFile = new File(strPath + "/" + newFileName);

		oldFile.renameTo(newFile);

		oldFile.delete();
	}

	/**
	 * 
	 * <br/>Description:自动按日期重命名文件
	 
	 * 
	 * @param filePath
	 */
	public static void renameFile(String filePath){

		int ll = filePath.lastIndexOf("/");

		String strPath = filePath.substring(0,ll + 1);
		String strFileName = filePath.substring(ll + 1,filePath.length());

		File oldFile = new File(filePath);
		File newFile = new File(strPath + "/" + strFileName + "_bak_" + getCurrentDate("yyyyMMdd_HHmmss"));

		oldFile.renameTo(newFile);

		oldFile.delete();
	}

	/**
	 * 
	 * <br/>Description:判断一个文件是否存在
	 
	 * 
	 * @param filePath
	 * @return
	 */
	public static Boolean isEmptyFile(String filePath){
		File f = new File(filePath);
		Boolean isEmptyFile = new Boolean(false);
		if(f.exists()){
			isEmptyFile = true;
		}
		return isEmptyFile;
	}

	/**
	 * 
	 * <br/>Description:判断文件夹是否存在
	 
	 * 
	 * @param directoryPath
	 * @return
	 */
	public static Boolean isDirectory(String directoryPath){
		File f = new File(directoryPath);
		Boolean isDirectory = new Boolean(false);
		if(f.isDirectory()){
			isDirectory = true;
		}
		return isDirectory;
	}

	/**
	 * 
	 * <br/>Description:获取当前日期
	 
	 * 
	 * @return
	 */
	public static String getCurrentDate(){
		Date d = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = date.format(d);
		return str;
	}

	/**
	 * 
	 * <br/>Description:返回国际化时间，含时区
	 
	 * 
	 * @return
	 */
	public static String getCurrentInternationalizationDate(){
		String str = null;
		try{
			Date d = new Date();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			str = date.format(d);
		}catch(Exception e){
			System.out.println("转换日期异常！");
		}
		return str;
	}

	/**
	 * 
	 * <br/>Description:获取当前日期,返回指定日期格式
	 
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String getCurrentDate(String dataFormat){
		Date d = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dataFormat);
		String str = date.format(d);
		return str;
	}

	/**
	 * 
	 * <br/>Description:根据格转式转换时间
	 
	 * 
	 * @param date
	 * @param dataFormat
	 * @return
	 */
	public static String conversionDate(Date date,String dataFormat){
		SimpleDateFormat d = new SimpleDateFormat(dataFormat);
		String str = d.format(date);
		return str;
	}

	/**
	 * 
	 * <br/>Description:格式化日期时间，返回String类型
	 
	 * 
	 * @param dateTime
	 * @param dataFormat
	 * @return
	 */
	public static String conversionDateReturnString(String dateTime,String dataFormat){
		SimpleDateFormat dateTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat date = new SimpleDateFormat(dataFormat);
		String str = null;
		try{
			str = date.format(dateTemp.parse(dateTime));
		}catch(ParseException e){
			throw SystemException.unchecked(e, SysError.Parse_ERROR);
		}

		return str;
	}

	/**
	 * 
	 * <br/>Description:long转Date
	 
	 * 
	 * @param longDate
	 * @return
	 */
	public static Date longToDate(long longDate){
		return new Date(longDate);
	}

	/**
	 * 
	 * <br/>Description:long转String
	 
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static String longToString(String dateStr,String dateFormat){
		String str = null;
		try{
			str = conversionDate(longToDate(Long.parseLong(dateStr)),dateFormat);
		}catch(Exception e){
			throw SystemException.unchecked(e, ConversionError.Conversion_ERROR);

		}
		return str;
	}

	/**
	 * 
	 * <br/>Description:获取当前日期返回Date类型
	 
	 * 
	 * @return
	 */
	public static Date getCurrentDateReturnDate(){
		Date strDate = new Date();
		return strDate;
	}

	/**
	 * 
	 * <br/>Description:格式化日期时间，返回Date类型
	 
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date conversionDateReturnDate(String dateTime){
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date strDate = null;
		try{
			strDate = date.parse(dateTime);
		}catch(ParseException e){
			throw SystemException.unchecked(e, SysError.Parse_ERROR);
		}
		return strDate;
	}

	/**
	 * 
	 * <br/>Description:格式化日期时间，返回Date类型(输入方式月日年时分秒)
	 
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date conversionDateReturnDateInput_Mdy(String dateTime){
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date strDate = null;
		try{
			strDate = date.parse(dateTime);
		}catch(ParseException e){
			throw SystemException.unchecked(e, SysError.Parse_ERROR);
		}
		return strDate;
	}

	/**
	 * 
	 * <br/>Description:转换国际化时间
	 
	 * 
	 * @param dateTime 2010-06-01T20:00:00+08:00
	 * @return
	 */
	public static Date conversionDateToDateByInternationalization(String dateTime){
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date strDate = null;
		try{
			strDate = date.parse(dateTime);
		}catch(ParseException e){
			throw SystemException.unchecked(e, SysError.Parse_ERROR);
		}
		return strDate;
	}

	/**
	 * 
	 * <br/>Description:转换国际化时间
	 
	 * 
	 * @param dateTime 2010-06-01T20:00:00+08:00
	 * @param dateFormat 日常日期格式 例如：yyyy-MM-dd'T'HH:mm:ss+hh:mm
	 * @return
	 */
	public static String conversionDateToStringByInternationalization(String dateTime,String dateFormat){
		SimpleDateFormat dateTemp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String str = null;
		try{
			str = date.format(dateTemp.parse(dateTime));
		}catch(ParseException e){
			throw SystemException.unchecked(e, SysError.Parse_ERROR);
		}
		return str;
	}

	/**
	 * 
	 * <br/>Description:计算未来几钟后的时间
	 
	 * 
	 * @param minutes
	 * @param outDataFormat
	 * @return
	 */
	public static String generateFutureTime(int minutes,String outDataFormat){
		String dateTime = null;
		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy-MM-dd  HH:mm:ss G E D F w W a E F");
		SimpleDateFormat formatter = new SimpleDateFormat(outDataFormat);
		Date date = new Date();
		long Time = (date.getTime() / 1000) + 60 * minutes;
		date.setTime(Time * 1000);
		dateTime = formatter.format(date);
		return dateTime;
	}

	/**
	 * 
	 * <br/>Description:计算未来几钟后的时间
	 
	 * 
	 * @param minutes
	 * @param inputDateTime
	 * @param outDataFormat
	 * @return
	 */
	public static String generateFutureTime(int minutes,String inputDateTime,String outDataFormat){
		String dateTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat(outDataFormat);
		Date date = conversionDateReturnDate(inputDateTime);
		long Time = (date.getTime() / 1000) + 60 * minutes;
		date.setTime(Time * 1000);
		dateTime = formatter.format(date);
		return dateTime;
	}

	/**
	 * 
	 * <br/>Description:计算未来几钟后的时间
	 
	 * 
	 * @param minutes
	 * @param inputDateTime
	 * @param outDataFormat
	 * @return
	 */
	public static String generateFutureTime(int minutes,Date inputDateTime,String outDataFormat){
		String dateTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat(outDataFormat);
		long Time = (inputDateTime.getTime() / 1000) + 60 * minutes;
		inputDateTime.setTime(Time * 1000);
		dateTime = formatter.format(inputDateTime);
		return dateTime;
	}

	/**
	 * 
	 * <br/>Description:根据传入时间返回国际化时间，含时区
	 
	 * 
	 * @param d
	 * @return
	 */
	public static String getInternationalizationDate(Date d){
		String str = null;
		try{
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			str = date.format(d);
		}catch(Exception e){
			throw SystemException.unchecked(e, SysError.Parse_ERROR);
		}
		return str;
	}

	/**
	 * 
	 * <br/>Description:删除多余的空格和回车
	 
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteTabsAndEnter(String str){
		String s = null;

		Pattern p = Pattern.compile("\\s*|\t|\r|\n");

		Matcher m = p.matcher(str);

		String after = m.replaceAll("");

		s = after;

		return s;
	}

	/**
	 * 
	 * <br/>Description:Map 排序
	 
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings({"rawtypes","unchecked","unused"})
	public static LinkedHashMap<String,Integer> sortMap(Map<String,Integer> map){

		LinkedHashMap<String,Integer> tmp = new LinkedHashMap<String,Integer>();

		Map<String,Integer> treeMap = new TreeMap<String,Integer>(map);

		List arrayList = new ArrayList(map.entrySet());
		Collections.sort(arrayList,new Comparator(){
			public int compare(Object o1,Object o2){
				Map.Entry obj1 = (Map.Entry) o1;
				Map.Entry obj2 = (Map.Entry) o2;
				return ((Integer) obj2.getValue()).compareTo((Integer) obj1.getValue());
			}
		});

		int v = arrayList.size();

		for(int i = 0;i < v;i ++ ){
			String strs[] = arrayList.get(i).toString().split("=");
			tmp.put(strs[0],Integer.parseInt(strs[1]));
		}

		return tmp;
	}

	/**
	 * 
	 * <br/>Description:返回当前项目编译路径
	 
	 * 
	 * @return
	 */
	public String getProjectClassesPath(){
		return this.getClass().getResource("/").getPath();
	}

	/**
	 * 
	 * <br/>Description:返回当前项目编译路径
	 * 
	 * @author Eric
	 * @return
	 */
	public static String getClassesPath(){
		MvcPageUtil utils = new MvcPageUtil();
		String str = null;
		try{
			str = urlDecoder(utils.getProjectClassesPath());
		}catch(Exception e){
			throw SystemException.unchecked(e, SysError.FOUNDNOPATH_ERROR);
		}
		return str;
	}

	/**
	 * 
	 * <br/>Description:读取Properties文件
	 * 
	 * @author Eric
	 * @param path Properties文件路径
	 * @param key key值
	 * @return
	 */
	public static String getProperties(String path,String key){
		java.util.Properties properties = null;
		String str = null;
		try{
			if(isEmptyFile(path)){
				properties = new java.util.Properties();
				java.io.FileInputStream fileStream = new java.io.FileInputStream(path);
				properties.load(fileStream);
				fileStream.close();
				str = properties.getProperty(key);
			}else{
				System.out.println("Properties文件不存在。");
			}
		}catch(Exception e){
			throw SystemException.unchecked(e, SysError.FOUNDNOPATH_ERROR);
		}

		return str;
	}

	/**
	 * 
	 * <br/>Description:读取Src下的属性文件
	 
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String getSrcProperties(String filePath,String key){
		java.util.Properties properties = null;
		String str = null;
		try{
			if(isEmptyFile(getClassesPath() + filePath)){
				properties = new java.util.Properties();
				java.io.FileInputStream fileStream = new java.io.FileInputStream(getClassesPath() + filePath);
				properties.load(fileStream);
				fileStream.close();
				str = properties.getProperty(key);
			}else{
				System.out.println("Properties文件不存在。");
			}
		}catch(Exception e){
			throw SystemException.unchecked(e, SysError.FOUNDNOPATH_ERROR);
		}

		return str;
	}

	/**
	 * 
	 * <br/>Description:判断请求是否为Ajax请求
	 
	 * 
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest req){
		boolean boo = false;
		String header = req.getHeader("X-Requested-With");
		if(header != null && "XMLHttpRequest".equals(header)){
			boo = true;
		}
		return boo;
	}

	/**
	 * 
	 * <br/>Description:返射工具类
	 
	 * 
	 * @param clazz
	 * @param methodName
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object reflectionTools(Class<T> clazz,String methodName,Object[] params){
		Object o = null;
		try{
			Object obj = clazz.newInstance();

			int inputArgsLength = params.length;

			Class<T>[] inClassArgs = new Class[inputArgsLength];

			Object[] inMethodArgs = new Object[inputArgsLength];

			for(int i = 0;i < params.length;i ++ ){
				Object object = params[i];
				inClassArgs[i] = (Class<T>) object.getClass();
				inMethodArgs[i] = params[i];
			}

			Method method = obj.getClass().getMethod(methodName,inClassArgs);

			o = method.invoke(obj,inMethodArgs);
		}catch(Exception e){
			throw SystemException.unchecked(e, SysError.REFLECT_ERROR);
		}

		return o;
	}

	/**
	 * 
	 * <br/>Description:返回一个ClassLoader
	 
	 * 
	 * @return
	 */
	public static ClassLoader getClassLoader(){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader;
	}

	/**
	 * 
	 * <br/>Description:根据classLoader和类名称返回一个类
	 
	 * 
	 * @param classLoader
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByClassLoader(ClassLoader classLoader,String className){
		Class<?> clazz = null;
		try{
			if(null != className && null != classLoader){
				clazz = classLoader.loadClass(className);
			}
		}catch(ClassNotFoundException e){
			throw SystemException.unchecked(e, SysError.ClassNotFound_ERROR);
		}
		return clazz;
	}

	/**
	 * 
	 * <br/>Description:根据classForName和类名称返回一个类
	 
	 * 
	 * @param classLoader
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByClassForName(String className){
		Class<?> clazz = null;
		try{
			if(null != className){
				clazz = Class.forName(className);
			}
		}catch(ClassNotFoundException e){
			throw SystemException.unchecked(e, SysError.ClassNotFound_ERROR);
		}
		return clazz;
	}

	/**
	 * 
	 * <br/>Description:删除字符串两端的大括号
	 
	 * 
	 * @param val
	 * @return
	 */
	public static String deleteBigBrackets(String val){
		String v = val;
		String begin = v.substring(0,1);
		String end = v.substring(v.length() - 1,v.length());
		if(begin.equalsIgnoreCase("{") && end.equalsIgnoreCase("}")){
			v = v.substring(1,v.length() - 1);
		}
		return v;
	}

	/**
	 * 
	 * <br/>Description:给字符串包裹一个大括号
	 
	 * 
	 * @param val
	 * @return
	 */
	public static String addBigBrackets(String val){

		return "{" + val + "}";
	}

	/**
	 * 
	 * <br/>Description:删除字符串尾部是否包含 “/”
	 
	 * 
	 * @param val
	 * @return
	 */
	public static String deleteRightBar(String val){
		boolean boo = checkRightBar(val);

		String s = val;

		if(boo){
			while(checkRightBar(s)){
				s = s.substring(0,s.length() - 1);
			}
		}
		return s;
	}

	/**
	 * 
	 * <br/>Description:判断字符串尾部是否包含 “/”
	 
	 * 
	 * @param val
	 * @return
	 */
	public static boolean checkRightBar(String val){
		boolean boo = false;
		if(val.length() > 1){
			String s = val.substring(val.length() - 1,val.length());

			if("/".equalsIgnoreCase(s)){
				boo = true;
			}
		}

		return boo;
	}

	/**
	 * 
	 * <br/>Description:根据传入的类型返回对应数据的类型--反射工具专用
	 
	 * 
	 * @param pmType
	 * @param val
	 * @return
	 * @throws ConversionTypeException
	 */
	public static Object createObjectByParamType(String pmType,String val) throws SystemException{

		Object obj = null;

		try{
			if(pmType.equals("java.lang.String")){
				obj = String.valueOf(val);
			}else if(pmType.equals("java.util.Date")){
				obj = String.valueOf(MvcPageUtil.conversionDateReturnDate(val));
			}else if(pmType.equals("java.lang.Boolean")){
				obj = new Boolean(val);
			}else if(pmType.equals("boolean")){
				obj = new Boolean(val);
			}else if(pmType.equals("java.lang.Integer")){
				obj = Integer.parseInt(val);
			}else if(pmType.equals("int")){
				obj = Integer.parseInt(val);
			}else if(pmType.equals("java.lang.Long")){
				obj = Long.parseLong(val);
			}else if(pmType.equals("java.lang.Double")){
				obj = Double.parseDouble(val);
			}else if(pmType.equals("java.lang.Byte")){
				obj = Byte.parseByte(val);
			}else if(pmType.equals("java.lang.Short")){
				obj = Short.parseShort(val);
			}else if(pmType.equals("java.lang.Float")){
				obj = Float.parseFloat(val);
			}else if(pmType.equals("java.math.BigDecimal")){
				obj = new BigDecimal(val);
			}else if(pmType.equals("java.math.BigInteger")){
				obj = new BigInteger(val);
			}else if(pmType.equals("java.io.File")){
				obj = new File(val);
			}
		}catch(Exception e){
			throw SystemException.unchecked(e, ConversionError.Conversion_ERROR);
		}

		return obj;
	}

	/**
	 * 
	 * <br/>Description:根据转入类型返回对应的类型名称
	 
	 * 
	 * @param pmType
	 * @return
	 */
	public static String getTypeNameByParamType(String pmType){

		String str = null;

		if(pmType.equals("class java.lang.String")){
			str = "string";
		}else if(pmType.equals("class java.util.Date")){
			str = "date";
		}else if(pmType.equals("class java.lang.Boolean")){
			str = "boolean";
		}else if(pmType.equals("boolean")){
			str = "boolean";
		}else if(pmType.equals("class java.lang.Integer")){
			str = "integer";
		}else if(pmType.equals("int")){
			str = "int";
		}else if(pmType.equals("class java.lang.Long")){
			str = "long";
		}else if(pmType.equals("class java.lang.Double")){
			str = "double";
		}else if(pmType.equals("class java.lang.Byte")){
			str = "byte";
		}else if(pmType.equals("class java.lang.Short")){
			str = "short";
		}else if(pmType.equals("class java.lang.Float")){
			str = "float";
		}else if(pmType.equals("class java.math.BigDecimal")){
			str = "bigDecimal";
		}else if(pmType.equals("class java.math.BigInteger")){
			str = "bigInteger";
		}else if(pmType.equals("class java.io.File")){
			str = "file";
		}else if(pmType.equals("interface java.util.Map") || pmType.equals("class java.util.TreeMap") || pmType.equals("class java.util.HashMap") || pmType.equals("class java.util.EnumMap") || pmType.equals("class java.util.LinkHashMap")){
			str = "map";
		}else if(pmType.equals("interface java.util.List") || pmType.equals("class java.util.ArrayList") || pmType.equals("class java.util.LinkedList")){
			str = "list";
		}else if(pmType.equals("interface java.util.Set") || pmType.equals("class java.util.TreeSet") || pmType.equals("class java.util.HashSet") || pmType.equals("class java.util.BitSet") || pmType.equals("class java.util.EnumSet") || pmType.equals("class java.util.LinkedHashSet") || pmType.equals("class java.util.TreeSet")){
			str = "set";
		}else if(pmType.equals("java.util.Vector")){
			str = "vector";
		}

		return str;
	}

	/**
	 * 
	 * <br/>Description:将传入字符串进行首字母小写
	 
	 * 
	 * @param val
	 * @return
	 */
	public static String lowercaseFirstLetter(String val){
		String v = val;

		if(null != val && ! "".equals(val.trim())){
			String cb = val.substring(0,1);
			String ce = val.substring(1,val.length());
			v = cb.toLowerCase() + ce;
		}

		return v;
	}

	/**
	 * 
	 * <br/>Description:将传入字符串进行首字母大写
	 
	 * 
	 * @param val
	 * @return
	 */
	public static String upperCaseFirstLetter(String val){
		String v = val;

		if(null != val && ! "".equals(val.trim())){
			String cb = val.substring(0,1);
			String ce = val.substring(1,val.length());
			v = cb.toUpperCase() + ce;
		}

		return v;
	}

	/**
	 * 
	 * <br/>Description:读取 ServletInputStream
	 
	 * 
	 * @param request
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getServletInputStream(InputStreamReader in){

		int BUFFER_SIZE = 4096;

		StringWriter out = new StringWriter();

		try{
			// int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead = - 1;
			while((bytesRead = in.read(buffer)) != - 1){
				out.write(buffer,0,bytesRead);
				// byteCount += bytesRead;
			}
			out.flush();
		}catch(IOException e){
			throw SystemException.unchecked(e, IOEError.IOE_ERROR);
		}finally{
			try{
				in.close();
			}catch(IOException ex){
				throw SystemException.unchecked(ex, IOEError.IOE_ERROR);

			}
			try{
				out.close();
			}catch(IOException ex){
				throw SystemException.unchecked(ex, IOEError.IOE_ERROR);

			}
		}

		return out.toString();
	}

	/**
	 * 
	 * <br/>Description:根据URl地址和Key返回对应值
	 
	 * 
	 * @param val
	 * @param key
	 * @return
	 */
	public static Map<String,String> getUrlParamByKey(String val){
		Map<String,String> map = null;
		if(null != val && ! "".equalsIgnoreCase(val)){
			map = new HashMap<String,String>();

			String[] maps = val.split("&");

			if(maps.length != 0){
				for(String m:maps){
					String[] km = m.split("=");
					map.put(km[0],km[1]);
				}
			}else{
				String[] km = val.split("=");
				map.put(km[0],km[1]);
			}
		}

		return map;
	}

	public static boolean isMultipartRequest(HttpServletRequest request){

		boolean boo = false;
		if(request.getContentType()==null ||request.getContentType().equals("null")||request.getContentType().equals("")){
			return boo;
		}
		String[] cts = request.getContentType().split(";");

		String contentType = cts[0];

		int ct = contentType.toLowerCase().indexOf("multipart/".toLowerCase());

		if(ct != - 1){
			boo = true;
		}

		return boo;
	}

	/**
	 * 
	 * <br/>Description:获取文件上传信息
	 
	 * 
	 * @param request
	 * @param charset
	 * @return
	 * @throws UploadFileException
	 * @throws UnknownException
	 */
	public static Map<String,String> getMultipartParams(HttpServletRequest request,String charset) throws SystemException{
		Map<String,String> map = new HashMap<String,String>();
		try{
			request.setCharacterEncoding(charset); // 设置编码
			
			// 获得磁盘文件条目工厂
			org.apache.commons.fileupload.disk.DiskFileItemFactory factory = new org.apache.commons.fileupload.disk.DiskFileItemFactory();
			// 获取文件需要上传到的路径
			// String path = request.getServletContext().getRealPath("/upload");
			String path = null;
			if(isWindowsOS()){
				path = "C://Windows//Temp";
			}else{
				path = "/tmp";
			}
			// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
			// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
			/**
			 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上，
			 * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
			 * 然后再将其真正写到 对应目录的硬盘上
			 */
			factory.setRepository(new File(path));
			// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
			factory.setSizeThreshold(1024 * 1024);

			// 高水平的API文件上传处理
			org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);

			// 可以上传多个文件
			List<org.apache.commons.fileupload.FileItem> list = (List<org.apache.commons.fileupload.FileItem>) upload.parseRequest(request);

			for(org.apache.commons.fileupload.FileItem item:list){
				// 获取表单的属性名字
				String name = item.getFieldName();

				// 如果获取的 表单信息是普通的 文本 信息
				if(item.isFormField()){
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					String value = item.getString(charset);
					map.put(name,value);
				}
				// 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
				else{
					/**
					 * 以下三步，主要获取 上传文件的名字
					 */
					// 获取路径名
					String value = item.getName();
					// 索引到最后一个反斜杠
					int start = value.lastIndexOf("\\");
					// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
					String filename = value.substring(start + 1);

					map.put(name,path + "/" + filename);

					// 真正写到磁盘上
					// 它抛出的异常 用exception 捕捉

					item.write(new File(path,filename));// 第三方提供的

					// 手动写的
					// OutputStream out = new FileOutputStream(new
					// File(path,filename));
					//
					// InputStream in = item.getInputStream();
					//
					// int length = 0;
					// byte[] buf = new byte[1024];
					//
					// System.out.println("获取上传文件的总共的容量：" + item.getSize());
					//
					// // in.read(buf) 每次读到的数据存放在 buf 数组中
					// while((length = in.read(buf)) != - 1){
					// // 在 buf 数组中 取出数据 写到 （输出流）磁盘上
					// out.write(buf,0,length);
					//
					// }
					//
					// in.close();
					// out.close();
				}
			}

		}catch(org.apache.commons.fileupload.FileUploadException e){
			throw SystemException.unchecked(e, UploadFileExceptionError.UploadFile_Error);
		}catch(Exception e){
			throw SystemException.unchecked(e, UnknownError.Unknown_ERROR);
		}
		return map;
	}


	/**
	 * 
	 * <br/>Description:判断操作系统是否是windows
	 
	 * 
	 * @return
	 */
	public static boolean isWindowsOS(){
		boolean boo = false;
		try{
			Properties sp = System.getProperties();

			String osName = sp.getProperty("os.name");

			int osInt = osName.toLowerCase().indexOf("win");

			if(osInt != - 1){
				boo = true;
			}
		}catch(Exception e){
			System.out.println("获取操作系统名称异常。");
		}

		return boo;
	}


}
