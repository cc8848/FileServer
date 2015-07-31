package com.shiyi.qiniu;

import java.io.IOException;

import org.junit.Test;

import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuTest {
@Test
	public void testUploadLoad() throws IOException {
		String filePath = "C:/Users/Administrator/Pictures/aaa.jpg";
		QiniuUtils qiniu = new QiniuUtils(filePath);
		DefaultPutRet ret = qiniu.uploadFile();
		System.out.println("key:" + ret.key + ", hash: " + ret.hash);
	}
}
