package com.matheusadriel.model;

import java.util.Timer;
import java.util.TimerTask;

public class PomodoroTimer {

    private int time;
    private final int initialTime;
    private Timer timer;
    private boolean running = false;

    public PomodoroTimer(int seconds) {
        this.time = seconds;
        this.initialTime = seconds;
    }

    public int getTime() {
        return time;
    }

    public boolean isRunning() {
        return running;
    }

    private void decrease() {
        if (time > 0) {
            time--;
        }
    }

    public void start() {
        if (running) return;

        running = true;
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                decrease();

                System.out.println("Time: " + time);

                if (time == 0) {
                    stop();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        running = false;
    }

    public void reset() {
        stop();
        time = initialTime;
    }
}
