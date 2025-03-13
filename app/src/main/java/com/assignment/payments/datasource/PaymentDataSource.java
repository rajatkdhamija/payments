package com.assignment.payments.datasource;

import android.content.Context;
import android.util.Log;

import com.assignment.payments.model.Payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PaymentDataSource {
    private static final String TAG = PaymentDataSource.class.getSimpleName();
    private final String fileName;

    public PaymentDataSource(String fileName) {
        this.fileName = fileName;
    }

    public void savePayments(Context context, List<Payment> payments) {
        File file = new File(context.getFilesDir(), fileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(payments);
        } catch (IOException e) {
            Log.e(TAG, "Error saving payments: " + e.getMessage());
        }
    }

    public List<Payment> loadPayments(Context context) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<?> rawList = (List<?>) obj;
                List<Payment> payments = new ArrayList<>();
                for (Object item : rawList) {
                    if (item instanceof Payment) {
                        payments.add((Payment) item);
                    } else {
                        Log.e(TAG, "Invalid object type found in file.");
                    }
                }
                return payments;
            }
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Error loading payments: " + e.getMessage());
        }

        return new ArrayList<>();
    }

}
