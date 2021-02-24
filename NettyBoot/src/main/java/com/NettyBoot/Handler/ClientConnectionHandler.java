package com.NettyBoot.Handler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.LogManager;
import com.NettyBoot.Redis.RedisComm;
import com.NettyBoot.Redis.RedisManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

/**
 * 개별 클라이언트와 연결된 소켓의 모든 작업을 수행하는 추상 클래스.
 * 클라이언트와의 메세지 송수신 기능 수행.
 * OnReceivePacket, OnClose 메소드를 구현해서 사용해야 함.
 * 
 * @author jymoon
 *
 */
public abstract class ClientConnectionHandler extends ChannelInboundHandlerAdapter  {
	/** 현재 연결된 클라이언트 접속자수 */
	static int connectedCnt = 0;
	/** 클라이언트 접속자수 계산을 위한 Thread-safe용 오브젝트 */
	static Object sync_conCnt = new Object();	
	
	/** Logger */
	static Logger logger = LogManager.GetConfiguredLogger(ClientConnectionHandler.class);
	
	/** 접속자 IP */
	private String userIP = null;
	private ChannelHandlerContext ctx = null;
	
	//private TelegramParser tp = null;
	
	@Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
				
		//tp = new TelegramParser();
    }
	
	/** Client 접속시 실행됨 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException */
	@Override
    public void channelActive(final ChannelHandlerContext ctx) throws ParserConfigurationException, SAXException, IOException { // (1)
		
		this.ctx = ctx;
		userIP = ctx.channel().remoteAddress().toString();
		String pid = userIP.substring(userIP.indexOf(":")+1);
		userIP = userIP.substring(1, userIP.lastIndexOf(":"));
		
		synchronized(sync_conCnt) {
			ServerLog("", userIP + " connected. connections : " + ++connectedCnt);
			
			RedisComm redis = new RedisComm();
			
			if(redis.getConnect()) {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String realTime = format1.format(System.currentTimeMillis());
				ServerLog("", "log redis connection :: " + userIP + " // " + realTime);
			}
			
			RedisManager rm = RedisManager.getInstance();
			rm.putRedisPool(userIP, redis);
			ServerLog("", "connect :: " + redis.getConnect());
		}		
    }
	
	/** Client 접속 종료시 실행됨 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		synchronized(sync_conCnt) {
			ServerLog("", userIP + " disconnected. connections : " + --connectedCnt);	
		}
		
		//super.channelInactive(ctx);
		OnClose();
	}
       
	/** Client에서 패킷 받았을 때 실행됨 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		ServerLog("", "channelRead !!!");
		
		// 받은 메시지를 ByteBuf형으로 캐스팅합니다.
		ByteBuf byteBufMessage = (ByteBuf) msg;
		// 읽을 수 있는 바이트의 길이를 가져옵니다.
		int size = byteBufMessage.readableBytes();

		// 읽을 수 있는 바이트의 길이만큼 바이트 배열을 초기화합니다.
		byte [] byteMessage = new byte[size];
		// for문을 돌며 가져온 바이트 값을 연결합니다.
		for(int i = 0 ; i < size; i++){
			byteMessage[i] = byteBufMessage.getByte(i);
		}

		// 바이트를 String 형으로 변환합니다.
		String str = new String(byteMessage);
		
		ServerLog("", "client msg rcv :: " + str);
		userIP = ctx.channel().remoteAddress().toString();
		String pid = userIP.substring(userIP.indexOf(":")+1);
		userIP = userIP.substring(1, userIP.lastIndexOf(":"));
		
		RedisManager rm = RedisManager.getInstance();
		RedisComm redis = rm.getRedisPool(userIP);
		
		redis.set(userIP, str);
    	
		ServerLog("R",str);
		
	    ctx.write(msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ServerLog("", "exceptionCaught ::" + cause);
		ctx.close();
	}
	
	/**
	 * 클라이언트로부터 패킷이 전송되었을 때 실행되는 함수.
	 * 추상함수이므로, 패킷 내용에 따른 처리 과정을 구현해 주어야 함
	 * 
	 * @param buf : 패킷 내용 버퍼
	 * @param size : 받은 패킷의 바이트수
	 */
	public void OnReceivePacket(byte[] buf, int size) {
		
	}
	
	/**
	 * 클라이언트와의 연결이 종료되었을 때 실행되는 함수.
	 * 추상함수이므로, 연결 종료시의 추가적인 처리(예: DB 접속 종료 등)를 구현해 주어야 함
	 */
	public abstract void OnClose();
		 
	/**
	 * 클라이언트에 메세지 전송
	 * 
	 * @param msgBuf	전송할 메세지 버퍼
	 * @param offset	버퍼의 실제 메세지 시작 위치
	 * @param len		전송할 메세지의 길이
	 * @throws SocketException 
	 */
	public void SendToClient(byte[] msgBuf, int offset, int len) throws IOException	{
//		if (len < 256)
//			logger.debug("Send To Client : " + new String(msgBuf, "utf-8"));
//		else
//			logger.debug("Send To Client : " + len + " bytes");
		ByteBuf out = Unpooled.wrappedBuffer(msgBuf, offset, len); 
		ctx.write(out);
		ctx.flush();
	}
	
	/**
	 * 클라이언트에 파일 전송
	 * 
	 * @param filePath : 전송할 파일의 경로
	 * @throws IOException
	 */
	public void SendFileToClient(String filePath) throws IOException {
        RandomAccessFile raf = null;
        long length = -1;
        try {
            raf = new RandomAccessFile(filePath, "r");
            length = raf.length();
        } catch (Exception e) {
        	ServerLog("", e.getMessage());
            return;
        } finally {
            if (length < 0 && raf != null) {
                raf.close();
            }
        }

        ServerLog("", "File Send Start. file : " + filePath + ", len : " + raf.length());
        if (ctx.pipeline().get(SslHandler.class) == null) {
            // SSL not enabled - can use zero-copy file transfer.
            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length));
        } else {
            // SSL enabled - cannot use zero-copy file transfer.
        	logger.debug("SSH");
            ctx.write(new ChunkedFile(raf));
        }
        ctx.flush();
        ServerLog("", "File Send end. file : " + filePath);
    }
	
	public void ServerLog(String type, String msg) {
		
		IniFile ini = IniFile.getInstance();
		boolean Log = ini.getIni("LOG", "Print").equals("true")?true:false;		
		
		if(Log != true) {
			return;
		}
		
		if(type.equals("R")) {
			logger.info("[RECV] :: " + msg);			
		} else if(type.equals("S")) {
			logger.info("[SEND] :: " + msg);
		} else {
			logger.info(msg);
		}

	}
}
