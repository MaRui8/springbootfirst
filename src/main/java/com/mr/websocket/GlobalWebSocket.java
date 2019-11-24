package com.mr.websocket;

import com.mr.common.Cache;
import com.mr.common.JsonUtil;
import com.mr.common.WebsocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint(value = "/globalWebSocket/{userName}", configurator = HttpWebSocketConfigurator.class)
public class GlobalWebSocket {

    private static Logger logger = LoggerFactory.getLogger(GlobalWebSocket.class);

    private static Map<String, Session> userSessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        //方式一、使用请求参数名传递用户
        InetSocketAddress remoteAddress = WebsocketUtil.getRemoteAddress(session);
        logger.info(remoteAddress.toString()," connected");
        if (null == userName || !Cache.userIsOnline(userName)) {
            try {
                session.getBasicRemote().sendText("scram ! bitch!");
                session.close();
            } catch (IOException e) {
                logger.error("close illegal connect error ", e);
            }
            return;
        }
//        方式二、使用配置类传递用户 @ServerEndpoint添加,configurator = HttpWebSocketConfigurator.class
//        HttpSession httpSession = (HttpSession)session.getUserProperties().get("session");
//        if(null == httpSession){
//            try {
//                session.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println(session.getBasicRemote()+"/"+((User)httpSession.getAttribute("userName")).getUserName());

        synchronized (userSessionMap) {
            session.getUserProperties().put("userName", userName);
            userSessionMap.put(userName, session);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(session.getBasicRemote());
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) {
        userSessionMap.remove(session.getUserProperties().get("userName"));
        logger.info(session.getUserProperties().get("userName") + "已关闭连接");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        userSessionMap.remove(session.getUserProperties().get("userName"));
        logger.error(session.getUserProperties().get("userName") + "强制关闭连接", t.getCause());
    }

    public static void sendMessageToUser(WebSocketMessage webSocketMessage) {
        synchronized (userSessionMap) {
            Session session = userSessionMap.get(webSocketMessage.getReceiverName());
            if (null == session) {
                if (Cache.getExistUserNameList().contains(webSocketMessage.getReceiverName())) {
                    Cache.addUserMessage(webSocketMessage.getReceiverName(), webSocketMessage);
                }
                return;
            }
            try {
                webSocketMessage.setBrowserType(Cache.getUser(webSocketMessage.getReceiverName()).getBrowserType());
                session.getBasicRemote().sendText(JsonUtil.mapper.writeValueAsString(webSocketMessage));
                Cache.removeUserMessage(webSocketMessage.getReceiverName(), webSocketMessage.getNo());
            } catch (IOException e) {
                logger.error("globalWebSocket send message to client error ", e);
            }
        }
    }

}
