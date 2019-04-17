package com.forg.thread;

/**
 * threadLocal 线程级别的全局变量
 * 线程安全的安全隐患，原子性操作，内存可见性，指令重排序
 * 处理原子性隐患，可以从数据的共享和可变性两个方面来处理。
 * 1.ThreadLocal，不同线程操作同一个 ThreadLocal 对象执行各种操作而不会影响其他线程里的值
 * 2.可变现，对变量加final修饰，让其不可变
 * 3.加锁，同步代码块，见SynchronizedDemo
 */
public class ThreadLocalDemo {

    public static ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<String>(){
        @Override
        protected String initialValue(){
            return "调用threadlocal.initialValue初始化值";
        }
    };



    public static void main(String[] args){
        System.out.println(ThreadLocalDemo.THREAD_LOCAL.get());
        ThreadLocalDemo.THREAD_LOCAL.set("与main函数关联的字符串");
        Thread t = Thread.currentThread();
        System.out.println(t.getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t1线程从ThreadLocal中获取到的值: "
                        + ThreadLocalDemo.THREAD_LOCAL.get());
                ThreadLocalDemo.THREAD_LOCAL.set("与t1线程关联的字符串");
                System.out.println("t1线程再次从ThreadLocal中获取到的值: "
                        + ThreadLocalDemo.THREAD_LOCAL.get());
            }
        },"t1").start();
        System.out.println("main线程从ThreadLocal里获取到的值: " + ThreadLocalDemo.THREAD_LOCAL.get());
    }

}
