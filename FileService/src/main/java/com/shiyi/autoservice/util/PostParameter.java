/**
 * 名称: PostParameter.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年1月2日 下午5:42:55
 * @since  2014年1月2日
 * @author malb
 */ 
package com.shiyi.autoservice.util;

import java.io.File;

import com.shiyi.fileservice.file.FileUtil;


/**
 * @author malb
 *
 */
public class PostParameter {

	private String name;
	private String value;
	private File file = null;
	private String md5;
	private String ext;
	private String boundary;
	private byte[] fileImage;
	
    public PostParameter(String name, String value, String bound) {
        this.name = name;
        this.value = value;
        this.boundary = bound;
    }

    public PostParameter(String name, double value, String bound) {
        this.name = name;
        this.value = String.valueOf(value);
        this.boundary = bound;
    }

    public PostParameter(String name, int value, String bound) {
        this.name = name;
        this.value = String.valueOf(value);
        this.boundary = bound;
    }

    public PostParameter(String name, byte[] image, String bound)
    {
    	this.name = name;    
    	this.fileImage = image;
    	this.value = "abc.jpg";
    	this.boundary = bound;
    }
    
    public PostParameter(String name, File file, String bound) {
        this.name = name;
        this.file = file;
        this.value = file.getName();
        String md5val = FileDigest.getFileMD5(file);        
        this.md5 = md5val;
        this.ext = FileUtil.getFileExt(file);
        this.boundary = bound;
    }
    
    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }

    public File getFile() {
        return file;
    }
    
    public String getFileExt()	{
    	return ext;
    }
    
    public String getBoundary()	{
    	return boundary;
    }

    public boolean isFile(){
        return ((null != file) || (null != fileImage));
    }

    public String getFileMD5()
    {
    	if (isFile())	{
    		if (CommonFunc.isEmpty(md5))	{
    			return FileDigest.getFileMD5(file);
    		}
    	}
    	return md5;
    }
    
    private static final String JPEG = "image/jpeg";
    private static final String GIF = "image/gif";
    private static final String PNG = "image/png";
    private static final String OCTET = "application/octet-stream";
	

   
    
    /**
     * 
     * @return content-type
     */
    public String getContentType() {
        if (!isFile()) {
            throw new IllegalStateException("not a file");
        }
        String contentType;
        String extensions = file.getName();
        int index = extensions.lastIndexOf(".");
        if (-1 == index) {
            // no extension
            contentType = OCTET;
        } else {
            extensions = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
            if (extensions.length() == 3) {
                if ("gif".equals(extensions)) {
                    contentType = GIF;
                } else if ("png".equals(extensions)) {
                    contentType = PNG;
                } else if ("jpg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else if (extensions.length() == 4) {
                if ("jpeg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else {
                contentType = OCTET;
            }
        }
        return contentType;
    }
    
    
    @Override
    public String toString()
    {
    	return isFile() ? String.format("\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\" \r\nContent-Type: application/octet-stream\r\n\r\n", boundary, name, value) : 
    		String.format("\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s", boundary, name, value);    	
    }
    
    
    
    /**
     * 是否包含文件
     * @param params
     * @return
     */
    public static boolean containsFile(PostParameter[] params) {
        boolean containsFile = false;
        if(null == params){
            return false;
        }
        for (PostParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }
    
}
