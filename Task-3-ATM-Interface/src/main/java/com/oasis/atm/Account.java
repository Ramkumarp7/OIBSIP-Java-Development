package com.oasis.atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

public class Account {
    private final String accountId;
    private final String userId;
    private final String holderName;
    private final String pin;
    private BigDecimal balance;
    private final List<Transaction> transactions = new ArrayList<>();
    private BigDecimal dailyWithdrawn = BigDecimal.ZERO;
    private LocalDate withdrawalDate = LocalDate.now();

    public Account(String accountId, String userId, String holderName, String pin, BigDecimal openingBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = openingBalance;
    }
    public String getAccountId() { return accountId; }
    public String getUserId() { return userId; }
    public String getHolderName() { return holderName; }
    public synchronized BigDecimal getBalance() { return balance; }
    public synchronized BigDecimal getDailyWithdrawn() {
        resetDailyIfNeeded();
        return dailyWithdrawn;
    }
    public synchronized boolean withdrawWithinDailyLimit(BigDecimal amount, BigDecimal dailyLimit) {
        resetDailyIfNeeded();
        if (amount == null || amount.signum() <= 0 || dailyWithdrawn.add(amount).compareTo(dailyLimit) > 0) return false;
        if (!withdraw(amount)) return false;
        dailyWithdrawn = dailyWithdrawn.add(amount);
        return true;
    }
    private void resetDailyIfNeeded() {
        if (!LocalDate.now().equals(withdrawalDate)) {
            withdrawalDate = LocalDate.now();
            dailyWithdrawn = BigDecimal.ZERO;
        }
    }
    public boolean verifyPin(String enteredPin) { return pin.equals(enteredPin); }
    public synchronized List<Transaction> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }
    public synchronized boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0 || balance.compareTo(amount) < 0) return false;
        balance = balance.subtract(amount);
        return true;
    }
    public synchronized boolean deposit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) return false;
        balance = balance.add(amount);
        return true;
    }
    public synchronized void record(Transaction transaction) { transactions.add(transaction); }
}
