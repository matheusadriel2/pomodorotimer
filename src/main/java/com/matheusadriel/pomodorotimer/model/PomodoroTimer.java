package com.matheusadriel.pomodorotimer.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class PomodoroTimer {

    private int time;
    private int initialTime;
    private Timeline timeline;
    private boolean running = false;
    private final List<TimerListener> listeners = new ArrayList<>();
    private TimerType timerType;

    public PomodoroTimer(int seconds) {
        this.time = seconds;
        this.initialTime = seconds;
        this.timerType = TimerType.FOCUS;
    }
    
    public PomodoroTimer(int seconds, TimerType type) {
        this.time = seconds;
        this.initialTime = seconds;
        this.timerType = type;
    }
    
    public TimerType getTimerType() {
        return timerType;
    }
    
    public void setTimerType(TimerType type) {
        this.timerType = type;
    }
    
    public void setDuration(int seconds) {
        this.initialTime = seconds;
        if (!running) {
            this.time = seconds;
            notifyTick(time);
        }
    }
    
    public int getInitialTime() {
        return initialTime;
    }
    
    public double getProgress() {
        if (initialTime == 0) return 0.0;
        return 1.0 - ((double) time / (double) initialTime);
    }

    public int getTime() {
        return time;
    }

    public boolean isRunning() {
        return running;
    }

    public void addListener(TimerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(TimerListener listener) {
        listeners.remove(listener);
    }

    private void notifyTick(int remaining) {
        Platform.runLater(() -> {
            for (TimerListener listener : listeners) {
                listener.onTick(remaining);
            }
        });
    }

    private void notifyFinish() {
        Platform.runLater(() -> {
            for (TimerListener listener : listeners) {
                listener.onFinish();
            }
        });
    }

    private void decrease() {
        if (time > 0) {
            time--;
            notifyTick(time);
        }
    }

    public void start() {
        if (running) return;

        running = true;
        
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            decrease();
            if (time == 0) {
                stop();
                notifyFinish();
            }
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
        running = false;
    }

    public void reset() {
        stop();
        time = initialTime;
        notifyTick(time);
    }
}
