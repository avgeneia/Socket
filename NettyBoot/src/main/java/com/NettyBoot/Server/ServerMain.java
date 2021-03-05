package com.NettyBoot.Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.NettyBoot.NettyBootApplication;
import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.ProcessInfoGetter;

public class ServerMain {

	/** 서버 소켓 Listener */
	static ServerListener sl = null;	

	/** Logger */
	static Logger logger = null;
	
	public ServerMain() throws NumberFormatException, IOException {

		// logger 생성
		logger = LogManager.getLogger(NettyBootApplication.class);
		
		try {
			
			Init();		
			
			String pInfo = ProcessInfoGetter.getPid();
			
			printLog("==================================================================================");
			printLog("=======================  SYNC SERVER START");
			printLog("=======================  [" + pInfo + "]");
			printLog("==================================================================================");
			
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
			
			printLog(e.getMessage());
		}
		
	}
	
	public static void Init() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {
		
		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();

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
	
	public void printLog(String msg) {
		
		// 설정파일 관리자 선언
		logger.info(msg);
	}
}
