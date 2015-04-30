/**
 * 名称: ExpiredFileError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:15:53
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class ExpiredFileError extends Error {

	public ExpiredFileError() {
		super("11", "expired file");
	}
	
}
