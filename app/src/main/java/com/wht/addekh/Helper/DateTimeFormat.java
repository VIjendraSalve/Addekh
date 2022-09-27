package com.wht.addekh.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YoYoNituSingh on 12/10/2016.
 */
public class DateTimeFormat
{
    //public static final SimpleDateFormat dateFormat_1 = new SimpleDateFormat("EEE, d-MMM-yy");
    public static final SimpleDateFormat dateFormat_2 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateFormat_3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateFormat_4 = new SimpleDateFormat("d-MMM-yy hh:mm a");
    public static final SimpleDateFormat dateFormat_1 = new SimpleDateFormat("d-MMM-yyyy");

    public static final SimpleDateFormat timeFormat_1 = new SimpleDateFormat("hh:mm aa");
    public static final SimpleDateFormat timeFormat_2 = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat dateFormat_5 = new SimpleDateFormat("dd MMM,yyyy hh:mm aa");

   /* DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    String date = df.format(Calendar.getInstance().getTime());
    Whereas you can have DateFormat patterns such as:

        "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
    "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
        "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
        "yyMMddHHmmssZ"-------------------- 010704120856-0700
        "K:mm a, z" ----------------------- 0:08 PM, PDT
    "h:mm a" -------------------------- 12:08 PM
    "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01*/

    public static String  getDate(String string)
    {
        Date date1 = null;
        String date="";
        try {
            date1 = DateTimeFormat.dateFormat_3.parse(""+string);
            date=DateTimeFormat.dateFormat_5.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
     public static String  getDateReverse(String string)
    {
        Date date1 = null;
        String date="";
        try {
            date1 = DateTimeFormat.dateFormat_5.parse(""+string);
            date=DateTimeFormat.dateFormat_2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



}
