package com.oasis.atm;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bank {
    private final Map<String, Account> accountsByUserId = new LinkedHashMap<>();
    private final Map<String, Account> accountsByAccountId = new LinkedHashMap<>();

    public void addAccount(Account account) {
        accountsByUserId.put(account.getUserId(), account);
        accountsByAccountId.put(account.getAccountId(), account);
    }
    public Account authenticate(String userId, String pin) {
        Account account = accountsByUserId.get(userId);
        return account != null && account.verifyPin(pin) ? account : null;
    }
    public Account findByAccountId(String accountId) { return accountsByAccountId.get(accountId); }

    public synchronized boolean transfer(Account sender, Account recipient, BigDecimal amount) {
        if (sender == null || recipient == null || sender == recipient || amount == null || amount.signum() <= 0)
            return false;
        // Lock accounts in a stable order to avoid inconsistent concurrent transfers.
        Account first = sender.getAccountId().compareTo(recipient.getAccountId()) < 0 ? sender : recipient;
        Account second = first == sender ? recipient : sender;
        synchronized (first) {
            synchronized (second) {
                if (!sender.withdraw(amount)) return false;
                recipient.deposit(amount);
                sender.record(new Transaction("TRANSFER", amount, "Sent to " + recipient.getAccountId()));
                recipient.record(new Transaction("RECEIVED", amount, "Received from " + sender.getAccountId()));
                return true;
            }
        }
    }

    public static Bank createDemoBank() {
        Bank bank = new Bank();
        bank.addAccount(new Account("10010001", "ram", "Ram Kumar", "1234", new BigDecimal("25000.00")));
        bank.addAccount(new Account("10010002", "sita", "Sita Devi", "4321", new BigDecimal("18000.00")));
        bank.addAccount(new Account("10010003", "arjun", "Arjun Rao", "2468", new BigDecimal("12000.00")));
        return bank;
    }
}
