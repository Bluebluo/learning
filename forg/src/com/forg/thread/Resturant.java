package com.forg.thread;

import java.util.LinkedList;
import java.util.Queue;

public class Resturant {
    public static void main(String[] args){
        Queue<Food> queue = new LinkedList<Food>();
        new Cook(queue, "1号厨子").start();
        new Cook(queue, "2号厨子").start();
        new Cook(queue, "3号厨子").start();
        new Cook(queue, "4号厨子").start();
        new Cook(queue, "5号厨子").start();
        SleepUtil.randomSleep();
        new Waiter(queue,"1号服务员").start();
        new Waiter(queue,"2号服务员").start();
        new Waiter(queue,"3号服务员").start();
    }
}

class Cook extends Thread {

    private Queue<Food> queue;

    public Cook(Queue<Food> queue, String name){
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true){
            SleepUtil.randomSleep(getName());
            Food food = new Food();
            synchronized (queue){
                while (queue.size() > 4){
                    try{
                        System.out.println("厨师持有锁,队列大小为" + queue.size());
                        queue.wait();
                    }catch (InterruptedException ex){
                        throw new RuntimeException(ex);
                    }
                }
                queue.add(food);
                System.out.println(getName() + "生产了" + food);
                queue.notifyAll();
            }
        }
    }
}

class Waiter extends Thread{

    private Queue<Food> queue;

    public Waiter(Queue<Food> queue, String name){
        super(name);
        this.queue = queue;
    }

    @Override
    public void run(){
        while (true){
            Food food;
            synchronized (queue){
                while (queue.size() < 1){
                    try {
                        System.out.println(getName() + "来了，队列大小为"+ queue.size() + ",外面等一下");
                        queue.wait();
                    }catch (InterruptedException ex){
                        throw new RuntimeException(ex);
                    }
                }
                food = queue.remove();
                System.out.println(getName() + "获取到" + food + "队列现在大小为" + queue.size());
                queue.notifyAll();
                SleepUtil.randomSleep(getName());
            }
        }
    }
}

class Food {

    private static int count = 0;

    private int i;

    public Food(){
        i = ++count;
    }

    @Override
    public String toString(){
        return "第" + i + "个菜";
    }

}
