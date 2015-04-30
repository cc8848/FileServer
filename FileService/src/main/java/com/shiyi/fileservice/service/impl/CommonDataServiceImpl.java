/**
 * 名称: CommonDataServiceImpl.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午7:53:23
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiyi.fileservice.dao.CommonDataDAO;
import com.shiyi.fileservice.model.FileServer;
import com.shiyi.fileservice.service.CommonDataService;
import com.shiyi.fileservice.util.SpringContextUtils;

/**
 * @author malb
 *
 */

@Service
public class CommonDataServiceImpl implements CommonDataService {

	private final static int FILE_SERVERS_TYPE = 0;
	
	@Autowired
	private CommonDataDAO commonDataDAO;
	
	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.CommonDataService#findFileServer(int)
	 */
	@Override
	public List<FileServer> findFileServer(int fileServerType) {
		return commonDataDAO.findFileServer(fileServerType);
	}

	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.CommonDataService#findDownloadUrl()
	 */
	@Override
	public String findDownloadUrl() {
		return commonDataDAO.findDownloadUrl();
	}

	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.CommonDataService#getHttpUrl(java.lang.String, java.lang.String)
	 */
	@Override
	public String getHttpUrl(String groupName, String remoteFile) {
		
		List<FileServer> serverLst = commonDataDAO.findFileServer(FILE_SERVERS_TYPE);
		if (serverLst.size() > 0) {
			FileServer server = serverLst.get(0);
			return String.format(String.format("http://%s:%d/%s/%s", server.getDomain(), server.getHttpPort(), groupName, remoteFile));
		}
		
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.shiyi.fileservice.service.CommonDataService#findQiniuConfig()
	 */
	@Override
	public HashMap<String, String> findQiniuConfig() {
		HashMap<String, String> result = new HashMap<String, String>();
		
		List<HashMap<String, String>> list = commonDataDAO.findQiniuConfig(); 
		if (list != null) {
			for (HashMap<String, String> record : list) {
				if (record.containsValue("Qiniu_appkey")) {
					result.put("Qiniu_appkey", MapUtils.getString(record, "ParameterValue", ""));
				}
				
				if (record.containsValue("Qiniu_appsecret")) {
					result.put("Qiniu_appsecret", MapUtils.getString(record, "ParameterValue", ""));
				}
				
				if (record.containsValue("Qiniu_policy")) {
					result.put("Qiniu_policy", MapUtils.getString(record, "ParameterValue", ""));
				}
			}
		}
		
		return result;
	}

}
