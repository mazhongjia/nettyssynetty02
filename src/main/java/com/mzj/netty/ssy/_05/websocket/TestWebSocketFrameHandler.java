package com.mzj.netty.ssy._05.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

public class TestWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {//范型代表：传递的数据类型，比如之前的HttpObject、String、这里的类型是websocket文本阵类型

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息内容：" + msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器收到消息时间："+ LocalDateTime.now()));//参数为Object、根据协议及处理器不同，传递不同的类型
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
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