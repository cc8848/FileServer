/**
 * 名称: UploadFile.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年1月2日 下午2:59:47
 * @since  2014年1月2日
 * @author malb
 */
package com.shiyi.upload;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.util.CharArrayBuffer;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.shiyi.autoservice.util.CommonFunc;
import com.shiyi.autoservice.util.FileDigest;
import com.shiyi.autoservice.util.PostParameter;

/**
 * @author malb
 *
 */
public class UploadFile {
	
	public static AtomicInteger scount = new AtomicInteger(0);
	
/*	static{
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8888");
	}*/

	private final static Logger log = Logger.getLogger(UploadFile.class);
	private File uploadFile;

	private static String uploadUrl = null; // 上传接口地址

	private final String PARAM_FILEEXT = "fileext";
	private final String PARAM_CHUNK = "chunk";
	private final String PARAM_CHUNKS = "chunks";
	private final String PARAM_FILE = "file";
	private final String PARAM_FILEMD5 = "md5value";
	private final String PARAM_FILETYPE = "file_type";

	private final int FILE_CHUNK_LEN = 1024 * 1024 * 1; // 每个分块的大小为1MB
	private long totalSize;

	private int errorCode = -1;
	private int nFileId = -1; // 数据中心的fileid
	private String errorMessage = "";

	private boolean isRepeatUpload = false; // 是否是重复上传，当返回值为10时，是重复上传

	/**
	 * 上传文件
	 * 
	 * @param srcFile
	 *            ，需要上传的文件名称
	 */
	public UploadFile(String srcFile) {
		uploadFile = new File(srcFile);
		totalSize = uploadFile.length();

		// uploadUrl =
		// "http://filedownload.ktvme.com:8080/FileService/uploadfile.do";		
		// uploadUrl = "http://192.168.82.119:3003/FileService/uploadfile.do";
		//uploadUrl = "http://192.168.82.68:5081/FileService/uploadfile.do";
		//uploadUrl = "http://192.168.73.125:5081/FileService/uploadfile.do";

		log.info(String.format("[上传URL接口=%s]准备上传...[%s]", uploadUrl, srcFile));
	}

