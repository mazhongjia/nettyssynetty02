package com.mzj.netty.ssy._09_nio._04_Charset;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 将EndeCodeTest.txt文件内容读取出来，写入EndeCodeTest_out.txt文件
 */
public class EndeCodeTest {
    public static void main(String[] args) throws IOException {
        String inputFile = "EndeCodeTest.txt";
        String outputFile = "EndeCodeTest_out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile,"r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile,"rw");

        //input文件总长度
        long inputLength = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        //内存映射inputFile文件全部内容，最大支持映射整形int的最大值长度的字节数，也就是2GB文件大小
        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY,0,inputLength);//MappedByteBuffer继承了ByteBuffer

        //创建utf8字符集、获取字符集  编解码器
        //这里设置字符集是utf-8时不会出现中文乱码
//        Charset charset = Charset.forName("utf-8");
        //理解为什么这里是iso-8859-1字符集，在输出文件中也不会出现乱码现象
        Charset charset = Charset.forName("iso-8859-1");
        /**
         * 原因：
         * 1、iso-8859-1字符集，1个字符用1个字节表示，原UTF-8文件中，英文是1个字节，中文是3个字节
         * 2、用iso-8859-1字符集进行解码：utf-8编码的英文（1个字节）正常解码成iso-8859-1的英文字符（1个字节），utf-8编码的中文中（3个字节）每一个字节都解码成iso-8859-1对应的一个字符，但是是错误的字符
         * 3、再用iso-8859-1字符集进行编码：将错误的iso-8859-1字符编码成字节（1个字符编码成1个字节），写入输出文件，写入文件的字节与先前输入文件中uft-8编码的字节内容相同
         * 4、IDEA打开文件默认采用uft-8编码，因此不是乱码
         */
        CharsetDecoder decoder = charset.newDecoder();//解码器：字节数组  转换  字符串 -
        CharsetEncoder encoder = charset.newEncoder();//编码器：字符串  转换  字节数组

        //解码：对内存映射文件的bytebuffer进行解码（ByteBuffer  ->  CharBuffer）
        CharBuffer charBuffer = decoder.decode(inputData);
        System.out.println(charBuffer);//此行输出的意义是便于分析使用iso-8859-1解码+编码汉字，没有问题的原因，解码后输出是乱码
        //编码：上面一行解码完的结果再反过来进行编码（CharBuffer  ->  ByteBuffer）
        ByteBuffer outputData = encoder.encode(charBuffer);

        //向output文件写
        outputFileChannel.write(outputData);

        //关闭io
        inputRandomAccessFile.close();
        outputRandomAccessFile.close();
    }
}

