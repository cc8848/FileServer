/**
 * 名称: SuccessUpload.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午2:44:06
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class SuccessUpload extends Error {

	private int fileid;
	
	private String fileurl;
	
	/**
	 * @param code
	 * @param msg
	 */
	public SuccessUpload(int fileId) {
		super("-1", "save file succeed!");
		setFileid(fileId);
	}
	
	/**
	 * 当file_type=201时，需要返回fileurl字段
	 * @param fileId
	 * @param fileUrl
	 */
	public SuccessUpload(int fileId, String fileUrl) {
		super("-1", "save file succeed!");
		setFileid(fileId);
		setFileurl(fileUrl);
	}
	

	/**
	 * @return the fileid
	 */
	public int getFileid() {
		return fileid;
	}

	/**
	 * @param fileid the fileid to set
	 */
	public void setFileid(int fileid) {
		this.fileid = fileid;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	
	
}
