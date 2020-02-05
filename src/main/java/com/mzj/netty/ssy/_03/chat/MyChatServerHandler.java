package com.mzj.netty.ssy._03.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 *
 * handler实现的业务逻辑：（本示例需求）
 *
 * 1、首个客户端A登陆，无
 * 2、第二个客户端B登陆，通知A，客户端B已经上线
 * 3、第三个客户说C登陆，通知A、B，客户端C已经上线
 * 4、客户端下线时，也会通知其他客户端XX已经下线
 * 5、其中任何一个客户端发送消息，都通知包括自己在内所有客户端，同时自己输出“自己：”，实际上就是实现消息的广播
 *
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {//客户端与服务器传输数据类型为字符串

    //使用netty提供的ChannelGroup来保存客户端连接
    //好处是：通过操作ChannelGroup，实现对加入到ChannelGroup中的所有的Channel进行统一操作
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //!!!!!!经过测试ChannelGroup channelGroup必须是static的，否则向其加入的channel不在一个group里

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //某客户端发送消息给服务器，服务器收到消息后执行逻辑：

        //1.获取到当前发送消息的客户端
        Channel channel = ctx.channel();
        //2.循环所有客户端（ChannelGroup实现了Set接口）
        channelGroup.forEach(ch ->{
            if(channel != ch){//使用==判断
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息：" + msg + "\n");
            }else{
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });
    }

    /**
     *客户端与服务端建立好连接时的回调函数
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //对ChannelGroup中已经建立好连接的channel发送消息
        channelGroup.writeAndFlush("【服务器】 - " + channel.remoteAddress() + " 加入！\n");
        //将新的连接加入到ChannelGroup
        channelGroup.add(channel);
    }

    /**
     * 连接关闭时回调函数
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【服务器】 - " + channel.remoteAddress() + " 离开！\n");
//        channelGroup.remove(channel);//此行代码会被netty自动调用，我们调用与否都可以，可以不进行显式调用
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + " 上线！\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + " 下线！\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
