package com.example.catalog.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    public static final String CALENDAR_PICKER_PATTERN_DATE = "dd/MM/yyyy";
    public static final String CALENDAR_PICKER_PATTERN_TIMESTAMP = "dd/MM/yyyy H:mm";
    public static final String CALENDAR_PICKER_PATTERN_TIME = "H:mm";

    public static String getPickerFormat(final Calendar cal, final boolean includeTime) {
        if (cal == null) return "";
        final SimpleDateFormat df = new SimpleDateFormat(includeTime ? CALENDAR_PICKER_PATTERN_TIMESTAMP : CALENDAR_PICKER_PATTERN_DATE);
        return df.format(cal.getTime());
    }

    public static String getTimeFormatted(final Calendar cal) {
        if (cal == null) return "";
        final SimpleDateFormat df = new SimpleDateFormat(CALENDAR_PICKER_PATTERN_TIME);
        return df.format(cal.getTime());
    }

    public static String getLengthInMinutes(final Calendar start, final Calendar end) {
        if (start == null || end == null) return "0:00";

        long millis = end.getTimeInMillis() - start.getTimeInMillis();
        String minVal = (millis / 60000) + ":";
        String secVal = ((millis % 60000) / 1000) + "";

        return minVal + (secVal.length() < 2 ? ("0" + secVal) : secVal);
    }
}
