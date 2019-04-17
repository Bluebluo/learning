package com.forg.thread;

public class LockUtil {

    public static void sleep(int seconds){
        try{
            Thread.sleep(seconds);
        }catch (InterruptedException ex){
            throw new RuntimeException(ex);
        }
    }
}
