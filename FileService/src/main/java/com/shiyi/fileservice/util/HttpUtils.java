/**
 * 名称: HttpUtils.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年9月26日 下午5:29:53
 * @since  2014年9月26日
 * @author malb
 */ 
package com.shiyi.fileservice.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author malb
 * http相关常用工具类
 */


public class HttpUtils {

	/**
	 * 返回请求的http头，比如：http://www.ktvme.com:8080/FileService/
	 * @param request
	 * @return
	 */
	public static String getRequestSchema(HttpServletRequest request) {
		return String.format("http://%s:%d/%s/", request.getServerName(), request.getServerPort(), "FileService");
	}
	
	
	/**
	 * 返回www方式的缓存文件夹，比如：http://www.ktvme.com:8080/FileService/cache
	 * @param request
	 * @return
	 */
	public static String getWWWCache(HttpServletRequest request) {
		return String.format("%s%s/", getRequestSchema(request), "cache");
	}
	
}
