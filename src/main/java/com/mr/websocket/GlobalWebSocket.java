package com.mr.websocket;

import com.mr.common.Cache;
import com.mr.common.Constant;
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

	private static final Map<String, Session> USER_SESSION_MAP = new HashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("userName") String userName) {
		//方式一、使用请求参数名传递用户
		InetSocketAddress remoteAddress = WebsocketUtil.getRemoteAddress(session);
		logger.info(remoteAddress.toString(), " connected");
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
//        HttpSession httpSession = (HttpSession)session.getUserProperties().get(Constant.HTTP_SESSION_WEBSOCKET_SESSION_RELATION_KEY);
//        if(null == httpSession){
//            try {
//                session.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println(session.getBasicRemote()+"/"+((User)httpSession.getAttribute(Constant.HTTP_SESSION_ATTRIBUTE)).getUserName());

		synchronized (USER_SESSION_MAP) {
			session.getUserProperties().put(Constant.HTTP_SESSION_ATTRIBUTE, userName);
			USER_SESSION_MAP.put(userName, session);
		}
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		logger.info("receive:{} message:{}", session.getBasicRemote(), message);
	}

	@OnClose
	public void onClose(Session session) {
		String websocketUser = (String) session.getUserProperties().get(Constant.HTTP_SESSION_ATTRIBUTE);
		USER_SESSION_MAP.remove(websocketUser);
		logger.info(websocketUser + "已关闭连接");
	}

	@OnError
	public void onError(Session session, Throwable t) {
		String websocketUser = (String) session.getUserProperties().get(Constant.HTTP_SESSION_ATTRIBUTE);
		USER_SESSION_MAP.remove(websocketUser);
		logger.error(websocketUser + "强制关闭连接", t);
	}

	public static void sendMessageToUser(WebSocketMessage webSocketMessage) {
		synchronized (USER_SESSION_MAP) {
			Session session = USER_SESSION_MAP.get(webSocketMessage.getReceiverName());
			if (null == session) {
				if (Cache.getExistUserNameList().contains(webSocketMessage.getReceiverName())) {
					Cache.addUserMessage(webSocketMessage.getReceiverName(), webSocketMessage);
				}
				return;
			}
			try {
				webSocketMessage.setBrowserType(Cache.getUser(webSocketMessage.getReceiverName()).getBrowserType());
				session.getBasicRemote().sendText(JsonUtil.MAPPER.writeValueAsString(webSocketMessage));
				Cache.removeUserMessage(webSocketMessage.getReceiverName(), webSocketMessage.getNo());
			} catch (IOException e) {
				logger.error("globalWebSocket send message to client error ", e);
			}
		}
	}

}
