package com.NettyBoot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.NettyBoot.Business.BusinessMain;
import com.NettyBoot.Client.NettyClient;
import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.MainApplicationArgument;
import com.NettyBoot.Server.ServerMain;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

@SpringBootApplication
public class NettyBootApplication {
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication app = new SpringApplication(NettyBootApplication.class);
		String port = setPort(app, args); //GUI port를 args에서 읽어오도록
		app.run(args); 
		
		//어플리케이션 시작 시 설정값을 저장한 변수를 읽어옴
		Map<String, String> appArg = MainApplicationArgument.getAppArg();
		
		String systemMode = appArg.get("Mode").equals("S")?"Server":appArg.get("Mode").equals("B")?"Business":"Client";
		
		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();

		// logger 생성

		/** Logger */
		Logger logger = LogManager.getLogger(NettyBootApplication.class);
		
		//System 별 Log 출력파일 변경
		logger.info("==================================================================================");
		logger.info("=======================  SYSTEM MODE : " + systemMode);
		logger.info("=======================  GUI PORT    : " + port);
		
		switch(systemMode) {
			case "Server":
				
				new ServerMain();
				
				break;
				
			case "Client":
				
				EventLoopGroup group = new NioEventLoopGroup();
				
				int cnt = Integer.parseInt(ini.getIni("Client", "CNT"));
				
				NettyClient.Init();
				
				for(int i = 1 ; i <= cnt ; i++ ){
					NettyClient th = new NettyClient(i, ini, group);
					th.start(); // 이 메소드를 실행하면 Thread 내의 run()을 수행한다.
				}
				
				break;
			case "Business":
				
				Map<String, Object> globalArg = new HashMap<String, Object>();
				
				new BusinessMain("MAIN", 1, globalArg);
				
				break;
		}
	}
	
	public static String setPort(SpringApplication app, String[] args) {
		
		String port = "80";
		
		for(int i = 0; i < args.length; i++) {
			
			if(args[i].indexOf("Port") > 0) {
				port = args[i].split("=")[1];
			}
		}
		
		app.setDefaultProperties(Collections.singletonMap("server.port", port));
		
		return port;
	}
}
