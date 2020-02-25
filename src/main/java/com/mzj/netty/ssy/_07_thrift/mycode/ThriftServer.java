package com.mzj.netty.ssy._07_thrift.mycode;

import com.mzj.netty.ssy._07_thrift.PersonService;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * @Auther: mazhongjia
 * @Description:
 */
public class ThriftServer {
    public static void main(String[] args) throws TTransportException {
        //Socket对象：异步非阻塞服务器
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        //Server参数
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        //骨架 (PersonService.Processor)
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        arg.protocolFactory(new TCompactProtocol.Factory());//面向协议层（对应网络7层结构）：决定数据如何进行压缩（解码与编码）
        arg.transportFactory(new TFramedTransport.Factory());//面向传输层（对应网络7层结构）:决定底层以什么形式将数据由一端传给另一端
        arg.processorFactory(new TProcessorFactory(processor));

        //半同步半异步的Server，（Thrift可以做集群，性能不错）
        TServer server = new THsHaServer(arg);

        //启动服务器，相当于死循环
        System.out.println("..................");
        server.serve();
        System.out.println("..................");
    }
}
