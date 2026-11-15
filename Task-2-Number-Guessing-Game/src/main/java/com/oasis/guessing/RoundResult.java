package com.oasis.guessing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RoundResult(int round, Difficulty difficulty, int target, int attemptsUsed,
                          boolean won, LocalDateTime completedAt) {
    public String summary() {
        String outcome = won ? "Won" : "Lost";
        return String.format("Round %d  •  %s  •  %s in %d/%d attempts  •  Number: %d  •  %s",
                round, difficulty.name(), outcome, attemptsUsed, difficulty.getAttempts(),
                target, completedAt.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a")));
    }
}
