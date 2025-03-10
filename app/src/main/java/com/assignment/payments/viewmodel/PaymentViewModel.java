package com.assignment.payments.viewmodel;

import static com.assignment.payments.utils.Constants.PAYMENTS_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.assignment.payments.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<List<Payment>> _payments;

    public PaymentViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        _payments = savedStateHandle.getLiveData(PAYMENTS_KEY, new ArrayList<>());
    }

    public LiveData<List<Payment>> getPayments() {
        return _payments;
    }

    public void addPayment(Payment payment) {
        List<Payment> currentPayments = new ArrayList<>(Objects.requireNonNull(_payments.getValue()));
        currentPayments.add(payment);
        savedStateHandle.set(PAYMENTS_KEY, currentPayments);
        _payments.setValue(currentPayments);
    }

    public void removePayment(Payment payment) {
        List<Payment> currentPayments = new ArrayList<>(Objects.requireNonNull(_payments.getValue()));
        currentPayments.remove(payment);
        savedStateHandle.set(PAYMENTS_KEY, currentPayments);
        _payments.setValue(currentPayments);
    }

    public void setPayments(List<Payment> loadedPayments) {
        savedStateHandle.set(PAYMENTS_KEY, loadedPayments);
        _payments.setValue(loadedPayments);
    }
}
