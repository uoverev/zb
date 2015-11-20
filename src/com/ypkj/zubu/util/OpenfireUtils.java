package com.ypkj.zubu.util;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class OpenfireUtils {
	/**
	 *定义connection
	 */
	private static Connection connection=null;
	static {
		try {
			ConnectionConfiguration config = new ConnectionConfiguration("192.168.1.2",5222);
		    config.setSASLAuthenticationEnabled(false); 
			connection = new XMPPConnection(config);
			connection.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 注册
	 * @param conn
	 * @param yhzh
	 * @param yhmm
	 * @return
	 */
	public static boolean Register(String yhzh,String yhmm){
		boolean flag = true;
		AccountManager amgr = connection.getAccountManager();
		try {
			amgr.createAccount(yhzh, yhmm);
			//更改用户状态代码：
			Presence presence = new Presence(Presence.Type.available);   //这里如果改成unavailable则会显示用户不在线
			presence.setStatus("Go fishing");
			connection.sendPacket(presence);
			connection.getRoster();
		} catch (XMPPException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 登陆
	 * @param yhzh
	 * @param yhmm
	 * @return
	 */
	public static boolean login(String yhzh,String yhmm){
		boolean flag = true;
		try {
			connection.login(yhzh, yhmm);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 发送消息
	 * @param yhzh
	 * @param yhmm
	 * @return
	 */
	public static boolean sendMsg(){
		boolean flag = true;
		
		return flag;
	}
	
	
	

}
