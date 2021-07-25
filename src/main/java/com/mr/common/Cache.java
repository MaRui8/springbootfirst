package com.mr.common;

import com.mr.pojo.User;
import com.mr.websocket.WebSocketMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

	/**
	 * 待接收的消息缓存
	 * key:用户名
	 * value:消息对象
	 */
	private static final Map<String, List<WebSocketMessage>> USER_MESSAGE_MAP = new ConcurrentHashMap<>(5);

	/**
	 * 获取待接收的消息
	 *
	 * @return
	 */
	public static List<WebSocketMessage> getUserMessage(String userName) {
		synchronized (USER_MESSAGE_MAP) {
			return USER_MESSAGE_MAP.get(userName);
		}
	}

	/**
	 * 添加待接收消息
	 *
	 * @param userName         接收者
	 * @param webSocketMessage 消息对象
	 */
	public static void addUserMessage(String userName, WebSocketMessage webSocketMessage) {
		synchronized (USER_MESSAGE_MAP) {
			List<WebSocketMessage> webSocketMessageList = USER_MESSAGE_MAP.computeIfAbsent(userName, key -> new ArrayList<>());
			webSocketMessageList.add(webSocketMessage);
		}
	}


	/**
	 * 移除待接收消息
	 *
	 * @param messageNo 消息序号
	 * @return 成功/失败
	 */
	public static boolean removeUserMessage(String userName, int messageNo) {
		List<WebSocketMessage> webSocketMessages = USER_MESSAGE_MAP.get(userName);
		if (null == webSocketMessages) {
			return true;
		}
		for (WebSocketMessage item : webSocketMessages) {
			if (messageNo == item.getNo()) {
				return webSocketMessages.remove(item);
			}
		}
		return true;
	}

	/**
	 * 在线用户
	 */
	private static final Map<String, User> ONLINE_USER_MAP = new ConcurrentHashMap<>(5);

	public static void addOnlineUser(String userName, User user) {
		synchronized (ONLINE_USER_MAP) {
			ONLINE_USER_MAP.put(userName, user);
		}
	}

	public static boolean userIsOnline(String userName) {
		synchronized (ONLINE_USER_MAP) {
			return ONLINE_USER_MAP.containsKey(userName);
		}
	}

	public static User getUser(String userName) {
		synchronized (ONLINE_USER_MAP) {
			return ONLINE_USER_MAP.get(userName);
		}
	}

	public static void removeOnlineUser(String userName) {
		synchronized (ONLINE_USER_MAP) {
			ONLINE_USER_MAP.remove(userName);
		}
	}


	/**
	 * 缓存用户发送的文件，用于定时删除
	 */
	private static final Map<Integer, WebSocketMessage> CACHE_FILE_MAP = new ConcurrentHashMap<>();

	public static Map<Integer, WebSocketMessage> getCacheFileMap() {
		return CACHE_FILE_MAP;
	}

	public static WebSocketMessage addCacheFile(WebSocketMessage userExchangeFile) {
		synchronized (CACHE_FILE_MAP) {
			return CACHE_FILE_MAP.put(userExchangeFile.getNo(), userExchangeFile);
		}
	}

	/**
	 * 存在的用户名
	 */
	private static List<String> existUserNameList = new ArrayList<>();

	public static List<String> getExistUserNameList() {
		return existUserNameList;
	}
}
