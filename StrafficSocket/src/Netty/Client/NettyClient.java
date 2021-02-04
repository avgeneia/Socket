package Netty.Client;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import Common.IniFile;
import Common.LogManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient extends Thread { 
	
	private int id;
	private IniFile ini;
	private EventLoopGroup group;

	/** Logger */
	private static Logger logger = null;
	
	public NettyClient(int id, IniFile ini, EventLoopGroup group){
		this.id = id;
		this.ini = ini;
		this.group = group;
	}
	
	public static void Init() throws FileNotFoundException, IOException {

		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();
		
		// log4j 설정
		LogManager.SetLoggerProperties(
				ini.getIni("LOG", "Path"), ini.getIni("LOG", "ClientLogName"), 
				ini.getIni("LOG", "Level"), ini.getIni("LOG", "DatePattern"),
				Integer.parseInt(ini.getIni("LOG", "ExpireAfterDay")));
		
		// logger 생성
		logger = LogManager.GetConfiguredLogger(NettyClient.class);
		
	}
	
	public void run(){

        try {
        	
        	logger.info("client id :: " + id + "send !!!");
        	
            Bootstrap b = new Bootstrap();
            
            String ip = ini.getIni("Client", "IP");
            int port = Integer.parseInt(ini.getIni("Client", "PORT"));
            
            b.group(group)
             .channel(NioSocketChannel.class) // (3)
             .handler(new ChannelInitializer<SocketChannel>() {
                 
				@Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                	 ServerConnectionHandler sch = new ServerConnectionHandler();
                	 sch.setClientId(id);
                	 /** 
            	 	  * 2020-09-28 jslee
            	 	  * 
            	 	  * 파일변경 감지 시 파일명과 파일경로 담을 변수 선언
            	 	  * */
                     ch.pipeline().addLast(sch);	//SyncClientHandler 을 pipeline으로 설정 한다.
                 }
             });
            
            ChannelFuture f = b.connect(ip, port).sync();
            
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		IniFile ini = IniFile.getInstance();
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		Init();
		
		int cnt = Integer.parseInt(ini.getIni("Client", "CNT"));
		
		for(int i = 1 ; i <= cnt ; i++ ){
			NettyClient th = new NettyClient(i, ini, group);
			th.start(); // 이 메소드를 실행하면 Thread 내의 run()을 수행한다.
		}
		
	}
	
}
