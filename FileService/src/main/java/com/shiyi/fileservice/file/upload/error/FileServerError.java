/**
 * 名称: FileServerError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:09:00
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class FileServerError extends Error {


	public FileServerError(String msg) {
		super("6", msg);

	}

}