	/**
	 * 解析post文件后返回的json格式
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private boolean parseJson(String json) throws Exception {
		if (CommonFunc.isEmpty(json))
			return false;

		JSONObject list = CommonFunc.asJSONObject(json);

		try {
			errorCode = list.getInt("errorcode");

			if (errorCode == 10)
				this.setRepeatUpload(true);

			// 当全部上传完成，或者已经上传过了，就当作成功
			// errorCode = ((errorCode == -1) || (errorCode == 10)) ? 0 :
			// errorCode;
			nFileId = list.getInt("fileid");
			errorMessage = list.getString("message");
			return true;

		} catch (JSONException e) {
			errorCode = -1;
			errorMessage = e.getMessage();
			nFileId = -1;
			return false;
		}

	}

	/**
	 * 获得post文件后的返回字符串
	 * 
	 * @param httpConn
	 * @return
	 */
	private String getRespMessage(HttpURLConnection httpConn) {
		int responCode = -1;

		try {
			responCode = httpConn.getResponseCode();
			System.out.println("responCode" + responCode);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (responCode != 200) {
			return "";
		}

		InputStream is = null;
		DataInputStream dis = null;
		String resp = null;
		try {
			resp = readString(httpConn.getInputStream());
/*			resp = readString(httpConn.getInputStream());
			is = httpConn.getInputStream();
			dis = new DataInputStream(is);
			byte d[] = new byte[dis.available()];
			int nRecvCount = dis.read(d);
			log.info(String.format("recive count =[%d]", nRecvCount));
			resp = new String(d);
			log.info(resp);*/
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (dis != null)
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return resp;

	}

	/**
	 * 将file的分块信息
	 * 
	 * @param file
	 * @param chunk
	 * @param chunks
	 * @param dos
	 * @throws Exception
	 */
	private void writeToStream(File file, int chunk, int chunks,
			DataOutputStream dos) throws Exception {

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			raf.seek(chunk * FILE_CHUNK_LEN);

			byte[] buffer = new byte[FILE_CHUNK_LEN];

			int nRead = -1;
			if ((nRead = raf.read(buffer)) > -1) {

				dos.write(buffer, 0, nRead);
			}
			buffer = null;

		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 分块上传文件(每块大小为1MB)
	 * 
	 * @param fileType
	 *            ，文件类型
	 * @return，上传成功(包括了10，-1)
	 */
	public boolean uploadByPiece(int fileType) {

		int chunks = (int) (totalSize / FILE_CHUNK_LEN);
		if (totalSize % FILE_CHUNK_LEN > 0) {
			chunks++;
		}
		String md5Value = FileDigest.getFileMD5(uploadFile);

		for (int chunk = 0; chunk < chunks; chunk++) {

			int result = uploadByPiece(fileType, chunk, chunks, md5Value);

			switch (result) {
			case 10:
				log.info("重复上传成功");
				return true;
			case -1:
				log.info(String.format(
						"[chunk=%d], [chunks=%d], [md5=%s], 全部上传成功", chunk,
						chunks, md5Value));
				return true;
			case 0:
				log.info(String.format(
						"[chunk=%d], [chunks=%d], [md5=%s], 分块上传成功", chunk,
						chunks, md5Value));
				break;
			default:
				log.error(String.format(
						"[chunk=%d], [chunks=%d], [md5=%s], 分块上传失败[err=%d]",
						chunk, chunks, md5Value, result));
				return false;
			}
		}

		return false;
	}

	/**
	 * 分块上传
	 * 
	 * @param fileType
	 *            ，filetype，文件类型
	 * @param chunk
	 *            ，第几块
	 * @param chunks
	 *            ，总共多少块
	 * @return，返回上传下载接口的errocde值，-1：分块全部上传OK；0，当前块上传成功；10，重复上传
	 */
	private int uploadByPiece(int fileType, int chunk, int chunks,
			String md5Value) {

		String bound = "***" + UUID.randomUUID().toString();
		PostParameter fileParam = new PostParameter(PARAM_FILE, uploadFile,
				bound);
		PostParameter[] paramLst = new PostParameter[] {
				new PostParameter(PARAM_FILEEXT, fileParam.getFileExt(), bound),
				new PostParameter(PARAM_CHUNK, chunk, bound),
				new PostParameter(PARAM_CHUNKS, chunks, bound),
				new PostParameter(PARAM_FILEMD5, md5Value, bound),
				new PostParameter(PARAM_FILETYPE, fileType, bound), fileParam };

		try {
			URL dataUrl = new URL(uploadUrl);
			HttpURLConnection con = (HttpURLConnection) dataUrl
					.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(10 * 1000);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					String.format("multipart/form-data; boundary=%s", bound));
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Accept-Charset", "utf8");
			con.setRequestProperty("User-Agent", "kmjar/1.0.0.1/2/java");

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			log.info("准备分片提交http数据");
			con.connect();
			OutputStream os = con.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			for (PostParameter v : paramLst) {
				String post = "";
				if (v.isFile()) { // 如果是文件，则进行操作
					post = v.toString();
					dos.writeBytes(post);
					writeToStream(v.getFile(), chunk, chunks, dos);
				} else {
					post = v.toString();
					dos.writeBytes(post);
				}

			}
			dos.writeBytes(String.format("\r\n--%s--", bound));
			dos.flush();
			log.info(String.format("提交到上传http之后的状态码[%d][%s]",
					con.getResponseCode(), con.getResponseMessage()));
			dos.close();

			String resp = getRespMessage(con);
			if(resp == null || resp.equals("")){
				scount.incrementAndGet();
				System.err.println("返回消息出错");
			}else{
				System.out.println("返回消息是：" + resp);
			}

			if (parseJson(resp)) {
				return errorCode;
			} else {
				throw new Exception("回包解析出错");
			}

		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
			return -10;
		}
	}

	/**
	 * 上传到数据中心指定文件
	 * 
	 * @param picFileType
	 *            ，数据中心存储的文件类型
	 * @return，上传成功，返回true，反之，返回false
	 */
	public boolean upload(int picFileType) {
		String bound = "***" + UUID.randomUUID().toString();
		PostParameter fileParam = new PostParameter(PARAM_FILE, uploadFile,
				bound);
		PostParameter[] paramLst = new PostParameter[] {
				new PostParameter(PARAM_FILEEXT, fileParam.getFileExt(), bound),
				new PostParameter(PARAM_CHUNK, 0, bound),
				new PostParameter(PARAM_CHUNKS, 1, bound),
				new PostParameter(PARAM_FILEMD5, fileParam.getFileMD5(), bound),
				new PostParameter(PARAM_FILETYPE, picFileType, bound),
				fileParam };

		try {
			URL dataUrl = new URL(uploadUrl);
			HttpURLConnection con = (HttpURLConnection) dataUrl
					.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					String.format("multipart/form-data; boundary=%s", bound));
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Accept-Charset", "utf8");
			con.setRequestProperty("User-Agent", "kmjar/1.0.0.1/2/java");

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			log.info("准备提交http数据");
			con.connect();
			OutputStream os = con.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			for (PostParameter v : paramLst) {
				String post = "";
				if (v.isFile()) { // 如果是文件，则进行操作
					post = v.toString();
					dos.writeBytes(post);
					FileInputStream fis = new FileInputStream(v.getFile());
					byte[] buffer = new byte[1024 * 10];
					while (true) {
						int nRead = fis.read(buffer);
						if (nRead == -1)
							break;

						dos.write(buffer, 0, nRead);
					}
					fis.close();
				} else {
					post = v.toString();
					dos.writeBytes(post);
				}

			}
			dos.writeBytes(String.format("\r\n--%s--", bound));
			dos.flush();
			log.info(String.format("提交到上传http之后的状态码[%d][%s]",
					con.getResponseCode(), con.getResponseMessage()));
			dos.close();

			String resp = getRespMessage(con);
			if (parseJson(resp)) {
				if (errorCode == -1 || errorCode == 10)
					return true;
				else
					return false;
			} else {
				return false;
			}

		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
			return false;
		}
	}

	/**
	 * 如果上传成功，则，返回值大于0，反之为-1
	 * 
	 * @return
	 */
	public int getFileID() {
		return nFileId;
	}

	/**
	 * 上传之后，接口返回的错误描述
	 * 
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * 上传成功后，接口返回的错误代码
	 * 
	 * @return
	 */
	public int getErrorCode() {
		return errorCode;
	}

	

	/**
	 * @return the isRepeatUpload
	 */
	public boolean isRepeatUpload() {
		return isRepeatUpload;
	}

	/**
	 * @param isRepeatUpload
	 *            the isRepeatUpload to set
	 */
	public void setRepeatUpload(boolean isRepeatUpload) {
		this.isRepeatUpload = isRepeatUpload;
	}

	
	public static String readString(InputStream inputStream) throws IOException{
		 try{
			  final Reader reader = new InputStreamReader(inputStream, "UTF-8");
		      int capacity = 4096;
	          final CharArrayBuffer buffer = new CharArrayBuffer(capacity);
	          final char[] tmp = new char[1024];
	          int l;
	          while((l = reader.read(tmp)) != -1) {
	              buffer.append(tmp, 0, l);
	          }
	          return buffer.toString();
		 }finally{
			 inputStream.close();
		 }
	    
	 }
	
	public static void main(String[] args) throws Exception {
		uploadUrl = "http://127.0.0.1:3003/FileService/uploadfile.do";
		int amount = 1;
		for (int i = 0; i < amount; i++) {
			long startime = 0L;
			long endtime = 0L;
			String filename1 = "c:\\Users\\Administrator\\Pictures\\aaa.jpg";
			startime = System.nanoTime();
			UploadFile upload = new UploadFile(filename1);
			// boolean uploadResult = upload.upload(202);
			boolean uploadResult = upload.uploadByPiece(2);
			endtime = System.nanoTime();
			System.out.println("Elapsed Time is "
					+ ((endtime - startime) / 1000000000.0) + " seconds");
			System.out.println(uploadResult);
			//Thread.sleep(1000 * 30);
		}
		System.out.println("失败次数是" + scount + ",成功次数是：" + (amount-scount.intValue()));

	}
}
