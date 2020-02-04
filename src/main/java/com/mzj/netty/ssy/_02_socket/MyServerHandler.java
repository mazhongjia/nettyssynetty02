package com.mzj.netty.ssy._02_socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * 服务端自定义业务处理器
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {//客户端与服务器传输数据类型为字符串
    //如果可能传输多种类型，则泛型可以定义成父类或者Object，然后在channelRead0中对类型进一步判断

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //打印远程客户端的地址，端口号 + 服务端收到的请求数据
        System.out.println(ctx.channel().remoteAddress() + "，" + msg);
        //返回数据给客户端
        ctx.channel().writeAndFlush("from server：" + UUID.randomUUID());
    }

    /**
     * 服务端的handler一般还需要复写出现异常时的捕获方法
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //一般出现异常时，会将这个客户端连接关闭掉
        ctx.close();
    }
}
