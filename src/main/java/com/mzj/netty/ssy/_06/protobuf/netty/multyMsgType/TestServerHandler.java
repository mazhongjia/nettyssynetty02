package com.mzj.netty.ssy._06.protobuf.netty.multyMsgType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

//范型类型：服务端与客户端传递的数据对象类型实例，即编解码类型
public class TestServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();

        if (dataType == MyDataInfo.MyMessage.DataType.PersionType) {
            MyDataInfo.Person person = msg.getPerson();

            System.out.println("[服务端收到]" + person.getName());
            System.out.println("[服务端收到]" + person.getAge());
            System.out.println("[服务端收到]" + person.getAddress());
        } else if (dataType == MyDataInfo.MyMessage.DataType.DogType) {
            MyDataInfo.Dog dog = msg.getDog();

            System.out.println("[服务端收到]" + dog.getName());
            System.out.println("[服务端收到]" + dog.getAge());
        } else if (dataType == MyDataInfo.MyMessage.DataType.CatType) {
            MyDataInfo.Cat cat = msg.getCat();

            System.out.println("[服务端收到]" + cat.getName());
            System.out.println("[服务端收到]" + cat.getCity());
        }

        MyDataInfo.MyMessage myMessage = null;
        //模拟发送不同消息类型
        myMessage = MyDataInfo.MyMessage.newBuilder().
                setDataType(MyDataInfo.MyMessage.DataType.PersionType).
                setPerson(MyDataInfo.Person.newBuilder().
                        setName("mazhongjia123").
                        setAge(34).
                        setAddress("beijing").build()).
                build();


        ctx.channel().writeAndFlush(myMessage);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
