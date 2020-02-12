package com.mzj.netty.ssy._06.protobuf.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Channel初始化器
 */
public class TestClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //无论是客户端，还是服务端，都涉及在Person对象与protobuf格式的字节数组之间进行编解码，所以，客户端与服务端的initializer中处理器是一样的
        ChannelPipeline pipline = ch.pipeline();

        pipline.addLast(new ProtobufVarint32FrameDecoder());
        pipline.addLast(new ProtobufDecoder(MyDataInfo.Person.getDefaultInstance()));
        pipline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipline.addLast(new ProtobufEncoder());

        pipline.addLast(new TestClientrHandler());
    }
}
