package com.ypkj.zubu.service.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.dao.TaskDao;
import com.ypkj.zubu.util.Amount;
import com.ypkj.zubu.util.OrderStateUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.TaskUtilCode;

@Component("taskJob")
public class TaskJob
{
	@Resource(name = "taskDao")
	private TaskDao taskDao;
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Scheduled(cron = "0 0 0/1 * * ? ")
	public void job1()
	{
		System.out.println("查询过期任务:" + new Date());
		try
		{
			List<PageData> tasks = (List<PageData>) dao.findForList("TaskMapper.findExpirationTask", "");
			for (PageData pageData : tasks)
			{
				int businessType = Integer.parseInt(pageData.get("businessType").toString());
				// 判断是否已经选择过跑客
				if (businessType == TaskUtilCode.CATEGORY_SELL_1)
				{
					pageData.put("taskState", TaskUtilCode.TASK_OVERDUE_6);
					dao.update("TaskMapper.editTaskState", pageData);
				} else if (businessType == TaskUtilCode.CATEGORY_BUY_0)
				{
					pageData.put("taskState", TaskUtilCode.TASK_OVERDUE_6);
					Integer num = (Integer) dao.update("TaskMapper.editTaskState", pageData);
					if(num>0){
						PageData pdOrder = (PageData) dao.findForObject("OrderMapper.findOrderByTaskId", pageData.get("task_id"));
						if (pdOrder != null)
						{
							double orderMoneys = Double.parseDouble(pdOrder.get("moneys").toString());
							PageData pdRefund = new PageData();
							pdRefund.put("task_id", pageData.get("task_id"));
							pdRefund.put("userId", pdOrder.get("payuserId"));
							pdRefund.put("moneys", orderMoneys);
							pdRefund.put("state", 1);
							pdRefund.put("types", OrderStateUtil.REFUND_TYPE_2);
							pdRefund.put("yy", "任务过期退款");
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
		} catch (Exception e)
		{
			System.out.println("处理过期任务异常!");
			e.printStackTrace();
		}
		System.out.println("过期任务处理完成:" + new Date());
	}
}
