package com.mzj.netty.ssy._04.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 客户端
 *
 * 本示例中客户端没有任何修改，采用_03.chat的客户端
 */
public class MyChatClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new MyChatClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect("localhost",8899).sync();
            //这个聊天程序客户端，一旦建立好与服务器的连接后，就等待从标准输入流中获取一行行的文本
            Channel channel = channelFuture.channel();
//            channelFuture.channel().closeFuture().sync();
            BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

            for(;;){
                //将文本发给服务端
                channel.writeAndFlush(br.readLine() + "\r\n");
            }

        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
