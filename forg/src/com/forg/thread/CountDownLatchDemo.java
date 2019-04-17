package com.forg.thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLaunch是基于AQS的一种工具
 * 是一个计数器，使用.countDown和await可以达到和join方法一样的效果，且更为灵活
 * countDown一旦减至0，则无效
 * join  比如thread1.join()，线程1是在main线程里执行，join后，就是main要等thread1执行完才可以执行
 *
 */
public class CountDownLatchDemo {

    public static void main(String[] args){
        Thread[] threads = new Thread[5];
        CountDownLatch countDownLatch = new CountDownLatch(threads.length);

        for (int i = 0; i < threads.length; i++){
            int num = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SleepUtil.randomSleep();
                    System.out.println("第" + num + "个任务完成");
                    countDownLatch.countDown();
                }
            }).start();
        }

        try{
            countDownLatch.await();//一直到countDown到0，main线程才继续执行
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }

        System.out.println("等所有线程执行完之后执行");
    }

}

class BetterLatchDemo{
    public static void main(String[] args){
        Queue<Runnable> runnables = new LinkedList<Runnable>();
        CountDownLatch countDownLatch = new CountDownLatch(4);
        for (int i = 0; i < 5; i++){
            int num = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SleepUtil.randomSleep();
                    System.out.println("第" + num + "个子任务完成");
                    countDownLatch.countDown();
                }
            };
            runnables.add(runnable);//创建5个线程
        }

        for (int i = 0; i < 2; i++){
            int num = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        Runnable runnable = null;
                        synchronized (BetterLatchDemo.class){
                            if(runnables.size() < 1){
                                break;
                            }
                            runnable = runnables.remove();
                        }
                        System.out.println("第" + num + "个线程执行任务");
                        runnable.run();
                    }
                }
            }).start();
        }

        try{
            countDownLatch.await();
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }
        System.out.println("所有线程执行完后执行");
    }
}