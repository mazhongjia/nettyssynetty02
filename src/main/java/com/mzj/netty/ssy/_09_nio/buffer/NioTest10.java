package com.mzj.netty.ssy._09_nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件
 *
 * @Auther: mazhongjia
 * @Description:
 */
public class NioTest10 {

    public static void main(String[] args) throws IOException {
        System.out.println(Integer.MAX_VALUE);
        //创建RandomAccessFile对象
        RandomAccessFile randomAccessFile = new RandomAccessFile("MemoryMapFile.txt","rw");
        //通过RandomAccessFile对象获取：NIO的文件Channel对象（1.4版本与NIO一起增加到RandomAccessFile中）
        FileChannel fileChannel = randomAccessFile.getChannel();

        //创建内存映射文件对象，映射的内容是文件位置0，长度5个字节长度，最大支持映射整形int的最大值长度的字节数，也就是2GB文件大小
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,5);

        //修改第0个元素，内容改成a
        mappedByteBuffer.put(0, (byte) 'a');
        //修改第3个元素，内容改成b
        mappedByteBuffer.put(3, (byte) 'b');

        //关闭文件
        randomAccessFile.close();
    }
}
