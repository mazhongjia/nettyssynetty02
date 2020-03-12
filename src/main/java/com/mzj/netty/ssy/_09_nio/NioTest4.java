package com.mzj.netty.ssy._09_nio;

import java.nio.ByteBuffer;

/**
 * 示例4：测试slice
 */
public class NioTest4 {
    public static void main(String[] args) throws Exception {

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte)'a');
        byteBuffer.put((byte)'b');
        byteBuffer.put((byte)'c');
        byteBuffer.put((byte)'d');
        byteBuffer.put((byte)'e');
        byteBuffer.put((byte)'f');

        byteBuffer.flip();

        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.get());
//        byteBuffer.position(1);
        ByteBuffer sliceByteBuffer = byteBuffer.slice();
        System.out.println(byteBuffer.put((byte)'z'));
        System.out.println(sliceByteBuffer.get());
//        byteBuffer.compact();

    }
}
