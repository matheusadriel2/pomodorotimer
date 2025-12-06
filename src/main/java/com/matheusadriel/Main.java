package com.matheusadriel;

import com.matheusadriel.model.PomodoroTimer;

public class Main {
    static void main() {
        PomodoroTimer pomo1 = new PomodoroTimer(60 * 25);

        pomo1.start();
    }
}

