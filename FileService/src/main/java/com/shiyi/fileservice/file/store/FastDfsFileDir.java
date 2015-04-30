/**
 * 名称: FastDfsFileDir.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月16日 下午4:45:49
 * @since  2014年10月16日
 * @author malb
 */ 
package com.shiyi.fileservice.file.store;

import java.io.Serializable;

/**
 * @author malb
 *
 */
public class FastDfsFileDir implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2770325602990828227L;

	private String groupName;
	
	private String remoteFile;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemoteFile() {
		return remoteFile;
	}

	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}
	
}
