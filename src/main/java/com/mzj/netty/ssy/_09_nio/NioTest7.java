package com.mzj.netty.ssy._09_nio;

import java.nio.ByteBuffer;

/**
 * slice方法创建的buffer与底层buffer共有底层数据
 * @Auther: mazhongjia
 * @Description:
 */
public class NioTest7 {

    public static void main(String[] args) {

        //1.分配一个容量为10个字节的ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);

        //2.填充缓冲区
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }
        //3.修改position、limit以准备调用slice
        buffer.position(2);
        buffer.limit(6);
        /**
         * 创建新的字节缓冲区，其内容是此缓冲区内容的共享子序列。
         * 新缓冲区的内容将从此缓冲区的当前位置开始。此缓冲区内容的更改在新缓冲区中是可见的，反之亦然；这两个缓冲区的位置、界限和标记值是相互独立的。但是两个缓冲区底层的数据是一份
         */
        ByteBuffer sliceBuffer = buffer.slice();
        System.out.println(sliceBuffer);
        System.out.println("-------------");

        //4.修改创建新的字节缓冲区内容：原先ByteBuffer的每一个元素*2
        for (int i = 0; i < sliceBuffer.capacity(); ++i) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i, b);
        }

        //5.打印原缓冲区内容，结果也发生变化，证明两份缓冲区公用相同底层数据
        //重新调整position、limit值，以便打印全部数据
        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());//get方法是相对类型，调用会影响position或者limit值
        }
        System.out.println("-------------");
        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());//get方法是相对类型，调用会影响position或者limit值
        }
    }
}
