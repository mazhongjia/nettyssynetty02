package com.mzj.netty.ssy._01_simple;

public class TestThread {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println("111");
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        thread.setDaemon(true);
        thread.start();
    }
}
