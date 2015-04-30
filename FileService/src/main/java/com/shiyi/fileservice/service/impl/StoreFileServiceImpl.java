/**
 * 名称: StoreFileServiceImpl.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 上午9:14:03
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.service.impl;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.shiyi.fileservice.dao.StoreFileDAO;
import com.shiyi.fileservice.model.StoreFile;

import com.shiyi.fileservice.service.StoreFileService;

/**
 * @author malb
 *
 */
@Service
public class StoreFileServiceImpl implements StoreFileService {

	@Autowired
	private StoreFileDAO storeFileDAO;
	
	
	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.StoreFileService#insertStoreFile(com.shiyi.fileservice.model.StoreFile)
	 */
	@Override
	public int insertStoreFile(StoreFile storeFile) throws DataAccessException{
		storeFileDAO.insertStoreFile(storeFile);
		return storeFile.getFileId();
	}

	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.StoreFileService#findStoreFile(java.lang.String)
	 */
	@Override
	public List<StoreFile> findStoreFile(String md5Value) throws DataAccessException{
		return storeFileDAO.findStoreFile(md5Value);
		
	}

	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.StoreFileService#deleteStoreFile(int)
	 */
	@Override
	public void deleteStoreFile(int fileId) throws DataAccessException{
		storeFileDAO.deleteStoreFile(fileId);
		
	}


	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.StoreFileService#findStoreFileById(int)
	 */
	@Override
	public List<StoreFile> findMd5ById(int fileId)
			throws DataAccessException {
		return storeFileDAO.findMd5ById(fileId);
	}

	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.StoreFileService#findMd5ByIdWithoutStatus(int)
	 */
	@Override
	public List<StoreFile> findMd5ByIdWithoutStatus(int fileId)
			throws DataAccessException {		
		return storeFileDAO.findMd5ByIdWithoutStatus(fileId);
	}

}
