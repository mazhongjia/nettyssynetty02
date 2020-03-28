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

public class EndeCodeTest3 {
    public static void main(String[] args) throws IOException {
        String inputFile = "EndeCodeTest.txt";
        String outputFile = "EndeCodeTest_out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile,"r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile,"rw");

        long inputLength = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY,0,inputLength);//MappedByteBuffer继承了ByteBuffer

        Charset charset = Charset.forName("iso-8859-1");

        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = decoder.decode(inputData);

        ByteBuffer outputData = encoder.encode(charBuffer);//使用iso-8859-1对GBK源文件进行解码--编码

        //向output文件写
        outputFileChannel.write(outputData);

        //关闭io
        inputRandomAccessFile.close();
        outputRandomAccessFile.close();
    }
}
