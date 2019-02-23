/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.time;


/**
 *
 * @author Administrator
 */
public class Task {

    private final boolean append;
    private final long time;
    private final Runnable run;
    private long task;
    boolean disable;
    public Task(boolean append,long time,Runnable run){
        this.append = append;
        this.time = time;
        if(run == null){
            if(this instanceof Runnable){
                run = (Runnable) this;
            }
        }
        this.run = run;
        resetTask();
        disable = false;
    }
    public void call(){run.run();}
    public boolean autoAppend(){return append;}
    public long getTime(){return time;}
    public long nextSubTime(){return task;}
    public void register(){
        this.disable = false;
        Controller.c.register(this);
    }
    public void cannel(){
        Controller.c.cannel(this);
        this.disable = true;
    }
    public void resetTask() {
        this.task = System.currentTimeMillis() + time;
    }
}
