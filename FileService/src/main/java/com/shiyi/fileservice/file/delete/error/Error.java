/**
 * 名称: Error.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 上午11:38:03
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.delete.error;

import com.alibaba.fastjson.JSON;

/**
 * @author malb
 *
 */
public class Error {

	private int errorcode;
	
	private String message;
	
	public Error(int code, String msg) {
		setErrorcode(code);
		setMessage(msg);
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	/**
	 * @return the errorcode
	 */
	public int getErrorcode() {
		return errorcode;
	}

	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
}
