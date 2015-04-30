/**
 * 名称: SpringContextUtils.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月4日 下午6:01:00
 * @since  2014年8月4日
 * @author malb
 */ 
package com.shiyi.fileservice.util;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author malb
 * 用来获得配置文件中的bean
 */

public class SpringContextUtils implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext = null;
	
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	
	public static Object getBean(String beanName) throws BeansException{
		return applicationContext.getBean(beanName);
	}
	
	/**
	 * 获得javabean对象
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}
	
	
	/**
	 * 是否包含此javabean
	 * @param beanName
	 * @return，如果包含，返回true
	 */
	public static boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

}
