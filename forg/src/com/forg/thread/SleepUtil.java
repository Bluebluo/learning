package com.forg.thread;

import java.util.Random;

public class SleepUtil {

    public static Random random = new Random();

    public static void randomSleep(String name){
        try {
            int time = random.nextInt(1000);
            Thread.sleep(time);
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }
    }

    public static void randomSleep(){
        try {
            int time = random.nextInt(1000);
            Thread.sleep(time);
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }
    }

}
