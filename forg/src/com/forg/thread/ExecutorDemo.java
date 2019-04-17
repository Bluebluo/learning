package com.forg.thread;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * 线程实现的3种方式，继承Thread类，实现Runnable接口，使用Executor线程池
 */
public class ExecutorDemo {


    //最开始线程池大小为0，之后每提交一个任务创建一个线程，知道nThreads后，线程数量不再变化
    Executor executor = Executors.newFixedThreadPool(10);//创建固定个线程的线程池

    //为每个任务分配一个线程，一个线程执行完60s之后无动作，将回收该线程
    Executor executor1 = Executors.newCachedThreadPool();//创建可缓存的线程池

    //、。、、、
    Executor executor2 = Executors.newSingleThreadExecutor();//创建单线程的线程池

    //创建固定大小的线程池，可以选择以延时或定时的方式执行任务
    Executor executor3 = Executors.newScheduledThreadPool(10);

}

class SerialExecutor implements Executor{
    @Override
    public void execute(Runnable command) {
        //可以自定义执行策略
        command.run();//串行执行策略
        new Thread(command).start();//为每个任务创建一个线程
    }
}

/**
 *
 */
class SechedualedDemo {
    private static class PrintTask implements Runnable{

        private String s;

        public PrintTask(String s) {
            this.s = s;
        }

        @Override
        public void run(){
            System.out.println(s + LocalDateTime.now().toString());
        }

    }

    private static class AddTask implements Callable{
        private int i;

        private int j;

        public AddTask(int i, int j){
            this.i = i;
            this.j = j;
        }

        @Override
        public Object call() throws Exception {
            int sum = i + j;
            System.out.println("运算结果是：" + sum);
            return "执行完了";
        }

    }

    public static void runTest(){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        System.out.println(LocalDateTime.now().toString());
        Future future = service.schedule(new PrintTask("1s后输出："),1, TimeUnit.SECONDS);//隔1s后打印
        service.schedule(new PrintTask("又1s后输出："),2, TimeUnit.SECONDS);//与创建时比间隔2s打印
        service.scheduleAtFixedRate(new PrintTask("首次隔5s，之后隔1s"),5,1,TimeUnit.SECONDS);
        System.out.println("run接口返回值为void，我们无法得知线程运行的状态，所以有线程一直在运行");
        try {
            System.out.println("run方法返回的值:" +  future.get());
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }catch (ExecutionException e){
            throw new RuntimeException(e);
        }
    }

    public static void callTest(){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        Future future = service.submit(new AddTask(1,5));
        try {
            String result = (String) future.get();//获取线程return结果
            System.out.println("call方法返回的值:" + result);
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }catch (ExecutionException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        runTest();
        //callTest();
    }

}