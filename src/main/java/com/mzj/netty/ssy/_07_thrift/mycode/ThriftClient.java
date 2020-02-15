package com.mzj.netty.ssy._07_thrift.mycode;

import com.mzj.netty.ssy._07_thrift.Person;
import com.mzj.netty.ssy._07_thrift.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @Auther: mazhongjia
 * @Description:
 */
public class ThriftClient {

    public static void main(String[] args) throws TTransportException {
        //客户端transport类型与服务端需要对应——C/S传输方式得一致，600是超时时间
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);

        //客户端protocol类型与服务端需要对应——C/S编解码类型得一致
        TProtocol protocol = new TCompactProtocol(transport);//客户端与服务端的Protocol一定要对应，
        //桩 (PersonService.Client)
        PersonService.Client client = new PersonService.Client(protocol);

        try {
            //发起连接
            transport.open();
            //rpc调用
            Person person = client.getPersonByUsername("张山");
            //
            System.out.println(person.getUsername());
            System.out.println(person.getAge());
            System.out.println(person.isMarried());
            System.out.println("------------------");
            client.savePerson(person);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transport.close();//关闭连接
        }

    }
}
