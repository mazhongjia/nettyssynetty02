package com.mzj.netty.ssy._09_nio.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接缓冲区（DirectByteBuffer）
 * @Auther: mazhongjia
 * @Description:
 */
public class NioTest9 {

    public static void main(String[] args) throws IOException {

        FileInputStream inputStream = new FileInputStream("input2.txt");
        FileOutputStream outputStream = new FileOutputStream("output2.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(512);

        while(true){
            buffer.clear();

            int read = inputChannel.read(buffer);

            System.out.println("read : " + read);

            if(-1 == read){
                break;
            }
            buffer.flip();

            outputChannel.write(buffer);
        }
        inputChannel.close();
        outputChannel.close();
    }
}
