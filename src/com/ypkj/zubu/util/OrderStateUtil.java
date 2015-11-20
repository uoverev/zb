package com.ypkj.zubu.util;

public class OrderStateUtil {
	/***待付款 */
 	public static final int ORDER_WAIT_PAY_1 = 1;
 	/*** 已付款*/
 	public static final int ORDER_ALREADR_PAY_2 = 2;
 	/*** 支付失败*/
 	public static final int ORDER_PAY_FAIL_3 = 3;
 	/*** 取消订单*/
 	public static final int ORDER_CANCEL_4 = 4;
 	/***已退款  */
 	public static final int ORDER_REFUND_5 = 5; 
 	/*** 订单关闭 */
 	public static final int ORDER_CLOSE_6 = 6; 

 	
 	/*** 退款类型-1.取消任务退款*/
 	public static final int REFUND_TYPE_1 = 1; 
 	/*** 退款类型-2.任务过期退款 */
 	public static final int REFUND_TYPE_2 = 2; 
 	/*** 退款类型-3.协议退款 */
 	public static final int REFUND_TYPE_3 =3; 
 	
 	/***申请退款状态- 0:申请中*/
 	public static final int REFUND_STATE_0 = 0;
 	/*** 申请退款状态-1：同意退款*/
 	public static final int REFUND_STATE_1 = 1;
 	/*** 申请退款状态-2：不同意退款*/
 	public static final int REFUND_STATE_2 = 2;
 	
 	/** 支付类型- 0:余额支付*/
 	public static final int PAY_YOUPAO_0 = 0;
 	/** 支付类型- 1:支付宝支付*/
 	public static final int PAY_ALIPAY_1 = 1;
 	/** 支付类型- 2:微信支付*/
 	public static final int PAY_WEIXIN_2 = 2;
}
