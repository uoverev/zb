package com.ypkj.zubu.controller.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ypkj.zubu.util.DateUtil;

@SuppressWarnings("deprecation")
public class FileService
{
	/** 上传相册的请求路径 */
	private static final String SAVE_REQUEST = "http://120.55.125.76:8099/FileServer/album/save.do";
	/** 删除图像的请求路径 */
	private static final String DELETE_REQUEST = "http://120.55.125.76:8099/FileServer/album/delete.do";
	/** 个人头像 */
	private static final String PERSONAL_PATH = "http://120.55.125.76:8099/FileServer/uploadFiles/personalImgs/";
	/** 相册 */
	private static final String ALBUM_PATH = "http://120.55.125.76:8099/FileServer/uploadFiles/uploadImgs/";
	/** 用户邀请二维码 */
	private static final String QRCODE_PATH = "http://120.55.125.76:8099/FileServer/uploadFiles/uploadImgs/";
	public static void main(String[] args)
	{
		// upLoadFile();
	}

	// 将图片保存到图片服务器返回文件名
	public static String upLoadFile(MultipartHttpServletRequest request) throws IllegalStateException, IOException
	{
		List<File>fielDlete=new ArrayList<File>();
		String path = "";
		String filePath = null;
		HttpClient client = new HttpClient();
		MultipartPostMethod mPost = new MultipartPostMethod(SAVE_REQUEST);
		mPost.getParams().setSoTimeout(500000);
		if (request.getAttribute("head_portrait") != null)
		{
			filePath = PERSONAL_PATH;
			String imgUrl = (String) request.getAttribute("head_portrait");
			String[] strs = imgUrl.split("/");

			mPost.addParameter("originalPic", strs[strs.length - 1]);
		} else
		{
			filePath = ALBUM_PATH;
		}
		mPost.setRequestHeader("content-type", "multipart/form-data; boundary=,");
		Iterator<String> iter = request.getFileNames();
		int i=0;
		while (iter.hasNext())
		{
			i++;
			MultipartFile file = request.getFile(iter.next());
			if (file != null)
			{
				String fileName = DateUtil.getFileTime()+(i+"") + ".png";
				File f1 = new File(request.getSession().getServletContext().getRealPath("/") + fileName);
				fielDlete.add(f1);
				file.transferTo(f1);
				mPost.addParameter(f1.getName(), f1.getName(), f1);
				FilePart part1 = new FilePart("file1", f1);
				mPost.addPart(part1);
				path += filePath + fileName + ",";
			}
		}
		int response = client.executeMethod(mPost);
		System.out.println(response);
		mPost.releaseConnection();
        //删除缓存
		if(!fielDlete.isEmpty()){
			for (File file : fielDlete) {
				file.delete();
			}
		}
		return path;
	}

	// 将图片保存到图片服务器返回文件名
	public static String upLoadQrCodeFile(String userId,String imagePath) throws IllegalStateException, IOException
	{
		String path = "";
		String filePath = QRCODE_PATH;
		HttpClient client = new HttpClient();
		MultipartPostMethod mPost = new MultipartPostMethod(SAVE_REQUEST);
		mPost.getParams().setSoTimeout(500000);
		mPost.setRequestHeader("content-type", "multipart/form-data; boundary=,");
		String fileName = "zubu"+userId+".gif";
		File file = new File(imagePath);//指定输出路径
		mPost.addParameter(file.getName(), file.getName(), file);
		FilePart part1 = new FilePart("file1", file);
		mPost.addPart(part1);
		path += filePath + fileName;
		int response = client.executeMethod(mPost);
		System.out.println(response);
		mPost.releaseConnection();
		if(response==200){
			File outputFile = new File(imagePath);//指定输出路径
			outputFile.delete();
			return path;
		}else{
			return "";
		}
	}

	public static void delete(String url) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(DELETE_REQUEST);
		method.setParameter("url", url);
		int response = client.executeMethod(method);
		System.out.println(response);
		method.releaseConnection();
	}
}