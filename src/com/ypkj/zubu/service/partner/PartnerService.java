package com.ypkj.zubu.service.partner;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;

@Service("partnerService")
public class PartnerService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 申请合伙人
	 * @param pd
	 *            ( user_id 用户id application_level 申请级别 real_name 真实姓名
	 *            phone_number 联系电话 pay_type 支付类型（线上，现下）)
	 * @return
	 * @throws Exception
	 */
	public Object saveApplyPartner(PageData pd) throws Exception {
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA")
				.toString());
		String[] par = new String[] { "user_id", "application_level",
				"real_name", "phone_number", "pay_type" };
		if (!ResultUtil.isPageDate(par, map2)) {
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700,
					"参数不合法", par);
		}
		map2.put("application_status", 0);
		Integer num = (Integer) dao.save("PartnerMapper.save", map2);
		if (num > 0) {
			return ResultUtil
					.getResult(ResultCodeUtil.SUCCESS_100, "申请成功", "");
		} else {
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "申请失败", "");
		}
	}

	/**
	 * 查询申请合伙人
	 * @param pd(user_id	用户id)
	 * @return
	 * @throws Exception 
	 */
	public Object findApplyPartner(PageData pd) throws Exception {
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA")
				.toString());
		String[] par = new String[] { "user_id" };
		if (!ResultUtil.isPageDate(par, map2)) {
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700,
					"参数不合法", par);
		}

		PageData pd2=(PageData) dao.findForObject("PartnerMapper.findByUserId", map2.get("user_id"));
		if(pd2!=null){
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "查询成功",pd2);
		}else{
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "查询失败", "");
		}
	}

}
