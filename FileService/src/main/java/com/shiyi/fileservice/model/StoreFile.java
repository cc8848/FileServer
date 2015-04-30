/**
 * 名称: StoreFile.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午7:18:29
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.model;

import java.io.Serializable;

/**
 * @author malb
 * 存储文件表，用于存储所有的数据中心文件
 */
public class StoreFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5479659302116908062L;

	//文件唯一id
	private int		fileId;
	
	//文件名
	private String 	fileName;
	
	//文件大小(字节为单位)
	private int		fileSize;
	
	//文件后缀名
	private String	extension;
	
	//文件类型(0:录音文件 1:用户特征文件 2:会员头像 10:歌星头像 11:音准图片 12:MP3立体声 13:MP3原唱 14:MP3伴唱 15:原始音准文件 16:加密音准文件 17: 场所音准文件 18:种子文件 19:音准类型图片 20:广告图片 21歌曲分类图片 30:商家统计点配置文件 31:pkpass文件 32场所Logo, 33 升级文件, 34 商家场所展示图像) 41:grantsong 42:ftrall 43:songfilelist 44:prefile 45:normal
	private int		fileType;
	
	//文件的状态(0:未使用 1:使用 2:删除)
	private int		status;
	
	//组名
	private String	groupName;
	
	//下载文件命名
	private String	realFileName;
	
	//文件md5值
	private String	md5Value;
	
	//七牛存储的url
	private String url;
	

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
	 * @return the fileSize
	 */
	public int getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the realFileName
	 */
	public String getRealFileName() {
		return realFileName;
	}

	/**
	 * @param realFileName the realFileName to set
	 */
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
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
	 * @return the fileId
	 */
	public int getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
