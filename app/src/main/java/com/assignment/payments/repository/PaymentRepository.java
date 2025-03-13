package com.assignment.payments.repository;

import android.content.Context;

import com.assignment.payments.datasource.PaymentDataSource;
import com.assignment.payments.model.Payment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentRepository {
    private final PaymentDataSource paymentDataSource;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PaymentRepository(PaymentDataSource paymentDataSource) {
        this.paymentDataSource = paymentDataSource;
    }

    public void savePayments(Context context, List<Payment> payments) {
        executorService.execute(() -> paymentDataSource.savePayments(context, payments));
    }

    public void loadPayments(Context context, PaymentLoadCallback callback) {
        executorService.execute(() -> {
            List<Payment> payments = paymentDataSource.loadPayments(context);
            callback.onPaymentsLoaded(payments);
        });
    }

    public interface PaymentLoadCallback {
        void onPaymentsLoaded(List<Payment> payments);
    }
}
