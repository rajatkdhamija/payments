package com.assignment.payments.ui;

import static com.assignment.payments.utils.Constants.FILE_NAME;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;

import com.assignment.payments.R;
import com.assignment.payments.datasource.PaymentDataSource;
import com.assignment.payments.model.Payment;
import com.assignment.payments.model.PaymentType;
import com.assignment.payments.repository.PaymentRepository;
import com.assignment.payments.utils.BaseUtils;
import com.assignment.payments.viewmodel.PaymentViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private AppCompatTextView totalAmount;
    private ChipGroup chipGroup;
    private PaymentViewModel paymentViewModel;
    private AddPaymentDialog addPaymentDialog;

    private PaymentRepository paymentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PaymentDataSource paymentDataSource = new PaymentDataSource(FILE_NAME);
        paymentRepository = new PaymentRepository(paymentDataSource);
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        if (savedInstanceState == null) {
            loadPayments();
        }
        totalAmount = findViewById(R.id.totalAmount);
        AppCompatTextView addPaymentBtn = findViewById(R.id.addPaymentBtn);
        AppCompatButton saveBtn = findViewById(R.id.saveBtn);
        chipGroup = findViewById(R.id.chipGroup);

        paymentViewModel.getPayments().observe(this, this::updateUI);

        addPaymentBtn.setOnClickListener(v -> openAddPaymentDialog());
        saveBtn.setOnClickListener(v -> {
            paymentRepository.savePayments(
                    this,
                    Objects.requireNonNull(paymentViewModel.getPayments().getValue())
            );
            BaseUtils.showToast(this, R.string.payments_successfully_saved);
        });
    }

    private void loadPayments() {
        paymentRepository.loadPayments(this, payments -> runOnUiThread(() -> {
            paymentViewModel.setPayments(payments);
        }));
    }

    private void openAddPaymentDialog() {
        if (addPaymentDialog != null && addPaymentDialog.isShowing()) {
            addPaymentDialog.dismiss();
        }
        if (Objects.requireNonNull(paymentViewModel.getPayments().getValue()).size()
                >= PaymentType.values().length) {
            BaseUtils.showToast(this, R.string.all_payment_types_have_already_been_added);
            return;
        }

        Set<PaymentType> usedTypes = new HashSet<>();
        for (Payment payment : Objects.requireNonNull(paymentViewModel.getPayments().getValue())) {
            usedTypes.add(PaymentType.fromString(payment.getType(), this));
        }

        addPaymentDialog = new AddPaymentDialog(
                this,
                paymentViewModel::addPayment,
                usedTypes
        );
        addPaymentDialog.show();
    }

    void updateUI(List<Payment> payments) {
        chipGroup.removeAllViews();
        double total = 0;
        for (Payment payment : payments) {
            total += payment.getAmount();
            Chip chip = new Chip(this);
            chip.setText(
                    getString(R.string.payment_details,
                            PaymentType.fromString(payment.getType(), this).getDisplayName(this),
                            payment.getAmount()
                    )
            );
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> paymentViewModel.removePayment(payment));
            chipGroup.addView(chip);
        }
        totalAmount.setText(getString(R.string.total_amount, total));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addPaymentDialog != null && addPaymentDialog.isShowing()) {
            addPaymentDialog.dismiss();
            addPaymentDialog = null;
        }
    }
}