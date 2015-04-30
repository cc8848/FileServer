/**
 * 名称: StoreFileService.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 上午9:08:41
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.shiyi.fileservice.model.StoreFile;

/**
 * @author malb
 *
 */
public interface StoreFileService {
	public int insertStoreFile(StoreFile storeFile) throws DataAccessException;
	
	public List<StoreFile> findStoreFile(String md5Value) throws DataAccessException;
	
	public List<StoreFile> findMd5ById(int fileId) throws DataAccessException;
	
	public List<StoreFile> findMd5ByIdWithoutStatus(int fileId) throws DataAccessException;
	
	public void deleteStoreFile(int fileId) throws DataAccessException;
	
}
