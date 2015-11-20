package com.ypkj.zubu.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.dao.TaskDao;
import com.ypkj.zubu.util.Amount;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.OrderState;
import com.ypkj.zubu.util.OrderStateUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.PushMsgType;
import com.ypkj.zubu.util.PushUtil;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;
import com.ypkj.zubu.util.TaskUtilCode;

@Service("orderService")
public class OrderService
{

	@Resource(name = "taskDao")
	private TaskDao taskDao;

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public Object getOrder(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "task_id" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		String task_id = map.get("task_id").toString();
		PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", task_id);
		return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "查询成功", pdOrder);
	}

	/**
	 * 支付
	 * 
	 * @param pd
	 *            ( 订单号order_id 任务编号task_id 支付方式（0平台 1支付宝 2微信）order_type
	 *            支付金额moneys 使用的优惠券金额coupons 支付人payuserId)
	 * @return
	 * @throws Exception
	 */
	public Object updatePayOrder(PageData pd) throws Exception
	{

		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		// 判断传入参数是否合法
		String[] par = new String[]
		{ "task_id", "order_type", "moneys", "coupons", "payuserId" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		Integer payuserId = Integer.parseInt(map.get("payuserId").toString());// 支付人
		Integer order_type = Integer.parseInt(map.get("order_type").toString());// 支付方式
		double moneys = Double.parseDouble(map.get("moneys").toString());// 支付金额
		double coupons = Double.parseDouble(map.get("coupons").toString());// 使用优惠券金额
		String task_id = map.get("task_id").toString();// 任务id
		// 任务信息
		PageData pdTask = (PageData) dao.findForObject("TaskMapper.findTaskById", task_id);
		// 订单信息
		PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", map.get("task_id"));
		// 支付人id
		Integer payuserIdOrder = Integer.parseInt(pdOrder.get("payuserId").toString());
		if (payuserIdOrder.intValue() != payuserId.intValue())
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "您不能进行支付操作", "");
		}
		int payState = Integer.parseInt(pdOrder.get("payState").toString());
		if (payState == OrderState.ORDER_ALREADR_PAY_2.getCode() || payState == OrderState.ORDER_CANCEL_4.getCode()
				|| payState == OrderState.ORDER_REFUND_5.getCode() || payState == OrderState.ORDER_CLOSE_6.getCode())
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "该订单" + OrderState.getName(payState), "");
		}
		// 发布任务金额
		double moneysTask = Double.parseDouble(pdTask.get("moneys").toString());
		// 生成订单金额
		double moneysOrder = Double.parseDouble(pdOrder.get("moneys").toString());

		// 若任务金额和订单金额不一致
		if (moneysTask != moneysOrder)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "订单异常", "");
		}
		// 支付总额（支付金额+优惠券）
		double totalMoneys = Amount.add(moneys, coupons);
		if (moneysOrder != totalMoneys)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "订单支付异常", "");
		}
		int num = 0;
		Integer businessType = Integer.parseInt(pdTask.get("businessType").toString());
		// Map<String, Object> map2 = new HashMap<String, Object>();
		// map2.put("userId", payuserIdOrder);
		// Map mapUser = (Map)
		// dao.findForObject("UserLoginMapper.findtUserByExmaple", map2);
		Map mapUser = taskDao.findtUserByExmaple(payuserIdOrder.toString());
		double userMoneys = Double.parseDouble(mapUser.get("moneys").toString());
		if (order_type.intValue() == OrderStateUtil.PAY_YOUPAO_0 && userMoneys < moneys)
		{
			return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "账户余额不足!", "");
		}
		double yhj = Double.parseDouble(mapUser.get("yhj").toString());
		if (coupons > 0)
		{
			if (coupons > yhj)
			{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "优惠券金额不足!", "");
			}

		}
		// 余额平台支付
		// if (order_type == OrderStateUtil.PAY_YOUPAO_0)
		// {

		Map<String, Object> editMap = new HashMap<String, Object>();
		editMap.put("userId", payuserId);
		if (order_type.intValue() == OrderStateUtil.PAY_YOUPAO_0)
		{
			editMap.put("moneys", Amount.sub(userMoneys, moneys));
		}

		if (coupons > 0)
		{
			editMap.put("yhj", Amount.sub(yhj, coupons));
			pdOrder.put("moneys", moneys);
			pdOrder.put("coupons", coupons);
		}

		// 判断任务类别是买时间还是卖时间
		if (businessType.intValue() == TaskUtilCode.CATEGORY_SELL_1)
		{
			pdTask.put("taskState", TaskUtilCode.TASK_RUNNING_3);
		} else
		{
			pdTask.put("taskState", TaskUtilCode.TASK_GRABING_2);
		}
		pdOrder.put("payState", OrderStateUtil.ORDER_ALREADR_PAY_2);
		pdOrder.put("order_type", order_type);
		// 修改订单状态以及实际支付金额
		num = (Integer) dao.update("OrderMapper.editOrderState", pdOrder);
		if (num > 0)
		{
			// 修改任务状态
			num = (Integer) dao.update("TaskMapper.editTaskState", pdTask);
			if(num>0 && (order_type == OrderStateUtil.PAY_YOUPAO_0 || coupons > 0)){
				// 用户账户金额修改
				num = (Integer) dao.update("UserOperateMapper.modify", editMap);
			}
		}
		if (num > 0)
		{
			try
			{
				if (
				// order_type == OrderStateUtil.PAY_YOUPAO_0 &&
				businessType == TaskUtilCode.CATEGORY_BUY_0)
				{
					map.put("push_num", 0);
					PageData pd2 = new PageData();
					pd2.put("lat", mapUser.get("lat"));
					pd2.put("lng", mapUser.get("lnt"));
					pd2.put("userid", payuserId);
					List<String> listPushs = (List<String>) dao.findForList("TaskMapper.findNearPushIds", pd2);
					if (listPushs != null && listPushs.size() > 0)
					{
						PushUtil.SendPushToMany(listPushs, "有跑客发布了一条买时间的任务!", task_id + "", PushMsgType.NEAR__12);
						map.put("push_num", listPushs.size());
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "支付成功", map);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "支付失败", map);
		}
	}

	/**
	 * 非平台支付调用第三方支付平台成功后调用
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object updatePayOrderState(PageData pd) throws Exception
	{

		return pd;

	}
}
