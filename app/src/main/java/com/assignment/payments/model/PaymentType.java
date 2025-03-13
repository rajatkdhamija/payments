package com.assignment.payments.model;

import android.content.Context;
import androidx.annotation.NonNull;
import com.assignment.payments.R;

public enum PaymentType {
    CASH(R.string.payment_cash),
    BANK_TRANSFER(R.string.payment_bank_transfer),
    CREDIT_CARD(R.string.payment_credit_card);

    private final int stringResId;

    PaymentType(int stringResId) {
        this.stringResId = stringResId;
    }

    public String getDisplayName(Context context) {
        return context.getString(stringResId);
    }

    @NonNull
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Use getDisplayName(Context) instead");
    }

    public static PaymentType fromString(String type, Context context) {
        for (PaymentType paymentType : values()) {
            if (paymentType.name().equalsIgnoreCase(type) ||
                    context.getString(paymentType.stringResId).equalsIgnoreCase(type)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentType: " + type);
    }
}
