/**
 * 名称: StoreResult.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月16日 下午4:42:57
 * @since  2014年10月16日
 * @author malb
 */ 
package com.shiyi.fileservice.file.store;

import java.io.Serializable;

/**
 * @author malb
 * 上传结果
 */
public class StoreResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6708641717729842328L;

	/**
	 * 上传结果
	 */
	private boolean updateResult;
	
	/**
	 * 数据存储id,0-fastdfs，1-七牛，2-mongodb
	 */
	private StoreType srcId;
	
	/**
	 * 当srcId=0时，此字段有效
	 */
	private FastDfsFileDir dirInfo;
	
	/**
	 * 当srcid=1时，此字段有效
	 */
	private QiniuDir qDirInfo;

	/**
	 * 
	 */
	private String errMessage;
	
	

	public FastDfsFileDir getDirInfo() {
		return dirInfo;
	}

	public void setDirInfo(FastDfsFileDir dirInfo) {
		this.dirInfo = dirInfo;
	}

	public QiniuDir getqDirInfo() {
		return qDirInfo;
	}

	public void setqDirInfo(QiniuDir qDirInfo) {
		this.qDirInfo = qDirInfo;
	}

	public boolean isUpdateResult() {
		return updateResult;
	}

	public void setUpdateResult(boolean updateResult) {
		this.updateResult = updateResult;
	}

	public StoreType getSrcId() {
		return srcId;
	}

	public void setSrcId(StoreType srcId) {
		this.srcId = srcId;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
}
