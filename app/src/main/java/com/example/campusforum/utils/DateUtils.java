package com.example.campusforum.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    private static final String[] INPUT_PATTERNS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd"
    };
    private static final String OUTPUT_PATTERN = "dd/MM/yyyy";

    private DateUtils() {
    }

    public static String formatRelativeDate(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            return "";
        }

        Date date = parseDate(rawDate.trim());
        if (date == null) {
            return rawDate;
        }

        long dayDiff = getDayDiff(date);
        if (dayDiff == 0) {
            return "Aujourd'hui";
        }
        if (dayDiff == 1) {
            return "Hier";
        }
        if (dayDiff > 1 && dayDiff < 7) {
            return String.format(Locale.getDefault(), "Il y a %d jours", dayDiff);
        }

        return new SimpleDateFormat(OUTPUT_PATTERN, Locale.getDefault()).format(date);
    }

    private static Date parseDate(String rawDate) {
        for (String pattern : INPUT_PATTERNS) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
                formatter.setLenient(false);
                return formatter.parse(rawDate);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    private static long getDayDiff(Date date) {
        Calendar today = Calendar.getInstance();
        resetTime(today);

        Calendar target = Calendar.getInstance();
        target.setTime(date);
        resetTime(target);

        long diffMillis = today.getTimeInMillis() - target.getTimeInMillis();
        return diffMillis / (24L * 60L * 60L * 1000L);
    }

    private static void resetTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
