package com.ypkj.zubu.controller.app;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ypkj.zubu.controller.base.BaseController;
import com.ypkj.zubu.service.systemMessage.SystemSerivce;
import com.ypkj.zubu.service.usermanger.UserLoginService;
import com.ypkj.zubu.service.usermanger.UserOperateService;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;

/**
 * app-产品详情-接口类 手机app接口实例
 * 
 * 相关参数协议： 1 操作成功 -1 参数不完整 -2 接口编号不存在 -3服务器内部错误 -4业务验证错误
 */
@Controller
@RequestMapping(value = "/user")
public class UserQueryController extends BaseController {

	// 系统消息推送
	@Resource(name = "systemSerivce")
	private SystemSerivce systemSerivce;
	@Resource(name = "userLoginService")
	private UserLoginService userLoginService;
	@Resource(name = "userOperateService")
	private UserOperateService userOperateService;

	/**
	 * 查询接口 1开头
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query() {
		logBefore(logger, "调用查询接口");
		PageData pd = this.getPageData();
		try {
			int num = Integer.parseInt(pd.getString("STYPE"));
			switch (num) {
			case 100001:// 用户登录
				return userLoginService.getUser(pd);
			case 100002:// 首页查询接口
				return userLoginService.getUserByExmaple(pd);
			case 100008:// 个人资料查询查询接口
				return userLoginService.getUserById(pd);
			case 100012:// 兴趣查询接口
				return userLoginService.getUserInterestByMap(pd);
			case 100013:// 职业技能查询接口
				return userLoginService.getUserSKills(pd);
			case 100014:// 累计收入明细
				return userLoginService.getUserIncomings(pd);
			case 100015:// 消费记录明细
				return userLoginService.getCounsumeInfo(pd);
			case 1003:// 获取验证码
				return userLoginService.getMsgCode(pd);
			case 100119:// 查询我关注的人
				return userLoginService.getCarePeople(pd, 1);
			case 100120:// 查询总资产
				return userLoginService.getTotalMoneys(pd);
			case 100021:// 查询关注我的人
				return userLoginService.getCarePeople(pd, 2);
			case 100022:// 根据多个userId查询用户信息
				return userLoginService.getUserByIds(pd);
			case 100023:// 查询图像个数
				return userLoginService.getAblumNumber(pd);
			case 100124:// 查询优惠券和资产
				return userLoginService.getDiscountAndMoney(pd);
			case 100025:// 查询身份证认证信息
				return userLoginService.getIDInfo(pd);
			case 100026:// 查询职业证认证信息
				return userLoginService.getSkillCheck(pd);

			default:
				return ResultUtil.getResult(ResultCodeUtil.CODE_NOT_EXIST_300,
						"接口编码不存在", null);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return ResultUtil
					.getResult(ResultCodeUtil.ERROR_200, "服务器异常", null);
		} finally {
			logAfter(logger);
		}
	}

	/**
	 * 写入接口 2开头
	 */
	@RequestMapping(value = "/write")
	@ResponseBody
	public Object write() {

		logBefore(logger, "调用写入接口");
		PageData pd = this.getPageData();
		try {
			int num = Integer.parseInt(pd.getString("STYPE"));
			switch (num) {
			case 200001:// 普通用户注册
				return userOperateService.saveUser(pd);
			case 200004:// 个人资料修改
				return userOperateService.updateUser(pd);
			case 200005:// 个人头像修改
				return userOperateService.updateUserImage(pd,
						(MultipartHttpServletRequest) request);
			case 200006:// 相册删除图片
				return userOperateService.removePics(pd);
			case 200007:// 相册新增图片
				return userOperateService.savePics(pd,
						(MultipartHttpServletRequest) request);
			case 200009:// 兴趣添加
				return userOperateService.saveInterest(pd);
			case 200010:// 兴趣删除
				return userOperateService.removeInterest(pd);
			case 200011:// 职业技能添加
				return userOperateService.saveProfessionalSkill(pd);
			case 200020:// 关注
				return userOperateService.saveCare(pd);
			case 200120:// 取消关注
				return userOperateService.cancelCare(pd);
			case 200023: // 邀请好友
				return null;
			case 200036:// 添加用户反馈意见
				return userOperateService.saveFeedBack(pd);
			case 200037:// 修改密码
				return userOperateService.updatePassword(pd);
			case 200038:// 找回密码
				return userOperateService.resetPassword(pd);
			case 200039:// 身份证认证
				return userOperateService.saveID(pd,
						(MultipartHttpServletRequest) request);
			case 200040:// 职业认证
				return userOperateService.saveProfessionalInfo(pd,
						(MultipartHttpServletRequest) request);

			default:
				return ResultUtil.getResult(ResultCodeUtil.CODE_NOT_EXIST_300,
						"接口编码不存在", null);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return ResultUtil
					.getResult(ResultCodeUtil.ERROR_200, "服务器异常", null);
		} finally {
			logAfter(logger);
		}

	}

}
