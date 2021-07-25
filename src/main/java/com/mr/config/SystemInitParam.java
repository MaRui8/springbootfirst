package com.mr.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Mr
 * @date 2021/7/24
 */
@SpringBootConfiguration
@ConfigurationProperties(prefix = "system.init")
public class SystemInitParam {


	private String whiteClientPath;

	public String getWhiteClientPath() {
		return whiteClientPath;
	}

	public void setWhiteClientPath(String whiteClientPath) {
		this.whiteClientPath = whiteClientPath;
	}
}
