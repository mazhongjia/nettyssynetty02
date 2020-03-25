package com.mzj.netty.ssy._09_nio._01_buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *
 * 功能描述: NIO中Scattering与Gathering示例
 *
 * 创建个服务端用于测试，客户端向服务端写入数据，服务的通过三个buffer接收数据
 *
 * @Auther: mazhongjia
 * @Description:
 */
public class NioTest12 {

    public static void main(String[] args) throws IOException {
        //1、创建服务端并绑定端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);

        serverSocketChannel.socket().bind(address);

        //2、定义三个buffer的长度，总共为9个字节：第一个buffer长度2，第二个长度3，第三个长度4
        int messageLength = 2 +3 +4;

        //3、创建三个buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[3];

        //4、按照上面长度分配大小初始化buffers
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        //5、服务端等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true){
            //一次循环读取messageLength个字节
            System.out.println("【开始一次读入/写出....】");
            int bytesRead = 0;

            //6、3个buffer全部写满，才会退出内层循环
            while(bytesRead < messageLength){
                //每一次循环读取多少个字节，也就是每次打印r是多少，与是什么客户端调用有关，windows上telnet客户端调用时只要有键盘输入就会向服务端write并flush，一次只能是read到1个，ios的nc工具上按回车键才flush，一次read可以随意
                long r = socketChannel.read(byteBuffers);//------------Scattering-------------
                System.out.println("r..........."+r);
                bytesRead += r;

                System.out.println("bytesRead : " +bytesRead);//打印实际读到的字节信息
                //每次读完都会打印每次读入后，三个buffer的状态（position与limit）
                Arrays.asList(byteBuffers).stream().map(buffer -> "position : " + buffer.position() +",limit : "+buffer.limit()).forEach(System.out::println);
            }

            //7、3个buffer读满后，将buffer进行翻转，准备读取其中的数据，将数据写回客户端
            Arrays.asList(byteBuffers).stream().forEach(buffer -> buffer.flip());

            //8、将读到的数据写回客户端
            //执行到这里时，三个buffer的已经满了
            long bytesWrite = 0;
            while(bytesWrite < messageLength){
                System.out.println("这里应该只循环一次，三个buffer全部写出");
                long r = socketChannel.write(byteBuffers);//------------Gathering-------------
                bytesWrite += r;
            }

            //9、一次读入buffer/写出buffer后，清空buffer（将position = 0 limit = capacity  mark = -1）
            Arrays.asList(byteBuffers).stream().forEach(byteBuffer -> byteBuffer.clear());
        }
    }
}
