/**
 * 名称: FileExtError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:03:46
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class FileExtError extends Error {

	
	public FileExtError() {
		super("2", "no file ext");
	}
}
