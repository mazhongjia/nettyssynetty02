package com.mzj.netty.ssy._06.protobuf.netty.multyMsgType;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipline = ch.pipeline();

        pipline.addLast(new ProtobufVarint32FrameDecoder());
        pipline.addLast(new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));//修改了消息实例的类型
        pipline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipline.addLast(new ProtobufEncoder());

        pipline.addLast(new TestServerHandler());
    }
}
