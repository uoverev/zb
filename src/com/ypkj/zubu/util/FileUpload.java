package com.ypkj.zubu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 上传文件
 * 创建人：FH 创建时间：2014年12月23日
 * @version
 */
public class FileUpload {

	/**
	 * @param file 			//文件对象
	 * @param filePath		//上传路径
	 * @param fileName		//文件名
	 * @return  文件名
	 */
	public static String fileUp(MultipartFile file, String filePath, String fileName){
		String extName = ""; // 扩展名格式：
		try {
			if (file.getOriginalFilename().lastIndexOf(".") >= 0){
				extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			copyFile(file.getInputStream(), filePath, fileName+extName).replaceAll("-", "");
		} catch (IOException e) {
			System.out.println(e);
		}
		return fileName+extName;
	}
	
	/**
	 * 写文件到当前目录的upload目录中
	 * 
	 * @param in
	 * @param fileName
	 * @throws IOException
	 */
	private static String copyFile(InputStream in, String dir, String realName)
			throws IOException {
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		FileUtils.copyInputStreamToFile(in, file);
		return realName;
	}
	
	/**
	 * 实现文件的上传
	 * @param file
	 * @param path
	 * @author  徐作念  2015-04-29 14:05:24 下午
	 */
	public static String Upload(CommonsMultipartFile file,HttpServletRequest request){
		String path = request.getContextPath()+"/uploadFiles/uploadImgs/"+new Date().getTime()+file.getOriginalFilename();
		try {
			FileOutputStream os = new FileOutputStream(request.getRealPath("/")+path);
			InputStream in = file.getInputStream();
			int b = 0;
			while ((b = in.read()) != -1) {
				os.write(b);
			}
			os.flush();
			os.close();
			in.close();
			os.close();
			path = Tools.readTxtFile(Const.IMAGEURL)+path;
		} catch (Exception e) {
			path="";
			e.printStackTrace();
		}
		return path;
	}
	
	/**
	 * 实现文件上传
	 * @param file
	 * @param path
	 * @author  徐作念  2015-04-29 14:05:24 下午
	 */
	public static String AnyUpload( CommonsMultipartResolver multipartResolver, HttpServletRequest request){
		String path="";
		try {
			if(multipartResolver.isMultipart(request)){  
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
	            Iterator<String> iter = multiRequest.getFileNames();  
	            while(iter.hasNext()){  
	                MultipartFile file = multiRequest.getFile(iter.next());  
	                if(file != null){
	                    String myFileName = file.getOriginalFilename();  
	                    if(myFileName.trim() !=""){  
	                        String filepath = "uploadFiles/uploadImgs/"+new Date().getTime()+myFileName;  
	                        File localFile = new File(request.getRealPath("/")+filepath);  
	                        file.transferTo(localFile);
	                        path+=Tools.readTxtFile(Const.IMAGEURL)+filepath+",";
	                    }  
	                }  
	            }  
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path==""?"":path.substring(0, path.length()-1);
	}
	
}
