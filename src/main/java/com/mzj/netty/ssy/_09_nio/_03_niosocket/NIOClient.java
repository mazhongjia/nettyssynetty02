package com.mzj.netty.ssy._09_nio._03_niosocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端
 *
 * 客户端输入来自标准输入流（输入动作肯定不能放在主线程里，因为如果没有通过键盘输入则会一直阻塞当前线程）
 */
public class NIOClient {

    public static void main(String[] args) {

        try {
            //1、创建一个客户端Channel
            SocketChannel socketChannel = SocketChannel.open();
            //2、设置Channel非阻塞
            socketChannel.configureBlocking(false);
            //3、创建一个Selector
            Selector selector = Selector.open();
            //4、将客户端Channel注册到Selector上，关心事件为连接事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            //5、发起向服务端的连接
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));

            //如果客户端为长连接，则客户端线程也是不会退出的
            while (true) {
                //阻塞等待事件产生
                selector.select();
                //产生事件后，获取selectionKey集合
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();

                //处理事件
                Iterator<SelectionKey> iterable = selectionKeySet.iterator();
                while (iterable.hasNext()) {
                    SelectionKey selectionKey = iterable.next();

                    if (selectionKey.isConnectable()) {//客户端连接成功事件（isConnectable返回true表示已经与服务端建立好了连接）
                        //连接成功后，客户端通过selectionKey获取的SelectableChannel类型一定是Socketchannel类型
                        SocketChannel client = (SocketChannel) selectionKey.channel();

                        //判断连接是否处于正在进行的状态
                        if (client.isConnectionPending()) {
                            //完成这个连接
                            client.finishConnect();//需要主动调用出发的

                            //建立成功后，向服务器发送连接成功的消息
                            ByteBuffer writerBuffer = ByteBuffer.allocate(1024);

                            writerBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                            writerBuffer.flip();//写->读  之前，需要flip
                            client.write(writerBuffer);

                            //客户端通过一个线程，一直监听标准输入流，将输入数据发送给服务端
                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            executorService.submit(() -> {

                                while (true) {
                                    try {
                                        writerBuffer.clear();
                                        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                                        BufferedReader br = new BufferedReader(inputStreamReader);

                                        String sendMessage = br.readLine();
                                        writerBuffer.put(sendMessage.getBytes());
                                        writerBuffer.flip();
                                        client.write(writerBuffer);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }

                        //客户端连接成功后，注册客户端数据读取事件（用于接收服务端发送给客户端的数据）
                        //这里的read，是相对于客户端来说的，也就是客户端读取到（来自服务端的）数据
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {//处理客户端读取事件（读取服务端向客户端发送的数据）
                        SocketChannel client = (SocketChannel) selectionKey.channel();//第一件事永远是将selectionKey获取到的channel进行强制转换
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int count = client.read(readBuffer);

                        if (count > 0) {//如果读取到数据，则构造成字符串形式输出标准输出流
                            String receiveMessage = new String(readBuffer.array(), 0, count);
                            System.out.println(receiveMessage);
                        }
                    }

                    //下面这一行代码特别重要：处理完一个事件后，需要将事件在selectedKeys集合中删除~~~！！
                    iterable.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
