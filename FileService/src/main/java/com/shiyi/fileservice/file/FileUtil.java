/**
 * 名称: FileUtil.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2013年12月6日 下午5:49:12
 * @since  2013年12月6日
 * @author malb
 */ 
package com.shiyi.fileservice.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.IOUtils;





/**
 * @author malb
 *
 */
public class FileUtil {

	
	
	private FileUtil(){}
	
	
	/**
	 * 获得文件大小
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		return new File(filePath).length();
	}
	
	
	/**
	 * 获得某个文件的最后修改日期
	 * @param filePath
	 * @return，返回默认的时间格式：yyyy-MM-dd
	 * @throws IOException
	 */
	public static String getLastModDate(String filePath) throws Exception {
		return getLastModTime(filePath, "yyyy-MM-dd");
	}	
	
	/**
	 * 获得某个文件的最后修改日期
	 * @param file
	 * @return，返回默认的时间格式：yyyy-MM-dd
	 * @throws IOException
	 */
	public static String getLastModDate(File file) throws Exception {
		return getLastModTime(file, "yyyy-MM-dd");
	}	
	

	/**
	 * 获得某个文件的最后修改日期
	 * @param file
	 * @param parrtern，时间格式，如："yyyy-MM-dd HH:mm:ss SSS"
	 * @return
	 * @throws IOException
	 */
	public static String getLastModTime(File file, String parttern) throws Exception {
		Date date = new Date(file.lastModified());
		SimpleDateFormat df = new SimpleDateFormat(parttern);
		return df.format(date);
	}	
	
	
	/**
	 * 获得某个文件的最后修改日期
	 * @param filePath
	 * @param parrtern，时间格式，如："yyyy-MM-dd HH:mm:ss SSS"
	 * @return
	 * @throws IOException
	 */
	public static String getLastModTime(String filePath, String parttern) throws Exception {
		File file = new File(filePath);
		return getLastModTime(file, parttern);
	}
	
	
	/**
	 * 重命名文件
	 * @param file，源文件对象
	 * @param dstFileName，目标文件string
	 * @return
	 */
	public static boolean reNameFile(File file ,String dstFileName)
	{
		File dstFile = new File(dstFileName);
		return reNameFile(file, dstFile);
	}
	
	
	/**
	 * 重命名文件
	 * @param file，file对象
	 */
	public static boolean reNameFile(File file, File dstFile)
	{
		return file.renameTo(dstFile);
	}
	
	/**
	 * 重命名文件
	 * @param fileName，文件名称
	 */
	public static void reNameFile(String fileName, String dstFileName)
	{
		File file = new File(fileName);
		File dstFile = new File(dstFileName);
		reNameFile(file, dstFile);
	}
	
	
	
	/**
	 * 是否存在该文件
	 * @param fileName，文件路径
	 * @return，如果存在返回true，反之，false
	 */
	public static boolean isExists(String fileName)
	{
		return new File(fileName).isFile();	
	}
	
	
	/**
	 * 删除某个文件
	 * @param fileName
	 * @return
	 */
	public static boolean delFile(String fileName)
	{
		File file = new File(fileName);
		return file.delete();
	}
	
