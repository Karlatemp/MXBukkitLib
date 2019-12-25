/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Controller.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.time;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class Controller implements Runnable {

    private static ArrayList<Task> ts = new ArrayList();
    public static final Controller c = new Controller();
    private final Thread mainthr;
    public boolean isStarted(){return started;}
    public static Controller getController(){return c;}
    private Controller() {
        Thread main = Thread.currentThread();
        StackTraceElement[] st = main.getStackTrace();
        //consolePrintln(st[2].getClassName());
        if (!st[2].getClassName().equals(Controller.class.getName())) {
            throw new AssertionError("No " + getClass().getName() + " instances for you!");
        }
        mainthr = new Thread(this, "Timefor-each caller");
        started = false;
    }
    private boolean started = false;
    private boolean for_eaching = false;

    public void start() {
        if (started
                || (mainthr.getState() != Thread.State.NEW
                && mainthr.getState() != Thread.State.TERMINATED)) {
            throw new IllegalAccessError("The main runnable was started...");
        }
        try {
            //started = true;
            mainthr.start();
        } catch (Error err) {
            throw err;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public void run() {
        if (started) {
            throw new IllegalAccessError("The main runnable was started...");
        }
        try {
            started = true;
            for_eaching = true;
            ArrayList<Task> fuck = new ArrayList();
            while (for_eaching) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                fuck.clear();
                synchronized (ts) {
                    long now = System.currentTimeMillis();
                    while (!ts.isEmpty()) {
                        Task t = ts.remove(0);
                        if (t.disable) {
                        } else if (t.nextSubTime() <= now) {
                            if (t.autoAppend()) {
                                fuck.add(t);
                                t.resetTask();
                            }
                            try {
                                t.call();
                            } catch (Throwable thr) {
                                thr.printStackTrace();
                            }
                        } else {
                            fuck.add(t);
                        }
                    }
                    ts.addAll(fuck);
                    fuck.clear();
                }
            }
        } catch (Error err) {
            throw err;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        } finally {
            started = false;
        }
    }
    @Deprecated
    public void stop() {
        stop0();
    }
    private void stop0(){
        for_eaching = false;
    }
    public void register(Task task) {
        Objects.requireNonNull(task);
        task.resetTask();
        ts.add(task);
    }

    public void cannel(Task task) {
        Objects.requireNonNull(task);
        synchronized (ts) {
            if (ts.contains(task)) {
                ts.remove(task);
            }
        }
    }
    @SuppressWarnings("all")
    public static void main(String[] a) {
        //UFO<Task> ufo = new UFO();
        c.start();
        new Task(true, 1000, () -> {
            System.out.println("Wait time!");
        }).register();
        new Task(false,10000,()->{
            c.stop();
        }).register();
        
    }
}
