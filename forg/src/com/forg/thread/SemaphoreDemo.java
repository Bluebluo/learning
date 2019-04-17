package com.forg.thread;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

/**
 * 限制并发线程的数量
 * limit满后，多余线程只能等待有线程执行完，去竞争许可证
 */
public class SemaphoreDemo {

    public static void main(String[] args){

        Semaphore semaphore = new Semaphore(5);//只有5个许可证

        for (int i = 0; i < 20; i++){ //创建20个线程
            int num = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("第" + num + "个线程执行任务" + LocalDateTime.now().toString());
                    SleepUtil.randomSleep();
                    semaphore.release();//一次释放一个许可
                }catch (InterruptedException ex){
                    throw new RuntimeException(ex);
                }
            }).start();
        }
    }

}

/**
 * exchanger交换信息
 * 如A发送，B接收到，B先执行完，然后A执行完(目测)
 */
class ExchangerDemo{

    public static void main(String[] args){
        Exchanger<String> exchanger = new Exchanger();

        new Thread(() -> {
            String manWords = "老婆我爱你";
            try{
                String womenWords = exchanger.exchange(manWords);
                System.out.println("2老公听到的是：" + womenWords + LocalDateTime.now().toString());
            }catch (InterruptedException ex){
                throw new RuntimeException(ex);
            }
        },"man").start();//man发送后，会一直阻塞到women回应

        new Thread(() -> {
            String womenWords = "吃屎去吧";
            try{
                Thread.sleep(5000);
                String manWords = exchanger.exchange(womenWords);
                System.out.println("3老婆听到的是:" + manWords + LocalDateTime.now().toString());
            }catch (InterruptedException ex){
                throw new RuntimeException(ex);
            }
        },"women").start();

        new Thread(() -> {
            String womenWords = "别等了，跟我走吧";
            try{
                String manWords = exchanger.exchange(womenWords);
                System.out.println("1小三听到的是：" + manWords + LocalDateTime.now().toString());
            }catch (InterruptedException ex){
                throw new RuntimeException(ex);
            }
        },"小三").start();

        new Thread(() -> {
            String womenWords = "让你矫情，没了吧";
            try{
                Thread.sleep(2000);
                String manWords = exchanger.exchange(womenWords);
                System.out.println("4大妈听到的是：" + manWords + LocalDateTime.now().toString());
            }catch (InterruptedException ex){
                throw new RuntimeException(ex);
            }
        },"大妈").start();
    }
}