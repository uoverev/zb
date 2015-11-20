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

import com.ypkj.zubu.controller.app.SendSMS;
import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.LatLonUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.Tools;

@Service("userLoginService")
public class UserLoginService {
	protected Logger logger = org.slf4j.LoggerFactory
			.getLogger(this.getClass());
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	// 登录
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUser(PageData pd) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(pd.getString("DATA"),
				Map.class);
		// 验证参数
		if (Tools.isEmpty(map.get("mobilePhone") + "")
				|| Tools.isEmpty(map.get("password") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		Map<String, Object> obj = null;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mobilePhone", map.get("mobilePhone"));
			obj = (Map<String, Object>) dao.findForObject(
					"UserLoginMapper.findByUser", param);
			if (obj == null) {
				response.put("code", "101");
				response.put("msg", "用户不存在请先注册");
				return response;
			}
			if (!((String) map.get("password")).equals((String) obj
					.get("loginPassword"))) {
				response.put("code", "102");
				response.put("msg", "密码不正确请重新输入");
				return response;
			}
			if (obj.get("province") == null && obj.get("city") == null
					&& obj.get("area") == null) {
				if (!Tools.isEmpty(map.get("province") + "")
						|| (!Tools.isEmpty(map.get("city") + ""))
						|| (!Tools.isEmpty(map.get("area") + ""))) {
					dao.update("UserOperateMapper.modify", map);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("result", obj);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 获取验证码
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMsgCode(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty((String) params.get("mobilePhone"))) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		if (params.get("register") != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mobilePhone", params.get("mobilePhone"));
			// 判读用户的手机号是否已经注册
			List<Map<String, Object>> phoneList = (List<Map<String, Object>>) dao
					.findForList("UserLoginMapper.findtUserByExmaple", map);
			if (!phoneList.isEmpty()) {
				response.put("code", "102");
				response.put("msg", "手机号已被注册");
				return response;
			}
		}
		response.put("msgCode",
				SendSMS.sendMsg((String) params.get("mobilePhone")));
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 首页查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserByExmaple(PageData pd) throws Exception {
		logger.info("进入getUserByExmaple方法，入参：pd:{}", pd);
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("lon") + "")
				|| Tools.isEmpty(params.get("lat") + "")
				|| Tools.isEmpty(params.get("id") + "")
				) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		// 根据当前用户经度纬度计算范围【5000m内】
		Double lat = Double.parseDouble((String) params.get("lat"));
		Double lon = Double.parseDouble((String) params.get("lon"));
		double[] arr = LatLonUtil.getAround(lat, lon, 10000);
		params.put("minlat", arr[1]);
		params.put("minlnt", arr[0]);
		params.put("maxlat", arr[3]);// 经度
		params.put("maxlnt", arr[2]);// 纬度

		List<Map<String, Object>> userList = null;
		try {
			// 如果flag为1更新当前用户的经度和纬度
			if("1".equals(params.get("flag")+"")){
				params.put("userId", params.get("id"));
				dao.update("UserOperateMapper.modify", params);	
			}
			params.put("userId", null);
			userList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findtUserByExmaple", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("result", userList);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100008:个人资料查询接口
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserById(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> userList = null;
		List<Map<String, Object>> professionalList = null;
		List<Map<String, Object>> albumList = null;
		String flag = null;
		try {
			userList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findtUserByExmaple", params);
			if (userList.isEmpty()) {
				response.put("code", 500);
				response.put("msg", "帐号不存在");
				return response;
			}
			Map<String, Object> personal = userList.get(0);
			// 职业技能
			professionalList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findProfessionalSkill", params);
			// 照片墙
			albumList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findUserAlbum", params);
			// 查询是否关注
			if (!Tools.isEmpty(params.get("fansId") + "")) {
				Map<String, Object> care = (Map<String, Object>) dao
						.findForObject("UserLoginMapper.findCarePeople", params);
				//添加访客到 访客记录表中
				Map<String, Object> vistor = new HashMap<String, Object>();
				vistor.put("fkuserid", params.get("fansId"));
				vistor.put("userid", params.get("userId"));
				dao.save("visitorIMMapper.addvsrecord", vistor);
				if (care != null) {
					flag = "1";
				} else {
					flag = "0";
				}
				personal.put("isCare", flag);
			}
			response.put("result", personal);
			response.put("professional", professionalList);
			response.put("album", albumList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100012:兴趣查询接口
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInterestByMap(PageData pd)
			throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> existingInterests = null;
		List<Map<String, Object>> optionalInterests = null;
		try {
			// 用户已选的兴趣
			existingInterests = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findByUserInterest", params);
			// 用户未选的兴趣
			optionalInterests = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findUserOptionalInterest", params);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("existingInterests", existingInterests);
		response.put("optionalInterests", optionalInterests);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100013:职业技能查询接口
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserSKills(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("types") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> data = null;
		try {
			if ("1".equals((String) params.get("types"))) {
				data = (List<Map<String, Object>>) dao.findForList(
						"UserLoginMapper.findProfessionalSkill", params);
			} else {
				data = (List<Map<String, Object>>) dao.findForList(
						"UserLoginMapper.findSkill", null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("result", data);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100014:累计收入查询接口
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserIncomings(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> data = null;
		try {
			data = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findByUserIncomings", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("code", 100);
		response.put("msg", "成功");
		response.put("result", data);
		return response;
	}

	// 100015:全部消费查询接口
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounsumeInfo(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 200);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> data = null;
		try {
			data = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findCounsumeInfo", params);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		response.put("code", 100);
		response.put("msg", "成功");
		response.put("result", data);
		return response;
	}

	// 100018:查找用户相册
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserAblum(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> data = null;
		try {
			data = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findUserAlbum", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("result", data);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100019:得到关注的人
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarePeople(PageData pd, int type)
			throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		Object param = null;
		if (type == 1) {
			param = params.get("fansId");
		} else {
			param = params.get("userId");
		}
		// 验证参数
		if (Tools.isEmpty(param + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> data = null;
		try {
			data = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findCarePeople", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("result", data);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100020:得到 总资产
	@SuppressWarnings("unchecked")
	public Map<String, Object> getTotalMoneys(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Map<String, Object> data = null;
		try {
			data = (Map<String, Object>) dao.findForObject(
					"UserLoginMapper.findTotalMoneys", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		response.put("result", data);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 100022:根据多个手机号查询用户信息
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserByIds(PageData pd) throws IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		List<String> array = (ArrayList<String>) params.get("userId");
		params.put("list", array);
		List<Map<String, Object>> data = null;
		try {
			data = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findByUsersByIds", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.put("code", 200);
			response.put("msg", "服务器异常");
			return response;
		}
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		// 按传入的手机号顺序排序
		for (String str : array) {
			for (Map<String, Object> map : data) {
				if (str.equals(map.get("user_id") + "")) {
					maps.add(map);
					break;
				}
			}
		}
		response.put("result", maps);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	// 查询图像个数
	@SuppressWarnings("unchecked")
	public Object getAblumNumber(PageData pd) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Map<String, Object> data = null;
		try {
			data = (Map<String, Object>) dao.findForObject(
					"UserLoginMapper.findAlbumNumber", params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		response.put("result", data);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getDiscountAndMoney(PageData pd)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> userList = null;
		try {
			userList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findtUserByExmaple", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		Map<String, Object> data = userList.get(0);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moneys", data.get("moneys"));
		result.put("discountMoney", data.get("yhj"));
		response.put("result", result);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getIDInfo(PageData pd)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		Map<String, Object> user = null;
		try {
			user = (Map<String, Object>) dao.findForObject(
					"UserLoginMapper.findIDInfo", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		Map<String, Object> state = new HashMap<String, Object>();
		if (user == null) {
			state.put("state", "-1");
			response.put("result", state);
			response.put("code", 100);
			response.put("msg", "未提交资料");
			return response;
		}
		state.put("state", user.get("state"));
		response.put("result", state);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getSkillCheck(PageData pd)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> response = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> params = objectMapper.readValue(
				pd.getString("DATA"), Map.class);
		// 验证参数
		if (Tools.isEmpty(params.get("userId") + "")

		|| Tools.isEmpty(params.get("jobId") + "")) {
			response.put("code", 500);
			response.put("msg", "参数错误");
			return response;
		}
		List<Map<String, Object>> userList = null;
		try {
			userList = (List<Map<String, Object>>) dao.findForList(
					"UserLoginMapper.findSkillCheck", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		Map<String, Object> state = new HashMap<String, Object>();

		if (userList.isEmpty()) {
			state.put("state", "-1");
			response.put("result", state);
			response.put("code", 100);
			response.put("msg", "成功");
			return response;
		}
		Map<String, Object> data = userList.get(0);
		state.put("state", data.get("state"));
		response.put("result", state);
		response.put("code", 100);
		response.put("msg", "成功");
		return response;
	}

}
