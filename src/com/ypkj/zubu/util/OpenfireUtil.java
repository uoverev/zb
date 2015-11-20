package com.ypkj.zubu.util;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class OpenfireUtil {
	

	public static void main(String[] args) throws Exception {
		createAccount("hzh3221", "231012");
	}

	/** 注册openfire用户* */
	public static void createAccount(final String username, final String password) {
		new Thread(new Runnable() {
			public void run() {
				Connection connection = null;
				try {
					XMPPConnection.DEBUG_ENABLED = false;
					AccountManager accountManager;
					ConnectionConfiguration connectionConfig = new ConnectionConfiguration(
							"xysh1991.vicp.net", Integer.parseInt("5222"), "tec-xysh");
					// 允许自动连接
					connectionConfig.setReconnectionAllowed(true);
					connectionConfig.setSendPresence(true);
					connection = new XMPPConnection(connectionConfig);
					connection.connect();// 开启连接
					accountManager = connection.getAccountManager();// 获取账户管理类
					accountManager.createAccount(username, password);
				} catch (XMPPException e) {
					e.printStackTrace();
				} catch (Exception e) {
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).run();
		

	}
}
