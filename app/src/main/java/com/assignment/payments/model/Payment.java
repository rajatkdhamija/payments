package com.assignment.payments.model;

import java.io.Serializable;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type;
    private final double amount;
    private final String provider;
    private final String transactionReference;

    public Payment(String type, double amount, String provider, String transactionReference) {
        this.type = type;
        this.amount = amount;
        this.provider = provider;
        this.transactionReference = transactionReference;
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getProvider() { return provider; }
    public String getTransactionReference() { return transactionReference; }
}
