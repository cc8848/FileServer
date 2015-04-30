/**
 * 名称: HttpFilePostParams.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 上午10:47:28
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shiyi.fileservice.file.upload.error.UploadFileException;


/**
 * @author malb
 * http post上来的参数
 */
public class HttpFilePostParams {

	private int		version;	
	
	private String 	userAgent;
	
	private String 	fileExt;
	
	private int		chunk;
	
	private	int		chunks;
	
	private String	md5Value;
	
	private String	fileName;
	
	private	int		fileType;
	
	private String cacheDir;
	
	private MultipartFile file;
	
	private String wwwSchema;

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 * @throws UploadFileException 
	 */
	public void setUserAgent(String userAgent) throws UploadFileException {
		this.userAgent = userAgent;
		
		if (StringUtils.isEmpty(userAgent))
			return;
		
		/*
		String[] agents = StringUtils.split(userAgent, "/");
		if (agents.length != 4) {
			throw new UploadFileException(12, "user-agent error");
		}
		if (StringUtils.isNumeric(agents[2])) {
			setVersion(Integer.parseInt(agents[2])); 
		}
		*/
		
	}

	/**
	 * 是否是临时文件上传
	 * @return
	 */
	public boolean isTmpFile() {
		return (this.fileType == 201);
	}
	
	/**
	 * @return the fileExt
	 */
	public String getFileExt() {
		return fileExt;
	}

	/**
	 * @param fileExt the fileExt to set
	 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/**
	 * @return the chunk
	 */
	public int getChunk() {
		return chunk;
	}

	/**
	 * @param chunk the chunk to set
	 */
	public void setChunk(int chunk) {
		this.chunk = chunk;
	}

	/**
	 * @return the chunks
	 */
	public int getChunks() {
		return chunks;
	}

	/**
	 * @param chunks the chunks to set
	 */
	public void setChunks(int chunks) {
		this.chunks = chunks;
	}

	/**
	 * @return the md5Value
	 */
	public String getMd5Value() {
		return md5Value;
	}

	/**
	 * @param md5Value the md5Value to set
	 */
	public void setMd5Value(String md5Value) {
		this.md5Value = md5Value;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public int getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	private void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the cacheDir
	 */
	public String getCacheDir() {
		return cacheDir;
	}

	/**
	 * @param cacheDir the cacheDir to set
	 */
	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
	}

	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getWwwSchema() {
		return wwwSchema;
	}

	public void setWwwSchema(String wwwSchema) {
		this.wwwSchema = wwwSchema;
	}
	
	
	
}	
