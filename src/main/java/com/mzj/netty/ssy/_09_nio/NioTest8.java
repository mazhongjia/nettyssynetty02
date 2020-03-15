package com.mzj.netty.ssy._09_nio;

import java.nio.ByteBuffer;

/**
 * 只读buffer
 * @Auther: mazhongjia
 * @Description:
 */
public class NioTest8 {

    public static void main(String[] args) {

        //1.分配一个容量为10个字节的ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);

        //2.填充缓冲区
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }
        //3.创建只读buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
    }
}
