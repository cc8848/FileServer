/**
 * 名称: DownloadFile.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月11日 下午5:51:44
 * @since  2014年8月11日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload;

import java.util.List;

import com.shiyi.fileservice.file.FileUtil;
import com.shiyi.fileservice.file.HttpClient;
import com.shiyi.fileservice.model.StoreFile;
import com.shiyi.fileservice.service.impl.CommonDataServiceImpl;
import com.shiyi.fileservice.service.impl.StoreFileServiceImpl;
import com.shiyi.fileservice.util.CommonUtils;
import com.shiyi.fileservice.util.SpringContextUtils;

/**
 * @author malb
 * 下载文件
 */

public class DownloadFile {

	//private final Logger logger = LoggerFactory.getLogger(DownloadFile.class);
		
	private int fileId;
	
	private StoreFileServiceImpl fileService;
	
	private CommonDataServiceImpl commonService;
	
	private String fileExt;
	
	private String cacheDir;
	
	private String cacheFile;
	
	private String groupName;
	
	private String remoteFile;
		
	private String realFileName;
	
	private int fileLength;
		
	private String wwwCache;
	
	//存储来源
	private String qiniuUrl;
	
	/**
	 * fdfsFileUrl，文件的地址
	 */
	private String fdfsFileUrl;
	
	/**
	 * 缓存的WWW文件夹
	 */
	private String wwwCacheUrl;
	
	/**
	 * APK文件名称
	 */
	private String fileName;
	

	/**
	 * 
	 * @param fileId，fileid
	 * @param cacheDir，缓存目录
	 */
	public DownloadFile(int fileId, String cacheDir) {
		this.fileId = fileId;
		this.cacheDir = cacheDir;
		
		FileUtil.createDir(this.cacheDir);
		this.wwwCache = getWWWCache();
		
		fileService = SpringContextUtils.getBean(StoreFileServiceImpl.class);
	}
	
	
	public String getWWWCache() {
		commonService = SpringContextUtils.getBean(CommonDataServiceImpl.class);
		String downUrl = commonService.findDownloadUrl();
		
		int idx = downUrl.lastIndexOf("/");
		
		return downUrl.substring(0, idx) + "/cache/";
	}
	
	/**
	 * 下载并设置本地缓存
	 * @return
	 */
	/*
	private byte[] setCacheFile() {
		
		byte[] tmp = HttpClient.getFileContent(fdfsFileUrl); 		
		FileUtil.writeFile(this.cacheFile, tmp);
		
		return tmp;
	}*/
	
	public String getCaheFilePath() {
		
		if (!isExistsCacheFile(this.cacheDir, fileId, this.fileExt, this.fileLength)) {
			return HttpClient.downLoadFile(fdfsFileUrl, this.cacheFile);
		}
		
		return this.cacheFile;
	}
	
	/**
	 * 获得该文件的fileid
	 * @return
	 */
	/*
	private byte[] getFileStream() {
		
		//如果找到该值，下一步看是否本地缓存中是否存在
		if (isExistsCacheFile(this.cacheDir, fileId, this.fileExt, this.fileLength)) {
			//如果存在此缓存
			return FileUtil.getFile(this.cacheFile);
			
		}else {
			return setCacheFile();
		}
	}
	*/
	
	/**
	 * 获得数据库中该fileid的相关参数
	 * @param fileId
	 * @return
	 */
	private boolean getFileParam(int fileId) {
		
		List<StoreFile> lst = fileService.findMd5ByIdWithoutStatus(fileId);
		if (lst.size() > 0) {
			StoreFile storeFile = lst.get(0);
						
			this.groupName = storeFile.getGroupName();
			this.remoteFile = storeFile.getFileName();
						
			this.realFileName = storeFile.getRealFileName();
			this.fileExt = storeFile.getExtension();
			this.fileLength = storeFile.getFileSize();						
			
			this.qiniuUrl = storeFile.getUrl();
			
			//commonService = SpringContextUtils.getBean(CommonDataServiceImpl.class);
			this.fdfsFileUrl = commonService.getHttpUrl(this.groupName, this.remoteFile);
						
			//设置该fileid的文件名称
			this.fileName = "apk".equalsIgnoreCase(fileExt) ? String.format("%s.%s", this.realFileName, fileExt) :
				String.format("%d.%s", this.fileId, fileExt);
			
			//设置该fileid的缓存文件路径以及缓存文件的www url地址
			if (CommonUtils.isApk(fileExt)) {
				this.cacheFile = String.format("%s/%s", cacheDir, this.realFileName);
				this.wwwCacheUrl = String.format("%s%s", wwwCache, this.realFileName);				
				
			} else {
				this.cacheFile = String.format("%s/%d.%s", cacheDir, fileId, fileExt);
				this.wwwCacheUrl = String.format("%s%d.%s", wwwCache, fileId, this.fileExt);
			}
			
			
			
			return true;
		}
		
		return false;
	}

	
	
	
	/**
	 * 是否存在本地缓存文件
	 * @param cacheDir
	 * @param fileId
	 * @param fileSize
	 * @return，如果filesize跟数据库中的大小不一致，也视为不存在
	 */
	private boolean isExistsCacheFile(String cacheDir, int fileId, String fileExt, int fileSize) {
		
		return FileUtil.isExists(cacheFile) && (FileUtil.getFileSize(cacheFile) == fileSize);			
	}

	
	/**
	 * 获得与下载相关的参数值
	 * @return，如果成功，返回true，反之，返回false
	 */
	public boolean getDownLoadParam() {
		if (this.fileId < 0) {
			return false;
		}
		
		if (!getFileParam(this.fileId)) {
			return false;
		}
				
		return true;
	}


	public String getFileExt() {
		return fileExt;
	}

	public String getFileUrl() {
		return fdfsFileUrl;
	}


	public String getQiniuUrl() {
		return qiniuUrl;
	}


	public String getWwwCacheUrl() {
	
		return wwwCacheUrl;
	}

	public String getFileName() {
		return fileName;
	}

	
}
