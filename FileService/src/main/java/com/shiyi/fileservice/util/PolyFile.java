/**
 * 名称: PolyFile.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月8日 下午4:21:57
 * @since  2014年8月8日
 * @author malb
 */ 
package com.shiyi.fileservice.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shiyi.fileservice.file.FileUtil;

/**
 * @author malb
 * 扫描临时文件夹，聚合tmp文件，将tmp文件move到cache文件夹
 */
public class PolyFile {

	private final Logger logger = LoggerFactory.getLogger("PlyFile");
	
	private String cacheDir;
	
	private int fileSize;
	
	private String fileExt;
	
	private int chunks;
	
	private String md5Value;
	
	private String polyFilePath;
	
	
	/**
	 * 
	 * @param cacheDir，缓存文件夹
	 * @param chunks，总块数
	 * @param md5Value，该文件的md5值
	 */
	public PolyFile(String cacheDir, int chunks, String md5Value) {
		this.cacheDir = cacheDir;
		this.chunks = chunks;
		this.md5Value = md5Value;
		
		scanParam();
	}
	
	/**
	 * 获得文件的扩展名
	 * @return
	 */
	private void scanParam() {
		fileSize = 0;
		
		File[] files = FileUtil.getFiles(cacheDir, "tmp");
		int fileCount = files.length;
		
		for(int i=0; i<fileCount; i++) {
			String fileName = files[i].getName();
			String[] strLst = StringUtils.split(fileName, "-");
			
			//当是  "扩展名" 时
			if (strLst.length == 2) {
				String tmp = strLst[1];
				int idx = tmp.lastIndexOf(".");
				setFileExt(tmp.substring(0, idx));
				
			} else {
				fileSize += files[i].length();
			}
		}
		
	}

	
	/**
	 * 聚合，并提取相关信息
	 * @return
	 */
	public boolean poly() {
		
		String polyFile = String.format("%s%s%s.%s", new File(this.cacheDir), File.separator, md5Value, getFileExt());
		String tmpfile = null;
		
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(polyFile);
			bos = new BufferedOutputStream(fos);
			
			for(int i=0; i<chunks; i++) {
				tmpfile = String.format("%s/%s-%d-%d.tmp", this.cacheDir , md5Value, i, this.chunks);				
				Files.copy(Paths.get(tmpfile), bos);
				bos.flush();
				//logger.info(String.format("正在聚合[%s][%d][%s][长度=%d]", this.md5Value, i, tmpfile, read));
			}
						
			polyFilePath = polyFile;
			logger.info(String.format("聚合完成[%s][%s]", this.md5Value, polyFile));
			
			return true;
			
		} catch (Exception e) {
			logger.error(String.format("正在聚合[%s][%s][失败]%s", this.md5Value, tmpfile, e.getMessage()));
			return false;
			
		} finally {
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(bos);
		}
		
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
	 * @return the polyFilePath
	 */
	public String getPolyFilePath() {
		return polyFilePath;
	}

	/**
	 * @param polyFilePath the polyFilePath to set
	 */
	public void setPolyFilePath(String polyFilePath) {
		this.polyFilePath = polyFilePath;
		
	}

	
	
}
