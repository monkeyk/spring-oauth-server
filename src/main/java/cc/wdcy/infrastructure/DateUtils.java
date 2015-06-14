package cc.wdcy.infrastructure;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Shengzhao Li
 */
public abstract class DateUtils {

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * Private constructor
     */
    private DateUtils() {
    }

    public static Date now() {
        return new Date();
    }


    //Create new  SimpleDateFormat
    private static SimpleDateFormat newDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
    }

    public static String toDateTime(Date date) {
        return toDateText(date, DEFAULT_DATE_TIME_FORMAT);
    }


    public static String toDateText(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        SimpleDateFormat dateFormat = newDateFormat(pattern);
        return dateFormat.format(date);
    }


}