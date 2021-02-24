package com.NettyBoot.Common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * (ini파일에서 읽어온) 로그 설정을 모든 개별 클래스의 logger에 동일하게 적용하기 위한 
 * logger 설정 관리자
 * 
 * @author jymoon
 *
 */
public class LogManager {
	private static String logFileFullPath = "log.log";
	private static String datePattern = "'.'yyyy-MM-dd";
	static Level logLevel = Level.DEBUG;
	static List<Logger> listLogger = new ArrayList<Logger>();
	private static LogDelete logDel = null;
	private static Timer deleteScheduler = null;
	
	/**
	 * logger속성 설정
	 * 
	 * @param _logFileName : 로그파일명(폴더 경로 + 기본 파일명)
	 * @param strLevel : 로그 레벨("ALL", "DEBUG", "ERROR", "FATAL", "INFO", "OFF", "TRACE", "WARN" 중 하나)
	 * @param strDatePattern : 로그파일명에 붙는 날짜 형식
	 * @throws IOException
	 */
	public static void SetLoggerProperties(
			String _logFolder,
			String _logFileName, 
			String strLevel, 
			String strDatePattern,
			int expirationDay) throws IOException {
		
		// 로그 삭제 설정
		if (deleteScheduler == null) {
			logDel = new LogDelete();
			logDel.init(_logFolder, _logFileName, expirationDay);
			deleteScheduler = new Timer(true);
			if (expirationDay > 0)
				deleteScheduler.scheduleAtFixedRate(logDel, 1000, 24 * 60 * 60 * 1000);
			else
				deleteScheduler.scheduleAtFixedRate(logDel, 1000, 10000);
		}
		
		// 로그 파일명 설정
		if(ProcessInfoGetter.isWindows()) {
			logFileFullPath = _logFolder + "\\" + _logFileName;	
		} else {
			logFileFullPath = _logFolder + "//" + _logFileName;
		}
		
		datePattern = strDatePattern;
		
		// Level 설정
		List<String> listLevelStr = Arrays.asList(
				new String[] {"ALL", "DEBUG", "ERROR", "FATAL", "INFO", "OFF", "TRACE", "WARN"});
		Level[] levels = {Level.ALL, Level.DEBUG, Level.ERROR, Level.FATAL, Level.INFO, Level.OFF,
				Level.TRACE, Level.WARN};
		int idLevel = listLevelStr.indexOf(strLevel);
		if (idLevel >= 0) {
			logLevel = levels[idLevel];
		} else {	
			logLevel = Level.DEBUG;
		}
		
		// 기존 로거 속성 다시 설정
		for (int i = 0; i < listLogger.size(); i++) {
			listLogger.get(i).removeAllAppenders();
			AddAppender(listLogger.get(i));
		}
	}
	
	/**
	 * 설정된 속성에 맞춰진 logger 생성 
	 * 
	 * @param cls : logger를 연결할 클래스
	 * @return : 속성 설정 및 클래스 연결이 완료된 logger
	 */
	public static Logger GetConfiguredLogger(Class<?> cls) {
		Logger logger = Logger.getLogger(cls);
		
		AddAppender(logger);
		
		// logList에 추가
		listLogger.add(logger);
		
		return logger;
	}
	
	/**
	 * logger에 속성이 설정된 Appender 추가
	 * (GetConfiguredLogger의 Sub 함수)
	 * 
	 * @param logger : 속성 설죙된 Appender를 추가할 logger
	 */
	private static void AddAppender(Logger logger) {
		// path, 형식 설정
		PatternLayout patternlayout = new PatternLayout("[%d][%-5p][%F(%M):%L] %m%n");
		ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
		logger.addAppender(consoleAppender);
		
		ExtDailyRollingFileAppender appender = null;
		try {
			appender = new ExtDailyRollingFileAppender(patternlayout, logFileFullPath, datePattern);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addAppender(appender);
		
		// Level 설정
		logger.setLevel(logLevel);
		
	}
	
}

class LogDelete extends TimerTask {
	private String logFolder = "";
	private String logFileName = "";
	private int logHoldingDay = 90;
	private long logHoldingMillis = 90 * 24 * 60 * 60 * 1000;
	public void init(String _logFolder, String _logFileName, int _logHoldingDay) {
		logFolder = _logFolder;
		logFileName = _logFileName;
		logHoldingDay = _logHoldingDay;
		if (logHoldingDay > 0)
			logHoldingMillis = (long)logHoldingDay * 24 * 60 * 60 * 1000;
		else
			logHoldingMillis = (long)3 * 60 * 1000;
	}
   
	public void run() {
		DeleteOldLog();
	}
   
	private void DeleteOldLog() {
		for (int i = 0; i < LogManager.listLogger.size(); i++) {
			if (LogManager.logLevel == Level.ERROR) {
				LogManager.listLogger.get(i).error("Forced roll over at " + LogManager.listLogger.get(i).getName());
			} else {
				LogManager.listLogger.get(i).warn("Forced roll over at " + LogManager.listLogger.get(i).getName());
			}
		}
		
		File dir = new File(logFolder);
		File[] files = dir.listFiles();
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && 
						files[i].getName().startsWith(logFileName) &&
						files[i].getName().endsWith(".log")) {
					long diffMillis = System.currentTimeMillis() - files[i].lastModified();
					if (diffMillis > logHoldingMillis) {
						//System.out.println("Delete " + files[i].getName());
						files[i].delete();
					}
				}
			}
		}
	}
}
