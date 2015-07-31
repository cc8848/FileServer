package com.shiyi.qiniu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuUtils {

	private static final Logger log = LoggerFactory.getLogger(QiniuUtils.class);
	public static final String ACCESS_KEY = "FEVsfqfhzAtor9yHW4_7d02JKjTbFR4uVik0eLXP";
	public static final String SECRET_KEY = "B3qUvGNIGVxWAH7TUEzJ_jnYnNNyqeuDhqvzu4lB";

	public static final String BUCKET_NAME = "l727054205";

	// 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
	private UploadManager uploadManager = new UploadManager();
	private String filePath;
	private Auth auth;

	public QiniuUtils(String filePath) {
		this.filePath = filePath;
		init();
	}

	private void init() {
		if (auth == null) {
			auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		}
	}

	// 简单上传，使用默认策略
	private String getUpToken0() {
		return auth.uploadToken(BUCKET_NAME);
	}

	// 覆盖上传
	private String getUpToken1() {
		return auth.uploadToken(BUCKET_NAME, "key");
	}

	// 设置指定上传策略
	private String getUpToken2() {
		return auth.uploadToken(
				"bucket",
				null,
				3600,
				new StringMap().put("callbackUrl", "call back url")
						.putNotEmpty("callbackHost", "")
						.put("callbackBody", "key=$(key)&hash=$(etag)"));
	}

	// 设置预处理、去除非限定的策略字段
	private String getUpToken3() {
		return auth.uploadToken(
				"bucket",
				null,
				3600,
				new StringMap().putNotEmpty("persistentOps", "")
						.putNotEmpty("persistentNotifyUrl", "")
						.putNotEmpty("persistentPipeline", ""), true);
	}

	public String uploadToken(String bucket, String key, long expires,
			StringMap policy, boolean strict) {
		return key;

	}

	/**
	 * 上传数据
	 *
	 * @param data
	 *            上传的数据 byte[]、File、filePath
	 * @param key
	 *            上传数据保存的文件名
	 * @param token
	 *            上传凭证
	 * @param params
	 *            自定义参数，如 params.put("x:foo", "foo")
	 * @param mime
	 *            指定文件mimetype
	 * @param checkCrc
	 *            是否验证crc32
	 * @return
	 * @throws QiniuException
	 */
	public Response put(byte[] data, String key, String token,
			StringMap params, String mime, boolean checkCrc)
			throws QiniuException {
		return null;

	}

	// 上传内存中数据
	public void upload(byte[] data, String key, String upToken) {
		try {
			Response res = uploadManager.put(data, key, upToken);
			// log.info(res);
			// log.info(res.bodyString());
			// Ret ret = res.jsonToObject(Ret.class);
			if (res.isOK()) {
				// success
			} else {
				//
			}
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时简单状态信息
			log.error(r.toString());
			try {
				// 响应的文本信息
				log.error(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
	}
	
	public void upload(String filePath, String key, String upToken) {
		try {
			Response res = uploadManager.put(filePath, key, upToken);
			// log.info(res);
			// log.info(res.bodyString());
			// Ret ret = res.jsonToObject(Ret.class);
			if (res.isOK()) {
				// success
			} else {
				//
			}
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时简单状态信息
			log.error(r.toString());
			try {
				// 响应的文本信息
				log.error(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
	}

	public byte[] getFile() throws IOException {
		File file = new File(filePath);
		byte[] buffer = null;

		if (file.exists()) {

			FileInputStream fis = null;
			ByteArrayOutputStream bos = null;
			try {
				fis = new FileInputStream(file);
				bos = new ByteArrayOutputStream(1000);
				byte[] b = new byte[1000];
				int n = 0;
				while ((n = fis.read(b)) >= 0) {
					bos.write(b, 0, n);
				}
				buffer = bos.toByteArray();
			} finally {
				if (fis != null) {
					fis.close();
				}
				if (bos != null) {
					bos.close();
				}
			}

		}
		return buffer;
	}

	public String getKey() {
		return String.valueOf(System.currentTimeMillis());
	}

	public String getUpToken() {
		return getUpToken0();
	}

	public DefaultPutRet uploadFile() throws IOException {
		try {
			Response res = uploadManager.put(getFile(), getKey(), getUpToken());
			if(res.isOK()){
				log.info(res.bodyString());
				DefaultPutRet ret = res.jsonToObject(DefaultPutRet.class);
				Map<String, Object> map = res.jsonToObject(Map.class);
				System.out.println(map);
				return ret;
			}else{
				Map<String, Object> map = res.jsonToObject(Map.class);
				System.out.println(map);
			}
		
		} catch (QiniuException e) {
			Response r = e.response;
			Map<String, Object> map = r.jsonToObject(Map.class);
			System.out.println(map);
			// 请求失败时简单状态信息
			log.error(r.toString());
			try {
				// 响应的文本信息
				log.error(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
		return null;
	}


}
