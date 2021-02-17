package com.NettyBoot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.LogManager;
import com.NettyBoot.Common.ProcessInfoGetter;
import com.NettyBoot.Common.PropertyUtil;
import com.NettyBoot.Common.TelegramParser;
import com.NettyBoot.Server.ServerListener;
import com.NettyBoot.Server.SyncSingleClient;

@SpringBootApplication
public class NettyBootApplication {
	
	/** 서버 소켓 Listener */
	static ServerListener sl = null;	
	
	/** Logger */
	static Logger logger = null;

	public static void Init() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {
		
		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();

		TelegramParser.getInstance();
		
		// Socket listener 생성
		sl = new ServerListener();
		
		// 서버 정보 설정
		sl.SetIPAddress(ini.getIni("Network Interface", "ServiceAddress"));
		sl.SetPortNo(Integer.parseInt(ini.getIni("Network Interface", "Port")));

		// ClientConnection을 구현한 클래스가 SingleClientManager임을 ServerListener에 알려줌
		sl.SetClientConnectionImpl(SyncSingleClient.class);
		
		// Listening 시작
		sl.StartListen();
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		//NettyBootApplication server = new NettyBootApplication();		
		SpringApplication.run(NettyBootApplication.class, args);

		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();

		// logger 생성
		logger = LogManager.GetConfiguredLogger(NettyBootApplication.class);

		// log4j 설정
		LogManager.SetLoggerProperties(
				ini.getIni("LOG", "Path"), ini.getIni("LOG", "ServerLogName"), 
				ini.getIni("LOG", "Level"), ini.getIni("LOG", "DatePattern"),
				Integer.parseInt(ini.getIni("LOG", "ExpireAfterDay")));
		
		logger.info("==================================================================================");
		logger.info("=======================  SYSTEM MODE : " + PropertyUtil.getProperty("System.Mode"));
		
		try {
			Init();		
			
			String pInfo = ProcessInfoGetter.getPid();
			
			logger.info("==================================================================================");
			logger.info("=======================  SYNC SERVER START");
			logger.info("=======================  [" + pInfo + "]");
			logger.info("==================================================================================");
			
			//System.out.println("Initialized. type 'exit' to exit.");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String inStr = br.readLine();
				if (inStr.equals("exit")) {
					sl.EndListen();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
