/**
 * 名称: ExtensionFileFilter.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2013年12月9日 上午11:39:22
 * @since  2013年12月9日
 * @author malb
 */ 
package com.shiyi.fileservice.file;

import java.io.File;
import java.io.FileFilter;

/**
 * @author malb
 *
 */
public class ExtensionFileFilter implements FileFilter {

	
	private String extension;
	
	/**
	 * 构造函数
	 * @param extension
	 */
	public ExtensionFileFilter(String extension){
		this.extension = extension;
	}
	
		
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File pathname) {
		if (pathname.isFile()){
			String name = pathname.getName( );
			// find the last
			int idx = name.lastIndexOf(".");
			if(idx == -1) {
				return false;
			} else if(idx == name.length()-1) {
				return false;
			} else {
				return this.extension.equalsIgnoreCase(name.substring(idx+1));
			}

		}
		return false;
	}

}
