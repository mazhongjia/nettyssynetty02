package com.mzj.netty.ssy._06.protobuf.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestClientrHandler extends SimpleChannelInboundHandler<MyDataInfo.Person> {//范型类型也是Person类型

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.Person msg) throws Exception {

    }

    /**
     * 当client连接成功时，由client发起请求
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MyDataInfo.Person person = MyDataInfo.Person.newBuilder()
                .setName("zhangshan")
                .setAge(34)
                .setAddress("beijing")
                .build();
        ctx.channel().writeAndFlush(person);
    }
}
