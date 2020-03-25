package com.mzj.netty.ssy._09_nio._01_buffer;

import java.nio.ByteBuffer;

public class NioTest6 {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(64);//分配一个容量为64个字节的ByteBuffer

        buffer.putInt(666);
        buffer.putLong(50000000000L);
        buffer.putDouble(3.1415926);
        buffer.putChar('你');
        buffer.putShort((short) 2);
        buffer.putChar('我');

        buffer.flip();

        System.out.println(buffer.getInt());//按照放置的顺序依次取出
        System.out.println(buffer.getLong());//这种行为、特性适合自定义协议
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
    }
}
