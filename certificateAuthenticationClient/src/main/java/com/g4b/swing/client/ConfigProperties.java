package com.g4b.swing.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/***
 * @ClassName: ConfigProperties  
 * @Description: TODO 类初始化属性类
 * @author NiePengXiang
 * @date 2018年7月2日  
 *
 */
public class ConfigProperties {
	
	private static Logger logger = Logger.getLogger(ConfigProperties.class);
	
	private static FileChangedReloadingStrategy reloadingStrategy ;
	
	private static PropertiesConfiguration clinetProperties;
	
	private static final String SRCPATH = ConfigProperties.class.getClassLoader().getResource("//").getPath();
	
	/**初始化类*/
	static {
		try {
			reloadingStrategy = new FileChangedReloadingStrategy();
			reloadingStrategy.setRefreshDelay(Long.parseLong("1000"));
			clinetProperties = new PropertiesConfiguration();
			clinetProperties.setReloadingStrategy(reloadingStrategy);
			String path = new File(SRCPATH).getParentFile().getParentFile().getPath() + "/src/main/resources/config.properties";
			clinetProperties.load(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		} catch (Exception ex) {
			logger.error("ConfigUtils 初始化失败！", ex);
		}
	}
	
	/**
	 * @Title: loadder  
	 * @Description: TODO 根据Key获取Value  
	 * @param @param key
	 * @param @return    参数  
	 * @return String    返回类型  
	 * @throws
	 */
	public static String loadder(String key) {
		String value = null;
		if (StringUtils.isNotBlank(key)) {
			value = clinetProperties.getString(key);
		}
		return value;
	}
}
