/**
 * 名称: UploadFile.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 上午10:44:32
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shiyi.fileservice.file.FileUtil;
import com.shiyi.fileservice.file.store.Store;
import com.shiyi.fileservice.file.store.StoreResult;
import com.shiyi.fileservice.file.upload.error.ChunkError;
import com.shiyi.fileservice.file.upload.error.FileExtError;
import com.shiyi.fileservice.file.upload.error.FileServerError;
import com.shiyi.fileservice.file.upload.error.MD5Error;
import com.shiyi.fileservice.file.upload.error.RepeatUpload;
import com.shiyi.fileservice.file.upload.error.SuccessChunkUpload;
import com.shiyi.fileservice.file.upload.error.SuccessUpload;
import com.shiyi.fileservice.model.StoreFile;
import com.shiyi.fileservice.service.CommonDataService;
import com.shiyi.fileservice.service.StoreFileService;
import com.shiyi.fileservice.service.impl.StoreFileServiceImpl;
import com.shiyi.fileservice.util.CommonUtils;
import com.shiyi.fileservice.util.PolyFile;
import com.shiyi.fileservice.util.SpringContextUtils;


/**
 * @author malb
 * 上传类
 */

public class UploadFile {
	
	private final Logger logger = LoggerFactory.getLogger("UploadFile");

	private String resultJsonStr;
	
	private HttpFilePostParams fileParams;
	
	private StoreFileService storeFileService;
	
	private CommonDataService commonService;
	
	private String tmpFileName;
	
	
	public UploadFile(HttpFilePostParams params) {
		this.fileParams = params;
		storeFileService =  SpringContextUtils.getBean(StoreFileServiceImpl.class);
		commonService = SpringContextUtils.getBean(CommonDataService.class);
	}
	

	/**
	 * 将post传入的file存为临时文件
	 * @return，如果存成功，返回临时文件名称，反之，返回null
	 */
	private String saveAsTempFile() {
		tmpFileName = String.format("%s%s-%d-%d.tmp", fileParams.getCacheDir(), fileParams.getMd5Value(), fileParams.getChunk(), fileParams.getChunks());
	
		try {
			fileParams.getFile().transferTo(new File(tmpFileName));
		} catch (Exception e) {
			logger.error(String.format("临时文件存储fail[%s]", tmpFileName));
			return null;
		} 
		logger.info(String.format("临时文件存储ok[%s]", tmpFileName));
		return tmpFileName;
		
	} 	
	
	/**
	 * 将fileext保存起来
	 * @return，如果保存成功，返回true
	 */
	private boolean saveExtName() {
		if (fileParams.getChunk() == 0) {
			tmpFileName = String.format("%s%s-%s.tmp", fileParams.getCacheDir(), fileParams.getMd5Value(), fileParams.getFileExt());
			
			try {
				new File(tmpFileName).createNewFile();
				
			} catch (IOException e) {
				logger.error(String.format("创建fileext文件失败=%s[dir=%s]", e.getMessage(), tmpFileName));
				return false;
				
			}
			
		}
		
		return true;
	}
	
	/**
	 * 是否最后一个包
	 * @return
	 */
	private boolean isLastBag() {
		return fileParams.getChunk() + 1 == fileParams.getChunks();
	}
	
	
	
	/**
	 * 通知上传
	 * @param params
	 */
	private String notifyUpload(HttpFilePostParams params) {		
		
		String md5Value = params.getMd5Value();
		String cacheDir = params.getCacheDir();
		
		PolyFile polyFile = new PolyFile(cacheDir, params.getChunks(), md5Value);
		//聚合临时文件
		if (polyFile.poly()) {			
			//聚合成功		
			//计算md5值
			//logger.debug(String.format("计算md5值[%s]", md5Value));
			String newMd5Value = FileUtil.getFileMD5(new File(polyFile.getPolyFilePath()));			
			//logger.info(String.format("聚合后的md5值=%s", newMd5Value));
			
			if (md5Value.equalsIgnoreCase(newMd5Value)) {
				
				StoreFile storeFile = getStoreFile(fileParams, polyFile.getFileExt());
				
				Store upload = new Store(polyFile.getPolyFilePath(), newMd5Value, polyFile.getFileExt(), fileParams.getFileType());
				//StoreResult resultParams = upload.saveAs();
				StoreResult resultParams = upload.saveAsFds();
				//上传该文件

				if (resultParams != null && resultParams.isUpdateResult()) {	
					storeFile.setFileSize(polyFile.getFileSize());
					storeFile.setStatus(1);
					
					switch (resultParams.getSrcId()) {
					case FASTDFS:
						storeFile.setFileName(resultParams.getDirInfo().getRemoteFile());
						storeFile.setGroupName(resultParams.getDirInfo().getGroupName());
						break;

					case QINIU:
						storeFile.setFileName(resultParams.getDirInfo().getRemoteFile());
						storeFile.setGroupName(resultParams.getDirInfo().getGroupName());
						
						storeFile.setUrl(resultParams.getqDirInfo().getQiniuUrl());
						break;
						
					default:
						break;
					}
					
					
					delTmpDir(cacheDir, md5Value);
					//插入数据库
					int fileId = storeFileService.insertStoreFile(storeFile);
					String fileUrl = String.format("%s?fileid=%d", commonService.findDownloadUrl(), fileId);

					CommonUtils.notifyUploadQiniu(fileId, fileUrl, String.format("%s.%s", md5Value, polyFile.getFileExt()), md5Value, params.getFileType());
					
					//如果是属于临时文件上传，则直接返回给其fileurl地址
					if (params.isTmpFile()) {
						
						return new SuccessUpload(fileId, fileUrl).toString();
						
					}
										
					
					return new SuccessUpload(fileId).toString();
				} else {
					delTmpDir(cacheDir, md5Value);
					return new FileServerError("upload storage error").toString();
				}
			
			} else {
				delTmpDir(cacheDir, md5Value);
				return new MD5Error().toString();   
			}
			
		} else {
			//聚合失败
			logger.info(String.format("[%s]聚合失败", md5Value));
			delTmpDir(cacheDir, md5Value);
			return new ChunkError(0).toString();			
		}
				
	}
	
