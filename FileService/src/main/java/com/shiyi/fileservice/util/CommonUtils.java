/**
 * 名称: CommonUtils.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月15日 下午5:55:52
 * @since  2014年10月15日
 * @author malb
 */ 
package com.shiyi.fileservice.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.shiyi.fileservice.file.store.StoreType;

/**
 * @author malb
 *
 */
public class CommonUtils {

	private static final String QINIU_URL = "qiniudn.com";
	
	private static final int FT_USERIMAGE = 2;  // 用户头像的图片类型值	
	private static final int FT_MIXSOUND =  49;	//混音后文件
	private static final int FT_CELLPHONE = -1;	//混音后文件
	
	private static List<String> extLst;
	
	static {
		extLst = new ArrayList<String>(Arrays.asList("mp3", "jpg", "jpeg", "png", "gif", "apk"));
	}
	
	/**
	 * 获得随机值
	 * @return
	 */
	public static String getUUID() {
		String sUid = UUID.randomUUID().toString();
		return sUid.replaceAll("-", "").toUpperCase();
	}
	
	
	/**
	 * 获得真实路径
	 * @param request
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		return realPath;
		
	}
	
	/**
	 * 获得缓存目录
	 * @param md5Value
	 * @param request
	 * @return
	 */
	public static String getMd5CacheDir(String md5Value, HttpServletRequest request) {
		return String.format("%scache/%s/", getRealPath(request), md5Value);
		
	}
	
	
	/**
	 * 是否需要重定向
	 * @param ext
	 * @return
	 */
	public static boolean isNeedLocation(String ext) {		
		return extLst.contains(ext);
		
	}
	
	
	
	/**
	 * 是否重定向到七牛url
	 * @param srcFrm  ，0-重定向到七牛下载,1-从本地存储下载
	 * @param qiniuUrl ，七牛的下载url地址
	 * @return，如果需要从七牛下载，返回true
	 */
	public static boolean isDownFromQiniu(Integer srcFrm, String qiniuUrl) {
		if (srcFrm == null) {
			srcFrm = 0;	
		}
		
		return ((srcFrm == 0) && !StringUtils.isEmpty(qiniuUrl));
	}
	
	/**
	 * 是否需要输出url
	 * @return
	 */
	public static boolean isGetUrl(String typeId) {
		return "1".equalsIgnoreCase(typeId);
		
	}
	
	
	
	/**
	 * 是否apk文件
	 * @param ext
	 * @return
	 */
	public static boolean isApk(String ext) {
		return "apk".equalsIgnoreCase(ext);
	}
	
	/**
	 * 根据数字返回类型
	 * @param num
	 * @return
	 */
	public static StoreType getType(int num) {
		
		if (StoreType.FASTDFS.ordinal() == num) {
			return StoreType.FASTDFS; 
			
		} else if (StoreType.QINIU.ordinal() == num) {
			return StoreType.QINIU;
			
		} else {
			return StoreType.MONGO;
		}
		
		
	}
	
	/**
	 * 获得七牛存储的url
	 * @param group
	 * @param file
	 * @return
	 */
	public static String getQiNiuUrl(String group, String file) {
		
		return String.format("http://%s.%s/%s", group, QINIU_URL, file);
		
	}
	
	
	/**
	 * 是否需要七牛进行存储
	 * @param fileType
	 * 	private static final int FT_USERIMAGE = 2;  // 用户头像的图片类型值	
	 *	private static final int FT_MIXSOUND = 49;	//混音后文件
	 * @return
	 */
	public static boolean isNeedQiniuUpload(int fileType) {
		
		return (fileType == FT_USERIMAGE || fileType == FT_MIXSOUND || fileType == FT_CELLPHONE);
		
	}
	
	/**
	 * 根据fileType获得七牛存储的前缀
	 * @param fileType
	 * @return
	 */
	public static String getPrefix(String ext) {
		
		return StringUtils.isEmpty(ext) ? "" : ext;
		
	}
	
	
	/**
	 * 将XML document转换成string
	 * @param doc
	 * @return
	 */
	public static String docToString(Document doc)
	{
		String s = "";
		try	(ByteArrayOutputStream out = new ByteArrayOutputStream();){
			XMLWriter writer = new XMLWriter(out, new OutputFormat(" ", true, "UTF-8"));
			writer.write(doc);
			s = out.toString("UTF-8");
			s = s.replaceAll("(\r\n|\r|\n|\n\r)", "");
			writer.close();
			
		} catch(Exception e)	{			
			s = "";
		}
		
		return s;
	}
	
	
	
	/**
	 * 发送通知上传七牛存储
	 * @param fileId
	 * @param url
	 * @param fileName
	 * @param md5value
	 * @param fileType
	 */
	public static void notifyUploadQiniu(int fileId, String url, String fileName, String md5value, int fileType) {
		
		Document doc = DocumentHelper.createDocument();
		
		Element root = doc.addElement("msg");		
		root.addAttribute("id", "0012");
		root.addAttribute("md5value", md5value);
		root.addAttribute("filename", fileName);
		root.addAttribute("fileid", Integer.toString(fileId));
		root.addAttribute("url", url);
		root.addAttribute("filetype", Integer.toString(fileType));
		
		MqClient mq = new MqClient();
		mq.sendMQMessage(docToString(doc));
		
	}
	
	
}
