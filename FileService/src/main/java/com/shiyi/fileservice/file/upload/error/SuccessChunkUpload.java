/**
 * 名称: SuccessChunkUpload.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午2:54:37
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 * 分块上传成功
 */
public class SuccessChunkUpload extends Error {

	/**
	 * @param code
	 * @param msg
	 */
	public SuccessChunkUpload() {
		super("0", "save chunk file succeed!");

	}

}
