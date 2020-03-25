package com.mzj.netty.ssy._09_nio._03_niosocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 比较完整的NIO Server+Client示例，目的是学习NIO编程的一般模式
 *
 * 实现多个客户端连接一个Server，某个client发送给server消息后，将其转发给其他客户端
 *
 * 本示例并不完善，待处理：某个客户端关闭了，没有从列表中删除
 */
public class NIOServer {
    /**
     * 选择器
     */
    private Selector selector;

    /**
     * 通道
     */
    ServerSocketChannel serverSocketChannel;

    /**
     * 在服务端记录客户端信息，使得可以在后续向客户端发送数据
     */
    private Map<String, SocketChannel> clients = new HashMap<>();

    public void initServer(int port) throws IOException {
        //------------使用NIO进行socket编程，以下四步创建服务器端socket是模板代码---------------
        //1、打开一个通道
        serverSocketChannel = ServerSocketChannel.open();

        //2、一定要把通道设置非阻塞（必须）
        serverSocketChannel.configureBlocking(false);

        //3、serverSocketChannel.socket()绑定端口号
        serverSocketChannel.socket().bind(new InetSocketAddress("localhost", port));

        //4、注册
        this.selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//根据选择器模型，这里的注册是将Channel注册到Selector上

        //注册完后就开始事件的处理
    }

    public void listen() throws IOException {
        System.out.println("Server started succeed!");

        while (true) {

            try {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                selectionKeys.forEach(selectionKey -> {

                    final SocketChannel client;

                    try {
                        if (selectionKey.isAcceptable()) {
//                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();//通过产生accept事件的SelectionKey的channel方法获取到的channel对象就是serverSocketChannel，可以通过强制类型转换成ServerSocketChannel，因为只有ServerSocketChannel才有accept类型的事件产生
                            client = serverSocketChannel.accept();//SocketChannel：服务端与客户端连接的通道对象
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            //在服务端记录客户端信息，使得可以在后续向客户端发送数据
                            String key = "[" + UUID.randomUUID() + "]";
                            clients.put(key, client);
                        } else if (selectionKey.isReadable()) {

                            client = (SocketChannel) selectionKey.channel();//因为这里只有客户端发送数据，所以read事件产生时，可以直接强制类型转换成SocketChannel
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            int count = client.read(readBuffer);//向bytebuffer中写入客户端发送来的数据（以字节方式写入）
                            if (count > 0) {//如果有数据
                                readBuffer.flip();//反转ByteBuffer（准备读取bytebuffer中数据）

                                Charset charset = Charset.forName("utf-8");//创建uft-8的字符集
                                String receiveMessage = String.valueOf(charset.decode(readBuffer).array());//将读到的字节素组以utf-8形式进行解码
                                System.out.println(client + ": " + receiveMessage);

                                //获取发送消息客户端的key： String key = "[" + UUID.randomUUID() + "]";
                                String senderKey = null;
                                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                //向所有client发送消息
                                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                                    SocketChannel value = entry.getValue();

                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                                    writeBuffer.put((senderKey + " : " + receiveMessage).getBytes());
                                    writeBuffer.flip();//写->读  之前，需要flip

                                    value.write(writeBuffer);
                                }

                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                selectionKeys.clear();//处理完selectionKey后，将selected-key set集合清空也可以，不非得 通过iterator删除

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void recvAndReply(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int i = channel.read(buffer);
        if (i != -1) {
            String msg = new String(buffer.array()).trim();
            System.out.println("NIO server received message =  " + msg);
            System.out.println("NIO server reply =  " + msg);
            channel.write(ByteBuffer.wrap(msg.getBytes()));
        } else {
            channel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(8080);
        server.listen();
    }
}

