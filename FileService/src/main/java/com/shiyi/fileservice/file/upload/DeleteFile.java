/**
 * 名称: DeleteFile.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月21日 下午1:41:37
 * @since  2014年8月21日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shiyi.fileservice.file.FileUtil;
import com.shiyi.fileservice.model.StoreFile;
import com.shiyi.fileservice.service.impl.StoreFileServiceImpl;
import com.shiyi.fileservice.util.FastFdsClient;
import com.shiyi.fileservice.util.SpringContextUtils;

/**
 * @author malb
 *
 */
public class DeleteFile {

	private final Logger logger = LoggerFactory.getLogger("DeleteFile");
	
	private int fileId;
	
	private StoreFileServiceImpl fileService;
	
	private String fileExt;
	
	private String cacheDir;
	
	private String cacheFile;
	
	private String groupName;
	
	private String remoteFile;

	
	public DeleteFile(int fileid, String cacheDir) {
		fileId = fileid;
		this.cacheDir = cacheDir;
		
		fileService = SpringContextUtils.getBean(StoreFileServiceImpl.class);
	}
	
	
	/**
	 * 是否存在本地缓存文件
	 * @param cacheDir
	 * @param fileId
	 * @return
	 */
	private boolean isExistsCacheFile(String cacheDir, int fileId, String fileExt) {
		this.cacheFile = String.format("%s/%d.%s", cacheDir, fileId, fileExt);
		
		return FileUtil.isExists(cacheFile);			
	}
	
	
	/**
	 * 删除存储文件
	 * @return
	 */
	public boolean doDelete() {
		List<StoreFile> lst = fileService.findMd5ById(fileId);
		//如果找到该文件status=1的情况
		if (lst.size() > 0) {
			/*StoreFile storeFile = lst.get(0);
			this.setFileExt(storeFile.getExtension());
			this.groupName = storeFile.getGroupName();
			this.remoteFile = storeFile.getFileName();		
			
			//是否存在该缓存文件
			if (isExistsCacheFile(this.cacheDir, fileId, this.getFileExt())) {
				boolean b = FileUtil.delFile(this.cacheFile);
				logger.info(String.format("delete file inf [%s]删除结果[%b]", this.cacheFile, b));
			}*/
			
			//从数据库中删除该记录
			try {
				fileService.deleteStoreFile(fileId);
			} catch(Exception e) {
				logger.error(String.format("删除fileid=%d文件失败", fileId));
				return false;
			}
			
			/*
			//删除存储记录
			FastFdsClient client = new FastFdsClient();
			client.deleteFileFromDfs(groupName, remoteFile);
			*/
		}
				
		return true;
	}



	public String getFileExt() {
		return fileExt;
	}



	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
}
