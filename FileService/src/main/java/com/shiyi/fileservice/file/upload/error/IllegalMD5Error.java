/**
 * 名称: IllegalMD5Error.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:14:37
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class IllegalMD5Error extends Error {

	
	public IllegalMD5Error() {
		super("9", "md5 error");
	}
}
