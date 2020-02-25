package com.mzj.netty.ssy._08_grpc.mycode;

import com.mzj.netty.ssy._08_grpc.MyRequest;
import com.mzj.netty.ssy._08_grpc.MyResponse;
import com.mzj.netty.ssy._08_grpc.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    /**
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
}
