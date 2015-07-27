/**
 * 名称: UploadFileController.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月6日 下午6:58:15
 * @since  2014年8月6日
 * @author malb
 */ 
package com.shiyi.fileservice.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.shiyi.fileservice.file.FileUtil;
import com.shiyi.fileservice.file.upload.DownloadFile;
import com.shiyi.fileservice.file.upload.HttpFilePostParams;
import com.shiyi.fileservice.file.upload.UploadFile;
import com.shiyi.fileservice.file.upload.error.UploadFileException;
import com.shiyi.fileservice.util.CommonUtils;
import com.shiyi.fileservice.util.HttpUtils;


/**
 * @author malb
 *
 */

@Controller
public class UploadFileController {

	private final Logger logger = LoggerFactory.getLogger("UploadFileController");
	

	
	
	/**
	 * 输出 资源网址
	 * @param response
	 * @param url
	 */
	private void writeReponseUrl(HttpServletResponse response, String url) {
		writeResponse(response, url.getBytes(), -2);
	}
	
	
	private boolean writeResponse(HttpServletResponse response, String filePath, int fileid) {
	
		OutputStream outputStream = null;
		FileInputStream fis = null; 
		
		try {
			fis = new FileInputStream(filePath);
			outputStream = new BufferedOutputStream(response.getOutputStream());
			
			byte[] bytes = new byte[1024 * 8];
			int len;
			while ((len = fis.read(bytes)) > 0) {
				
				outputStream.write(bytes, 0, len);
				outputStream.flush();
			}
			bytes = null;
			
		    return true;
		    
		} catch (Exception e) {
			logger.error(String.format("writeResponse file 失败%s[fileid=%d]", e.getMessage(), fileid));
			return false;
			
		} finally{
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(fis);
		}
		
	}
	
