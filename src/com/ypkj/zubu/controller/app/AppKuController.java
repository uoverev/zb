package com.ypkj.zubu.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ypkj.zubu.controller.base.BaseController;
import com.ypkj.zubu.service.cash.CashService;
import com.ypkj.zubu.service.comment.CommetService;
import com.ypkj.zubu.service.dynamic.DpraiseService;
import com.ypkj.zubu.service.dynamic.MydynamicService;
import com.ypkj.zubu.service.dynamic.ShareService;
import com.ypkj.zubu.service.freeTalk.FreeTalkService;
import com.ypkj.zubu.service.mbrprice.HypricesService;
import com.ypkj.zubu.service.newbie.NewbieService;
import com.ypkj.zubu.service.order.OrderService;
import com.ypkj.zubu.service.partner.PartnerService;
import com.ypkj.zubu.service.system.user.UserQrCodeService;
import com.ypkj.zubu.service.systemMessage.SystemSerivce;
import com.ypkj.zubu.service.task.TaskService;
import com.ypkj.zubu.service.userfs.AppkuCService;
import com.ypkj.zubu.service.usermanger.UserLoginService;
import com.ypkj.zubu.service.usermanger.UserOperateService;
import com.ypkj.zubu.service.visitorsrecord.VisitorsrecordService;
import com.ypkj.zubu.util.AppUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;

/**
 * app-产品详情-接口类 手机app接口实例
 * 
 * 相关参数协议： 1 操作成功 -1 参数不完整 -2 接口编号不存在 -3服务器内部错误 -4业务验证错误
 */
@Controller
@RequestMapping(value = "/appKu")
public class AppKuController extends BaseController
{

	@Resource(name = "appkuCService")
	// 粉丝接口
	private AppkuCService appkuCService;

	@Resource(name = "mydynamicservice")
	// 我的动态接口
	private MydynamicService mydynamicservice;

	@Resource(name = "newbieService")
	// 新手指引
	private NewbieService newbieService;

	@Resource(name = "hypriceService")
	// 会员接口
	private HypricesService hypriceService;

	// @Resource(name="hypricesService")//会员接口
	// private HypricesService hypricesService;

	@Resource(name = "visitorsrecordService")
	// 访客接口
	private VisitorsrecordService visitorsrecordService;

	@Resource(name = "dpraiseService")
	// 点赞接口
	private DpraiseService dpraiseService;

	@Resource(name = "commetService")
	// 评论接口
	private CommetService commetService;

	@Resource(name = "cashService")
	// 提现接口
	private CashService cashService;

	@Resource(name = "shareService")
	// 分享接口
	private ShareService shareService;

