/**
 * 名称: QiniuDir.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月16日 下午4:48:51
 * @since  2014年10月16日
 * @author malb
 */ 
package com.shiyi.fileservice.file.store;

import java.io.Serializable;

/**
 * @author malb
 *
 */
public class QiniuDir implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5201282022165770035L;

	private String fileKey;
	
	private String group;
	
	private String qiniuUrl;

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getQiniuUrl() {
		return qiniuUrl;
	}

	public void setQiniuUrl(String qiniuUrl) {
		this.qiniuUrl = qiniuUrl;
	}
	
}
