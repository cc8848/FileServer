/**
 * 名称: DeleteFail.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月21日 下午2:27:00
 * @since  2014年8月21日
 * @author malb
 */ 
package com.shiyi.fileservice.file.delete.error;



/**
 * @author malb
 *
 */
public class DeleteFail extends Error {

	/**
	 * @param code
	 * @param msg
	 */
	public DeleteFail() {
		super(0, "delte file fail");
	}

}