	/**
	 * 输出文件流
	 * @param response
	 * @param file
	 */
	private boolean writeResponse(HttpServletResponse response, byte[] byteStream, int fileid) {
			
		OutputStream outputStream = null;
		
		try {
			outputStream = new BufferedOutputStream(response.getOutputStream());
		    outputStream.write(byteStream);  
		    outputStream.flush();  
		    byteStream = null;
		    return true;
		    
		} catch (Exception e) {
			logger.error(String.format("writeResponse下载失败%s[fileid=%d]", e.getMessage(), fileid));
			return false;
			
		} finally{
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	
	private void send404Error(HttpServletResponse response, int fileId) {
		response.addHeader("ERROR", String.format("errorcode: no file fileid=%d", fileId));
		response.setContentLength(0);
		try {
			response.sendError(404);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@RequestMapping(value="downloadstorefile", method=RequestMethod.GET)
	@ResponseBody
	public void downloadfile(@RequestParam("fileid") int fileid,
			@RequestParam(value="type", required=false, defaultValue="0") String typeid,
			@RequestParam(value="s", required=false) Integer s,
			HttpServletRequest request, HttpServletResponse response) {
				
		DownloadFile downFile = new DownloadFile(fileid, CommonUtils.getRealPath(request) + "cache",false);
		response.reset();
		if (downFile.getDownLoadParam()) {
			
			//当typeid=1时表示输出地址，否则，输出文件流
			if (CommonUtils.isGetUrl(typeid)) {
				writeReponseUrl(response, downFile.getFileUrl());
				return ;
			}

			//如果是七牛存储，并且需要从七牛下载，则直接重定向到七牛存储
			if (CommonUtils.isDownFromQiniu(s, downFile.getQiniuUrl())) {
				try {
					response.sendRedirect(downFile.getQiniuUrl());
				} catch (Exception e) {					
				}
				return ;
			}
			
			//如果是需要重定向到本地缓存，则先下载到本地缓存，再进行重定向到本地缓存中
			if (CommonUtils.isNeedLocation(downFile.getFileExt())) {
				try {
					String cacheFile = downFile.getCaheFilePath();
					
					if (StringUtils.isEmpty(cacheFile)) {
						send404Error(response, fileid);
					}  else {
						response.sendRedirect(downFile.getWwwCacheUrl());
					}
					
					downFile = null;
				} catch (Exception e) {					
				}
				return ;
			}

			String fileName = downFile.getFileName();
			
			//从存储中下载文件流
			String cacheFile = downFile.getCaheFilePath();
			
			if (StringUtils.isEmpty(cacheFile)) {
				
				send404Error(response, fileid);
				return ;
				
			} 			
			downFile = null;
			
			response.setContentType("application/octet-stream;charset=UTF-8");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    response.setContentLength((int) FileUtil.getFileSize(cacheFile));  
		      
		    //输出文件
		    writeResponse(response, cacheFile, fileid);
		    
		} else {  //输出404错误。
			send404Error(response, fileid);
			
		}
		
	}
	

	/**
	 * 下载接口downfile/{fileid}
	 * @param fileid
	 * @return
	 */
	@RequestMapping(value="downloadstorefile.do/{fileid}", method=RequestMethod.GET)
	public ModelAndView downloadfileredirect(@PathVariable int fileid) {
		return new ModelAndView(String.format("redirect:/downloadstorefile?fileid=%d", fileid));  
		
	}
	

	/**
	 * 删除接口
	 * @param fileid
	 * @param request
	 */
	
/*	@RequestMapping(value="deletefile", method=RequestMethod.GET)
	public ModelAndView deletefile(@RequestParam("fileid") int fileid,
			HttpServletRequest request) {	
		if (fileid < 0) {
			return new ModelAndView("jsp/resultjson", "result", new DeleteError().toString());
		}
		
		DeleteFile delete = new DeleteFile(fileid, this.getRealPath(request));		
		return new ModelAndView("jsp/resultjson", "result", 
				 delete.doDelete() ? new DeleteError().toString() : new DeleteFail().toString());

	}*/	
	
	
	
	
	
	
	@RequestMapping(value="uploadfile", method=RequestMethod.POST)
	public ModelAndView  uploadfile(HttpServletRequest request, 
			@RequestParam(value= "fileext", required = false) String fileext, 
			@RequestParam("chunk") int chunk, @RequestParam("chunks") int chunks,
			@RequestParam("file") MultipartFile file, @RequestParam("md5value") String md5value,			
			@RequestParam(value= "filename", required = false) String filename,
			@RequestParam(value= "relfilename", required = false) String relfilename,
			@RequestParam(value= "file_type", required = false) Integer file_type, 
			@RequestHeader("User-Agent") String agentvalue) {
			
		HttpFilePostParams postParams = new HttpFilePostParams();
		postParams.setChunks(chunks);
		postParams.setChunk(chunk);
		
		filename = StringUtils.isEmpty(filename) ? 
				(StringUtils.isEmpty(relfilename) ? file.getOriginalFilename() :  relfilename) 
				: filename ;
				
		if (StringUtils.isEmpty(fileext)) {
			fileext = FilenameUtils.getExtension(filename);
		}				
		postParams.setFileExt(fileext);
		
		postParams.setFileName(StringUtils.abbreviate(filename, 45));
		
		if ( null == file_type) {
			file_type = -1;
		}
		postParams.setFileType(file_type);
		postParams.setMd5Value(md5value);
		
		postParams.setCacheDir(CommonUtils.getMd5CacheDir(md5value, request));
		postParams.setFile(file);
		try {
			postParams.setUserAgent(agentvalue);
		} catch (UploadFileException e1) {
			return new ModelAndView("jsp/resultjson", "result", e1.toString());	
			
		}

		UploadFile upload = new UploadFile(postParams);
		String result = upload.upload();
		postParams = null;
		upload = null;
		logger.info("upload result = " + result);
		return new ModelAndView("jsp/resultjson", "result", result);
		
	}
	
	@RequestMapping(value="downloadfileIntranet", method=RequestMethod.GET)
	public void downloadfileIntranet(@RequestParam("fileid") int fileid,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		DownloadFile downFile = new DownloadFile(fileid, CommonUtils.getRealPath(request) + "cache",true);
		response.reset();
		if (downFile.getDownLoadParam()) {		
			//如果是需要重定向到本地缓存，则先下载到本地缓存，再进行重定向到本地缓存中
			if (CommonUtils.isNeedLocation(downFile.getFileExt())) {
				try {
					String cacheFile = downFile.getCaheFilePath();
					
					if (StringUtils.isEmpty(cacheFile)) {
						forward404(request, response);
						return;
					}  else {
						response.sendRedirect(downFile.getWwwCacheUrl());
					}
					
					downFile = null;
				} catch (Exception e) {					
				}
				return ;
			}

			String fileName = downFile.getFileName();
			
			//从存储中下载文件流
			String cacheFile = downFile.getCaheFilePath();
			
			if (StringUtils.isEmpty(cacheFile)) {
				
				forward404(request, response);
				return ;
				
			} 			
			downFile = null;
			
			response.setContentType("application/octet-stream;charset=UTF-8");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    response.setContentLength((int) FileUtil.getFileSize(cacheFile));  
		      
		    //输出文件
		    writeResponse(response, cacheFile, fileid);
		    
		} else {  //输出404错误。
			forward404(request, response);
			return;
		}
		
	}
	@RequestMapping(value="404", method=RequestMethod.GET)
	public String filenotExist(){
		return "404";
	}
	
	/**
	 * foward到404d页面
	 * 
	 * @author deng
	 * @throws IOException
	 * @throws ServletException
	 */
	private void forward404(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("404").forward(request, response);
	}
}
