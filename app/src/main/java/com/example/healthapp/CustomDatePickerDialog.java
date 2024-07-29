package com.example.healthapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import java.util.Calendar;

public class CustomDatePickerDialog extends DatePickerDialog {

    private boolean isUpdating = false;

    public CustomDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        if (isUpdating) {
            return;
        }

        super.onDateChanged(view, year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        // Lấy ngày hiện tại
        Calendar today = Calendar.getInstance();

        // Kiểm tra nếu không phải là thứ 2 thì tự động lùi lại đến thứ 2 trước đó
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        // Nếu tuần chứa ngày hiện tại thì giới hạn ngày đến là ngày hiện tại
        if (calendar.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            calendar.setTimeInMillis(today.getTimeInMillis());
        }

        // Đặt cờ trước khi cập nhật ngày để tránh vòng lặp vô hạn
        isUpdating = true;
        view.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        isUpdating = false;
    }
}
