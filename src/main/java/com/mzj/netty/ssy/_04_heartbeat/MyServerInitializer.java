package com.mzj.netty.ssy._04_heartbeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 服务端Channel初始化器
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipline = ch.pipeline();
        //netty针对空闲检测提供的handler（netty的handler模式与其他框架过滤器、拦截器等都属于设计模式中“责任链模式“）
        //这里的空闲，是相对于服务端来说的，比如一直有客户端发送服务端数据，没有服务端写回客户端数据，会产生写空闲
        pipline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
        pipline.addLast(new MyServerHandler());
    }
}
