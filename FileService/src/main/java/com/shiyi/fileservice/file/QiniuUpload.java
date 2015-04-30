/**
 * 名称: QiniuUpload.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年10月15日 下午5:10:40
 * @since  2014年10月15日
 * @author malb
 */ 
package com.shiyi.fileservice.file;

import java.util.HashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;
import com.shiyi.fileservice.service.impl.CommonDataServiceImpl;
import com.shiyi.fileservice.util.CommonUtils;
import com.shiyi.fileservice.util.SpringContextUtils;

/**
 * @author malb
 *
 */
public class QiniuUpload {

	private final Logger logger = LoggerFactory.getLogger(QiniuUpload.class);
	
	private String srcFile;
	
	private String key;
	private String serect;
	private String policy;
	
	private CommonDataServiceImpl commonData;
	
	public QiniuUpload(String srcFile) {
		this.srcFile = srcFile;
		
		commonData = SpringContextUtils.getBean(CommonDataServiceImpl.class);
		HashMap<String, String>  record =  commonData.findQiniuConfig();
		
		if (record != null) {			
			key = MapUtils.getString(record, "Qiniu_appkey", "");
			serect = MapUtils.getString(record, "Qiniu_appsecret", "");
			policy = MapUtils.getString(record, "Qiniu_policy", "");
		}
		
	}
	
	/**
	 * 执行上传操作
	 * @param prefix，七牛存储的前缀
	 * @param qiniuKey，用文件的md5值作为文件名
	 * @return，如果上传成功，则返回string[2]，[0]--七牛存储空间名称，[1]-文件key；失败，则返回null
	 */
	public String[] upload(String prefix, String qiniuKey, String fileExt) {
		
		qiniuKey = StringUtils.isEmpty(prefix) ?  String.format("%s.%s", qiniuKey, fileExt)  
			: String.format("%s/%s.%s", prefix, qiniuKey, fileExt) ;	
		
        Mac mac = new Mac(key, serect);
        PutPolicy putPolicy = new PutPolicy(policy);
        try {
			String uploadToken = putPolicy.token(mac);
			PutExtra putExtra = new PutExtra();
			PutRet ret = IoApi.putFile(uploadToken, qiniuKey, srcFile, putExtra);

			if (ret.ok()) {
				String[] result = new String[2];
				result[0] = policy;
				result[1] = ret.getKey();
				
				logger.info(String.format("七牛上传完成key=%s hash=%s", result[1], ret.getHash()));
				return result;
			} else {
				logger.info(String.format("七牛上传完成失败，response=%s", ret.getResponse()));
				return null;
			}
			
		} catch (Exception e) {
			logger.error(String.format("七牛上传出错=%s", e.getMessage()));
			return null;
		} 	

	}
	
}
