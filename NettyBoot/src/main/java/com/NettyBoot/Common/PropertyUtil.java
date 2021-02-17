package com.NettyBoot.Common;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class PropertyUtil {

	/** Logger */
	static Logger logger = LogManager.GetConfiguredLogger(PropertyUtil.class);;
	
	public static String getProperty(String propertyName) {
		
		return getProperty(propertyName, null);
	}
	
	public static String getProperty(String propertyName, String defaultValue) {
		
		String value = defaultValue;
		ApplicationContext applicationContext = ApplicationContextServe.getApplicationContext();
		
		if(applicationContext.getEnvironment().getProperty(propertyName) == null) {

			logger.warn(propertyName + " properties was not loaded.");
		} else {
			
			value = applicationContext.getEnvironment().getProperty(propertyName).toString();
		}
		return value;
	}
}