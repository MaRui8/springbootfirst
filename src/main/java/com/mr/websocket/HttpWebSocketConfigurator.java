package com.mr.websocket;

import com.mr.common.Constant;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class HttpWebSocketConfigurator extends ServerEndpointConfig.Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		HttpSession session = (HttpSession) request.getHttpSession();
		if (null != session) {
			sec.getUserProperties().put(Constant.HTTP_SESSION_WEBSOCKET_SESSION_RELATION_KEY, session);
		}
	}
}
