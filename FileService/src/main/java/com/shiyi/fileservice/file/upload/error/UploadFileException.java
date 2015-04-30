/**
 * 名称: UploadFileException.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 上午11:28:45
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;


/**
 * @author malb
 * 上传错误
 */
public class UploadFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5559514172191444308L;

	private Error error;
	
	public UploadFileException(String errCode, String errMessage) {
		error = new Error(errCode, errMessage);
	}
	
	
	
	public String toString() {
		return error.toString();
	}
	

	
}
