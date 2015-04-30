/**
 * 名称: DeleteError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月21日 下午2:23:45
 * @since  2014年8月21日
 * @author malb
 */ 
package com.shiyi.fileservice.file.delete.error;

/**
 * @author malb
 *
 */
public class DeleteError extends Error {

	/**
	 * @param code
	 * @param msg
	 */
	public DeleteError() {
		super(1, "delete ok");
		
	}

}
