package com.wyf.codenotes;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VolatileTest extends  Thread{
    private boolean isRunning = true;
    public boolean isRunning(){
        return isRunning;
    }
    public void setRunning(boolean isRunning){
        this.isRunning= isRunning;
    }
    public void run(){
        System.out.println("进入了run...............");
        while (isRunning){System.out.println("run...............");}
        System.out.println("isRunning的值被修改为为false,线程将被停止了");
    }
    @Test
    public void mainTest() throws Exception {
        VolatileTest volatileThread = new VolatileTest();
        volatileThread.start();
        Thread.sleep(1);
        volatileThread.setRunning(false);   //停止线程
        Thread.sleep(100);
        System.out.println("程序停止");
//        AtomicInteger inc = new AtomicInteger();

    }
}