package com.forg.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 显式锁
 * 内置锁是实现在java虚拟机的，显示锁是借用AQS实现的，内置锁一个锁对应一个等待队列，
 * 显式锁一个lock可以对应多个等待队列，多个condition
 * tryLock()返回是否获取到锁，对于显示锁，极大提升了变成灵活性，可以在尝试n次后停止获取等等
 * ReentrantLock内部私有类继承了AQS，实现了独占模式的同步，可以通过condition对象获取它的等待队列
 * 内置锁有wait和notify方法，显示锁需借用condition对象实现这样的功能
 */
public class LockDemo {

    public static void main(String[] args){
        //简易显示锁
        Lock lock = new ReentrantLock();
        while (true){
            if(lock.tryLock()){
                //执行锁中的代码块
                try{
                    System.out.println(((ReentrantLock) lock).isLocked());
                }finally {
                    lock.unlock();
                }
            }
            SleepUtil.randomSleep();
        }
    }

    //可中断的锁
    public void interruptDemo(){
        Lock lock1 = new ReentrantLock();
        try{
            lock1.lockInterruptibly();
            try{
                //具体的代码块
            }catch (Exception ex){

            }finally {
                lock1.unlock();
            }
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }
    }

}

/**
 * 显示锁的等待与通知线程
 */
class WaitLockDemo{
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    //等待线程的典型模式
    public void conditionAwait() throws InterruptedException{
        lock.lock();
        try{
            while (true){//条件不满足
                condition.await();//使线程等待
            }
            //执行满足的代码
        }finally {
            lock.unlock();
        }
    }

    //通知线程的典型模式
    public void conditionSignal() throws InterruptedException{
        lock.lock();
        try {
            if (true){//完成条件
                condition.signalAll();//唤醒等待线程
            }
        }finally {
            lock.unlock();
        }
    }
}

/**
 * 线程接受到中断后会抛出interruptedException,可以捕捉，处理
 */
class InterruptDemo{
    public static void main(String[] args){
        Thread t1 = new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while (true){
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("我去把这个线程停掉");
                        break;
                    }
                    System.out.println(i++);
                }
            }
        },"t1");

        t1.start();
        SleepUtil.randomSleep();
        System.out.println("线程当前处于中断吗" + t1.isInterrupted());
        System.out.println("我准备给这个线程发中断信号");
        t1.interrupt();
        System.out.println("线程当前处于中断吗" + t1.isInterrupted());
    }

}
/**
 * 公平锁
 * 公平锁，对于先来到并阻塞的线程，上一个线程释放锁后，由最先排队的获取到锁，默认为false（非公平锁）
 */
class FairDemo {
    private ReentrantLock lock = new ReentrantLock(true);
}

class TimedLock{

    public static void main(){
        Lock lock1 = new ReentrantLock();
        try{
            lock1.tryLock(1000, TimeUnit.SECONDS);
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }finally {
            lock1.unlock();
        }
    }
}

/**
 * 读写锁,于之前互斥锁不同
 * 读写锁的原则是仅可同时被多个线程读，或者只有一个线程写，但是两者不得同时访问
 */
class ReadWriteLock {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private int i;

    public int getI(){
        readLock.lock();
        try{
            return i;
        }finally {
            readLock.unlock();
        }
    }

    public void setI(int i){
        writeLock.lock();
        try{
            this.i = i;
        }finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args){

    }
}