	@Resource(name = "systemSerivce")
	// 系统消息推送
	private SystemSerivce systemSerivce;
	// @Resource(name="kuService")
	// private KuService kuService;
	@Resource(name = "userLoginService")
	private UserLoginService userLoginService;
	@Resource(name = "userOperateService")
	private UserOperateService userOperateService;
	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "orderService")
	private OrderService orderService;

	@Resource(name = "partnerService")
	private PartnerService partnerService;

	@Resource(name = "freeTalkService")
	private FreeTalkService freeTalkService;
	
	@Resource(name="userQrCodeService")
	private UserQrCodeService userQrCodeService;

	/**
	 * 测试
	 */
	@RequestMapping(value = "/test")
	@ResponseBody
	public Object shenqing()
	{
		String result = "01";
		logBefore(logger, "测试");
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		try
		{
			System.out.println(pd);
			result = "01";
		} catch (Exception e)
		{
			logger.error(e.toString(), e);
		} finally
		{
			map.put("result", result);
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 查询接口 1开头
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query()
	{
		String result = "00";
		logBefore(logger, "调用查询接口");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		try
		{
			// if (AppUtil.checkParam("interface", pd)) {
			result = "1";
			int num = Integer.parseInt(pd.getString("STYPE"));
			switch (num)
			{
			case 100001:// 用户登录
				return userLoginService.getUser(pd);
			case 100002:// 首页查询接口
				return userLoginService.getUserByExmaple(pd);
			case 100003:// 所有粉丝信息查询
				return appkuCService.fslist(pd);
			case 100004:// 我的动态查询
				return mydynamicservice.Mydtlist(pd);
			case 100005:// 查询我的任务(发布的/已抢单)
				return taskService.findMyTask(pd);
			case 100006:// 新手指引查询
				return newbieService.newbilezylist();
			case 100007:// 动态查询接口
				return mydynamicservice.dynamiclist(pd);
			case 100008:// 个人资料查询查询接口
				return userLoginService.getUserById(pd);
			case 100009:// 查询任务列表(买/卖)
				return taskService.findNearTaskList(pd);
			case 100010:// 查询抢单人列表
				return taskService.findRunnersList(pd);
			case 100011:// 会员价格查询接口
				return hypriceService.findhHpricelist(pd);
			case 100012:// 兴趣查询接口
				return userLoginService.getUserInterestByMap(pd);
			case 100013:// 职业技能查询接口
				return userLoginService.getUserSKills(pd);
			case 100014:// 累计收入
				return userLoginService.getUserIncomings(pd);
			case 100015:// 消费记录
				return userLoginService.getCounsumeInfo(pd);
			case 1003:// 获取验证码
				return userLoginService.getMsgCode(pd);

			case 100016:// 现金提现查询接口
				return cashService.querycasht(pd);
			case 100017:// 查询任务详情
				return taskService.findTaskDetails(pd);
			case 100118:// 查询相册图像
				return userLoginService.getUserAblum(pd);
			case 100019:// 查询申请合伙人接口
				return partnerService.findApplyPartner(pd);
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
			case 100024:// 访客记录查询接口
				return visitorsrecordService.Visitorsrecordlist(pd);
			case 100018:// 评论查询接口
				return commetService.commentlist(pd);
			case 100020:// 获取订单信息
				return orderService.getOrder(pd);
			case 100030:// 获取订单信息
				return orderService.getOrder(pd);
			case 100031://支付宝密码验证
				return cashService.Zfbpasswordverify(pd);
			case 100032:// 系统消息推送查询
				return systemSerivce.systemtsmsg(pd);
			case 100033://获取邀请好友验证码
				return userQrCodeService.updateGetQrCode(pd,request);
			default:
				return ResultUtil.getResult(ResultCodeUtil.CODE_NOT_EXIST_300, "接口编码不存在", null);
			}
			// } else {
			// result = "-1";
			// }
		} catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "服务器异常", null);
		} finally
		{
			logAfter(logger);
		}
	}

	/**
	 * 写入接口 2开头
	 */
	@RequestMapping(value = "/write")
	@ResponseBody
	public Object write()
	{

		String result = "00";
		logBefore(logger, "调用写入接口");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		try
		{
			// if (AppUtil.checkParam("interface", pd)) {
			int num = Integer.parseInt(pd.getString("STYPE"));
			switch (num)
			{
			case 200001:// 普通用户注册
				return userOperateService.saveUser(pd);
			case 200002:// 发布任务
				return taskService.saveTask(pd);
			case 200003:// 动态发布接口
				return mydynamicservice.save_dt(pd,(MultipartHttpServletRequest) request);
			case 200004:// 个人资料修改
				return userOperateService.updateUser(pd);
//			case 200008:// 访客记录添加接口
//				return visitorsrecordService.saveVisitorsrecord(pd);
			case 200005:// 个人头像修改
				return userOperateService.updateUserImage(pd, (MultipartHttpServletRequest) request);
			case 200006:// 相册删除图片
				return userOperateService.removePics(pd);
			case 200007:// 相册新增图片
				return userOperateService.savePics(pd, (MultipartHttpServletRequest) request);
			case 200009:// 兴趣添加
				return userOperateService.saveInterest(pd);
			case 200010:// 兴趣删除
				return userOperateService.removeInterest(pd);
			case 200011:// 职业技能添加
				return userOperateService.saveProfessionalSkill(pd);
			case 200013:// 添加评论
				return commetService.savecomment(pd);
			case 200014:// 转发
				return mydynamicservice.savezf(pd);
			case 200015:// 点赞
				return dpraiseService.updateDp(pd);
			case 200016:// 任务评价
				return taskService.saveEvaluation(pd);
			case 200017:// 跑客抢单
				return taskService.saveRobOrder(pd);
			case 200018:// 确认选择跑客
				return taskService.updateConfirmSelect(pd);
			case 200019:// 确认完成任务
				return taskService.updateCompleteTask(pd);
			case 200020:// 关注
				return userOperateService.saveCare(pd);
			case 200120:// 取消关注
				return userOperateService.cancelCare(pd);

			case 200022:// 会员开通
				return hypriceService.saveMemberdredge(pd);
			case 200023: // 邀请好友
				return null;
			case 200024: // 分享
				return shareService.saveshare(pd);
			case 200025: // 现金充值
				return cashService.savecashcz(pd);
			case 200026:// 现金提现
				return cashService.updatecashtx(pd);
			case 200027:// 申请合伙人
				return partnerService.saveApplyPartner(pd);
			case 200030:// 取消任务
				return taskService.updateCancelTask(pd);
			case 200031:// 申请退款
				return taskService.saveApplyRefund(pd);
			case 200032:// 同意/不同意退款
				return taskService.updateAgreeRefund(pd);
			case 200033:// 支付任务订单
				return orderService.updatePayOrder(pd);
			case 200034:// 申诉接口
				return taskService.saveComplaint(pd, (MultipartHttpServletRequest) request);
			case 200035:// 用户设置推送免打扰时间段
				return freeTalkService.saveFreeTalk(pd);
			case 200036:// 添加用户反馈意见
				return userOperateService.saveFeedBack(pd);
			case 200037:// 修改密码
				return userOperateService.updatePassword(pd);
			case 200038:// 找回密码
				return userOperateService.resetPassword(pd);
			case 200039://身份证认证
				return userOperateService.saveID(pd,(MultipartHttpServletRequest) request);
			case 200040://职业认证
				return userOperateService.saveProfessionalInfo(pd,(MultipartHttpServletRequest) request);
				
			default:
				return ResultUtil.getResult(ResultCodeUtil.CODE_NOT_EXIST_300, "接口编码不存在", null);
			}
			/*
			 * result = "1"; } else { result = "-1"; }
			 */
		} catch (Exception e)
		{
			logger.error(e.toString(), e);
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "服务器异常", null);
		} finally
		{
			logAfter(logger);
		}

	}
	

}
