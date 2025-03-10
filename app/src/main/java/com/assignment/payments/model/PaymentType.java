package com.assignment.payments.model;

import androidx.annotation.NonNull;

public enum PaymentType {
    CASH("Cash"),
    BANK_TRANSFER("Bank Transfer"),
    CREDIT_CARD("Credit Card");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }

    public static PaymentType fromString(String type) {
        for (PaymentType paymentType : values()) {
            if (paymentType.name().equalsIgnoreCase(type) || paymentType.displayName.equalsIgnoreCase(type)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentType: " + type);
    }
}
