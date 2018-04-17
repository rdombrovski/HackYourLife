package com.bif812.roman.hackyourlife;

/**
 * Created by Sachin on 2018-04-07.
 */

public class Task {
    private String name,time;

    public Task(){

    }

    public Task(String name, String time){
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
