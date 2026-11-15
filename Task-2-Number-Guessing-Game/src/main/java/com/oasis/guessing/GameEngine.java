package com.oasis.guessing;

import java.security.SecureRandom;

public class GameEngine {
    public enum GuessStatus { TOO_LOW, TOO_HIGH, CORRECT, OUT_OF_RANGE, NO_ATTEMPTS }

    private final SecureRandom random = new SecureRandom();
    private Difficulty difficulty;
    private int target;
    private int attemptsUsed;
    private boolean won;

    public void startRound(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.target = random.nextInt(difficulty.getMaximum()) + 1;
        this.attemptsUsed = 0;
        this.won = false;
    }

    public GuessStatus guess(int value) {
        if (isOver()) return GuessStatus.NO_ATTEMPTS;
        if (value < 1 || value > difficulty.getMaximum()) return GuessStatus.OUT_OF_RANGE;
        attemptsUsed++;
        if (value == target) {
            won = true;
            return GuessStatus.CORRECT;
        }
        return value < target ? GuessStatus.TOO_LOW : GuessStatus.TOO_HIGH;
    }

    public boolean isOver() { return won || attemptsUsed >= difficulty.getAttempts(); }
    public boolean isWon() { return won; }
    public int getTarget() { return target; }
    public int getAttemptsUsed() { return attemptsUsed; }
    public int getAttemptsRemaining() { return difficulty.getAttempts() - attemptsUsed; }
    public Difficulty getDifficulty() { return difficulty; }
}
