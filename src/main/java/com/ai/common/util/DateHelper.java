package com.ai.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pc on 2017-05-10.
 */
public class DateHelper {

    /**
     *
     * @param mills 毫秒
     * @return
     */
    public static String getMonth(long mills) {

        DateFormat formatter = new SimpleDateFormat("MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        String month = formatter.format(calendar.getTime());

        return month;
    }

    /**
     *
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getMonth(String date) {

        String month = date.split("-")[1];
        return month;
    }

    /**
     *
     * @param mills 毫秒
     * @return
     */
    public static String getDate(long mills) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        String month = formatter.format(calendar.getTime());

        return month;
    }

    public static void main(String[] args) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1482984000000L);
        String time = formatter.format(calendar.getTime());
        System.out.println(time);


        //1494492988860
    }
}
