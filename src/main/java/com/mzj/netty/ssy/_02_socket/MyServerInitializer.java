package com.mzj.netty.ssy._02_socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 服务端Channel初始化器
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipline = ch.pipeline();

        pipline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipline.addLast(new LengthFieldPrepender(4));
        pipline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipline.addLast(new MyServerHandler());
    }
}
