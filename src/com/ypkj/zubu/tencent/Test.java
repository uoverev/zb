package com.ypkj.zubu.tencent;

import java.util.Map;

import com.ypkj.zubu.tencent.common.XMLParser;
import com.ypkj.zubu.tencent.protocol.pay_protocol.ScanPayReqData;

public class Test
{

	public static void main(String[] args)
	{
//		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
//		parameters.put("appid", ConfigUtil.APPID);
//
//		parameters.put("mch_id", ConfigUtil.MCH_ID);
//		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
//		parameters.put("body", "LEO测试");
//		parameters.put("out_trade_no", "201412051510");
//		parameters.put("total_fee", "1");
//		parameters.put("spbill_create_ip",IpAddressUtil.getIpAddr(request));
//		parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
//		parameters.put("trade_type", "JSAPI");
//		parameters.put("openid", "o7W6yt9DUOBpjEYogs4by1hD_OQE");
//		String sign = PayCommonUtil.createSign("UTF-8", parameters);
//		parameters.put("sign", sign);
//		String requestXML = PayCommonUtil.getRequestXml(parameters);
        //---------------生成订单号 开始------------------------  
        //当前时间 yyyyMMddHHmmss  
        
        //订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行  
        String out_trade_no = "20151021142010";  
        //---------------生成订单号 结束------------------------ 
		ScanPayReqData scanPayReqData=new ScanPayReqData
				("", "测试", "", out_trade_no, 
						10, "", "127.0.0.1", 
						String.valueOf(System.currentTimeMillis() / 1000), "20151025110610", "");
		try
		{
			String xmlString=new WXPay().requestUnifiedorderPayService(scanPayReqData);
			System.out.println(xmlString);
			Map<String, Object> map = XMLParser.getMapFromXML(xmlString);
			System.out.println();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
