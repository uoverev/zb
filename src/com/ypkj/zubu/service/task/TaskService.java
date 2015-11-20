package com.ypkj.zubu.service.task;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ypkj.zubu.controller.app.FileService;
import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.dao.TaskDao;
import com.ypkj.zubu.entity.Page;
import com.ypkj.zubu.util.Amount;
import com.ypkj.zubu.util.DateUtil;
import com.ypkj.zubu.util.Logger;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.OrderIDUtil;
import com.ypkj.zubu.util.OrderStateUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.PushMsgType;
import com.ypkj.zubu.util.PushUtil;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;
import com.ypkj.zubu.util.TaskUtilCode;
import com.ypkj.zubu.util.UuidUtil;

@Service("taskService")
public class TaskService
{

	@Resource(name = "taskDao")
	private TaskDao taskDao;
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 发布任务
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object saveTask(PageData pd) throws Exception
	{
		logger.info("发布任务开始!");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());

		int businessType = Integer.parseInt(map2.get("businessType").toString());
		if (businessType == TaskUtilCode.CATEGORY_SELL_1)
		{
			map2.put("taskState", TaskUtilCode.TASK_GRABING_2);
		} else
		{
			map2.put("taskState", TaskUtilCode.TASK_WAIT_PAY_1);
		}
		map2.put("numbers", 0);
		map2.put("grabSingle", 0);
		int num = (Integer) dao.save("TaskMapper.save", map2);
		String lat = map2.get("lat").toString();
		String lng = map2.get("lng").toString();
		int task_id = Integer.parseInt(map2.get("task_id").toString());
		map.put("task_id", task_id);
		if (businessType == TaskUtilCode.CATEGORY_BUY_0 && num > 0)
		{
			Map<String, Object> orderData = new HashMap<String, Object>();
			String order_id = OrderIDUtil.getOrderNum();// 生成订单号
			System.err.println(order_id);
			orderData.put("order_id", order_id);
			orderData.put("task_id", task_id);
			orderData.put("order_type", "0");
			orderData.put("moneys", map2.get("moneys"));
			orderData.put("payState", OrderStateUtil.ORDER_WAIT_PAY_1);
			orderData.put("payuserId", map2.get("userid"));
			orderData.put("coupons", 0);
			num = (Integer) dao.save("OrderMapper.save", orderData);
			if (num > 0)
			{
				map.put("order_id", order_id);
			}
			map.put("moneys", map2.get("moneys"));
		}

