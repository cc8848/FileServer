/**
 * 名称: MqClient.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年11月5日 下午5:40:27
 * @since  2014年11月5日
 * @author malb
 */ 
package com.shiyi.fileservice.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author malb
 * rabbitmq客户端
 */
public class MqClient {

	private final Logger logger = LoggerFactory.getLogger(MqClient.class); 
	
	private AmqpTemplate amqpTemplate;
	
	/**
	 * 发送消息
	 * @param message
	 */
	public void sendMQMessage(String message) {
		
		byte[] msg = null;
		
		try {
			
			msg = message.getBytes("utf-8");
			
		} catch (UnsupportedEncodingException e) {
			msg = message.getBytes();
		} 
		
		try {
			amqpTemplate = SpringContextUtils.getBean(AmqpTemplate.class);
			
			amqpTemplate.send(new Message(msg, new MessageProperties()));
			
			msg = null;
			
		}catch (Exception e) {
			logger.error(String.format("发送MQ消息失败[%s][%s]", e.getMessage(), message));
		}
		
	}
	
}
