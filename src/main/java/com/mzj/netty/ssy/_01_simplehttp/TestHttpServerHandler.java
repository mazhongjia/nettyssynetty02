package com.mzj.netty.ssy._01_simplehttp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println(msg.getClass());
        System.out.println(ctx.channel().remoteAddress());

        if(msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) msg;

            System.out.println("请求方法名...：" + httpRequest.method().name());
            //过滤chrome等浏览器请求网站图标的请求
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求favicon.ico");
                return;
            }

            System.out.println("channelRead0执行了...");
            //模拟响应结果数据
            ByteBuf result = Unpooled.copiedBuffer("mazhongjia", CharsetUtil.UTF_8);

            //FullHttpResponse是netty提供的封装http响应的类
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,result);

            //设置http返回对象头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,result.readableBytes());

            //返回response
            ctx.writeAndFlush(response);
            //模拟http请求/响应后，服务的关闭客户端连接
            //省略：判断http1.1或者1.0版本
            ctx.channel().close();
        }
    }

    /**
     * 客户端与服务端建立好连接时的回调函数
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded.....");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered.....");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive.....");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive.....");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered.....");
        super.channelUnregistered(ctx);
    }
}
