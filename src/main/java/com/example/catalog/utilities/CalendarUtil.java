package com.example.catalog.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    public static String getPickerFormat(final Calendar cal) {
        if (cal == null) return "";

        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(cal.getTime());
    }
}
