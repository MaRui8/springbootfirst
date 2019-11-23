package com.mr.task;

import com.mr.common.Cache;
import com.mr.websocket.GlobalWebSocket;
import com.mr.websocket.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheMessageSendTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(CacheMessageSendTask.class);

    private String userName;

    public CacheMessageSendTask(String userName) {
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            logger.error("sleep interrupted ->", e);
        }
        sendCacheMessageToUser(userName);
    }

    private void sendCacheMessageToUser(String userName) {
        List<WebSocketMessage> userMessage = Cache.getUserMessage(userName);
        if (null != userMessage) {
            userMessage.forEach(GlobalWebSocket::sendMessageToUser);
        }
    }
}
