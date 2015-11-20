package com.ypkj.zubu.util;

public enum OrderState
{
	ORDER_WAIT_PAY_1(1,"待付款"),
	ORDER_ALREADR_PAY_2(2,"已付款"),
	ORDER_PAY_FAIL_3(3,"支付失败"),
	ORDER_CANCEL_4(4,"已取消"),
	ORDER_REFUND_5(5,"已退款"),
	ORDER_CLOSE_6(6,"已关闭");

	private int code;
	private String name;

	private OrderState(int code, String name)
	{

		this.code = code;
		this.name = name;

	

	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	public static  String getName(int code){
		OrderState[] orderStates = OrderState.values();
		for(OrderState orderState:orderStates){
			if(orderState.getCode()==code){
				return orderState.getName();
			}

		}
		return "";
	}
}
