package com.forg.thread;

import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * 牛逼的AQS
 */
public class PlainLock {

    private class Sync extends AbstractQueuedLongSynchronizer{
        @Override
        protected boolean tryAcquire(long arg) {
            return compareAndSetState(0,1);
        }

        @Override
        protected boolean tryRelease(long arg) {
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
    }


    private Sync sync = new Sync();

    public void lock(){
        sync.acquire(1);
    }

    public void unlock(){
        sync.release(1);
    }

}


class PlainIncrement{
    private int i;

    private PlainLock plainLock = new PlainLock();

    public PlainIncrement(int i){
        this.i = i;
    }

    public int getI(){
        return i;
    }

    public void increse(){
        plainLock.lock();
        i++;
        plainLock.unlock();
    }

    public static void test(int num, int loopTimes){
        PlainIncrement pl = new PlainIncrement(0);
        for(int i = 0; i < num; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int k = 0; k < loopTimes; k++){
                        pl.increse();
                    }
                }
            });
            t.start();
            try{
                t.join();
            }catch (InterruptedException ex){
                throw  new RuntimeException();
            }
        }
        System.out.println("num=" + num + ", i=" + pl.getI());
    }

    public static void main(String[] args){

        test(200,10);
        test(200,100);
        test(200,1000);
        test(200,10000);
        test(200,100000);
    }

}