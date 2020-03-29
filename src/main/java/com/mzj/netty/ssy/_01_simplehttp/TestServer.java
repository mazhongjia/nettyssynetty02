package com.mzj.netty.ssy._01_simplehttp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务端程序流程
 */
public class TestServer {

    public static void main(String[] args) throws InterruptedException {

        //1.创建boss与worker线程组（事件循环组，死循环）
        EventLoopGroup boss = new NioEventLoopGroup(1);//netty最新版本中叫：parent
        EventLoopGroup worker = new NioEventLoopGroup();//netty最新版本中叫：child
        //2.创建服务器启动辅助类，服务端是 ServerBootstrap，作用是用于简化服务端创建工作
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //3.装配服务端（采用方法链的编程风格：.XX.YY.ZZ）
            serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class).
                    childHandler(new TestServerInitiallzer());//childHandler是针对workgroup，handler是针对bossgroup

            //5.绑定服务器端口并启动服务器，同步等待服务器启动完毕
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            //6.阻塞启动线程，并同步等待服务器关闭，因为如果不阻塞启动线程，则会在finally块中执行优雅关闭，导致服务器也会被关闭了
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();//线程组优雅关闭
            worker.shutdownGracefully();
        }

    }
}
