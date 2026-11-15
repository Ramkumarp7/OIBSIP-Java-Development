package com.oasis.guessing;

public enum Difficulty {
    EASY("Easy", 50, 10),
    MEDIUM("Medium", 100, 7),
    HARD("Hard", 200, 5);

    private final String label;
    private final int maximum;
    private final int attempts;

    Difficulty(String label, int maximum, int attempts) {
        this.label = label;
        this.maximum = maximum;
        this.attempts = attempts;
    }

    public int getMaximum() { return maximum; }
    public int getAttempts() { return attempts; }

    @Override
    public String toString() { return label + "  (1-" + maximum + ", " + attempts + " tries)"; }
}
