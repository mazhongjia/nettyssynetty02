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

public class EndeCodeTest2 {
    public static void main(String[] args) throws IOException {
        String inputFile = "EndeCodeTest.txt";
        String outputFile = "EndeCodeTest_out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile,"r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile,"rw");

        long inputLength = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY,0,inputLength);//MappedByteBuffer继承了ByteBuffer

        Charset charset = Charset.forName("iso-8859-1");//使用ISO-8859-1对源UTF-8源文件进行解码

        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = decoder.decode(inputData);

        /**
         * 再使用UTF-8进行编码后写入目标文件，出现乱码
         *
         *  原因：先使用iso-8859-1进行解码成字符，此时是错误的字符：ä½ å¥½å（共计6个字符），再对这6个字符使用utf-8编码，肯定后续都是错误的了
         */
        ByteBuffer outputData = Charset.forName("utf-8").encode(charBuffer);


        //向output文件写
        outputFileChannel.write(outputData);

        //关闭io
        inputRandomAccessFile.close();
        outputRandomAccessFile.close();
    }
}
