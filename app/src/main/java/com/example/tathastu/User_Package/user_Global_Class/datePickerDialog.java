package com.example.tathastu.User_Package.user_Global_Class;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class datePickerDialog extends DatePickerDialog {
    private int minYear;
    private int maxYear;

    public datePickerDialog(@NonNull Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);

        // Set your desired year range
        minYear = 1990;
        maxYear = Calendar.getInstance().get(Calendar.YEAR)-21;


        DatePicker datePicker = getDatePicker();
        if (datePicker != null) {
            datePicker.setMinDate(getMinDate());
            datePicker.setMaxDate(getMaxDate());
        }
    }




    private long getMinDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(minYear, 0, 1);
        return calendar.getTimeInMillis();
    }

    private long getMaxDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(maxYear, 11, 31);
        return calendar.getTimeInMillis();
    }

}
