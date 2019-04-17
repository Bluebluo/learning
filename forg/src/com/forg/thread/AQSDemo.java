package com.forg.thread;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AQS比较牛逼
 * 所以衍生出来的工具类很多
 * 1.ReentrantLock
 * 2.CountDownLatch
 * 3.CyclicBarrier
 * 4.Semaphore
 * 5.
 */
public class AQSDemo {

    ConditionBlockedQueue reenreantLock = new ConditionBlockedQueue(4);

    CountDownLatchDemo countDownLatchDemo = new CountDownLatchDemo();

    CyclicBarrierDemo cyclicBarrierDemo = new CyclicBarrierDemo();

    SemaphoreDemo semaphoreDemo = new SemaphoreDemo();




}
