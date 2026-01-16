package com.matheusadriel.pomodorotimer.model;

public class TimerSettings {
    private int focusDuration = 25; // minutos
    private int shortBreakDuration = 5; // minutos
    private int longBreakDuration = 15; // minutos
    private int longBreakAfter = 4; // sessões
    
    public int getFocusDuration() {
        return focusDuration;
    }
    
    public void setFocusDuration(int focusDuration) {
        this.focusDuration = Math.max(1, Math.min(60, focusDuration));
    }
    
    public int getShortBreakDuration() {
        return shortBreakDuration;
    }
    
    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = Math.max(1, Math.min(30, shortBreakDuration));
    }
    
    public int getLongBreakDuration() {
        return longBreakDuration;
    }
    
    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = Math.max(1, Math.min(60, longBreakDuration));
    }
    
    public int getLongBreakAfter() {
        return longBreakAfter;
    }
    
    public void setLongBreakAfter(int longBreakAfter) {
        this.longBreakAfter = Math.max(1, Math.min(10, longBreakAfter));
    }
    
    public int getFocusDurationSeconds() {
        return focusDuration * 60;
    }
    
    public int getShortBreakDurationSeconds() {
        return shortBreakDuration * 60;
    }
    
    public int getLongBreakDurationSeconds() {
        return longBreakDuration * 60;
    }
}
