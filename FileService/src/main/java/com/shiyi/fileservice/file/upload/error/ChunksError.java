/**
 * 名称: ChunksError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午2:51:29
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 *
 */
public class ChunksError extends Error {

	private int chunks;
	
	public ChunksError(int chunks) {
		super("5", "error chunks");
		this.setChunks(chunks);
		
	}

	/**
	 * @return the chunks
	 */
	public int getChunks() {
		return chunks;
	}

	/**
	 * @param chunks the chunks to set
	 */
	public void setChunks(int chunks) {
		this.chunks = chunks;
	}

}
