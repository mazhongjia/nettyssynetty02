package com.mzj.netty.ssy._08_grpc.mycode;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @Auther: mazhongjia
 * @Description:
 */
public class GrpcServer {

    private Server server;

    /**
     * 服务端启动
     * @throws IOException
     */
    private void start() throws IOException {
        //以下编写方式参考的官方示例
        this.server = ServerBuilder.forPort(8080).addService(new StudentServiceImpl()).build().start();

        System.out.println("server started....");

        //官方示例：grpc server退出的方式：JVM退出之前，关闭grpc server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("进行关闭");
            GrpcServer.this.stop();
        }));

        System.out.println("server start invork end....");
    }

    /**
     * 服务端退出
     */
    private void stop(){
        if (null != server){
            this.server.shutdown();
        }
    }

    /**
     * 阻塞等待服务器关闭没，需要自己调用（与thrift不同，需要自己阻塞等待服务器关闭，而thrift调用serve后会阻塞当前线程）
     * @throws InterruptedException
     */
    private void awaitTermination() throws InterruptedException {
        if (null != server){
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        GrpcServer grpcServer = new GrpcServer();
        grpcServer.start();
        grpcServer.awaitTermination();
    }
}
