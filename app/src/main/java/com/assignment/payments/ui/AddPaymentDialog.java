package com.assignment.payments.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.assignment.payments.R;
import com.assignment.payments.model.Payment;
import com.assignment.payments.model.PaymentType;
import com.assignment.payments.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AddPaymentDialog extends Dialog {
    private AppCompatEditText amountInput, providerInput, transactionRefInput;
    private Spinner paymentTypeSpinner;
    private final PaymentListener listener;

    private final Set<PaymentType> usedTypes;
    private List<PaymentType> availableTypes;

    public interface PaymentListener {
        void onPaymentAdded(Payment payment);
    }

    public AddPaymentDialog(
            @NonNull Context context,
            PaymentListener listener,
            Set<PaymentType> usedTypes
    ) {
        super(context);
        this.listener = listener;
        this.usedTypes = usedTypes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_payment);

        if (getWindow() != null) {
            getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        amountInput = findViewById(R.id.amountInput);
        paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        providerInput = findViewById(R.id.providerInput);
        transactionRefInput = findViewById(R.id.transactionRefInput);
        AppCompatButton savePaymentBtn = findViewById(R.id.okBtn);
        AppCompatButton cancelBtn = findViewById(R.id.cancelBtn);

        setupPaymentTypeSpinner();

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaymentType selected = availableTypes.get(position);
                if (selected == PaymentType.BANK_TRANSFER || selected == PaymentType.CREDIT_CARD) {
                    providerInput.setVisibility(View.VISIBLE);
                    transactionRefInput.setVisibility(View.VISIBLE);
                } else {
                    providerInput.setVisibility(View.GONE);
                    transactionRefInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                providerInput.setVisibility(View.GONE);
                transactionRefInput.setVisibility(View.GONE);
            }
        });

        savePaymentBtn.setOnClickListener(v -> savePayment());
        cancelBtn.setOnClickListener(v -> dismiss());
    }

    private void setupPaymentTypeSpinner() {
        availableTypes = new ArrayList<>();
        List<String> displayNames = new ArrayList<>();

        for (PaymentType type : PaymentType.values()) {
            if (!usedTypes.contains(type)) {
                availableTypes.add(type);
                displayNames.add(type.getDisplayName(getContext()));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                displayNames
        );
        paymentTypeSpinner.setAdapter(adapter);
    }

    private void savePayment() {
        int selectedPosition = paymentTypeSpinner.getSelectedItemPosition();
        PaymentType type = availableTypes.get(selectedPosition);

        String amountText = Objects.requireNonNull(amountInput.getText()).toString().trim();
        String provider = Objects.requireNonNull(providerInput.getText()).toString().trim();
        String transactionRef = Objects.requireNonNull(transactionRefInput.getText()).toString().trim();

        if (amountText.isEmpty() || amountText.equals(".")) {
            BaseUtils.showToast(getContext(), R.string.please_enter_an_amount);
            return;
        }
        try {
            if (Double.parseDouble(amountText) <= 0) {
                BaseUtils.showToast(getContext(), R.string.invalid_amount);
                return;
            }
        } catch (NumberFormatException e) {
            BaseUtils.showToast(getContext(), R.string.please_enter_an_amount);
            return;
        }

        if ((type == PaymentType.BANK_TRANSFER || type == PaymentType.CREDIT_CARD) && provider.isEmpty()) {
            BaseUtils.showToast(getContext(), R.string.please_enter_a_provider);
            return;
        }

        if ((type == PaymentType.BANK_TRANSFER || type == PaymentType.CREDIT_CARD) && transactionRef.isEmpty()) {
            BaseUtils.showToast(getContext(), R.string.please_enter_a_transaction_reference);
            return;
        }

        Payment payment = new Payment(type.name(), Double.parseDouble(amountText), provider, transactionRef);
        listener.onPaymentAdded(payment);
        dismiss();
    }
}