		if (num > 0)
		{

			try
			{
				if (businessType == TaskUtilCode.CATEGORY_SELL_1)
				{
					map.put("push_num", 0);
					PageData pd2 = new PageData();
					pd2.put("lat", lat);
					pd2.put("lng", lng);
					pd2.put("userid", map2.get("userid"));
					List<String> listPushs = (List<String>) dao.findForList("TaskMapper.findNearPushIds", pd2);
					if (listPushs != null && listPushs.size() > 0)
					{
						PushUtil.SendPushToMany(listPushs, "有跑客发布了一条卖时间的任务!", task_id + "", PushMsgType.NEAR__12);
						map.put("push_num", listPushs.size());
					}
				}
			} catch (Exception e)
			{
				logger.error("发布任务推送失败!",e);
				e.printStackTrace();
			}
			logger.info("发布任务成功!");
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "发布成功", map);
		} else
		{
			logger.info("发布任务失败!");
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "发布失败", map);
		}
	}

	/**
	 * 跑客抢单/取消抢单
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object saveRobOrder(PageData pd) throws Exception
	{
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "task_id", "evaluationPeopleId", "types" };
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		// 0：取消抢单 1：抢单
		int type = Integer.parseInt(map2.get("types").toString());
		String evaluationPeopleId = map2.get("evaluationPeopleId").toString();
		String task_id = map2.get("task_id").toString();
		PageData pd4 = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		if (pd4 != null)
		{
			String userid = pd4.get("userid").toString();
			if (userid.equals(evaluationPeopleId))
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "自己不能抢单", null);
			}
			int taskState = Integer.parseInt(pd4.get("taskState").toString());
			if (taskState == TaskUtilCode.TASK_CANCEL_5)
			{
				return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "任务已被取消", null);
			}
			if (taskState != TaskUtilCode.TASK_GRABING_2)
			{
				return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "该任务已结束抢单", null);
			}
		}
		PageData pd2 = new PageData();
		pd2.put("user_id", evaluationPeopleId);
		pd2.put("task_id", task_id);
		PageData pd3 = (PageData) dao.findForObject("TaskMapper.findRobOrder", pd2);
		int size = pd3 != null ? 1 : 0;
		int num = 0;
		String registrationId = null;
		if (type == TaskUtilCode.GRAB_1)
		{
			if (size > 0)
			{
				if (Integer.parseInt(pd3.get("grabSingleState").toString()) == TaskUtilCode.GRAB_1)
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您已经抢过单了!", null);
				}
				pd3.put("createTime", new Date());
				pd3.put("grabSingleState", TaskUtilCode.GRAB_1);
				num = (Integer) dao.update("TaskMapper.editRobOrder", pd3);
			} else
			{
				pd3 = new PageData();
				pd3.put("user_id", evaluationPeopleId);
				pd3.put("task_id", task_id);
				pd3.put("grabSingleState", TaskUtilCode.GRAB_1);
				num = (Integer) dao.save("TaskMapper.saveRobOrder", pd3);
			}
		} else
		{
			pd3.put("cancelTime", new Date());
			pd3.put("grabSingleState", TaskUtilCode.GRAB_CANCEL_0);
			num = (Integer) dao.update("TaskMapper.editRobOrder", pd3);
		}
		String cancel = "";
		if (type == TaskUtilCode.GRAB_CANCEL_0)
		{
			cancel = "取消";
		}
		if (num > 0)
		{
			try
			{
				if (type == 1)
				{
					PageData upd =new PageData();
					upd.put("user_id", pd4.get("userid"));
					PageData pdPush = (PageData) dao.findForObject("TaskMapper.findPushIdsByUserid", upd);
					if (pdPush != null)
					{
						registrationId = pdPush.get("pushId").toString();
					}
					if (registrationId != null && !"".equals(registrationId))
					{
						PushUtil.SendPushToOnePub(registrationId, "有跑客抢单!",
								ResultUtil.getExtras(PushMsgType.RELEASE_TASK_200, task_id + "", null, evaluationPeopleId));
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, cancel + "抢单成功", null);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, cancel + "抢单失败", null);
		}
	}

	/**
	 * 确认选择跑客
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Object updateConfirmSelect(PageData pd) 
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = null;
		try
		{
			map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		
		// 检查参数是否合法
		String[] par = new String[]
		{ "user_id", "task_id", "grabSingleId", "types" };
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		// 0：取消1：确认
		int type = Integer.parseInt(map2.get("types").toString());
		String user_id = map2.get("user_id").toString();
		int task_id = Integer.parseInt(map2.get("task_id").toString());
		int grabSingleId = Integer.parseInt(map2.get("grabSingleId").toString());
		int num = 0;
		PageData pd2 = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		String userid = pd2.get("userid").toString();
		if (!userid.equals(user_id))
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能确认确认跑客!", map);
		}
		PageData rob = new PageData();
		rob.put("user_id", grabSingleId);
		rob.put("task_id", task_id);
		// 判断该跑客是否在抢单状态
		PageData pd3 = (PageData) dao.findForObject("TaskMapper.findRobOrder", rob);
		if (pd3 == null || "0".equals(pd3.get("grabSingleState").toString()))
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "跑客已经取消抢单", map);
		}
		// 选择抢单人的极光推送id
		String registrationId = null;
		if (type == 1)
		{
			if (pd2 != null)
			{
				int taskState = Integer.parseInt(pd2.get("taskState").toString());

				if (taskState != TaskUtilCode.TASK_GRABING_2)
				{
					// 任务不在抢单中
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务不在抢单中", map);
				}
				int businessType = Integer.parseInt(pd2.get("businessType").toString());
				// 判断是否已经选择过跑客
				if (businessType == TaskUtilCode.CATEGORY_SELL_1)
				{
					pd2.put("taskState", TaskUtilCode.TASK_WAIT_PAY_1);
				} else
				{
					pd2.put("taskState", TaskUtilCode.TASK_RUNNING_3);
				}
				pd2.put("grabSingle", grabSingleId);
				num = (Integer) dao.update("TaskMapper.confirmSelect", pd2);
				if(businessType == TaskUtilCode.CATEGORY_BUY_0  && num > 0){
					// 订单信息
					PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
					pdOrder.put("payeeId", grabSingleId);
					// 修改订单状态以及实际支付金额
					num = (Integer) dao.update("OrderMapper.editOrderState", pdOrder);
				}
				if (businessType == TaskUtilCode.CATEGORY_SELL_1 && num > 0)
				{
					Map<String, Object> orderData = new HashMap<String, Object>();
					String order_id = OrderIDUtil.getOrderNum();// 生成订单号
					orderData.put("order_id", order_id);
					orderData.put("task_id", task_id);
					orderData.put("order_type", "0");
					orderData.put("moneys", pd2.get("moneys"));
					orderData.put("payState", OrderStateUtil.ORDER_WAIT_PAY_1);
					orderData.put("payuserId", grabSingleId);
					orderData.put("payeeId", pd2.get("userid"));
					orderData.put("coupons", 0);
					num = (Integer) dao.save("OrderMapper.save", orderData);
					if (num > 0)
					{
						map.put("order_id", order_id);
					}
				}

			}
		}
		if (num > 0)
		{
			try
			{
				PageData upd =new PageData();
				upd.put("user_id", grabSingleId);
				PageData pdPush = (PageData) dao.findForObject("TaskMapper.findPushIdsByUserid", upd);
				if (pdPush != null)
				{
					registrationId = pdPush.get("pushId").toString();
				}
				if (registrationId != null && !"".equals(registrationId))
				{
					PushUtil.SendPushToOnePub(registrationId, "恭喜你,抢单成功!!!",
							ResultUtil.getExtras(PushMsgType.GRAB_SUCCESS_300, task_id + "", null, grabSingleId + ""));
				}
				PageData pd4 = new PageData();
				pd4.put("task_id", task_id);
				pd4.put("user_id", grabSingleId);
				// 查询除了选中跑客外的跑客极光推送id
				List<String> listPushs = (List<String>) dao.findForList("TaskMapper.findRunnersPushIds", pd4);
				if (listPushs != null && listPushs.size() > 0)
				{
					PushUtil.SendPushToManyNoGrab(listPushs, task_id + "", PushMsgType.NO_RUNNERS__13);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "确认成功", map);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "确认失败", map);
		}
		} catch (Exception e1)
		{
			e1.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * 抢单人列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object findRunnersList(PageData pd) throws Exception
	{
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "task_id", "user_id" };
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao.findForList("TaskMapper.findRunnersList", map2);
		if (list != null)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "查询成功", list);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "查询失败", list);
		}
	}

	/**
	 * 确认完成任务
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Object updateCompleteTask(PageData pd) throws Exception
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "user_id", "task_id", "state", "payPassword" };
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		int user_id_com = Integer.parseInt(map2.get("user_id").toString());
		int task_id = Integer.parseInt(map2.get("task_id").toString());
		int state = Integer.parseInt(map2.get("state").toString());
		String payPassword = map2.get("payPassword").toString();
		int num = 0;
		// 1:完成
		if (state == 1)
		{
			PageData pd2 = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
			if (pd2 != null)
			{
				int businessType = Integer.parseInt(pd2.get("businessType").toString());
				int user_id = 0;
				Integer pkId = 0;
				if (businessType == TaskUtilCode.CATEGORY_SELL_1)
				{
					user_id = Integer.parseInt(pd2.get("grabSingle").toString());
					pkId = Integer.parseInt(pd2.get("userid").toString());
				} else
				{
					user_id = Integer.parseInt(pd2.get("userid").toString());
					pkId = Integer.parseInt(pd2.get("grabSingle").toString());
				}
				// 判断确认完成任务的人是不是付款人
				if (user_id != user_id_com)
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能进行此操作!", "");
				}
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("userId", user_id);
				// 获取用户信息
				Map mapUser = (Map) dao.findForObject("UserLoginMapper.findtUserByExmaple", map3);
				String userPayPassword = mapUser.get("payPassword").toString();
				if (!userPayPassword.equals(payPassword))
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "支付密码错误!", "");
				}
				int taskState = Integer.parseInt(pd2.get("taskState").toString());
				if (taskState == TaskUtilCode.TASK_RUN_FINISH_4)
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务已经确认!", map);
				}
				if (taskState != TaskUtilCode.TASK_RUNNING_3)
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务不在奔跑中,无法确认!", map);
				}
				pd2.put("taskState", TaskUtilCode.TASK_RUN_FINISH_4);
				num = (Integer) dao.update("TaskMapper.editTaskState", pd2);
				if (num > 0)
				{
					// 用户账户余额
					Map<String, Object> map4 = new HashMap<String, Object>();
					map4.put("userId", pkId);
					// 获取用户信息
					Map mapUser2 = (Map) dao.findForObject("UserLoginMapper.findtUserByExmaple", map4);
					double userMoneys = Double.parseDouble(mapUser2.get("moneys").toString());
					// 任务金额
					double moneys = Double.parseDouble(pd2.get("moneys").toString());

					// 应支付给用户的金额
					double addMoneys = Amount.mul(moneys, 0.95);
					map4.put("moneys", Amount.add(userMoneys, addMoneys));
					// 用户账户金额修改
					num = (Integer) dao.update("UserOperateMapper.modify", map4);
					// 公司盈利金额
					double getMoneys = Amount.sub(moneys, addMoneys);
					PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
					
					Object top= mapUser.get("topId");
					if(top!=null && !"".equals(top.toString().trim())){
						int topId=Integer.parseInt(top.toString());
						// 用户账户余额
						Map<String, Object> map5 = new HashMap<String, Object>();
						map5.put("user_id", topId);
						// 获取用户信息
						Map mapUser3 = (Map) dao.findForObject("UserLoginMapper.findTopUserById", map5);
						if(mapUser3!=null){
							int daig=Integer.parseInt(mapUser3.get("daig").toString());
							if(daig>0){
								double tcbl=Double.parseDouble(mapUser3.get("tcbl").toString());
								//代理用户收益
								double tc=Amount.mul(getMoneys, Amount.mul(tcbl, 0.01));
								getMoneys=Amount.sub(getMoneys, tc);
								PageData pdDl=new PageData();
								pdDl.put("task_id", task_id);
								pdDl.put("consumers_id", user_id);
								pdDl.put("agent_id", topId);							
								pdDl.put("task_moneys", tc);
								pdDl.put("commission_ratio", tcbl);
								num=(Integer) dao.save("DlssyiMapper.save", pdDl) ;
								double mo=Double.parseDouble(mapUser3.get("moneys").toString());
								mapUser3.put("moneys", Amount.add(mo, tc));
								// 用户账户金额修改
								num = (Integer) dao.update("UserOperateMapper.modify", mapUser3);
								
							}
						}
						
					}
					pdOrder.put("company_earnings", getMoneys);
					if(num>0){
						num = (Integer) dao.update("OrderMapper.editOrderState", pdOrder);
					}
					if (num > 0)
					{
						PageData pd3 = new PageData();
						pd3.put("task_id", task_id);
						pd3.put("user_id", user_id);
						pd3.put("pkId", pkId);
						num = (Integer) dao.save("TaskMapper.saveConfirmTask", pd3);
					}
				}
			}
		}
		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "确认完成", map);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "确认失败", map);
		}
	}

	/**
	 * 任务评价
	 * 
	 * @param pd
	 *            ( task_id 任务id evaluationPeopleId 评价人id attitude 态度 literacy
	 *            素养 professional 专业 punctual 守时 content 内容 byEvaluationId
	 *            被评价人id)
	 * @return
	 * @throws Exception
	 */

	public Object saveEvaluation(PageData pd) throws Exception
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "task_id", "evaluationPeopleId", "attitude", "literacy", "professional", "punctual",  "byEvaluationId" };
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		PageData pdTask = (PageData) dao.findForObject("TaskMapper.findTaskById", map2.get("task_id"));
		if (pdTask == null)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "未找到任务信息", map);
		}
		int count = (Integer) dao.findForObject("EvaluationMapper.countByTaskId", map2.get("task_id"));
		if (count > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "已经评价", map);
		}
		int evaluationPeopleId = Integer.parseInt(map2.get("evaluationPeopleId").toString());
		int businessType = Integer.parseInt(pdTask.get("businessType").toString());
		int user_id = 0;
		if (businessType == TaskUtilCode.CATEGORY_SELL_1)
		{
			user_id = Integer.parseInt(pdTask.get("grabSingle").toString());
			map2.put("byEvaluationId", pdTask.get("userid")) ;
		} else
		{
			user_id = Integer.parseInt(pdTask.get("userid").toString());
			map2.put("byEvaluationId", pdTask.get("grabSingle")) ;
		}
		// 判断确认完成任务的人是不是付款人
		if (user_id != evaluationPeopleId)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能进行此操作!", "");
		}
		int num = (Integer) dao.save("EvaluationMapper.save", map2);
		if(num>0){
			num = (Integer) dao.update("EvaluationMapper.editUserIntegral", map2);
		}
		
		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "评价成功", map);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "评价失败", map);
		}
	}

	/**
	 * 查询任务列表
	 * 
	 * @param pd
	 *            (latitude 纬度 longitude 经度 userid 用户id content 查询内容 types 类别)
	 * @return
	 * @throws Exception
	 */
	public Object findNearTaskList(PageData pd) throws Exception
	{
		// latitude 纬度
		// longitude 经度
		// userid 用户id
		// content 查询内容
		// thumbNumbers 主题
		// age 年龄
		// userName 姓名
		// types 类别
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "longitude", "latitude", "types", "currentPage" };
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		int currentPage = Integer.parseInt(map2.get("currentPage").toString());

		// 查询总数
		int totalResult = (Integer) dao.findForObject("TaskMapper.countNearTaskList", map2);
		System.out.println(totalResult);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int totalPage = 0;
		Map<String, Object> pageMap = new HashMap<String, Object>();
		// if (totalResult <= 0) {
		// pageMap.put("totalPage", 0);
		// pageMap.put("currentPage", currentPage);
		// return ResultUtil.getResultPage(ResultCodeUtil.SUCCESS_100, "查询成功",
		// list, pageMap);
		// }
		Page page = new Page();
		page.setTotalResult(totalResult);
		page.setCurrentPage(currentPage);
		totalPage = page.getTotalPage();
		pageMap.put("totalPage", totalPage);
		pageMap.put("currentPage", page.getCurrentPage());
		if (totalPage < currentPage)
		{
			return ResultUtil.getResultPage(ResultCodeUtil.SUCCESS_100, "查询成功", list, pageMap);
		}
		// 分页
		map2.put("start", page.getCurrentResult());
		map2.put("limit", page.getShowCount());
		list = (List<Map<String, Object>>) dao.findForList("TaskMapper.findNearTaskList", map2);

		for (Map<String, Object> map : list)
		{
			Map<String, Object> mapT = new HashMap<String, Object>();
			mapT.put("task_id", map.get("task_id"));
			// 查询任务抢单人前十个人
			List<Map<String, Object>> lists = (List<Map<String, Object>>) dao.findForList("TaskMapper.findRunnersLimit", mapT);
			map.put("runners", lists);
		}

		if (list != null)
		{
			return ResultUtil.getResultPage(ResultCodeUtil.SUCCESS_100, "查询成功", list, pageMap);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "查询失败", list);
		}

	}

	/**
	 * 任务详情
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object findTaskDetails(PageData pd) throws Exception
	{

		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "task_id", "user_id", "lat", "lng" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		String task_id = map.get("task_id").toString();
		String user_id = map.get("user_id").toString();// 查看任务用户id
		PageData pd2 = (PageData) dao.findForObject("TaskMapper.findTaskByIdLatLng", map);
		if (pd2 != null)
		{
			String userid = pd2.get("userid").toString();
			if (!userid.equals(user_id))
			{
				// 增加用户浏览量
				dao.update("TaskMapper.editlls", task_id);
			}
			int taskState = Integer.parseInt(pd2.get("taskState").toString());
			int grabSingle = Integer.parseInt(pd2.get("grabSingle").toString());
			PageData pd3 = new PageData();
			pd3.put("task_id", task_id);
			if (taskState != TaskUtilCode.TASK_GRABING_2 && grabSingle > 0)
			{
				pd3.put("user_id", grabSingle);
			}
			List<Map<String, Object>> list = (List<Map<String, Object>>) dao.findForList("TaskMapper.findRunnersLimit", pd3);
			pd2.put("runners", list);
			if (taskState == TaskUtilCode.TASK_RUN_FINISH_4)
			{
				Integer count = (Integer) dao.findForObject("EvaluationMapper.countByTaskId", task_id);
				pd2.put("evaluation", count);
			}
			if (taskState == TaskUtilCode.TASK_CANCELING_7)
			{
				PageData pd4 = (PageData) dao.findForObject("TaskMapper.findCancelTask", task_id);
				if (pd4 != null)
				{
					pd2.put("cancel_user_id", pd4.get("userId"));
				}
			}

			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "查询成功", pd2);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "查询失败", pd2);
		}
	}

	/**
	 * 查询我的任务
	 * 
	 * @param pd
	 *            (userid:用户id;content:搜索内内容;types:类型-0.我发布的任务，1.我已抢单的任务)
	 * @return
	 * @throws Exception
	 */
	public Object findMyTask(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "userid", "types", "currentPage" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		// Integer types = Integer.parseInt(map.get("types").toString());
		List<Map<String, Object>> list = null;
		// Map<String, Object> map2 = new HashMap<String, Object>();
		// map2.put("userid", map.get("userid"));
		// map2.put("content", map.get("content"));
		// if (types == 0) {
		int totalPage = 0;
		int currentPage = Integer.parseInt(map.get("currentPage").toString());

		// 查询总数
		int totalResult = (Integer) dao.findForObject("TaskMapper.countMyReleaseTaskList", map);
		Page page = new Page();
		page.setTotalResult(totalResult);
		page.setCurrentPage(currentPage);
		totalPage = page.getTotalPage();
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("totalPage", totalPage);
		pageMap.put("currentPage", page.getCurrentPage());
		if (totalPage < currentPage)
		{
			return ResultUtil.getResultPage(ResultCodeUtil.SUCCESS_100, "查询成功", list, pageMap);
		}
		map.put("start", page.getCurrentResult());
		map.put("limit", page.getShowCount());
		list = (List<Map<String, Object>>) dao.findForList("TaskMapper.findMyReleaseTaskList", map);
		// } else if (types == 1) {
		// list = (List<Map<String, Object>>) dao.findForList(
		// "TaskMapper.findMyGrabTaskList", map2);
		// }
		if (list != null)
		{
			return ResultUtil.getResultPage(ResultCodeUtil.SUCCESS_100, "查询成功", list, pageMap);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "查询失败", list);
		}
	}

	/**
	 * 申请退款
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object saveApplyRefund(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "user_id", "task_id", "content" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		String user_id = map.get("user_id").toString();
		String task_id = map.get("task_id").toString();
		String content = map.get("content").toString();

		PageData pdTask = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		int num = 0;
		if (pdTask != null)
		{
			int businessType = Integer.parseInt(pdTask.get("businessType").toString());
			// double moneys=Double.parseDouble(map.get("moneys").toString());
			PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
			// String orderMoneys=pdOrder.get("moneys").toString();
			// if(moneys>orderMoneys){
			// return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600,
			// "申请退款金额超过支付金额", null);
			// }
			int taskState = Integer.parseInt(pdTask.get("taskState").toString());
			if (taskState == TaskUtilCode.TASK_CANCELING_7)
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务已在商议取消中!", null);
			}
			PageData pdRefund = new PageData();
			pdRefund.put("task_id", task_id);
			pdRefund.put("userId", pdOrder.get("payuserId"));
			pdRefund.put("moneys", pdOrder.get("moneys"));
			pdRefund.put("state", 0);
			pdRefund.put("types", OrderStateUtil.REFUND_TYPE_3);
			pdRefund.put("yy", content);
			num = (Integer) dao.save("RefundMapper.saveRefund", pdRefund);
			// 修改任务状态为商议取消中
			pdTask.put("taskState", TaskUtilCode.TASK_CANCELING_7);
			num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
			// 添加取消任务记录
			PageData pd2 = new PageData();
			pd2.put("userId", user_id);
			pd2.put("task_id", task_id);
			num = (Integer) dao.save("TaskMapper.saveCancelTask", pd2);
		}

		if (num > 0)
		{
			// 推送给跑客
			try
			{
				String registrationId = null;
				String userId = "";
				// Integer businessType =
				// Integer.parseInt(pdTask.get("businessType").toString());
				// if (businessType == TaskUtilCode.CATEGORY_SELL_1)
				// {
				// userId = pdTask.get("userid").toString();
				// } else
				// {
				// userId = pdTask.get("grabSingle").toString();
				// }
				if (pdTask.get("userid").toString().equals(user_id))
				{
					userId = pdTask.get("grabSingle").toString();
				} else
				{
					userId = pdTask.get("userid").toString();
				}
				PageData upd =new PageData();
				upd.put("user_id", userId);
				PageData pdPush = (PageData) dao.findForObject("TaskMapper.findPushIdsByUserid", upd);
				if (pdPush != null)
				{
					registrationId = pdPush.get("pushId").toString();
				}
				if (registrationId != null && !"".equals(registrationId))
				{
					PushUtil.SendPushToOnePub(registrationId, "对方申请退款,请及时处理!!!",
							ResultUtil.getExtras(PushMsgType.APPLY_REFUND_500, task_id + "", null, null));
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "退款已申请，待对方处理！", null);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "申请退款异常", null);
		}
	}

	/**
	 * 同意退款
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object updateAgreeRefund(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "user_id", "task_id", "types" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		String user_id = map.get("user_id").toString();
		String task_id = map.get("task_id").toString();
		int types = Integer.parseInt(map.get("types").toString());// （0：不同意1：同意）

		PageData pdRefund = (PageData) dao.findForObject("RefundMapper.findRefundByTaskId", task_id);
		PageData pdTask = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		int num = 0;
		if (pdTask != null && pdRefund != null)
		{
			PageData pd4 = (PageData) dao.findForObject("TaskMapper.findCancelTask", task_id);
			if (pd4 == null)
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "没找到协议取消任务信息!", null);
			}
			String userId = pd4.get("userId").toString();
			if (userId.equals(user_id))
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "自己不能操作同意请求!", null);
			}
			String userid = pdTask.get("userid").toString();// 发布任务人id
			String grabSingle = pdTask.get("grabSingle").toString();// 抢单人id
			if (!userid.equals(user_id) && !grabSingle.equals(user_id))
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能进行此操作!", null);
			}
			PageData pd2 = new PageData();
			pd2.put("rid", pdRefund.get("rid"));
			if (types == 0)
			{
				pd2.put("state", OrderStateUtil.REFUND_STATE_2);
			} else if (types == 1)
			{
				pd2.put("state", OrderStateUtil.REFUND_STATE_1);
			} else
			{
				pd2.put("state", OrderStateUtil.REFUND_STATE_0);
			}
			num = (Integer) dao.update("RefundMapper.editRefundState", pd2);
			if (num > 0 && types == 1)
			{
				PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
				// 取消任务
//				pdOrder.put("payState", OrderStateUtil.ORDER_REFUND_5);
//				num = (Integer) dao.update("OrderMapper.editOrderState", pdOrder);
//				if (num > 0)
//				{
					pdTask.put("taskState", TaskUtilCode.TASK_REFUND_8);
					num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
					if (num > 0)
					{
						double orderMoneys = Double.parseDouble(pdOrder.get("moneys").toString());
						// 退款给发布任务用户
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2.put("userId", pdOrder.get("payuserId"));
						Map mapUser = (Map) dao.findForObject("UserLoginMapper.findtUserByExmaple", map2);
						double userMoneys = Double.parseDouble(mapUser.get("moneys").toString());
						map2.put("moneys", Amount.add(userMoneys, orderMoneys));
						// 用户账户金额修改
						num = (Integer) dao.update("UserOperateMapper.modify", map2);
					}

//				}

			}
		}
		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "同意退款成功", null);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "同意退款失败", null);
		}
	}

	/**
	 * 取消任务
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object updateCancelTask(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "user_id", "task_id" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		String user_id = map.get("user_id").toString();
		String task_id = map.get("task_id").toString();
		PageData pdTask = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		int num = 0;
		if (pdTask != null)
		{
			if (!isSelfRelevant(pdTask, user_id))
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能进行取消操作", null);
			}

			int businessType = Integer.parseInt(pdTask.get("businessType").toString());
			int taskState = Integer.parseInt(pdTask.get("taskState").toString());
			if (taskState == TaskUtilCode.TASK_OVERDUE_6)
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务已经过期", null);
			}
			if (taskState == TaskUtilCode.TASK_CANCEL_5)
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务已经取消", null);
			}
			// 任务没有付款的情况下可以取消
			if (taskState >= TaskUtilCode.TASK_RUNNING_3)
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务无法取消", null);
			}
			// 卖时间待付款的任务取消需要同时取消订单
			if (taskState == TaskUtilCode.TASK_WAIT_PAY_1)
			{
				PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
				if (pdOrder == null)
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务存在异常", null);
				}
				// 取消订单
				pdTask.put("taskState", TaskUtilCode.TASK_CANCEL_5);
				num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
				if (num > 0)
				{
					// 取消任务
					pdOrder.put("payState", OrderStateUtil.ORDER_CANCEL_4);
					num = (Integer) dao.update("OrderMapper.editOrderState", pdOrder);
				}
			} else if (taskState == TaskUtilCode.TASK_GRABING_2)
			{
				if (businessType == TaskUtilCode.CATEGORY_SELL_1)
				{
					pdTask.put("taskState", TaskUtilCode.TASK_CANCEL_5);
					num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
				} else
				{
					PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
					if (pdOrder == null)
					{
						return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务存在异常", null);
					}
					// 取消订单
					pdTask.put("taskState", TaskUtilCode.TASK_CANCEL_5);
					num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
					if (num > 0)
					{
						// 取消任务
//						pdOrder.put("payState", OrderStateUtil.ORDER_REFUND_5);
//						num = (Integer) dao.update("OrderMapper.editOrderState", pdOrder);
						if (num > 0)
						{
							double orderMoneys = Double.parseDouble(pdOrder.get("moneys").toString());
							PageData pdRefund = new PageData();

							pdRefund.put("task_id", task_id);
							pdRefund.put("userId", pdOrder.get("payuserId"));
							pdRefund.put("moneys", orderMoneys);
							pdRefund.put("state", 1);
							pdRefund.put("types", OrderStateUtil.REFUND_TYPE_1);
							pdRefund.put("yy", "取消任务");
							num = (Integer) dao.save("RefundMapper.saveRefund", pdRefund);
							// 退款给发布任务用户
							Map<String, Object> map2 = new HashMap<String, Object>();
							map2.put("userId", pdOrder.get("payuserId"));
							Map mapUser = (Map) dao.findForObject("UserLoginMapper.findtUserByExmaple", map2);
							double userMoneys = Double.parseDouble(mapUser.get("moneys").toString());
							map2.put("moneys", Amount.add(userMoneys, orderMoneys));
							// 用户账户金额修改
							num = (Integer) dao.update("UserOperateMapper.modify", map2);

						}
					}
				}
			}
			// 添加取消任务记录
			PageData pd2 = new PageData();
			pd2.put("user_id", user_id);
			pd2.put("task_id", task_id);
			num = (Integer) dao.save("TaskMapper.saveCancelTask", pd2);
		}
		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "取消任务成功", null);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "取消任务失败", null);
		}
	}

	/**
	 * 申诉成功
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object saveComplaint(PageData pd, MultipartHttpServletRequest request) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "user_id", "task_id", "content" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		String user_id = map.get("user_id").toString();
		String task_id = map.get("task_id").toString();
		PageData pdTask = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		if (pdTask == null)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "未找到任务信息!", "");
		}
		if (!isSelfRelevant(pdTask, user_id))
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能进行申诉操作", "");
		}  
		int taskState = Integer.parseInt(pdTask.get("taskState").toString());
		if(taskState!=TaskUtilCode.TASK_CANCELING_7){
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "该任务当前不能进行申诉操作!", "");
		}
		if (taskState == TaskUtilCode.TASK_COMPLAINT_9)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "任务已在申诉中!", "");
		}
		int count = 0;
		MultiValueMap<String, MultipartFile> filemap = request.getMultiFileMap();
		List<String> keys=new ArrayList<String>(); 
		for (String key : filemap.keySet()) {
			MultipartFile file = filemap.get(key).get(0);
			if (file != null && file.getSize()>0) {
				count += 1;
			}else{
				keys.add(key);
			}
		}
		//去处空图片
		for (String key : keys)
		{
			filemap.remove(key);
		}
		if(count>0){
			String urls = FileService.upLoadFile(request);
			String[] picUrl = urls.split(",");
			if (picUrl.length > 0)
			{
				List<Map> list = new ArrayList<Map>();
				for (String url : picUrl)
				{
					Map<String, Object> mapUrl = new HashMap<String, Object>();
					mapUrl.put("task_id", task_id);
					mapUrl.put("url", url);
					list.add(mapUrl);
				}
				count = (Integer) dao.save("ComplaintMapper.savePicture", list);
				if (count <= 0)
				{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "申诉提交失败", null);
				}
			}
		}
		
		int num = (Integer) dao.save("ComplaintMapper.save", map);
		pdTask.put("taskState", TaskUtilCode.TASK_COMPLAINT_9);
		if(num>0){
			num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
		}

		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "申诉提交成功", "");
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "申诉提交失败", null);
		}
	}

	/**
	 * 检查当前操作用户是否和任务发布人和抢单人相关
	 * 
	 * @param pd任务信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean isSelfRelevant(PageData pd, String userId) throws Exception
	{

		String userid = pd.get("userid").toString();
		String grabSingle = pd.get("grabSingle").toString();
		if (!userid.equals(userId) && !grabSingle.equals(userId))
		{
			return false;
		}
		return true;
	}

	public Integer modifyUserMon(String userId, double moneys) throws Exception
	{
		int num = 0;
		// 退款给发布任务用户
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("userId", userId);
		Map mapUser = (Map) dao.findForObject("UserLoginMapper.findtUserByExmaple", map2);
		double userMoneys = Double.parseDouble(mapUser.get("moneys").toString());
		map2.put("moneys", Amount.add(userMoneys, moneys));
		// 用户账户金额修改
		num = (Integer) dao.update("UserOperateMapper.modify", map2);
		return num;
	}

}
