package com.assignment.payments.repository;

import android.content.Context;
import com.assignment.payments.datasource.PaymentDataSource;
import com.assignment.payments.model.Payment;
import java.util.List;

public class PaymentRepository {
    private final PaymentDataSource paymentDataSource;

    public PaymentRepository(PaymentDataSource paymentDataSource) {
        this.paymentDataSource = paymentDataSource;
    }

    public void savePayments(Context context, List<Payment> payments) {
        paymentDataSource.savePayments(context, payments);
    }

    public List<Payment> loadPayments(Context context) {
        return paymentDataSource.loadPayments(context);
    }
}
