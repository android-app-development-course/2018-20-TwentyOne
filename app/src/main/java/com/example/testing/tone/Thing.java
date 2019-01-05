package com.example.testing.tone;

import java.io.Serializable;
/**
 * Created by hhhqzh on 2018/12/11.
 */
public class Thing implements Serializable{
    private String thing;
    private String encourage;
    private String time;
    private String id;
    private boolean flag;
    private String hour;
    private String minutes;

    public Thing(String id ,String thing ,String encourage,String time,boolean flag,String hour,String minutes){
        this.id = id;
        this.thing = thing;
        this.encourage = encourage;
        this.time = time;
        this.flag = flag;
        this.hour = hour;
        this.minutes = minutes;
    }

    public void set_flag(boolean flag){this.flag = flag;}

    public String get_id(){return id;}
    public String get_thing(){return thing;}
    public String get_encourage(){return encourage;}
    public String get_time(){return time;}
    public String get_hour(){return hour;}
    public String get_minutes(){return minutes;}
    public boolean get_flag(){return flag;}
}