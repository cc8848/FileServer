package com.shiyi.fileservice.file;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.shiyi.fileservice.service.impl.CommonDataServiceImpl;
import com.shiyi.fileservice.util.SpringContextUtils;

/**
 * 七牛新版本[7.0.0, 7.0.99]的上传工具类
 * 
 * @author deng
 * @date 2015年7月31日
 * @version 1.0.0
 */
public class QiniuUploadNew {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
	private static final UploadManager uploadManager = new UploadManager();
	private String srcFile;
	private String key;
	private String serect;
	private String bucket;
	private String domain;
	
	private CommonDataServiceImpl commonData;

	private Auth auth;

	public QiniuUploadNew(String srcFile) {
		this.srcFile = srcFile;
		commonData = SpringContextUtils.getBean(CommonDataServiceImpl.class);
		HashMap<String, String> record = commonData.findNewQiniuConfig();

		if (record != null) {
			this.key = MapUtils.getString(record, "Qiniu_appkey", "");
			this.serect = MapUtils.getString(record, "Qiniu_appsecret", "");
			this.bucket = MapUtils.getString(record, "Qiniu_bucket", "");
			this.domain =MapUtils.getString(record, "Qiniu_domain", "");
			this.auth = Auth.create(key, serect);
		}

	}
	
	/**
	 * 上传文件到七牛
	 * @author deng
	 * @param prefix
	 * @param qiniuKey
	 * @param fileExt
	 * @return
	 */
	public 	Map<String, Object> upload(String prefix, String qiniuKey, String fileExt){
		qiniuKey = StringUtils.isEmpty(prefix) ?  String.format("%s.%s", qiniuKey, fileExt)  
				: String.format("%s/%s.%s", prefix, qiniuKey, fileExt) ;	
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> m = null;
		try {
		Response res = uploadManager.put(this.srcFile, qiniuKey, getUpToken());
		DefaultPutRet ret = res.jsonToObject(DefaultPutRet.class);
		retMap.put("ret", 0);
		retMap.put("hash", ret.hash);
		retMap.put("key", ret.key);
		retMap.put("url", domain + "/" + ret.key);
		retMap.put("bucket", bucket);
		return retMap;
		}catch (QiniuException e) {
			Response res  = e.response;
			logger.error("上传七牛失败，错误信息是:{}",res.toString());

			try {
				retMap.put("ret", res.statusCode);
				m = res.jsonToObject(Map.class);
				retMap.put("msg", m.get("error"));
			} catch (QiniuException e1) {
				logger.error("七牛转换出错",e1);
			}
		

		}
		return retMap;
	}
	
	// 简单上传，使用默认策略
	private String getUpToken() {
		return auth.uploadToken(bucket);
	}
}
