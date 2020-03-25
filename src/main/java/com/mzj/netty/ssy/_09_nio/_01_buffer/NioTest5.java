package com.mzj.netty.ssy._09_nio._01_buffer;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 示例5：从一个文件读取内容，写入到另一个文件中
 */
public class NioTest5 {
    public static void main(String[] args) throws Exception {

        Path pathIn = Paths.get("src/main/resources/nio/input.txt");
        Path pathOut = Paths.get("src/main/resources/nio/output.txt");

        FileChannel inputChannel = FileChannel.open(pathIn, StandardOpenOption.READ);
        FileChannel outputChannel =FileChannel.open(pathOut,StandardOpenOption.WRITE);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true){
            byteBuffer.clear();

            int read = inputChannel.read(byteBuffer);
            if(-1 == read){//此处用-1判断是否读完需要依赖上面的 byteBuffer.clear();
                //文件读完了
                break;
            }

            //读到文件中内容了，翻转缓冲区，准备get其中数据
            byteBuffer.flip();//本例中从ByteBuffer获取数据没有显示的get，而是将其作为参数传给另一个Channel的write方法，但是也是读其中的数据，所以也要进行翻转

            outputChannel.write(byteBuffer);//将byteBuffer中内容写入到Channel中
        }

        inputChannel.close();
        outputChannel.close();
    }
}
