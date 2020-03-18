package com.mzj.netty.ssy._09_nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO开发的服务端程序
 * <p>
 * 服务端只用一个线程、一个selector，监听5个端口号上的连接请求、处理这5个客户端读/写
 *
 * @Auther: mazhongjia
 * @Version: 1.0
 */
public class SelectorTest1 {

    public static void main(String[] args) throws IOException {
        //1、5个端口号
        int[] ports = new int[5];

        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        //2、构造Selector
        Selector selector = Selector.open();//Selector的创建方式：Selector.open()

        //3、将一个Selector用于上述5个服务器的监听客户端连接的使用上
        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //设置Channel阻塞模式，false：非阻塞
            serverSocketChannel.configureBlocking(false);
            //获取到ServerSocket对象
            ServerSocket serverSocket = serverSocketChannel.socket();

            InetSocketAddress address = new InetSocketAddress(ports[i]);
            //服务器Socke绑定端口
            serverSocket.bind(address);

            //将Channel注册到Selector上，并返回SelectionKey
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//参数1：选择器，参数2：selectkey的集合（整形）,当前状态只能选择接受连接这一个

            System.out.println("监听端口：" + ports[i]);
        }

        while (true) {
            System.out.println("服务端selector等待事件发生.....");
            int numbers = selector.select();
            System.out.println("numbers：" + numbers);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();

                if (selectionKey.isAcceptable()) {//处理客户端连接事件
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();//服务端SocketChannel对象（在上面Open的时候已经配置了非阻塞）
                    //通过ServerSocketChannel的accept方法在有客户端端连接事件时，获取客户端的SocketChannel对象
                    SocketChannel socketChannel = serverSocketChannel.accept();//客户端SocketChannel对象
                    socketChannel.configureBlocking(false);//设置客户端Socket为非阻塞

                    socketChannel.register(selector, SelectionKey.OP_READ);//客户端也使用同一个selector，注册感兴趣事件：读
                    System.out.println(selector.keys());
                    iter.remove();//特别重要：处理完一个selectionkey的事件后，必须将这个selectionkey从selectedKeys集合中移除（当前选择器关心的事件类型集合）

                    System.out.println("获得客户端连接：" + socketChannel);
                } else if (selectionKey.isReadable()) {//服务端的Read事件：处理来自客户端数据的，读入事件，相当于读取客户端   发   给服务端的数据，这里的Read（包括上面的OP_READ），是相对于服务端的。如果这里理解不好，们可以参考：com.shengsiyuan.nio.niosocket包里的示例理解
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int bytesRead = 0;

                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

                        byteBuffer.clear();

                        int read = socketChannel.read(byteBuffer);

                        if (read <= 0) {
                            break;
                        }
                        byteBuffer.flip();

                        socketChannel.write(byteBuffer);//将从客户端读到的数据，再写回给客户端

                        bytesRead += read;
                    }
                    System.out.println("本次客户端读事件发生后，读取：" + bytesRead + "，来自于：" + socketChannel);

                    iter.remove();//非常重要：处理完一个selectionkey事件，就需要将其在selectionkey集合中删除（表示这个selectionkey事件已经用完、消费掉了）
                }
            }
        }
    }
}
