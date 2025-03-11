package com.assignment.payments.model;

public class Payment {
    private final String type;
    private final double amount;
    private String provider;
    private String transactionReference;

    public Payment(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public Payment(String type, double amount, String provider, String transactionReference) {
        this.type = type;
        this.amount = amount;
        this.provider = provider;
        this.transactionReference = transactionReference;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getProvider() {
        return provider;
    }

    public String getTransactionReference() {
        return transactionReference;
    }
}