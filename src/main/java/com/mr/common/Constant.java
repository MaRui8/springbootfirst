package com.mr.common;

/**
 * @author Mr
 * @date 2021/7/24
 */
public interface Constant {


	/**
	 * 登录后的用户http session参数
	 */
	String HTTP_SESSION_ATTRIBUTE = "userName";

	/**
	 * http session与websocket session关联的key
	 */
	String HTTP_SESSION_WEBSOCKET_SESSION_RELATION_KEY = "session";

	/**
	 * 一天的毫秒数
	 */
	int ONE_DAY_MILLISECOND = 86400000;

}
