package com.ypkj.zubu.service.cash;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.DateUtil;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;
/**
 * 
 * @author 邓辉
 * 时间:2015.8.24
 */
@Service("cashService")
public class CashService {
@Resource(name="daoSupport")
private DaoSupport daoSupport;

	/**200026
	 *  现金提现
	 */
	@Transactional
	public  Object updatecashtx(PageData pd){
		Map parameter=null;
		try{
			String [] str={"userid","moneys","zfbAccount","username"};
			parameter=ObjectUtil.stringToMap(pd.getString("DATA"));
			boolean parlegal= ResultUtil.isPageDate(str,parameter);
			if(parlegal==false){
				return ResultUtil.getResultD(-600, "参数不合法", str);
			}
			double Umoney =(Double)daoSupport.findForObject("CashMapper.ucashye", parameter);
			
			if(Umoney>=Double.parseDouble(parameter.get("moneys")+""))
			{
				daoSupport.update("CashMapper.cashtx",parameter );
				//生成日期组成的17位的提现流水号
				DateUtil running_water =new DateUtil();
				String running_waterNum=running_water.getFileTime();
				parameter.put("waterNumber", running_waterNum);
				daoSupport.save("CashMapper.txrecord", parameter);
			}else{
				return ResultUtil.getResultD(-600,"余额不足", null);
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
		return ResultUtil.getResultD(100,"成功", null);
	}
	/**200025
	 * 现金充值 
	 * @throws Exception 
	 */
	public Object  savecashcz(PageData pd) throws Exception{
		Map parameter=null;
		//Map<String, Object> message=new HashMap<String, Object>();
			try{
				String  [] str={"userid","moneys","types"};
				parameter=ObjectUtil.stringToMap(pd.getString("DATA"));	
				boolean islegal=ResultUtil.isPageDate(str, parameter);
				if(islegal==false){
					return ResultUtil.getResultD(-600, "参数不合法", str) ;
				}
				
				int count=(Integer)daoSupport.save("CashMapper.cashczrecord", parameter);
				if(count>0){
					
					daoSupport.findForObject("CashMapper.useraddmoneys",parameter);
				}
				else{
					return ResultUtil.getResult(-600, "充值失败", null);
				}
			}catch (Exception e){
				//spring 配置文件 中的事务管理必须抛出此异常才能捕获，回滚
				throw new RuntimeException();
			}		 
		return ResultUtil.getResult(100, "成功", null);
	}
	
	/**
	 * 100016
	 * 现金提现查询
	 */
	 public  Object  querycasht(PageData pd){
		 Map map=null;
		 ArrayList<Map<String, Object>> result=null;
		 ArrayList<Map<String, Object>> results=new ArrayList<Map<String,Object>>();
		 try{
			map= ObjectUtil.stringToMap(pd.getString("DATA"));
			result=(ArrayList<Map<String, Object>>)daoSupport.findForList("CashMapper.cashtxcx", map);
			
			for(Map<String, Object> r:result){
				if(r.get("state")==null){
					r.put("state", "");
					r.put("zfbmsg","空的");
				}
				if((r.get("state")+"").equals("0")){
					r.put("zfbmsg","待审核");
				}
				if((r.get("state")+"").equals("success_details")){
					r.put("state", "1");
					r.put("zfbmsg","审核通过");
				}
				if((r.get("state")+"").equals(Zfbstate.ERROR_OTHER_CERTIFY_LEVEL_LIMIT)){
					r.put("zfbmsg","收款账户实名认证信息不完整，无法收款");
				}
				if((r.get("state")+"").equals(Zfbstate.ILLEGAL_USER_STATUS)){
					r.put("zfbmsg","用户状态不正确");
				}
				if((r.get("state")+"").equals(Zfbstate.RECEIVE_USER_NOT_EXIST)){
					r.put("zfbmsg","收款用户不存在");
				}
				if((r.get("state")+"").equals(Zfbstate.RECEIVE_USER_NOT_EXIST)){
					r.put("zfbmsg","收款账户尚未实名认证，无法收款");
				}
				if((r.get("state")+"").equals(Zfbstate.ACCOUN_NAME_NOT_MATCH)){
					r.put("zfbmsg","用户姓名和收款名称不匹配");
				}
				if((r.get("state")+"").equals(Zfbstate.RECEIVE_MONEY_ERROR)){
					r.put("zfbmsg","收款金额格式有误");
				}
				if((r.get("state")+"").equals(Zfbstate.SYSTEM_DISUSE_FILE)){
					r.put("zfbmsg","用户逾期 15 天未复核，批次失败");
				}
				results.add(r);
			}
			
		 }catch (Exception e){
			 e.printStackTrace();
			 return ResultUtil.getResult(-200, "服务器异常",null );
		 }
		 return ResultUtil.getResult(100, "成功",results );
	 }
		/**
		 * 	100031
		 *  支付验证
		 * */
		public Object  Zfbpasswordverify(PageData pd){
			try {
			String [] par={"userid","paypassword"};
			Map<String, Object> map=ObjectUtil.stringToMap(pd.getString("DATA"));
			boolean islegal=ResultUtil.isPageDate(par, map);
			if(islegal==false){
				return ResultUtil.getResultD(-600, "参数不合法", null);
			}
			int iscorrect=(Integer)daoSupport.findForObject("CashMapper.zfpsverify", map);
			if(iscorrect<1){
				return ResultUtil.getResultD(-300, "支付密码错误", null);
			}
			} catch (Exception e) {
				
				e.printStackTrace();
				return ResultUtil.getResultD(-200, "服务器异常", null);
			}
			return ResultUtil.getResultD(100, "验证正确", null);
		}
}
