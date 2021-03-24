package com.NettyBoot.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.NettyBoot.Common.CmmUtil;
import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.ProcessInfoGetter;
import com.NettyBoot.Common.TelegramParser;

public class ServerMain {

	/** 서버 소켓 Listener */
	static ServerListener sl = null;	

	public ServerMain() throws NumberFormatException, IOException {

		// logger 생성
		try {


			Init();		
			
			String pInfo = ProcessInfoGetter.getPid();
			CmmUtil.print("i", "==================================================================================");
			CmmUtil.print("i", "=======================  SYNC SERVER START");
			CmmUtil.print("i", "=======================  [" + pInfo + "]");
			CmmUtil.print("i", "==================================================================================");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				
				String inStr = br.readLine();
				if (inStr.equals("exit")) {
					
					sl.EndListen();
					break;
				}
			}
		} catch (Exception e) {
			
			CmmUtil.print("w", e.getMessage());
		}
		
	}
	
	public static void Init() {
		
		//packet 파싱
		TelegramParser.getInstance();
		
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
		
		//단위테스트 로직
		String msg = ini.getIni("Server", "MSG");
		
		new ReceptionProcess(CmmUtil.hexStringToByteArray(msg));
	}
	
	public static byte[] hexStringToByteArray(String s) {
		
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
