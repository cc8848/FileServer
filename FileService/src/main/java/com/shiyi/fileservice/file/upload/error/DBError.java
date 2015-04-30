/**
 * 名称: DBError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:09:44
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class DBError extends Error {

	
	public DBError(String msg) {
		super("7", msg);
	
	}

}
