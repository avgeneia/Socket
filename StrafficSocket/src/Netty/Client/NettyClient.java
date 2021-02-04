package Netty.Client;

import Common.IniFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient { 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		IniFile ini = IniFile.getInstance();
		
		EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            
            String ip = ini.getIni("Client", "IP");
            int port = Integer.parseInt(ini.getIni("Client", "PORT"));
            
            b.group(group)
             .channel(NioSocketChannel.class) // (3)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                	 ServerConnectionHandler sch = new ServerConnectionHandler();
                	 
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
		} finally {
			
			//f.channel().close().sync();
			//group.shutdownGracefully();
		}
	}
	
}
