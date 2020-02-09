package com.mzj.netty.ssy._05.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * websocket本文阵自定义handler
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {//范型含义：传递的数据类型，比如之前的HttpObject、String、这里的类型是websocket文本阵类型

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息内容：" + msg.text());
        //向客户端发送数据
//        ctx.channel().writeAndFlush("ABC");//这里不能直接发送字符串，因为无法被这个server的handler进行处理
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器收到消息时间："+ LocalDateTime.now()));//参数为Object、根据协议及处理器不同，传递不同的类型
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //channel().id()代表channel全局唯一的ID
        System.out.println("handlerAdded channelid = " + ctx.channel().id().asLongText());;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved channelid = " + ctx.channel().id().asLongText());;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}