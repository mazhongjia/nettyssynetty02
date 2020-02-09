package com.mzj.netty.ssy._05.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 服务端Channel初始化器
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipline = ch.pipeline();
        pipline.addLast(new HttpServerCodec());
        pipline.addLast(new ChunkedWriteHandler());//以块的方式进行写的处理器
        /**
         * HttpObjectAggregator原理：netty对http请求与响应，是分块处理的，比如客户端向服务端发送请求，请求总长度是1000个字节，netty进行切块
         *
         *          比如切成10个块，每个快100字节，而每一块都会在handler链中走一个完整的流程，而我们的自定义处理器
         *
         *          每次也只读到其中一个块。因此，需要通过HttpObjectAggregator，将这10个块聚合成一个完整的http请求或者响应
         *
         *          一次性的经过我们的自定义业务逻辑handler
         */
        pipline.addLast(new HttpObjectAggregator(8192));//http消息聚合处理器，对http消息进行聚合，形成FullHttpRequest或者FullHttpResponse，参数是最大聚合字节长度
        pipline.addLast(new WebSocketServerProtocolHandler("/ws123"));//netty提供的专门用于处理websocket协议的handler，这里参数是访问websocket的路径，如：ws://localhost:8899/ws
        pipline.addLast(new TextWebSocketFrameHandler());//自定义的处理器
    }
}
