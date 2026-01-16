package com.matheusadriel.pomodorotimer.model;

public enum TimerType {
    FOCUS("FOCUS", "Focus Session", 25 * 60),
    SHORT_BREAK("SHORT BREAK", "Short break", 5 * 60),
    LONG_BREAK("LONG BREAK", "Long break", 15 * 60);
    
    private final String displayName;
    private final String settingsName;
    private final int defaultSeconds;
    
    TimerType(String displayName, String settingsName, int defaultSeconds) {
        this.displayName = displayName;
        this.settingsName = settingsName;
        this.defaultSeconds = defaultSeconds;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getSettingsName() {
        return settingsName;
    }
    
    public int getDefaultSeconds() {
        return defaultSeconds;
    }
}
