package com.ypkj.zubu.util;


public class TaskUtilCode {
	/**
	 * 	1.待付款2.抢单中3.奔跑中4.奔跑完成 5.任务取消6.任务过期7.商议取消中 8.已协议退款9.申诉中(客服介入中)10.任务关闭
	 *  买：1-2-3-4，卖2-1-3-4
	 */
	 	/***任务状态-待付款 */
	 	public static final int TASK_WAIT_PAY_1 = 1;//待付款
	 	/*** 任务状态-抢单中*/
	 	public static final int TASK_GRABING_2 = 2; //抢单中
	 	/***任务状态-奔跑中  */
	 	public static final int TASK_RUNNING_3 = 3; //奔跑中 
	 	/***任务状态- 奔跑完成 */
	 	public static final int TASK_RUN_FINISH_4 = 4; //奔跑完成 
	 	/*** 任务状态-取消任务 */
	 	public static final int TASK_CANCEL_5 = 5; 
	 	/*** 任务状态-任务过期 */
	 	public static final int TASK_OVERDUE_6 = 6; 
	 	/** 商议取消中 **/
	 	public static final int TASK_CANCELING_7 = 7; 
	 	/*** 任务状态-已协议退款 */
	 	public static final int TASK_REFUND_8 = 8; 
	 	/**申诉中(客服介入中) **/
	 	public static final int TASK_COMPLAINT_9 = 9; 
	 	/*** 任务状态-任务关闭 */
	 	public static final int TASK_CLOSE_10 = 10; 
	 	
	 	/*** 任务类别-买时间*/
	 	public static final int CATEGORY_BUY_0 = 0; 
	 	/*** 任务类别-卖时间 */
	 	public static final int CATEGORY_SELL_1 = 1; 
	 	
	 	/*** 取消抢单*/
	 	public static final int GRAB_CANCEL_0 = 0; 
	 	/*** 抢单 */
	 	public static final int GRAB_1 = 1; 
	 	
}
