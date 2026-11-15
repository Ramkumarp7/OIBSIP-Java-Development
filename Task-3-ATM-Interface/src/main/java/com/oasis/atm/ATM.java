package com.oasis.atm;

import java.math.BigDecimal;

/**
 * Application service for ATM operations. The graphical screens call this
 * class so business rules remain separate from presentation code.
 */
public class ATM {
    private static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("20000.00");
    private final Bank bank;
    public ATM(Bank bank) { this.bank = bank; }

    public boolean withdraw(Account account, BigDecimal amount) {
        if (account == null || amount == null || amount.signum() <= 0) return false;
        if (!account.withdrawWithinDailyLimit(amount, DAILY_WITHDRAWAL_LIMIT)) return false;
        account.record(new Transaction("WITHDRAW", amount, "Cash withdrawal"));
        return true;
    }
    public boolean deposit(Account account, BigDecimal amount) {
        if (account == null || amount == null || amount.signum() <= 0) return false;
        if (!account.deposit(amount)) return false;
        account.record(new Transaction("DEPOSIT", amount, "Cash deposit"));
        return true;
    }
    public boolean transfer(Account sender, String recipientId, BigDecimal amount) {
        Account recipient = bank.findByAccountId(recipientId);
        return bank.transfer(sender, recipient, amount);
    }
}
