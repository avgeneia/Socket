package com.NettyBoot.Client;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.LogManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


public class ServerConnectionHandler extends ChannelInboundHandlerAdapter {
	
	/** 
	 * 2020-09-23 jslee
	 * 클라이언트에서 동적으로 message를 전송하여야 하므로 변수선언 후 getter / setter 함수선언
	 * */
	
	/** Logger */
	private Logger logger = LogManager.GetConfiguredLogger(ServerConnectionHandler.class);
	
	/** 파일감지 후 서버에 보낼 전문변수 설정, DirWatchRunnable.java에서 대상파일 필터링 후 값 전달한다 jslee */
	private ByteBuf message;
	
	boolean sendstat = false;
	
	int i = 1;
	
	int clientId = 0;
	
	public ByteBuf getMessage() {
		return message;
	}

	public void setMessage(ByteBuf message) {
		this.message = message;
	}

	public void setClientId(int id) {
		// TODO Auto-generated method stub
		this.clientId = id;
	}
	
	/**
     * SyncClient.java 에서 서버로의 연결이 만들어지면 channelActive 메소드가 호출 된다. jslee
     */
	@Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
		
		logger.info("server connections Success!!");	
		/**
		 * 2020-09-25 
		 * jslee
		 * 
		 * 서버가 연결되면, 감지된 전문 메시지를 서버에 전송한다.
		 * */
		//ctx.writeAndFlush(message);
		
		sendMessage(ctx);
		i++;
    }
	
	/** Server 접속 종료시 실행됨 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}
       
	/** Server 에서 패킷 데이터를 수신 받았을 때 실행된다. jslee */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		System.out.println("read PONG");
		/** 서버로부터 수신받은 전문 메시지를 ByteBuf형태로 변환한다. jslee */
		ByteBuf in = (ByteBuf) msg;
		
		/** 로그찍기용 메시지 형 변환 jslee */
		String message = in.toString(Charset.forName("utf-8"));
		logger.info("Server -> Client [" + clientId + "] RECV : " + message);
		
	    try {
	    	
	    	/** 
	    	 * 서버로부터 전문 수신을 받았을 때 받은 전문을 바탕으로 
	    	 * 데이터 가공 후 재 전송하거나 클라이언트 접속 종료하는 메소드.
	    	 * 
	    	 * 수신 전문의 맨 앞 세글자가 920이면 930전문 다시 송신,
	    	 * 940이면 서버에서 파일수신 완료했다는 뜻이므로 접속 종료.
	    	 * 
	    	 * jslee
	    	 *  */
			//ctx.writeAndFlush(MessageToByteBuf(message, ctx));
	    } finally {
	        ReferenceCountUtil.release(msg); // (2)
	        //logger.info("서버 Listener 종료 요청");
	    }
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
		sendstat = true;
		System.out.println("Complete PONG");
		
		IniFile ini = IniFile.getInstance();
		
		int sendCnt = Integer.parseInt(ini.getIni("Client", "SEND_CNT"));
		
		if(i <= sendCnt) {
			sendMessage(ctx);
			i++;
		} else {
			ctx.close();
		}
	}
	
	public void sendMessage(ChannelHandlerContext ctx) {

		//ByteBuf message;
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String realTime = format1.format(System.currentTimeMillis());
		System.out.println("redis send start :: " + realTime);
//		
//		byte[] str = new byte[10];
//		
//		// 예제로 사용할 바이트 배열을 만듭니다.
//		str =  String.valueOf(i + " ").getBytes();
//		
//		message = Unpooled.wrappedBuffer(str, 0, str.length);
//			
		// 메시지를 쓴 후 플러쉬합니다.
		ctx.write(message);

		ctx.flush();
		
		System.out.println("PING");
		
    }

	
}
