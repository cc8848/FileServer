/**
 * 名称: FileServer.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午7:02:55
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.model;

import java.io.Serializable;


/**
 * @author malb
 * 文件服务器列表对象
 */
public class FileServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 288968147210342630L;

	//文件服务器IP
	private String	ip;					
	
	//文件服务器端口
	private int		port;				
	
	//文件服务器HTTP端口
	private int 	httpPort;			
	
	//服务器组类型:0表示录音服务器综控 1表示音准综控 2表示会员头像综控 3表示其它图片
	private int		fileServerType;		
	
	//文件服务器状态
	private int		status;				
	
	//服务器域名
	private String	domain;				
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the httpPort
	 */
	public int getHttpPort() {
		return httpPort;
	}
	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}
	/**
	 * @return the fileServerType
	 */
	public int getFileServerType() {
		return fileServerType;
	}
	/**
	 * @param fileServerType the fileServerType to set
	 */
	public void setFileServerType(int fileServerType) {
		this.fileServerType = fileServerType;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * @return the status，0--表示有效
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set，0--表示有效
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
