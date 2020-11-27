package com.example.catalog.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    public static final String CALENDAR_PICKER_PATTERN_DATE = "dd/MM/yyyy";
    public static final String CALENDAR_PICKER_PATTERN_TIMESTAMP = "dd/MM/yyyy H:mm";

    public static String getPickerFormat(final Calendar cal, final boolean includeTime) {
        if (cal == null) return "";
        final SimpleDateFormat df = new SimpleDateFormat(includeTime ? CALENDAR_PICKER_PATTERN_TIMESTAMP : CALENDAR_PICKER_PATTERN_DATE);
        return df.format(cal.getTime());
    }
}
