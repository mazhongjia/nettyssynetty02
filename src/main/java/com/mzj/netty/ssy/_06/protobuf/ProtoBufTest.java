package com.mzj.netty.ssy._06.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoBufTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        /**
         * 通过Message对应的Builder创建Message对象
         */
        DataInfo.Student student = DataInfo.Student.newBuilder().setName("马仲佳").setAge(34).setAddress("沈阳").build();
        byte[] student2ByteArray = student.toByteArray();

        DataInfo.Student student2 = DataInfo.Student.parseFrom(student2ByteArray);
        System.out.println(student2.getName());
        System.out.println(student2.getAge());
        System.out.println(student2.getAddress());
    }
}
