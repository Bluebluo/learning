package com.forg.thread;

import java.io.*;

/**
 * 线程间，通过管道通信
 */
public class PipedDemo {

    public static void main(String[] args){
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream();
        try {
            inputStream.connect(outputStream);
        }catch (IOException ex){
            throw  new RuntimeException(ex);
        }
        new readThread(inputStream).start();
        new writeThread(outputStream).start();
    }
}

class readThread extends Thread{
    private PipedInputStream in;

    public readThread(PipedInputStream in){
        this.in = in;
    }

    @Override
    public void run(){
        int i = 0;
        try {
            while ((i = in.read()) != -1){
                System.out.println(i);
            }
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }finally {
            try{
                in.close();
            }catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }
}

class writeThread extends Thread{
    private PipedOutputStream out;

    public writeThread(PipedOutputStream out){
        this.out = out;
    }

    @Override
    public void run(){
        byte[] bytes = {1,2,3,4,5};
        try{
            out.write(bytes);
            out.flush();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }finally {
            try{
                out.close();
            }catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }

}
