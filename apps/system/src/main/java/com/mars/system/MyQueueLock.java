package com.mars.system;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

public class MyQueueLock {

    private static Unsafe unsafe = unsafe();

    private static final long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(MyQueueLock.class.getDeclaredField("state"));
        }catch (NoSuchFieldException e){
            throw new RuntimeException(e);
        }
    }

    private volatile int state = 0;

    private Thread holder;

    private Queue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    public void lock(){

        if (aquire()){
            return;
        }

        Thread currentThread = Thread.currentThread();
        waitQueue.add(currentThread);

        for (;;){

            if (currentThread == waitQueue.peek() && aquire()){
                waitQueue.poll();
                return;
            }
            LockSupport.park(currentThread);
        }

    }

    public void unlock(){

        if (holder != Thread.currentThread()){
            throw new RuntimeException("no current thread to unlock");
        }

        int state = getState();
        if (compareAndSwapStatus(state, state - 1)){
            Thread head = waitQueue.peek();
            if (head != null){
                LockSupport.unpark(head);
            }
        }

    }

    boolean aquire(){
        int old = getState();
        Thread currentThread = Thread.currentThread();
        if (old != 0){
            /**
             * 此步的判断为可重入锁的实现
             */
            if (currentThread == holder){
                compareAndSwapStatus(old, old + 1);
                return true;
            }
            return false;
        }

        /**
         * waitQueue.size() == 0 表示第一个线程开始加锁
         * currentThread == waitQueue.peek() 判断当前抢锁的线程是否为队列中
         * 这两步的判断是为了实现公平锁
         * 如果非公平锁，则不用加此判断
         */
        if ((waitQueue.size() == 0 || currentThread == waitQueue.peek()) && compareAndSwapStatus(old, old + 1)){
            setHolder(currentThread);
            return true;
        }

        return false;
    }

    boolean compareAndSwapStatus(int expect, int res){
        return unsafe.compareAndSwapInt(this, stateOffset, expect, res);
    }

    public int getState() {
        return state;
    }

    public void setHolder(Thread holder) {
        this.holder = holder;
    }

    static Unsafe unsafe(){

        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        }catch (NoSuchFieldException | IllegalAccessException e){
        }

        return null;
    }

}
