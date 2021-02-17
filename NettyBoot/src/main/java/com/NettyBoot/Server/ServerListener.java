package com.NettyBoot.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.NettyBoot.Common.LogManager;
import com.NettyBoot.Handler.ClientConnectionHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 서버 소켓 리스너<br>
 * Step<br>
 * 1. 서버 정보 설정(SetIPAddress, SetPortNo)
 * 2. ClientConnection을 구현한 클래스 설정(SetCientConectionImpl)
 * 3. Listening 시작(StartListen)
 * 4. Client접속되면. ClientConnect 구현 클래스의 인스턴스를 생성한 후, 
 *   클라이언트가 연결된 소켓 전달(run)
 * 5. ClientConnect구현 클래스 인스턴스의 Receive 쓰레드 시작(run)
 *   
 * @author jymoon
 *
 */
public class ServerListener implements Runnable {
	/** 리스닝에 사용할 서버 주소 */
	private String ipStr = "127.0.0.1";
	
	/** 리스닝에 사용할 포트 */
	private int portNo = 2000;
	
	/** 추상클래스인 ClientConnection을 구현한 클래스 */
	private Class<?> clsClientCon = null;
	
	/** 리스닝 쓰레드 */
	private Thread listenThread = null;
	
	/** 비동기 채널 */
	private ChannelFuture f = null;

	/**
	 * Logger
	 */
	static Logger logger = LogManager.GetConfiguredLogger(ServerListener.class);
	

	/**
	 * ClientConnection(추상클래스)를 구현한 클래스 설정
	 * 
	 * @param clientConnectionImpl : ClientConnection(추상클래스)를 구현한 클래스 
	 */
	public void SetClientConnectionImpl(Class<?> clientConnectionImpl) {
		clsClientCon = clientConnectionImpl;	
	}

	/**
	 * 서버 소켓에서 사용할 IP 주소 셋팅
	 * 
	 * @param ipStr : IP 주소
	 */
	public void SetIPAddress(String ipStr) {
		this.ipStr = ipStr;
	}
	
	/**
	 * 서버 소켓에서 사용할 포트 정보 셋팅
	 * 
	 * @param _num : 포트 번호
	 */
	public void SetPortNo(int _num) {
		portNo = _num;
	}
	
	/**
	 * 포트 번호 확인
	 * 
	 * @return : 포트 번호
	 */
	public int GetPortNo() {
		return portNo;
	}
	

	/**
	 * Listener 시작
	 */
	public void StartListen() {
		 listenThread = new Thread(this);
		 listenThread.start();
	}
		 
	/**
	 * Listener 종료
	 */
	public void EndListen() {
		logger.info("서버 Listener 종료 요청");
		
		// 소켓 닫기
        f.channel().close(); 
	}
		
	/**
	 * Runnable Interface의 run 메소드 구현.
	 * Listening 하면서, 소켓 접속시마다 ClientConnection구현 클래스의 인스턴스 생성  
	 * StartListen을 통해 시작되고, EndListen을 통해 종료됨.
	 */
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast((ClientConnectionHandler)clsClientCon.newInstance());
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            f = b.bind(InetAddress.getByName(ipStr), portNo).sync(); // (7)

            logger.info("Server Listener Started");
                                    
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (UnknownHostException e) {
            logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		} finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("Server Listener Stopped"); 
        }
	}
		 
	/**
	 * 단위 테스트용 main
	 * @param args
	 */
//	 public static void main(String[] args) {
//		 try {
//			 ServerListener sl = new ServerListener();
//			 sl.StartListen();
//			 
//			 // 사용자가 종료 키워드('exit') 입력할 때까지 기다림
//			 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			 while(true){
//				String inStr = br.readLine();
//				//System.out.println(inStr);
//				if (inStr.equals("exit")) {
//					break;
//				}
//			 }
//			 
//			 sl.EndListen();            
//		 } catch (Exception e) {
//			 logger.error(e);
//		 }		  
//	 }
}
