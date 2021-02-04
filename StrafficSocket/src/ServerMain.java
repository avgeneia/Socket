import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import Common.IniFile;
import Common.LogManager;
import Common.ProcessInfoGetter;
import Netty.Server.ServerListener;
import Netty.Server.SyncSingleClient;

public class ServerMain {
	
	/** 서버 소켓 Listener */
	ServerListener sl = null;	
	
	/** Logger */
	static Logger logger = null;
	
	public void Init() throws FileNotFoundException, IOException {
		
		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();
		
		// log4j 설정
		LogManager.SetLoggerProperties(
				ini.getIni("LOG", "Path"), "ArgoSyncServer", 
				ini.getIni("LOG", "Level"), ini.getIni("LOG", "DatePattern"),
				Integer.parseInt(ini.getIni("LOG", "ExpireAfterDay")));

		// logger 생성
		logger = LogManager.GetConfiguredLogger(this.getClass());
		
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
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		ServerMain server = new ServerMain();
		
		try {
			
			server.Init();			
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
					server.sl.EndListen();
					break;
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
}