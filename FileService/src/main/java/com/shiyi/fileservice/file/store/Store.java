/**
 * 名称: Store.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月16日 下午4:25:34
 * @since  2014年10月16日
 * @author malb
 */ 
package com.shiyi.fileservice.file.store;


import com.shiyi.fileservice.file.QiniuUpload;
import com.shiyi.fileservice.util.CommonUtils;
import com.shiyi.fileservice.util.FastFdsClient;


/**
 * @author malb
 * 进行文件存储
 */
public class Store {

	//文件路径
	private String filePath;
	
	//文件类型
	private int fileType;
	
	
	//文件扩展名
	private String fileExt;
	
	//该文件的md5值
	private String md5Value;
	
	
	
	public Store(String path, String md5, String fileext, int filetype) {
		fileType = filetype;
		filePath = path;
		fileExt = fileext;
		this.md5Value = md5;
	}
	
	
	private void uploadDfs(StoreResult result) {
		//fastdfs存储
		FastFdsClient client = new FastFdsClient();		
		String[] resultParams = client.uploadFileToFds(this.filePath, this.fileExt);
		
		if (resultParams != null) {
			FastDfsFileDir info = new FastDfsFileDir();
			
			info.setGroupName(resultParams[0]);
			info.setRemoteFile(resultParams[1]);
			
			result.setDirInfo(info);
			result.setSrcId(StoreType.FASTDFS);
			
			result.setUpdateResult(true);
		} else {
			//设置存储失败
			result.setUpdateResult(false);
			result.setErrMessage("fastdfs upload fail");
		}
	}
	
	/**
	 * 另存到fds存储上
	 * @return
	 */
	public StoreResult saveAsFds() {
		
		StoreResult result = new StoreResult();
		result.setUpdateResult(false);
		
		uploadDfs(result);
		
		return result;
	}
	
	
	/**
	 * 根据filetype进行选择dfs或者七牛云存储 进行储存
	 * @return
	 */
	public StoreResult saveAs() {
		StoreResult result = new StoreResult();
		result.setUpdateResult(false);
		
		if (CommonUtils.isNeedQiniuUpload(fileType)) {
			//七牛存储
			QiniuUpload uploader = new QiniuUpload(this.filePath);			
			String[] resultParams = uploader.upload(CommonUtils.getPrefix(fileExt), md5Value, this.fileExt);
			
			if (resultParams != null) {
				QiniuDir info = new QiniuDir();
				
				info.setGroup(resultParams[0]);
				info.setFileKey(resultParams[1]);
				info.setQiniuUrl(CommonUtils.getQiNiuUrl(resultParams[0], resultParams[1]));
				
				result.setqDirInfo(info);
							
				result.setUpdateResult(true);
				result.setErrMessage("qiniu upload ok");
				
				uploadDfs(result);
				
				result.setSrcId(StoreType.QINIU);
			} else {
				result.setErrMessage("qiniu upload fail");
			}
			
		}  else {
			uploadDfs(result);
			
		}
		

		
		return result;
	}
	
	
}
