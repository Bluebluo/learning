package com.forg.thread;

/**
 * 利用修厕所这个场景，理解wait.nofifty/notifyAll
 * 以及内存可见性volatile关键字
 */
public class WashRoom {

    //使用volatile修饰，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值
    private volatile boolean isAvailiable = false;

    private Object lock = new Object();

    public boolean isAvailiable(){
        return isAvailiable;
    }

    public void setAvailiable(boolean isAvailiable){
        this.isAvailiable = isAvailiable;
    }

    public Object getLock(){
        return lock;
    }

    public static void main(String[] args){
        WashRoom washRoom = new WashRoom();
        new Thread(new ShitTask(washRoom,"阿猫0"),"shit-task-1").start();
        threadSleep(1000);
        new Thread(new RepairTask(washRoom),"repair-task").start();
        new Thread(new ShitTask(washRoom,"阿猫1"),"shit-task-1").start();
        new Thread(new ShitTask(washRoom,"阿猫2"),"shit-task-1").start();
        threadSleep(3000);
        new Thread(new BreakTsk(washRoom),"bread-task-1").start();
        new Thread(new ShitTask(washRoom,"阿猫3"),"shit-task-1").start();
        new Thread(new ShitTask(washRoom,"阿猫4"),"shit-task-1").start();
        new Thread(new ShitTask(washRoom,"阿猫5"),"shit-task-1").start();
        new Thread(new RepairTask(washRoom),"repair-task").start();
    }

    public static void threadSleep(int time){
        try {
            Thread.sleep(time);
        }catch (InterruptedException ex){
            throw new RuntimeException();
        }
    }
}

/**
 * 好多人等着shit，先获取锁，如果测试不可用，那就等
 */
class ShitTask implements Runnable{

    private WashRoom washRoom;

    private String name;

    public  ShitTask(WashRoom washRoom, String name){
        this.washRoom = washRoom;
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (washRoom.getLock()){
            System.out.println(name + "获取了锁");
            while (!washRoom.isAvailiable()){
                //wait
                try{
                    washRoom.getLock().wait();
                }catch (InterruptedException ex){
                    throw new RuntimeException();
                }
            }
            System.out.println(name + "上完了");
        }
    }
}

/**
 * 捣蛋线程，把厕所弄坏
 */
class BreakTsk implements Runnable{

    private WashRoom washRoom;

    public BreakTsk(WashRoom washRoom){
        this.washRoom = washRoom;
    }

    @Override
    public void run() {
        washRoom.setAvailiable(false);
        System.out.println("哈哈，我又把厕所搞坏了");
    }
}

/**
 * 修理工，获取锁，修厕所
 */
class RepairTask implements Runnable{

    private WashRoom washRoom;

    public RepairTask(WashRoom washRoom){
        this.washRoom = washRoom;
    }

    @Override
    public void run() {
        synchronized (washRoom.getLock()){
            while(!washRoom.isAvailiable()){
                System.out.println("维修工获取到了锁，正在修厕所，耗时3s");
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException ex){
                    throw new RuntimeException();
                }
                washRoom.setAvailiable(true);
                washRoom.getLock().notifyAll();
                System.out.println("厕所修好了,现在通知所有人");
            }
        }
    }
}