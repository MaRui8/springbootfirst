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
    private static Map<String, List<WebSocketMessage>> userMessageMap = new ConcurrentHashMap<>(5);

    /**
     * 获取待接收的消息
     *
     * @return
     */
    public static List<WebSocketMessage> getUserMessage(String userName) {
        synchronized (userMessageMap) {
            List<WebSocketMessage> webSocketMessageList = userMessageMap.get(userName);
            return webSocketMessageList;
        }
    }

    /**
     * 添加待接收消息
     * @param userName 接收者
     * @param WebSocketMessage 消息对象
     */
    public static void addUserMessage(String userName, WebSocketMessage WebSocketMessage) {
        synchronized (userMessageMap) {
            List<WebSocketMessage> WebSocketMessageList = userMessageMap.get(userName);
            if (null == WebSocketMessageList) {
                WebSocketMessageList = new ArrayList<>();
                userMessageMap.put(userName,WebSocketMessageList);
            }
            WebSocketMessageList.add(WebSocketMessage);
        }
    }


    /**
     * 移除待接收消息
     * @param messageNo 消息序号
     * @return 成功/失败
     */
    public static boolean removeUserMessage(String userName,int messageNo) {
        List<WebSocketMessage> webSocketMessages = userMessageMap.get(userName);
        if (null == webSocketMessages){
            return true;
        }
        for (WebSocketMessage item : webSocketMessages){
            if (messageNo == item.getNo()){
                return webSocketMessages.remove(item);
            }
        }
        return true;
    }

    /**
     * 在线用户
     */
    private static Map<String,User> onlineUserMap = new ConcurrentHashMap<>(5);

    public static void addOnlineUser(String userName, User user){
        synchronized (onlineUserMap) {
            onlineUserMap.put(userName, user);
        }
    }

    public static boolean userIsOnline(String userName){
        synchronized (onlineUserMap) {
            return onlineUserMap.containsKey(userName);
        }
    }
    public static User getUser(String userName){
        synchronized (onlineUserMap) {
            return onlineUserMap.get(userName);
        }
    }

    public static void removeOnlineUser(String userName){
        synchronized (onlineUserMap) {
            onlineUserMap.remove(userName);
        }
    }


    /**
     * 缓存用户发送的文件，用于定时删除
     */
    private static Map<Integer, WebSocketMessage> cacheFileMap = new ConcurrentHashMap<>();

    public static Map<Integer, WebSocketMessage> getCacheFileMap() {
        return cacheFileMap;
    }

    public static WebSocketMessage addCacheFile(WebSocketMessage userExchangeFile){
        synchronized (cacheFileMap){
            return cacheFileMap.put(userExchangeFile.getNo(),userExchangeFile);
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
