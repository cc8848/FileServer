/**
 * 名称: FileContentError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:02:31
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class FileContentError extends Error {


	public FileContentError() {
		super("1", "not find upload file");

	}

}
