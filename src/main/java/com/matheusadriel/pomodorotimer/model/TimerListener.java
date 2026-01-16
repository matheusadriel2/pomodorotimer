package com.matheusadriel.pomodorotimer.model;

public interface TimerListener {
    void onTick(int remaining);
    void onFinish();
}
