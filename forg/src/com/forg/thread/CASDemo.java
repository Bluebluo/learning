package com.forg.thread;

import java.util.concurrent.atomic.*;

/**
 * lock，synchronized都是阻塞型悲观锁，
 * CAS CompareAndSwap，冲突检查机制，先比较再更新
 * 使用原子变量，以原子的方式更新某种类型的变量，代替锁
 * 当多个线程使用CAS同时更新一个变量的时候，只有一个线程可以成功，失败的也不会被阻塞或挂起，而是重新尝试
 */
public class CASDemo {
    private static AtomicInteger atomicInt = new AtomicInteger(1);

    private static AtomicLongArray atomicLongArray= new AtomicLongArray(new long[]{1,2,3});

    private static AtomicReference<Obj> atomicReference = new AtomicReference<Obj>();

    private static AtomicIntegerFieldUpdater<MyObj> updater = AtomicIntegerFieldUpdater.newUpdater(MyObj.class,"1");

    private static class Obj{

        private int i;

        public Obj(int i){
            this.i = i;
        }

    }

    private static class MyObj{
        public volatile int i;

        public MyObj(int i){
            this.i = i;
        }
    }

    public static void main(String[] args){
        atomicInt.compareAndSet(1,5);
        atomicLongArray.compareAndSet(1,2,5);
        Obj obj = new Obj(1);
        atomicReference.set(obj);
        atomicReference.compareAndSet(obj,new Obj(6));
        MyObj myObj = new MyObj(5);
        updater.compareAndSet(myObj,6,7);
    }
}
