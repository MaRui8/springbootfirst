package com.mr.listener;

import com.mr.common.Cache;
import com.mr.common.Constant;
import com.mr.pojo.User;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		Cache.removeOnlineUser(((User) se.getSession().getAttribute(Constant.HTTP_SESSION_ATTRIBUTE)).getUserName());
	}
}
