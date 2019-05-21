package com.joyen.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    public static final SimpleDateFormat[] FORMAT;
    public static final SimpleDateFormat DEFAULT_FORMART = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    static {
        FORMAT = new SimpleDateFormat[] {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm"),
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy-MM"),
                new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"),
                new SimpleDateFormat("yyyy年MM月dd日 HH:mm"),
                new SimpleDateFormat("yyyy年MM月dd日"),
                new SimpleDateFormat("yyyy年MM月"),
                new SimpleDateFormat("yyyy年"),
                new SimpleDateFormat("yy-MM-dd HH:mm:ss"),
                new SimpleDateFormat("yy-MM-dd HH:mm"),
                new SimpleDateFormat("yy-MM-dd HH"),
                new SimpleDateFormat("yy-MM-dd"),
                new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"),
                new SimpleDateFormat("yyyy年MM月dd日 HH时mm分"),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
                new SimpleDateFormat("yyyy/MM/dd HH:mm"),
                new SimpleDateFormat("yyyy/MM/dd"),
                new SimpleDateFormat("yyyy/MM"),
                new SimpleDateFormat("HH:mm:ss"),
                new SimpleDateFormat("HH:mm"), new SimpleDateFormat("yyyy") };
    }

    public static Date toDate(String dateformat) {
        Date date = null;
        for (int i = 0; i < FORMAT.length; i++) {
            SimpleDateFormat df = FORMAT[i];
            try {
                date = df.parse(dateformat);
                if (date.getYear() == 70 && dateformat.indexOf("yy") < 0) {
                    Date now = new Date();
                    date.setYear(now.getYear());
                    if (0 > dateformat.indexOf("M"))
                        date.setMonth(now.getMonth());
                    if (0 > dateformat.indexOf("d"))
                        date.setDate(now.getDate());
                    if (0 > dateformat.indexOf("H"))
                        date.setHours(now.getHours());
                }
                return date;
            } catch (ParseException e) {
            }
        }
        return date;
    }

    public static String format(String date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(toDate(date));
    }

    public static String format(String date) {
        Date d = toDate(date);
        if (d == null)
            return date;
        else {
            return DEFAULT_FORMART.format(d);
        }
    }

    public static void main(String[] args) {
        System.out.println(format("2015-02-03 12:22:22"));
        System.out.println(format("2015-02-03 12:22"));
    }
}