	/**
	 * 删除临时文件
	 * @param file
	 * @param md5Value
	 */
	private void delTmpDir(String cacheDir, String md5Value) {
		try {
			FileUtils.deleteDirectory(new File(cacheDir));
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	

	
	/**
	 * 获得临时数据表km_tbl_storefile对象javabean
	 * @param params
	 * @return
	 */
	private StoreFile getStoreFile(HttpFilePostParams params, String fileExt) {
		StoreFile store = new StoreFile();		
		
		store.setFileId(0);
		store.setStatus(1);
		store.setFileSize(0);
		store.setFileType(params.getFileType());		
		store.setGroupName("tmp");
		store.setFileName("tmp");
		store.setExtension(fileExt);
		store.setMd5Value(params.getMd5Value());
		store.setRealFileName(params.getFileName());
		
		return store;
		
	}
	
	
	/**
	 * 上传文件
	 * @return，返回json的上传结果
	 */
	public String upload() {
		if (!checkParams(fileParams)) {
			return resultJsonStr;
		}
		
		//存储临时文件和保存fileext
		if (StringUtils.isEmpty(saveAsTempFile()) || !saveExtName() ) {
			resultJsonStr = new FileServerError("fileserver tmp file create fail").toString();
			return resultJsonStr;
			
		} else {						
			//如果是最后一个包
			if (isLastBag()) {				
				resultJsonStr = notifyUpload(fileParams);
				
			} else {
				resultJsonStr = new SuccessChunkUpload().toString();
			}

		}
		
		return resultJsonStr;
	}
	
		
	/**
	 * 判断参数的正确性
	 * @param params
	 * @return
	 */
	private boolean checkParams(HttpFilePostParams params) {
		//判断是否提供后缀名
		if (StringUtils.isEmpty(params.getFileExt()) && params.getChunk() == 0) {
			resultJsonStr = new FileExtError().toString();
			return false;
		}
		
		//校验MD5值的长度
		String md5 = params.getMd5Value();
		if (StringUtils.isEmpty(md5) || md5.length() != 32) {
			resultJsonStr = new MD5Error().toString();
			return false;
		}
		
		//校验MD5值的唯一性
		List<StoreFile> lst = storeFileService.findStoreFile(md5);
		if (lst != null && lst.size() > 0) {
			int fileId = lst.get(0).getFileId();
			
			resultJsonStr = params.isTmpFile() ? new RepeatUpload(fileId, String.format("%s?fileid=%d", commonService.findDownloadUrl(), fileId)).toString() : new RepeatUpload(lst.get(0).getFileId()).toString();
			
			lst.clear();
			return false;
		}
		
		//校验文件块数是否相同
		if (!checkChunk(params)) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 
	 * @param chunk，当前块数索引
	 * @param chunks，需要上传的总块数
	 * @param md5Value，该总文件的md5值
	 * @param cacheDir，缓存文件夹名 
	 * @return
	 */
	private boolean checkChunkIsEnough(int chunk, int chunks, String md5Value, String cacheDir) {
		String tmpFileName; 
		
		for(int i=0; i<chunk; i++) {
			tmpFileName = String.format("%s%s%s-%d-%d.tmp", cacheDir, File.separator, md5Value, i, chunks);
			if (!FileUtil.isExists(tmpFileName)) {
				resultJsonStr = new ChunkError(i).toString(); 
				return false;
			}
		}
		
		return true;
		
	}
	
	
	/**
	 * 检测块数是否合规
	 * @param params
	 * @return，如果合规，返回true
	 */
	private boolean checkChunk(HttpFilePostParams params) {
		int chunk = params.getChunk();
		int curChunk = chunk + 1;			//当前块数
		int chunks = params.getChunks();
			
		if (chunk == 0) {
			//删除该缓存文件夹
			FileUtil.deleteDirectory(params.getCacheDir());
			if (!FileUtil.createDir(params.getCacheDir())) {
				logger.info("创建cachedir失败");
			}
			return true;
			
		} else if (curChunk <= chunks) {
			//如果当前块数小于总块数，那就是正在上传中，检查该块数是否合规
			return checkChunkIsEnough(chunk, chunks, params.getMd5Value(), params.getCacheDir());
			
		} else { //if (curChunk > chunks) {
			//如果当前索引大于所有块数，告诉客户端从0开始传
			resultJsonStr = new ChunkError(0).toString(); 
			return false;
			
		} 
		
	}
	
	
}
