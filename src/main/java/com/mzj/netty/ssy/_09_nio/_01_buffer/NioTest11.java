package com.mzj.netty.ssy._09_nio._01_buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 文件锁
 *
 * @Auther: mazhongjia
 * @Description:
 */
public class NioTest11 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
               try{
                   //1、创建RandomAccessFile对象
                   RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9.txt","rw");
                   //2、通过RandomAccessFile对象获取：NIO的文件Channel对象（1.4版本与NIO一起增加到RandomAccessFile中）
                   FileChannel fileChannel = randomAccessFile.getChannel();

                   //3、获取文件锁对象
                   FileLock fileLock = null;//第三个参数：true共享锁，false：排他锁，前两个参数：从文件位置3开始，锁定6个字节长度
                   try {
                       fileLock = fileChannel.lock(3,6,true);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   System.out.println("Thread1...valid：" + fileLock.isValid());//判断锁有效性（是否成功锁定）
                   System.out.println("Thread1...lock type : " + fileLock.isShared());//判断锁的类型，是否是共享锁
                    Thread.sleep(100000);
                   fileLock.release();//释放锁

                   //4、关闭文件
                   randomAccessFile.close();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
        thread1.start();


        Thread.currentThread().sleep(100000);
    }
}
