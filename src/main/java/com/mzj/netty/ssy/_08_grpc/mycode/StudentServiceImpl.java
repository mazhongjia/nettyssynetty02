package com.mzj.netty.ssy._08_grpc.mycode;

import com.mzj.netty.ssy._08_grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    /**
     * 方式1：输入参数与返回值都是普通的java对象
     *
     * grpc生成得即使有返回值得方法，也是void，而方法得返回值通过调用StreamObserver对象的onNext方法实现向下一个处理器流转
     *
     * rpc业务方法
     * @param request：客户端发送的请求对象
     * @param responseObserver：用于向客户端返回结果的对象
     */
    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("[服务端] 接收到客户端信息："+request.getUsername());

        //返回客户端MyResponse对象
        responseObserver.onNext(MyResponse.newBuilder().setRealname("mazhongjia").build());
        //标识这次方法调用结束
        responseObserver.onCompleted();
    }

    /**
     * 方式2：输入参数是普通的java对象，返回值是流类型
     */
    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("接收到客户端信息：" + request.getAge());

        responseObserver.onNext(StudentResponse.newBuilder().setName("马仲佳").setAge(20).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("呼娜").setAge(21).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("maxiaotang").setAge(22).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("mawenge").setAge(23).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("lvying").setAge(20).setCity("北京").build());

        responseObserver.onCompleted();
    }


    /**
     * 方式3：输入参数是流类型，返回值是普通的java对象
     *
     * 此种方式，rpc方法参数仅有一个StreamObserver用来返回时调用，而请求参数是作为rpc方法的返回值存在（比较怪异）
     *
     * 实现此rpc方法时，需要返回一个StreamObserver<T>的实现，通过实现其中onNext、onError、onCompleted三个回调方法完成接收请求-->处理请求--->返回响应的三个流程
     */
    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver){

        return new StreamObserver<StudentRequest>() {

            /**
             * 请求到来时，会调用这里的onNext方法，因为是流式请求参数，所以会调用多次
             * @param value
             */
            @Override
            public void onNext(StudentRequest value) {
                //客户端请求到来了一次，会调用onNext方法一次，发送输入参数为StudentRequest类型，整体一次请求为多个StudentRequest对象组成的流
                System.out.println("onNext："+value.getAge());
            }

            /**
             * 出错时调用onError
             * @param t
             */
            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            /**
             * 客户端流式数据全部发送完毕后调用onCompleted，此方法中需要将请求处理结果（响应）返回给客户端
             */
            @Override
            public void onCompleted() {
                //客户端以流的方式发送完一次请求所有StudentRequest对象后，会调用此方法表示本次请求发送完毕
                //服务端被调用此方法后，需要将处理结果返回给客户端，此时返回的是一次普通java对象返回结果
                //模拟数据库查询后封装多个StudentResponse
                StudentResponse studentResponse = StudentResponse.newBuilder().setName("mazhongjia").setAge(34).setCity("北京").build();
                StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("huna").setAge(31).setCity("北京").build();

                //本服务接口返回数据类型为StudentResponseList（见IDL定义）
                StudentResponseList studentResponseList = StudentResponseList.newBuilder().addStudentResponse(studentResponse).addStudentResponse(studentResponse2).build();

                //服务端将结果返回给客户端
                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();

            }
        };

        //mzj分析：
        //客户端与服务端分别持有对方的StreamObserver对象：服务端持有调用getStudentsWrapperByAges的方法中参数StreamObserver对象，客户端持有getStudentsWrapperByAges方法返回的new的StreamObserver对象
        //建立好这种对象持有关系后，客户端通过调用自己持有StreamObserver的onNext方法将数据发送服务端
        //发送完所有数据后，客户端调用自己持有StreamObserver的onConpleted方法通知客户端
        //服务端发送给客户端返回结果时，操作的是自己的StreamObserver对象（通过getStudentsWrapperByAges方法参数拿到的）
    }

    /**
     * 方式4：输入参数是流类型，返回值也是流类型
     *
     * 请求参数泛型StreamRequest与返回结果泛型StreamRequest都是IDL中自定义的类型
     *
     * 说明：双向的流式数据传递原理：在完全不同的两个流中传递，两个流是互相独立的，一般情况从逻辑上来说，一方流关闭，另一方开着也没啥用，所以逻辑上来说也应该关闭
     *
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest value) {
                //打印客户端发送来的数据
                System.out.println("###"+value.getRequestInfo());
                //每收到客户端发送一条数据，也向客户端回复一条数据
                //这里的实现是在在收到客户端数据时也向客户端返回一条数据
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                //这里实现是：客户端发送完毕通知服务端时，服务端也发送完毕通知客户端
                //虽然客户端与服务端如果都是流式传递时，双方是在不同流上
                //但是从逻辑上来说，一方流关闭，另一方开着也没啥用，所以逻辑上来说也应该关闭
                responseObserver.onCompleted();
            }
        };
    }


}
