package com.mr.listener;

import com.mr.config.SystemInitParam;
import com.mr.controller.UserController;
import org.apache.catalina.connector.RequestFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mr
 * @date 2020/5/5
 */
@Component
@WebFilter
public class LegalClientFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Resource
	private SystemInitParam systemInitParam;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String remoteAddr = request.getRemoteAddr();
		logger.info("remoteAddr visit:{}", ((RequestFacade) request).getRequestURI());
		if (!isLegalClient(remoteAddr)) {
			logger.info("illegal client visited, addr:{}", remoteAddr);
			response.getOutputStream().write("<h1>fuck away bitch!".getBytes(StandardCharsets.UTF_8));
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isLegalClient(String ip) {
		String whiteClientPath = systemInitParam.getWhiteClientPath();
		try {
			byte[] bytes = Files.readAllBytes(new File(Thread.currentThread().getContextClassLoader().getResource("").getPath(), whiteClientPath).toPath());
			String[] legalIps = new String(bytes, StandardCharsets.UTF_8).split("(\r\n)|(\n)");
			List<String> legalIpList = Arrays.asList(legalIps);
			return legalIpList.contains(ip);
		} catch (IOException e) {
			logger.error("read legal client file error", e);
		}
		return true;
	}
}
