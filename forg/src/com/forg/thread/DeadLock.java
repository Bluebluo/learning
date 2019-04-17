package com.forg.thread;

import java.util.List;
/**
 *一旦系统进入死锁状态，将无法恢复，只能重新启动系统。，产生死锁的4个条件：
 *
 * 互斥条件：一个资源每次只能被一个线程使用。
 * 请求与保持条件：一个线程因请求资源而阻塞时，对已获得的资源保持不放。
 * 不剥夺条件：线程已获得的资源，在未使用完之前，不能强行剥夺。
 * 循环等待条件：若干线程之间形成一种头尾相接的循环等待资源关系。
 * 预防死锁只需要破坏上边四个条件之一就好，下边是一些建议：
 *
 * 线程在执行任务的过程中，最好进行开放调用。
 * 各个线程最好用固定的顺序来获取资源。
 * 可以让持有资源的时间有限。
 * 如果一个线程因为处理器时间全部被其他线程抢走而得不到处理器运行时间，这种状态被称之为饥饿，
 * 一般是由高优先级线程吞噬所有的低优先级线程的处理器时间引起的。
 *
 * 活锁虽然不会像死锁那样因为获取不到资源而阻塞，也不会像饥饿那样得不到处理器时间而无可奈何，
 * 活锁仍旧可以让程序无法执行下去，最好在遇到冲突重试时引入一定的随机性。
 */
public class DeadLock {
    /**
     * 这是一种较为明显的锁
     * 需要统一获取锁的顺序
     */
    public static void main(String[] args){
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    synchronized (lock1){
                        System.out.println("获取到了lock1的锁");
                        LockUtil.sleep(1000);
                        synchronized (lock2){
                            System.out.println("获取到了lock2的锁");
                        }
                    }
                }
            }
        },"t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    synchronized (lock2){
                        System.out.println("获取到了lock2的锁");
                        LockUtil.sleep(1000);
                        synchronized (lock1){
                            System.out.println("获取到了lock1的锁");
                        }
                    }
                }
            }
        },"t2").start();

    }
}

/**
 * 接下来看一种比较不明显的锁
 * setProcess里调用teacher.studentNotify
 * 则需要先获取student的锁，然后获取teacher的锁
 */
class Student{


    private Teacher teacher;

    private int process;

    public void setTeacher(Teacher teacher){
        this.teacher = teacher;
    }

    public synchronized int getProcess(){
        return process;
    }

    public synchronized void setProcess(int process){
        this.process = process;
        if(process == 100){
            teacher.studentNotify(this);//学生答完题，通知老师
        }
    }

    /**
     * 开放调用，避免循环等待条件
     * @param process
     */
    public void goodSetProcess(int process){
        synchronized (this){
            this.process = process;
        }
        if(process == 100){
            teacher.studentNotify(this);//学生答完题，通知老师
        }
    }
}

/**
 * teacher获取学生的成绩，调用student.getPorcess
 * 则需先获取teacher的锁，然后获取student的锁，有可能会产生死锁
 */
class Teacher{
    private List<Student> students;

    public void setStudents(List<Student> students){
        this.students = students;
    }

    public synchronized void studentNotify(Student student){
        students.remove(student);
    }

    public synchronized void getStudentsProcess(){
        students.forEach(student -> System.out.println(student.getProcess()));//获取学生答题状况
    }

    /**
     * 开放调用，避免循环等待条件
     */
    public void goodGetStudentProcess(){
        List<Student> newStudents ;
        synchronized (this){
            newStudents = students;
        }
        newStudents.forEach(student -> System.out.println(student.getProcess()));//获取学生答题状况
    }
}