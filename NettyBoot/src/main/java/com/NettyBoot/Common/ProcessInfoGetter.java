package com.NettyBoot.Common;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class ProcessInfoGetter {
	private static String pid = null;
	
	static {
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		String[] info = rt.getName().split("@");
		pid = "PID=" + info[0] + " / HOSTNAME=" + info[1]+" ";
	}
	
	public static String getPid(){
		return pid;
	}
	
	public static Boolean isWindows() {
		String OS = System.getProperty("os.name").toLowerCase();
		Boolean isWin = false;
		
		if (OS.indexOf("win") >= 0) 
			isWin = true;
		else
			isWin = false;
		
		return isWin;
	}
}
