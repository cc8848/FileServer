/**
 * 名称: ChunkError.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月5日 下午2:49:39
 * @since  2014年8月5日
 * @author malb
 */ 
package com.shiyi.fileservice.file.upload.error;

/**
 * @author malb
 * 错误的chunk号
 */
public class ChunkError extends Error {

	private int chunk;
	

	public ChunkError(int needChunkNum) {
		super("4", "error chunk");
		this.setChunk(needChunkNum);
		
	}

	/**
	 * @return the chunk
	 */
	public int getChunk() {
		return chunk;
	}

	/**
	 * @param chunk the chunk to set
	 */
	public void setChunk(int chunk) {
		this.chunk = chunk;
	}

}
