package com.assignment.payments.utils;

import android.content.Context;
import android.widget.Toast;

public class BaseUtils {
    public static void showToast(Context context, Integer id) {
        Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }
}
