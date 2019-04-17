package com.forg.thread;

import java.time.LocalDateTime;

/**
 * 如果一个线程持有了这个锁，它可以进入被这个锁保护的任何代码块，称为锁的重入
 * 这是锁对象的方法，可以锁SynchronizedDemo整个对象
 */
public class SynchronizedDemo {

    private Object lock = new Object();

    public void m1(){
        synchronized (lock){
            System.out.println("first method m1");
            m2();
        }
    }

    public void m2(){
        synchronized (lock){
            try {
                Thread.sleep(3000);
            }catch (InterruptedException ex){
                throw new RuntimeException();
            }
            System.out.println("second method m2");
        }
    }

    public static void test(){
        SynchronizedDemo demo = new SynchronizedDemo();
       Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我想先获取到这个锁" + LocalDateTime.now().toString());
                demo.m1();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我想执行m2但是被锁了" + LocalDateTime.now().toString());
                demo.m2();
            }
        });
        t1.start();
        t2.start();
    }


    public synchronized void synTest(){
        System.out.println("锁方法");
    }

    public static void main(String[] args){
        test();
    }

}
