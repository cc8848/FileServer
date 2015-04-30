/**
 * 名称: StoreFileDAO.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午8:18:22
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.shiyi.fileservice.model.StoreFile;


/**
 * @author malb
 * 操作存储表
 */
public interface StoreFileDAO {

	public void insertStoreFile(StoreFile storeFile) throws DataAccessException;
	
	public List<StoreFile> findMd5ById(int fileId) throws DataAccessException;
		
	public List<StoreFile> findMd5ByIdWithoutStatus(int fileId) throws DataAccessException;
		
	public List<StoreFile> findStoreFile(String md5Value) throws DataAccessException;
	
	public void deleteStoreFile(int fileId) throws DataAccessException;
	

		
}
