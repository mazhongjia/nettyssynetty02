package com.mzj.netty.ssy._09_nio._01_buffer;

import java.nio.ByteBuffer;

/**
 * 示例3：连续执行两次flip
 */
public class NioTest3 {
    public static void main(String[] args) throws Exception {

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte)'a');
        byteBuffer.put((byte)'b');
        byteBuffer.put((byte)'c');
        byteBuffer.put((byte)'d');
        byteBuffer.put((byte)'e');
        byteBuffer.put((byte)'f');

        byteBuffer.flip();
        System.out.println( byteBuffer.position());
        System.out.println( byteBuffer.limit());
        byteBuffer.flip();

        System.out.println( byteBuffer.position());
        System.out.println( byteBuffer.limit());

    }
}
