package com.ypkj.zubu.controller.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ypkj.zubu.util.DateUtil;

public class CopyOfFileService {
	/** 上传相册的请求路径 */
	private static final String SAVE_REQUEST = "http://192.168.1.152:8099/FileServer/album/save.do";
	/** 删除图像的请求路径 */
	private static final String DELETE_REQUEST = "http://192.168.1.152:8099/FileServer/album/delete.do";
	/** 个人头像 */
	private static final String PERSONAL_PATH = "http://192.168.1.152:8099/FileServer/uploadFiles/personalImgs/";
	/** 相册 */
	private static final String ALBUM_PATH = "http://192.168.1.152:8099/FileServer/uploadFiles/uploadImgs/";

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		long begin=System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {

			list.add(System.currentTimeMillis()+"");
		}
		System.out.println(list.size());
		for (String string : list) {
			set.add(string);
		}
		System.out.println(set.size());
		long end=System.currentTimeMillis();
		System.out.println(end-begin);
	}

	// 将图片保存到图片服务器返回文件名
	public static String upLoadFile(MultipartHttpServletRequest request)
			throws IllegalStateException, IOException {
		String path = "";
		String filePath = null;
		HttpClient client = new HttpClient();
		PostMethod mPost = new PostMethod(SAVE_REQUEST);
		List<Part> parts = new ArrayList<Part>();
		mPost.getParams().setSoTimeout(500000);
		if (request.getAttribute("head_portrait") != null) {
			filePath = PERSONAL_PATH;
			String imgUrl = (String) request.getAttribute("head_portrait");
			String[] strs = imgUrl.split("/");

			mPost.addParameter("originalPic", strs[strs.length - 1]);
		} else {
			filePath = ALBUM_PATH;
		}
		mPost.setRequestHeader("content-type",
				"multipart/form-data; boundary=,");
		Iterator<String> iter = request.getFileNames();
		while (iter.hasNext()) {
			MultipartFile file = request.getFile(iter.next());
			if (file != null) {
				String fileName = DateUtil.getFileTime()
						+ file.getOriginalFilename();
				File f1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fileName);
				file.transferTo(f1);
				FilePart part1 = new FilePart("file1", f1);
				parts.add(part1);
				path += filePath + fileName + ",";
			}
		}
		mPost.setRequestEntity(new MultipartRequestEntity(parts
				.toArray(new Part[0]), new HttpMethodParams()));
		int response = client.executeMethod(mPost);
		System.out.println(response);
		mPost.releaseConnection();

		
		
		
		return path;
	}

	public static void delete(String url) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(DELETE_REQUEST);
		method.setParameter("url", url);
		int response = client.executeMethod(method);
		System.out.println(response);
		method.releaseConnection();
	}
}