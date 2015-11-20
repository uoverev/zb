package com.ypkj.zubu.util;

import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class OrderIDUtil
{
	 /** 年月日时分秒毫秒(无下划线) yyyyMMddHHmmsssss */
    public static final String dtLong                  = "yyyyMMddHHmmsssss";
    
	 /**
     * 返回系统当前时间(精确到毫秒)+三位随机数,作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
	public  static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtLong);
		return df.format(date)+getThree();
	}
	
	/**
	 * 产生随机的三位数
	 * @return
	 */
	public static String getThree(){
		Random rad=new Random();
		return rad.nextInt(1000)+"";
	}
}
