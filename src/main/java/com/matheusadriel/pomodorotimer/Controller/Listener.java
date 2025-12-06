package com.matheusadriel.pomodorotimer.Controller;

public interface Listener {
    void onTick(int remaining);
    void onFinish();
}
