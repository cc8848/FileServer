/**
 * 名称: MD5Error.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午3:05:16
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class MD5Error extends Error {

	public MD5Error() {
		super("3", "invalid md5");
	}
}