	/**
	 * 创建某个目录
	 * @param dir
	 * @return，创建成功，返回true，反之，false
	 */
	public static boolean createDir(String dir)
	{
		File file = new File(dir);
		return file.mkdirs();

	}
		
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public static boolean deleteDirectory(String sPath) {  
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {  
            sPath = sPath + File.separator;  
        }  
        File dirFile = new File(sPath);  
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = delFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
	
	
	
	/**
	 * 获得文件的扩展名
	 * @param file，文件对象
	 * @return，如果未获得，返回""，如果获得，返回".ext"
	 */
	public static String getFileExt(File file)
	{
		String fileName = file.getName();
		// find the last
		int idx = fileName.lastIndexOf(".");
		if(idx == -1) {
			return "";
		} else if(idx == fileName.length()-1) {
			return "";
		} else {
			return fileName.substring(idx + 1, fileName.length());
		}				
	}
	
	/**
	 * 获得某个文件的文件名，例如"abc.txt"的文件名为"abc"
	 * @param file，File文件对象
	 * @return，如果获得成功，返回"name"，反之，返回""
	 */
	public static String getFileName(File file)
	{
		String fileName = file.getName();
		// find the last
		int idx = fileName.lastIndexOf(".");
		if(idx == -1) {
			return "";
		} else if(idx == fileName.length()-1) {
			return "";
		} else {
			return fileName.substring(0, idx);
		}		
	}
	
	
	/**
	 * 获得某个文件夹下的所有txt文件
	 * @param dir，文件夹
	 * @return，如果非文件夹，返回null，反之返回File[]数组
	 */
	public static File[] getFiles(String dir, String extension)
	{
		File file = new File(dir);
		return getFiles(file, extension);

	}
	
	/**
	 * 获得某个文件夹下的所有txt文件
	 * @param dir
	 * @return，如果非文件夹，返回null，反之返回File[]数组
	 */
	public static File[] getFiles(File dir, String extension)
	{
		if (dir.isDirectory())	{
			File[] files = dir.listFiles(new ExtensionFileFilter(extension));
			return files;
		}
	
		return null;
	}
	
	
	/**
	 * 获得某个文件夹下面的所有文件夹
	 * @param dir
	 * @return，如果非文件夹，返回null，反之返回File[]数组
	 */
	public static File[] getDirs(String dir)
	{
		File fDir = new File(dir);
		if (fDir.isDirectory())	{
			File[] files = fDir.listFiles();
			ArrayList<File> v = new ArrayList<File>();
			for (int i=0; i<files.length; i++)	{
				File file = files[i];
				if (file.isDirectory())	{
					v.add(file);
				}
			}
			
			return (File[]) v.toArray(new File[v.size()]);
		}
			
		return null;
	}
	
	
	/**
	 * copy文件
	 * @param srcFile，源文件路径
	 * @param dstFile，目标文件路径
	 * @return，copy成功，返回true
	 */
	public static boolean copyFile(String srcFile, String dstFile) {
		
	       try { 
	    	   Files.copy(Paths.get(srcFile), Paths.get(dstFile), StandardCopyOption.REPLACE_EXISTING);
	    	   return true;
	       } 
	       catch (Exception e) { 
	           return false;
	       } 		
	}
	
	/**
	 * 获得16进制字符串
	 * @param datas
	 * @return
	 */
	public static String toHexString(byte[] datas) {
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < datas.length; i++ ) {
			String hex = Integer.toHexString(datas[i] & 0xFF);
			if ( hex.length() <= 1 ) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	
	/**
	 * 获得某个文件的MD5值，只用来使用小文件的MD5值获得
	 * @param srcFile
	 * @return
	 */
	public static String getFileMD5(File srcFile)
	{	
		FileInputStream fis = null;
	
		try{
			fis = new FileInputStream(srcFile);
			
			byte[] buffer = new byte[1024 * 8];     
			
			MessageDigest md5 = MessageDigest.getInstance("MD5");     
			int numRead = 0;     
			while ((numRead = fis.read(buffer)) > 0) {     
				md5.update(buffer, 0, numRead);     
			}     
			buffer = null;
			return  toHexString(md5.digest());
			
		} catch (Exception e) {
			return "";
			
		} finally {
			
			IOUtils.closeQuietly(fis);
		}
			
		
	}
	
	
	public static byte[] getFileStream(String filePath) {
		File srcFile = new File(filePath);
		int len = (int) srcFile.length();
		try (FileInputStream fis = new FileInputStream(srcFile);){ 
			return  IOUtils.toByteArray(fis, len);
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return null;				
	}
}
