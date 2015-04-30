/**
 * 名称: ProlError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:10:40
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class ProlError extends Error {


	public ProlError(String msg) {
		super("8", msg);

	}

}
