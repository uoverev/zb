package com.ypkj.zubu.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.Object;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class PushUtil {
//    protected static final Logger LOG = LoggerFactory.getLogger(PushExample.class);
	
	private static final String appKey = "8409965fff52727a4f5d6bb7";
	private static final String masterSecret = "cafb3335043b4d53d0216f9e";
	
	public static final String TITLE = "发货通知！";
    public static final String ALERT = "您买的商品已发货，请注意查收！";
    public static final String MSG_CONTENT = "您买的商品已发货，请注意查收！";
    public static final String REGISTRATION_ID = "020c1e3a0b6";
    public static final String TAG = "tag_api";

	public static void main(String[] args) {
	   		 List<String>  list1=new ArrayList<String>();
	    	list1.add("04181f59177");
	    	list1.add("071fe8febcb");
	    	list1.add("070f370f865");
	    	SendPushToMany(list1,"有跑客发布了一条买时间的任务!","1","12");
	}
	
	
	public static void SendPushToMany(List<String> registrationIds,String alert,String taskNum,String messagetype) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payload = buildPushObject_android_and_ios(registrationIds,alert, taskNum, messagetype);
        System.out.println("传入的registrationId"+registrationIds);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result+"这里是推送返回的数据");
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
        	e.printStackTrace();
        }
	}
	
	/**
	 * 
	 * @param registrationIds
	 * @param taskNum
	 * @param messagetype
	 */
	public static void SendPushToManyNoGrab(List<String> registrationIds,String taskNum,String messagetype) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payload = buildPushObject_android_and_iosMany(registrationIds, taskNum, messagetype);
        System.out.println("传入的registrationId"+registrationIds);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result+"这里是推送返回的数据");
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
        	e.printStackTrace();
        }
	}
	
	/**
	 * 推送给单个用户(通用)
	 * @param registrationId
	 * @param extras
	 */
	public static void SendPushToOnePub(String registrationId,String msg,Map<String, String> extras ) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payload = buildPushObject_android_and_iosPub(registrationId,msg,extras);
        System.out.println("传入的registrationId"+registrationId);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result+"这里是推送返回的数据");
            
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
        	e.printStackTrace();
        }
	}
	
	 //任务被接受了 ，推送一条透传给发布任务的用户
    public static PushPayload buildPushObject_android_and_iosPub(String registrationId,String msg,Map<String, String> extras )  {
    	System.out.println("刚刚发送了一条任务接受透传消息");
    	return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationId))
                .setMessage(Message.newBuilder()
                		.setMsgContent(msg).addExtras(extras).build())
                .build();
    }
	
	
	/**
	 * 
	 * @param registrationId 设备标示
	 * @param text 消息类型说明
	 * @param noReadNum 消息未读数量
	 * @param type 推送消息类型
	 * @param uid 被推送消息的用户id
	 */
	public static void SendPushToOneMessage(String registrationId,String text,String noReadNum,String type,String uid) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payload = buildPushObject_android_and_ios9( registrationId, text,noReadNum,type,uid);
        System.out.println("传入的registrationId："+registrationId+text+"的推送消息");
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result+"这里是推送返回的数据");
            
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
        	e.printStackTrace();
        }
	}
	
	//消息透传（1、任务有人抢单/被接受了、2、申请加为好友了、3、动态被回复了、4、动态被点赞了、5、动态被转发、6、验证审核消息 7、好友发布动态    8、系统消息、）
    public static PushPayload buildPushObject_android_and_ios9(String registrationId,String text,String noReadNum,String type,String uid) {
    	System.out.println("刚刚发送了一条任务接受透传消息");
    	return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationId)).setMessage(Message.newBuilder()
                		.setMsgContent("title").setTitle("消息")
                		.addExtra("text", text)
                		.addExtra("noReadNum", noReadNum)
                		.addExtra("uid", uid)
                		.addExtra("messageType", type).build())
                .build();
    }			
	
	//向所有用户发送一条消息
	public static PushPayload buildPushObject_all_all_alert(String msgContent) {
	    return PushPayload.messageAll(msgContent);
	}
	
    
    
    
    //平台是Android与ios   推送目标是registrationIds的集合  推送的是通知
    public static PushPayload buildPushObject_android_and_ios(List<String> registrationIds,String alert,String taskNum,String messagetype) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.newBuilder()
                		.setAlert(alert)
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle("任务通知！").addExtra("text", "").setBuilderId(1).addExtra("taskNum", taskNum).addExtra("messageType", messagetype).build())
                		.addPlatformNotification(IosNotification.newBuilder().addExtra("text", "").addExtra("taskNum", taskNum).addExtra("messageType", messagetype)
                				.incrBadge(1)
                				.build())
                		.build())
                .build();
    }
    
    //平台是Android与ios   推送目标是registrationIds的集合  推送的是通知
    public static PushPayload buildPushObject_android_and_iosMany(List<String> registrationIds,String taskNum,String messagetype) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.newBuilder()
                		.setAlert("您抢的任务没有被选中噢,再接再厉吧,么么哒！")
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle("任务通知！").addExtra("text", "").setBuilderId(1).addExtra("taskNum", taskNum).addExtra("messageType", messagetype).build())
                		.addPlatformNotification(IosNotification.newBuilder()
                				.incrBadge(1)
                				.build())
                		.build())
                .build();
    }
    
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                 .setMessage(Message.content(MSG_CONTENT))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                 .build();
    }
    
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(MSG_CONTENT)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }
    
    
}

