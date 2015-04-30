/**
 * 名称: RepeatUpload.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午2:59:32
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class RepeatUpload extends Error {

	private int fileid;
	
	private String fileurl;
	
	/**
	 * @param code
	 * @param msg
	 */
	public RepeatUpload(int fileId) {
		super("10", "The corresponding file has been uploaded");
		setFileid(fileId);
		
	}
	
	public RepeatUpload(int fileId, String fileUrl) {
		super("10", "The corresponding file has been uploaded");
		setFileid(fileId);
		setFileurl(fileUrl);
		
	}
	

	public int getFileid() {
		return fileid;
	}

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
