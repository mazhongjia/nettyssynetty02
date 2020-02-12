package com.mzj.netty.ssy._06.protobuf.netty;

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

        //netty中一共提供了4个handler处理器，用于处理protobuf类型消息的编解码
        pipline.addLast(new ProtobufVarint32FrameDecoder());
        //ProtobufDecoder作用：将protobuf协议序列化后字节数组解码成MyDataInfo.Person
        //ProtobufDecoder参数为：服务端与客户端传递的数据对象类型实例
        //也就是编解码对象实例
        pipline.addLast(new ProtobufDecoder(MyDataInfo.Person.getDefaultInstance()));
        pipline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipline.addLast(new ProtobufEncoder());

        //自定义处理器
        pipline.addLast(new TestServerHandler());
    }
}
