package com.forg.thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 与餐馆的例子一致，使用显式锁实现
 * ReentrantLock实现的是独占模式的线程，所以进程一旦竞争到锁，则该进程完成所有任务（炒至5个菜，端至0个菜）
 * 而想要实现每个菜，每次端都由线程来竞争，我觉得需要重写ReentrantLock的lock和unlock方法。
 * @param <E>
 */
public class ConditionBlockedQueue<E> {

    private Lock lock = new ReentrantLock();

    Condition notEmptyCondition = lock.newCondition();//服务员的队列,队列未空条件

    Condition notFullCondition = lock.newCondition();//厨师的队列，队列未满条件

    private Queue<E> queue= new LinkedList<>();

    private int limit;

    public ConditionBlockedQueue(int i){
        this.limit = i;
    }

    public int size(){
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    public void add(E e) throws InterruptedException{
        lock.lock();
        try{
            while (true){
                while (size() >= limit) {
                    System.out.println("size = " + size() + Thread.currentThread().getName() +"厨师休息下");
                    notFullCondition.await();//厨师等待，让出锁
                }
                boolean result = queue.add(e);
                System.out.println("size = " + size() + Thread.currentThread().getName() + "，厨师开始工作了，队列加1咯");
                notEmptyCondition.signalAll();//通知非空队列，去减一
            }
        }finally {
            lock.unlock();
        }
    }

    public void remove() throws InterruptedException{
        lock.lock();
        try {
            while (true){
                while (size() == 0){
                    System.out.println("size = " +size() + Thread.currentThread().getName() + "服务员休息下");
                    notEmptyCondition.await();//服务员等待，让出锁
                }
                E e = queue.remove();
                System.out.println("size = " +size() + Thread.currentThread().getName() + "，服务员开始工作了，队列减1咯");
                notFullCondition.signalAll();
            }
        }finally {
            lock.unlock();
        }
    }

    public static void test(){
        ConditionBlockedQueue<Food> resturant = new ConditionBlockedQueue<Food>(5);
        for (int i = 0; i < 4; i++){
            new Thread(new Runnable() {
                Food food = new Food();
                @Override
                public void run() {
                    try{
                        resturant.add(food);
                    }catch (InterruptedException ex){
                        throw new RuntimeException(ex);
                    }
                }
            }).start();
        }
        for (int i = 0; i < 3; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        resturant.remove();
                    }catch (InterruptedException ex){
                        throw new RuntimeException(ex);
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args){
        test();
    }

}

class DoubleConditionBlockedQueue {

    private DoubleLock lock = new DoubleLock();

    Condition notFullCondition = lock.newCondition();

    Condition notEmptyCondition = lock.newCondition();

    private Queue<Food> queue = new LinkedList<Food>();

    private int limit;

    public DoubleConditionBlockedQueue(int limit){
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "DoubleConditionBlockedQueue";
    }

    public int size(){
        lock.lock();
        try{
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    public void add(Food food){
        lock.lock();
        try{
            while (true){
                while (size() >= limit){
                    System.out.println("size = " + size() + "厨师休息下，做菜进入等待队列");
                    notFullCondition.await();
                }
                queue.add(food);
                System.out.println("size = " + size() + Thread.currentThread().getName() + "，厨师开始工作了，队列加1");
                notEmptyCondition.signalAll();//通知端菜
            }
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }finally {
            lock.unlock();
        }
    }

    public void remove(){
        lock.lock();
        try {
            while (true){
                while (size() == 0) {
                    try {
                        System.out.println("size = " + size() + "服务员休息下，端菜进入等待队列");
                        notEmptyCondition.await();//服务员端菜线程等待
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                queue.remove();
                System.out.println("size = " +size() + Thread.currentThread().getName() + "，服务员开始工作了，队列减1");
                notFullCondition.signalAll();//通知做菜
            }
        }finally {
            lock.unlock();
        }
    }

    public static void test(){
        DoubleConditionBlockedQueue resturant = new DoubleConditionBlockedQueue(5);
        for (int i = 0; i < 4; i++){
            new Thread(new Runnable() {
                Food food = new Food();
                @Override
                public void run() {
                    resturant.add(food);//启动做菜线程
                }
            }).start();
        }
        for (int i = 0; i < 3; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    resturant.remove();//启动端菜线程
                }
            }).start();
        }
    }

    public static void main(String[] args){
        test();
    }

}