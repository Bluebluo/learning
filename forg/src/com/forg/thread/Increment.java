package com.forg.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Increment {

    private int i;

    private Object lock = new Object();
    //无锁
    /*private void increse(){
        this.i++;
    }*/

    //阻塞锁
    private void increse(){
        synchronized (lock){
            this.i++;
        }
    }

    public int getI() {
        synchronized (lock) {
            return i;
        }
    }

    public static void test(int threaNum, int loopTimes){
        Increment increment = new Increment();
        Thread[] threads = new Thread[threaNum];
        for(int i = 0; i < threads.length; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < loopTimes; i++){
                        increment.increse();
                    }
                }
            });
            threads[i] = thread;
            thread.start();
        }

        for(Thread thread : threads){
            try{
                thread.join();
            }catch (InterruptedException ex){
                throw  new RuntimeException();
            }
        }
        System.out.println(threaNum + "个线程," + loopTimes + "次循环,结果" + increment.getI());
    }

    public static void main(String[] args){
        test(200,1);
        test(200,10);
        test(200,100);
        test(200,1000);
        test(200,10000);
        test(200,100000);
    }

}
class UnBlockIncrement{

    private AtomicInteger j = new AtomicInteger();

    public UnBlockIncrement(int j){
        this.j.set(j);
    }

    public void unBlockIncrese(){
        while (true){
            int cur = j.get();
            if(j.compareAndSet(cur,cur+1)){
                break;//如果失败立即重试
            }
        }
    }

    public int getJ(){
        return j.get();
    }

    public void test(int num, int loopTimes){
        for(int i = 0; i < num; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int k = 0; k < loopTimes; k++){
                        unBlockIncrese();
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
        System.out.println("num=" + num + ", j=" + getJ());
    }

    public static void main(String[] args){
        UnBlockIncrement unBlockIncrement = new UnBlockIncrement(0);
        unBlockIncrement.test(200,10);
        unBlockIncrement.test(200,100);
        unBlockIncrement.test(200,1000);
        unBlockIncrement.test(200,10000);
        unBlockIncrement.test(200,100000);
    }
}
