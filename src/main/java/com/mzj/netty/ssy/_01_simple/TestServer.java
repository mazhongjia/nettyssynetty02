package com.mzj.netty.ssy._01_simple;

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

        //1.创建boss与worker线程组
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        //2.创建服务器启动辅助类，服务端是 ServerBootstrap
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //3.设置childHandler的初始化器
            serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class).
                    childHandler(new TestServerInitiallzer());

            //5.绑定服务器端口并启动服务器，同步等待服务器启动完毕
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            //6.阻塞启动线程，并同步等待服务器关闭，因为如果不阻塞启动线程，则会在finally块中执行优雅关闭，导致服务器也会被关闭了
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
