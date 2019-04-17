package com.forg.thread;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

/**
 * AQS之多个线程可以同时获取锁
 */
public class DoubleLock {

    private class Sync extends AbstractQueuedSynchronizer{

        public Sync(){
            super();
            setState(2);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            while (true){
                int cur = getState();
                int next = getState() - arg;
                if(compareAndSetState(cur,next)){
                    return next;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            while (true){
                int cur = getState();
                int next = getState() + arg;
                if(compareAndSetState(cur,next)){
                    return true;
                }
            }
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

    }

    private Sync sync = new Sync();

    public void lock(){
        sync.tryAcquireShared(1);
    }

    public void unlock(){
        sync.tryReleaseShared(1);
    }

    public Condition newCondition(){
        return sync.newCondition();
    }

    public boolean hasWaiters(Condition condition){
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

}
