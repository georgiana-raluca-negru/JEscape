package com.pao.escaperoom.model;

public enum Difficulty {
    EASY("easy", 60, 1.0),
    MEDIUM("medium", 45, 1.5),
    HARD("hard", 30, 2.5);

    private final String displayName;
    private final int timeLimitMinutes;
    private final double multiplier;

    Difficulty(String displayName, int timeLimitMinutes, double multiplier) {
        this.displayName = displayName;
        this.timeLimitMinutes = timeLimitMinutes;
        this.multiplier = multiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
