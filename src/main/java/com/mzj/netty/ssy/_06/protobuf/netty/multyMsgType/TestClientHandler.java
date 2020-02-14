package com.mzj.netty.ssy._06.protobuf.netty.multyMsgType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {//范型类型的变化

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();

        if (dataType == MyDataInfo.MyMessage.DataType.PersionType) {
            MyDataInfo.Person person = msg.getPerson();

            System.out.println("[客户端收到]" + person.getName());
            System.out.println("[客户端收到]" + person.getAge());
            System.out.println("[客户端收到]" + person.getAddress());
        } else if (dataType == MyDataInfo.MyMessage.DataType.DogType) {
            MyDataInfo.Dog dog = msg.getDog();

            System.out.println("[客户端收到]" + dog.getName());
            System.out.println("[客户端收到]" + dog.getAge());
        } else if (dataType == MyDataInfo.MyMessage.DataType.CatType) {
            MyDataInfo.Cat cat = msg.getCat();

            System.out.println("[客户端收到]" + cat.getName());
            System.out.println("[客户端收到]" + cat.getCity());
        }
    }

    /**
     * 当client连接成功时，由client发起请求
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        MyDataInfo.MyMessage myMessage = null;
        //模拟发送不同消息类型
        myMessage = MyDataInfo.MyMessage.newBuilder().
                setDataType(MyDataInfo.MyMessage.DataType.PersionType).
                setPerson(MyDataInfo.Person.newBuilder().
                        setName("mazhongjia").
                        setAge(34).
                        setAddress("beijing").build()).
                build();


        ctx.channel().writeAndFlush(myMessage);
    }
}
