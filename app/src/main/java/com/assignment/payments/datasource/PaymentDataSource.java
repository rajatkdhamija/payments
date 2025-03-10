package com.assignment.payments.datasource;

import static com.assignment.payments.utils.Constants.AMOUNT;
import static com.assignment.payments.utils.Constants.FILE_NAME;
import static com.assignment.payments.utils.Constants.PROVIDER;
import static com.assignment.payments.utils.Constants.TRANSACTION_REFERENCE;
import static com.assignment.payments.utils.Constants.TYPE;

import android.content.Context;
import android.util.Log;

import com.assignment.payments.model.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

        JSONArray jsonArray = new JSONArray();
        for (Payment payment : payments) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(TYPE, payment.getType());
                jsonObject.put(AMOUNT, payment.getAmount());
                jsonObject.put(PROVIDER, payment.getProvider());
                jsonObject.put(TRANSACTION_REFERENCE, payment.getTransactionReference());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON object: " + e.getMessage());
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonArray.toString(4));
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error saving payments: " + e.getMessage());
        }
    }

    public List<Payment> loadPayments(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        List<Payment> payments = new ArrayList<>();

        if (!file.exists()) return payments;

        try (FileReader reader = new FileReader(file)) {
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                payments.add(new Payment(
                        jsonObject.getString(TYPE),
                        jsonObject.getInt(AMOUNT),
                        jsonObject.optString(PROVIDER, ""),
                        jsonObject.optString(TRANSACTION_REFERENCE, "")
                ));
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error loading payments: " + e.getMessage());
        }

        return payments;
    }
}
