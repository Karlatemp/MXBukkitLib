/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ScheduledTasks.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.time;

import com.google.gson.Gson;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class ScheduledTasks implements Runnable {

    public static ScheduleTask init(HashMap map) {
        return ScheduleTask.create(map);
    }
    private final ArrayList<ScheduleTask> tasks;

    private ScheduledTasks() {
        tasks = new ArrayList();
    }

    public boolean register(ScheduleTask t) {
        if (tasks.contains(t)) {
            return false;
        }
        tasks.add(t);
        return true;
    }

    public void start() {
        if(!Controller.getController().isStarted()){
            Controller.getController().start();
        }
        Controller.getController().register(new Task(true, 10, this));
    }

    public void run() {
        synchronized(tasks){
            for(ScheduleTask task : tasks){
                //System.out.println(task);
                if(task.test()){
                    task.run();
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {
        ScheduledTasks main = new ScheduledTasks();
        Gson g = new Gson();
        HashMap map = g.fromJson(new FileReader("E:\\Exes\\NetBeans\\Tomcat 9.0_TomcatServerV9\\webapps\\coolq\\private\\a.json"), HashMap.class);
        System.out.println(map);
        main.register(init(map).setCustom(
                (a)->{
                    boolean b = ManagementFactory.getRuntimeMXBean().getUptime() > 3000;
                    if(b){System.out.println("stop!");}
                    return b;
                }
        ));
        main.start();
    }

}
