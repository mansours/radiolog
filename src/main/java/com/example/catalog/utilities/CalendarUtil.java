package com.example.catalog.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    public static String getPickerFormat(final Calendar cal, final boolean includeTime) {
        if (cal == null) return "";

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd" + (includeTime ? "'T'HH:mm" : ""));
        return df.format(cal.getTime());
    }
}
