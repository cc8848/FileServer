/**
 * 名称: CommonDataDAO.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午7:38:58
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.dao;

import java.util.HashMap;
import java.util.List;

import com.shiyi.fileservice.model.FileServer;

import org.springframework.dao.DataAccessException;

/**
 * @author malb
 * 通用接口 DAO
 */
public interface CommonDataDAO {

	public List<FileServer> findFileServer(int fileServerType) throws DataAccessException;
	
	
	public String findDownloadUrl() throws DataAccessException;
	
	
	public List<HashMap<String, String>> findQiniuConfig() throws DataAccessException;
}
