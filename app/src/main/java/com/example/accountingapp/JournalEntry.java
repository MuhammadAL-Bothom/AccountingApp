package com.example.accountingapp;

public class JournalEntry {
    public String date;
    public String account;
    public double debit;
    public double credit;
    public String description;
    public String currency;

    // ✅ المُنشئ الكامل
    public JournalEntry(String date, String account, double debit, double credit, String description, String currency) {
        this.date = date;
        this.account = account;
        this.debit = debit;
        this.credit = credit;
        this.description = description != null ? description : "";
        this.currency = currency != null ? currency : "";
    }

    // ✅ طريقة الطباعة (لأغراض العرض في ListView مثلاً)
    @Override
    public String toString() {
        String line = date + " | " + account;
        line += " | مدين: " + debit;
        line += " | دائن: " + credit;

        if (!currency.isEmpty()) {
            line += " | " + currency;
        }

        if (!description.isEmpty()) {
            line += " | " + description;
        }

        return line;
    }
}
