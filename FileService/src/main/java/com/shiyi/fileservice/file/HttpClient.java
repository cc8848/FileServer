/**
 * 名称: HttpClient.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月1日 下午3:27:31
 * @since  2014年10月1日
 * @author malb
 */ 
package com.shiyi.fileservice.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author malb
 *
 */
public class HttpClient {

	private final static Logger logger = LoggerFactory.getLogger("HttpClient");
	
	
	
    /**  
     * 把二进制流转化为byte字节数组 
     * @param instream 
     * @return byte[] 
     * @throws Exception 
     */
	/*
    public static byte[] readInputStream(InputStream instream) throws Exception {  
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {  
	        byte[]  buffer = new byte[1024 * 1024];  
	        int len = 0;  
	        while ((len = instream.read(buffer)) != -1){  
	            outStream.write(buffer,0,len);  
	        }  
	        buffer = null;
	        
	        return outStream.toByteArray();           
        }
    }  	
    */
    
    /**
     * 将流写成文件
     * @param filePath
     * @param im
     * @return
     */
    private static String writeFileStream(String filePath, InputStream im) {
    	
    	FileOutputStream fos = null;
        try {
        	fos = new FileOutputStream(filePath);
        	
	        byte[]  buffer = new byte[100 * 1024];  
	        int len = 0;  
	        while ((len = im.read(buffer)) != -1){  
	        	try {
					fos.write(buffer,0,len);
					fos.flush();
					
				} catch (IOException e) {
					return null;
				}  
	        }  
	        buffer = null;
	        
	        return filePath;
	        
        } catch(Exception e) {
        	return null;
        	
        } finally {
        	IOUtils.closeQuietly(fos);
        }
    }
    
	
    /**
     * 下载url到文件中
     * @param picUrl
     * @return，如果成功，返回文件的路径，如果失败，返回null
     */
    public static String downLoadFile(String picUrl, String filePath) {
		if (StringUtils.isEmpty(picUrl)) 
			return null;
		
		int response = -1;
		HttpURLConnection conn = null;
		try {			
			URL url = new URL(picUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			HttpURLConnection.setFollowRedirects(true);
			conn.setConnectTimeout(10 * 1000);
			conn.connect();
			response = conn.getResponseCode();
			if (response == 200) {
				InputStream im = null;				
				try {
					im = conn.getInputStream();
					return writeFileStream(filePath, im);
				} finally {
					IOUtils.closeQuietly(im);
				}
			}
			
			return null;
			
		} catch (Exception e) {
			logger.error(String.format("下载到文件出错[url=%s][%s][responsecode=%d]", picUrl, e.getMessage(), response));
			return null;
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}    	
    }
    
	
	/**
	 * 读取文件流
	 * @param picUrl
	 * @return
	 */
    /*
	public static byte[] getFileContent(String picUrl)
	{
		if (StringUtils.isEmpty(picUrl)) 
			return null;
		
		int response = -1;
		HttpURLConnection conn = null;
		try {			
			URL url = new URL(picUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			HttpURLConnection.setFollowRedirects(true);
			conn.setConnectTimeout(10 * 1000);
			conn.connect();
			response = conn.getResponseCode();
			if (response == 200) {
				try(InputStream im = conn.getInputStream();) {
					return readInputStream(im);
				}
			}
			
			return null;
			
		} catch (Exception e) {
			logger.error(String.format("获得文件出错[url=%s][%s][responsecode=%d]", picUrl, e.getMessage(), response));
			return null;
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
	}
	*/
}
