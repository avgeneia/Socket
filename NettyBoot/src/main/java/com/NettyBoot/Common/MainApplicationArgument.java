package com.NettyBoot.Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MainApplicationArgument implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private ApplicationArguments applicationArguments;
 
	static Map<String, String> appArg = new HashMap<String, String>();
	
	public static Map<String, String> getAppArg() {
		return appArg;
	}

	public static void setAppArg(Map<String, String> appArg) {
		MainApplicationArgument.appArg = appArg;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
	    Set<String> optionNames = applicationArguments.getOptionNames();
 
	    for (String optionName : optionNames) {
	    	
	    	List<String> optionValues = applicationArguments.getOptionValues(optionName);
	    	
	    	for (String optionValue : optionValues) {
	    		
	    		appArg.put(optionName, optionValue);
	    	}
	    }
	}
}