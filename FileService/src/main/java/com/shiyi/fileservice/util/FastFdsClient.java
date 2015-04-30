/**
 * 名称: FastFdsClient.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午7:35:41
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.util;

import java.net.InetSocketAddress;
import java.util.List;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shiyi.fileservice.model.FileServer;
import com.shiyi.fileservice.service.impl.CommonDataServiceImpl;

/**
 * @author malb
 * 用于操作fastdfs
 */
public class FastFdsClient {

	private final Logger  logger = LoggerFactory.getLogger("FastFdsClient");
	
	private final int FILE_SERVERS_TYPE = 0;
	private CommonDataServiceImpl dataService;
	
	public FastFdsClient() {
		dataService = SpringContextUtils.getBean(CommonDataServiceImpl.class);
		
		initGlobal();
		
	}
	
	/**
	 * 从存储下载文件
	 * @param groupName
	 * @param fileName
	 * @return
	 */
	public byte[] downFile(String groupName, String fileName) {
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient client =  new StorageClient(trackerServer, null);
			return client.download_file(groupName, fileName);
			
		} catch(Exception e) {
			logger.error(String.format("存储服务器downfile [%s/%s]error[%s]", groupName, fileName, e.getMessage()));
			return null;
			
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (Exception e) {
				}
			}
		}		
	}
	
	
	/**
	 * 上传到fastdfs中
	 * @param locaFilePath
	 * @param fileExt
	 * @return，第一个值为groupName，第二个值为fileName；如果失败，就返回null
	 */
	public String[] uploadFileToFds(String locaFilePath, String fileExt) {
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient client =  new StorageClient(trackerServer, null);
		
			String[] result = client.upload_file(locaFilePath, fileExt, null);
			if (result != null && result.length == 2) {
				logger.info(String.format("上传文件success=%s[%s/%s]", "", result[0], result[1]));
				return result;
			}
			
			return null;
			
		} catch(Exception e) {
			logger.error(String.format("上传文件失败[%s]=%s", "", e.getMessage()));
			return null;
			
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (Exception e) {
				}
			}
		}
		
	}
	
	/**
	 * 从存储上删除文件
	 * @param groupName
	 * @param fileName
	 * @return，成功返回true
	 */
	public boolean deleteFileFromDfs(String groupName, String fileName) {
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient client =  new StorageClient(trackerServer, null);
			return client.delete_file(groupName, fileName) == 0;
			
		} catch(Exception e) {
			logger.error(String.format("存储服务器delete file [%s/%s]error[%s]", groupName, fileName, e.getMessage()));
			return false;
			
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (Exception e) {
				}
			}
		}	
	}
	
	
	/**
	 * 初始化接口
	 */
	private void initGlobal() {
		ClientGlobal.setG_connect_timeout(2 * 1000);
		
		ClientGlobal.setG_network_timeout(30 * 1000);
		
		ClientGlobal.setG_anti_steal_token(false);
		
		ClientGlobal.setG_charset("UTF-8");
		
		ClientGlobal.setG_secret_key(null);
		
		initTrackerServers();
	}
	
	private boolean initTrackerServers() {
		List<FileServer> serverLst = dataService.findFileServer(FILE_SERVERS_TYPE);
		int count = (serverLst != null) ? serverLst.size() : 0;
		
		if (count > 0) {			
			InetSocketAddress[] tracker_servers = new InetSocketAddress[count];
			for(int i=0; i<count; i++) {
				FileServer server = serverLst.get(i);
				tracker_servers[i] = new InetSocketAddress(server.getIp(), server.getPort());				
			}
			
			ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
			
		} else {
			return false;
			
		}
		
		return true;
		
	}
	
	
}
