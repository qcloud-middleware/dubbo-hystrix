package com.tencent.tsf.dubbo.hystrix.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public class ConfigurationManager {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

	private Properties properties = new Properties();

	public ConfigurationManager(String fileName) {
		init(fileName);
	}

	private void init(String fileName) {
		try {
			this.properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			logger.error("failed to load configuration file.", e);
		}
		logger.info("Load configuration file successfully.");
	}

	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}

	public Map<String, String> getPropertyMap() {
		Map<String, String> map = new HashMap<String, String>();

		for (String key : this.properties.stringPropertyNames())
			map.put(key, this.properties.getProperty(key));

		return map;
	}
}