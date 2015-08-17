package com.example.huma.almalzma.model.announcement;

public class Event {
    private long time;
    /*staring from 1 to 3, where 1 is most important, 2 mid important and 3 least important*/
    private int priority;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
