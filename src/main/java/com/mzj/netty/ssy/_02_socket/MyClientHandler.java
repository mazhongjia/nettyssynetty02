package com.mzj.netty.ssy._02_socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * 客户端自定义业务处理器
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {//客户端与服务器传输数据类型为字符串

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush("你好！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //服务端发送给客户端消息时触发
        System.out.println(ctx.channel().remoteAddress() + "，" +msg);
        System.out.println("client output :"  + msg);
        ctx.writeAndFlush("from client : " + LocalDateTime.now());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
