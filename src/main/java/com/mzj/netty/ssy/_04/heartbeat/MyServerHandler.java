package com.mzj.netty.ssy._04.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 服务端handler
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {//本次例子，自己的handler继承了SimpleChannelInboundHandler的父类ChannelInboundHandlerAdapter

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {//参数:上下文对象，事件对象

        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;

            switch(event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";//No data was either received or sent for a while.读/写二者之一没有就会触发“读写空闲”
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "超时事件："+eventType);
            ctx.channel().close();//关闭客户端连接
        }
    }
}
