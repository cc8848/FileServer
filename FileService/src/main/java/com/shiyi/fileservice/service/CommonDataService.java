/**
 * 名称: CommonDataService.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午7:50:50
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.service;

import java.util.HashMap;
import java.util.List;

import com.shiyi.fileservice.model.FileServer;

/**
 * @author malb
 *
 */
public interface CommonDataService {
	
	public List<FileServer> findFileServer(int fileServerType);
	
	public String findDownloadUrl();
	
	public String getHttpUrl(String groupName, String remoteFile);
	
	public HashMap<String, String> findQiniuConfig();

}
