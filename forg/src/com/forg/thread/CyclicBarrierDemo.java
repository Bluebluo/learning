package com.forg.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * AQS之CyclicBarrier
 * 多个线程在某个地方相互等待，直到有规定数量的线程都执行到这个地方才能同时继续往下执行
 * ex:等人去打架
 * 可以循环使用使用reset
 */
public class CyclicBarrierDemo {
}

class Fighter extends Thread{

    private CyclicBarrier cyclicBarrier;

    public Fighter(CyclicBarrier cyclicBarrier, String name){
        super(name);
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run(){
        try{
            SleepUtil.randomSleep();
            System.out.println(getName() + "放学了，向校门跑去" + cyclicBarrier.getNumberWaiting());
            cyclicBarrier.await(3000, TimeUnit.MILLISECONDS);//有足够多的线程调用await时，线程才会执行。main线程自己执行
            System.out.println("我们5个齐了，去打架把");
        }catch (TimeoutException ex){
            System.out.println(getName() + "不等了");
            SleepUtil.randomSleep();
            cyclicBarrier.reset();
            try{
                throw new BrokenBarrierException();
            }catch (BrokenBarrierException e){
                System.out.println(getName() + "我们直接去打架");
            }
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }catch (BrokenBarrierException ex){
            System.out.println(getName() + "我们直接去打架");
            //throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier1 = new CyclicBarrier(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("前5个已经上了，后面的直接来打，不等了"));
        new Fighter(cyclicBarrier, "小明").start();
        new Fighter(cyclicBarrier, "小李").start();
        new Fighter(cyclicBarrier, "小张").start();
        new Fighter(cyclicBarrier, "小三").start();
        new Fighter(cyclicBarrier, "小样").start();
        new Fighter(cyclicBarrier, "小红").start();
        new Fighter(cyclicBarrier, "小白").start();
        new Fighter(cyclicBarrier, "小黑").start();
        new Fighter(cyclicBarrier, "小白").start();
        new Thread(() -> System.out.println("main线程不受约束")).start();
    }
}