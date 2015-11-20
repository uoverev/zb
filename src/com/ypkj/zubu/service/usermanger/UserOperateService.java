package com.ypkj.zubu.service.usermanger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ypkj.zubu.controller.app.FileService;
import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.easemob.httpclient.apidemo.EasemobIMUsers;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.PushMsgType;
import com.ypkj.zubu.util.PushUtil;
import com.ypkj.zubu.util.ResponseUtils;
import com.ypkj.zubu.util.Tools;

@Service("userOperateService")
public class UserOperateService {
	protected Logger logger = org.slf4j.LoggerFactory
			.getLogger(this.getClass());
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	// 200001 用户注册服务
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveUser(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		if (Tools.isEmpty((String) map.get("password"))
				|| Tools.isEmpty(map.get("mobilePhone") + "")
				|| Tools.isEmpty(map.get("msgCode") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		String userName = null;
		try {
			userName = "zb" + Tools.getRandomCode();
			map.put("userName", userName);
			dao.save("UserOperateMapper.save", map);
			if (map.get("topId") != null) {
				// 验证推荐人ID是否正确
				List<String> list = new ArrayList<String>();
				list.add(map.get("topId") + "");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("list", list);
				List<Map<String, Object>> data = (List<Map<String, Object>>) dao
						.findForList("UserLoginMapper.findByUsersByIds", params);
				if (data.isEmpty()) {
					response.put("code", 200);
					response.put("msg", "推荐人ID不正确");
					return response;
				}
				// 给推荐人加5块的优惠券
				dao.update("UserOperateMapper.modifyCoupons", map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		ObjectNode datanode = JsonNodeFactory.instance.objectNode();

		datanode.put("username", "zubu_" + map.get("user_id"));// 生成环信用户名
		datanode.put("password", "zubu_" + map.get("user_id"));// 生成环信密码
																// （默认密码和用户名相同）
		EasemobIMUsers.createNewIMUserSingle(datanode);// 注册单一用户到环信
		Long userid = (Long) map.get("user_id");
		response.put("userid", userid);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 200004 个人资料修改
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateUser(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;
		try {
			count = (Integer) dao.update("UserOperateMapper.modify", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			response.put("code", 500);
			response.put("msg", "用户不存在");
			return response;
		}
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 20005 个人头像修改
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateUserImage(PageData pd,
			MultipartHttpServletRequest request) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("head_portrait") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;
		try {
			request.setAttribute("head_portrait", map.get("head_portrait"));
			String path = FileService.upLoadFile(request);
			String picUrl = path.split(",")[0];
			map.put("head_portrait", picUrl);
			count = (Integer) dao.update("UserOperateMapper.modify", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			response.put("code", 500);
			response.put("msg", "用户不存在");
			return response;
		}
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 20006 相册删除图片
	@SuppressWarnings("unchecked")
	public Map<String, Object> removePics(PageData pd) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		Map<String, Object> response = new HashMap<String, Object>();
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("url") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		List<String> urls = (ArrayList<String>) map.get("url");
		Integer count = 0;
		try {
			String urlstr = "";
			for (String pic : urls) {
				map.put("url", pic);
				count += (Integer) dao.delete("UserOperateMapper.removePics",
						map);
				urlstr += pic + ",";
			}
			// 删除物理图片
			FileService.delete(urlstr);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			return ResponseUtils.setResponse(500, "失败", null);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 20006 相册添加图片
	@SuppressWarnings("unchecked")
	public Map<String, Object> savePics(PageData pd,
			MultipartHttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;
		String urls = FileService.upLoadFile(request);
		String[] picUrl = urls.split(",");

		try {
			for (String url : picUrl) {
				map.put("url", url);
				count += (Integer) dao.save("UserOperateMapper.saveAlbum", map);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			return ResponseUtils.setResponse(500, "失败", null);
		}

		return ResponseUtils.setResponse(100, "成功", null);
	}

	// 20009 兴趣添加
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveInterest(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("xq_id") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;

		try {
			count = (Integer) dao.save("UserOperateMapper.saveInterest", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			return ResponseUtils.setResponse(500, "失败", null);
		}

		return ResponseUtils.setResponse(100, "成功", null);
	}

	// 20010 兴趣删除
	@SuppressWarnings("unchecked")
	public Map<String, Object> removeInterest(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("xq_id") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;

		try {
			count = (Integer) dao.delete("UserOperateMapper.removeInterest",
					map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 20011 职业技能添加 saveProfessionalSkill
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveProfessionalSkill(PageData pd)
			throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("xq_id") + "")
				|| Tools.isEmpty(map.get("job_cl") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;

		try {
			count = (Integer) dao.save(
					"UserOperateMapper.saveProfessionalSkill", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			return ResponseUtils.setResponse(500, "失败", null);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 20020 关注 saveCare
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveCare(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);

		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("fansId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Map<String, Object> isCare = new HashMap<String, Object>();

		Integer count = 0;

		try {
			isCare = (Map<String, Object>) dao.findForObject(
					"UserLoginMapper.findCarePeople", map);
			if (isCare != null) {
				response.put("code", 500);
				response.put("msg", "已经关注");
				return response;
			}
			count = (Integer) dao.save("UserOperateMapper.saveCare", map);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		// TODO 推送
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("pushMsgType", PushMsgType.CATE_SUCCESS_001);
		try {
			PushUtil.SendPushToOnePub((String) map.get("pushid"), "有人关注了你哦", extras);
		} catch (Exception e) {
			logger.error("推送失败");
		}
		return response;
	}

	// 200120 关注 cancelCare
	@SuppressWarnings("unchecked")
	public Map<String, Object> cancelCare(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);

		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("fansId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;
		try {

			count = (Integer) dao.delete("UserOperateMapper.cancelCare", map);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 添加用户反馈意见
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveFeedBack(PageData pd) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);

		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("content") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Integer count = 0;

		try {
			count = (Integer) dao.save("UserOperateMapper.saveFeedBack", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			response.put("code", 500);
			response.put("msg", "添加失败异常");
			return response;
		}
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 修改密码
	@SuppressWarnings("unchecked")
	public Map<String, Object> updatePassword(PageData pd)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("oldPassword") + "")
				|| Tools.isEmpty(map.get("newPassword") + "")
				|| Tools.isEmpty(map.get("type") + "")
				|| Tools.isEmpty(map.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}

		Integer count = 0;
		List<Map<String, Object>> userList = null;

		try {
			if ("0".equals((String) map.get("type"))) {
				// 验证原密码是否正确
				map.put("password", map.get("oldPassword"));
				map.put("loginPassword", map.get("newPassword"));
			} else {
				// 修改支付密码
				map.put("paypassword", map.get("oldPassword"));
				map.put("payPassword", map.get("newPassword"));
			}
			userList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findtUserByExmaple", map);
			if (userList.isEmpty()) {
				response.put("code", 500);
				response.put("msg", "原密码错误");
				return response;
			}
			count = (Integer) dao.update("UserOperateMapper.modify", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		if (count == 0) {
			response.put("code", 500);
			response.put("msg", "修改密码失败");
			return response;
		}
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> resetPassword(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if ((Tools.isEmpty(map.get("mobilePhone") + "") && Tools.isEmpty(map
				.get("userId") + ""))
				|| (Tools.isEmpty(map.get("loginPassword") + "") && Tools
						.isEmpty(map.get("payPassword") + ""))) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}

		try {
			// 判读用户的手机号是否已经注册
			List<Map<String, Object>> phoneList = (List<Map<String, Object>>) dao
					.findForList("UserLoginMapper.findtUserByExmaple", map);
			if (phoneList.isEmpty()) {
				response.put("code", "102");
				response.put("msg", "手机号未注册");
				return response;
			}
			dao.update("UserOperateMapper.modify", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveID(PageData pd,
			MultipartHttpServletRequest request) throws JsonParseException,
			JsonMappingException, IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("name") + "")
				|| Tools.isEmpty(map.get("cardNo") + "")
				|| Tools.isEmpty(map.get("addr") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		// 上传图片到服务器
		String urls = FileService.upLoadFile(request);
		String[] picUrl = urls.split(",");
		map.put("fullCard", picUrl[0]);
		map.put("positiveCard", picUrl[1]);
		map.put("negativeCard", picUrl[2]);
		map.put("state", "3");
		try {
			dao.save("UserOperateMapper.saveID", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveProfessionalInfo(PageData pd,
			MultipartHttpServletRequest request) throws JsonParseException,
			JsonMappingException, IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("userId") + "")
				|| Tools.isEmpty(map.get("name") + "")
				|| Tools.isEmpty(map.get("taskTypeId") + "")
				|| Tools.isEmpty(map.get("professionalName") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		// 上传图片到服务器
		String urls = FileService.upLoadFile(request);
		String[] picUrl = urls.split(",");
		map.put("array", picUrl);
		map.put("state", "3");
		try {
			dao.save("UserOperateMapper.saveProfessionalInfo", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}
	
}
