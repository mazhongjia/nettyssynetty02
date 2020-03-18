package com.mzj.netty.ssy._09_nio.buffer;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例2
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("NioTest2.txt");
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);//最多读取512个字节
        fileChannel.read(byteBuffer);

        //由写到读转换需要调用一次flip进行反转
        byteBuffer.flip();

        while (byteBuffer.remaining() > 0){
            byte b = byteBuffer.get();
            System.out.println("Char..." + (char)b);
            fileInputStream.close();
        }
    }
}
