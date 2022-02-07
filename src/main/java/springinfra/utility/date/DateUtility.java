package springinfra.utility.date;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@UtilityClass
public class DateUtility {

    private final String JALALI_DATE_FORMAT_STRING = "yyyy/MM/dd";
    private final String JALALI_DATE_TIME_FORMAT_STRING = "yyyy/MM/dd - HH:mm:ss";
    private final String GREGORIAN_DATE_FORMAT_STRING = "yyyy-MM-dd";
    private final String GREGORIAN_DATE_TIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private final ULocale US_LOCALE = new ULocale("en_US@calender=gregorian");
    private final ULocale JALALI_LOCALE = new ULocale("fa_IR@calendar=persian");
    private final SimpleDateFormat JALALI_DATE_TIME_FORMATTER = new SimpleDateFormat(JALALI_DATE_TIME_FORMAT_STRING, JALALI_LOCALE);
    private final SimpleDateFormat JALALI_DATE_FORMATTER = new SimpleDateFormat(JALALI_DATE_FORMAT_STRING, JALALI_LOCALE);
    private final SimpleDateFormat GREGORIAN_DATE_TIME_FORMATTER = new SimpleDateFormat(GREGORIAN_DATE_TIME_FORMAT_STRING, US_LOCALE);
    private final SimpleDateFormat GREGORIAN_DATE_FORMATTER = new SimpleDateFormat(GREGORIAN_DATE_FORMAT_STRING, US_LOCALE);
    private final ZoneId TEHRAN_TIME_ZONE = ZoneId.of("Asia/Tehran");

    //region Jalali Date and Time
    public String getCurrentJalaliDateTimeString() {
        return JALALI_DATE_TIME_FORMATTER.format(Calendar.getInstance(JALALI_LOCALE).getTime());
    }

    public String convertToJalaliDateString(Date date) {
        return JALALI_DATE_FORMATTER.format(date);
    }

    public String convertToJalaliDateString(LocalDate date) {
        return convertToJalaliDateString(convertToJalaliDate(date));
    }

    public String convertToJalaliDateTimeString(Date date) {
        return JALALI_DATE_TIME_FORMATTER.format(date);
    }

    public String convertToJalaliDateTimeString(LocalDateTime date) {
        return JALALI_DATE_TIME_FORMATTER.format(date);
    }

    public Date convertToJalaliDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(TEHRAN_TIME_ZONE).toInstant());
    }

    public Date convertToJalaliDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(TEHRAN_TIME_ZONE).toInstant());
    }

    public Date convertToJalaliDate(LocalTime time) {
        return Date.from(time.atDate(LocalDate.now()).atZone(TEHRAN_TIME_ZONE).toInstant());
    }

    public LocalDate convertToJalaliLocalDate(LocalDate date) {
        JALALI_DATE_FORMATTER.setNumberFormat(NumberFormat.getNumberInstance());
        String format = JALALI_DATE_FORMATTER.format(convertToJalaliDate(date));
        return convertToLocalDate(format, JALALI_DATE_FORMAT_STRING);
    }

    public LocalDateTime convertToJalaliLocalDateTime(LocalDateTime date) {
        JALALI_DATE_TIME_FORMATTER.setNumberFormat(NumberFormat.getNumberInstance());
        String format = JALALI_DATE_TIME_FORMATTER.format(convertToJalaliDate(date));
        return convertToLocalDateTime(format, JALALI_DATE_TIME_FORMAT_STRING);
    }
    //endregion

    //region Gregorian Date and Time
    public String convertToGregorianDateString(Date date) {
        return GREGORIAN_DATE_FORMATTER.format(date);
    }

    public String convertToGregorianDateString(LocalDate date) {
        return convertToGregorianDateString(convertToGregorian(date));
    }

    public String convertToGregorianDateTimeString(Date date) {
        return GREGORIAN_DATE_TIME_FORMATTER.format(date);
    }

    public String convertToGregorianDateTimeString(LocalDateTime date) {
        return GREGORIAN_DATE_TIME_FORMATTER.format(date);
    }

    public Date convertToGregorian(String date, String format) {
        LocalDate jalaliDate = convertToLocalDate(date, format);
        Calendar calendar = Calendar.getInstance(JALALI_LOCALE);
        calendar.set(jalaliDate.getYear(), jalaliDate.getMonthValue() - 1, jalaliDate.getDayOfMonth());
        return calendar.getTime();
    }

    public LocalDate convertToGregorian(LocalDate jalaliDate) {
        Calendar calendar = Calendar.getInstance(JALALI_LOCALE);
        calendar.set(jalaliDate.getYear(), jalaliDate.getMonthValue() - 1, jalaliDate.getDayOfMonth());
        String gregorianDate = GREGORIAN_DATE_FORMATTER.format(calendar.getTime());
        return convertToLocalDate(gregorianDate, GREGORIAN_DATE_FORMAT_STRING);
    }

    public LocalDateTime convertToGregorian(LocalDateTime jalaliDateTime) {
        Calendar calendar = Calendar.getInstance(JALALI_LOCALE);
        calendar.set(jalaliDateTime.getYear(), jalaliDateTime.getMonthValue() - 1, jalaliDateTime.getDayOfMonth());
        String gregorianDate = GREGORIAN_DATE_TIME_FORMATTER.format(calendar.getTime());
        return convertToLocalDateTime(gregorianDate, GREGORIAN_DATE_TIME_FORMAT_STRING);
    }
    //endregion

    public LocalDate convertToLocalDate(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDate convertToLocalDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), TEHRAN_TIME_ZONE).toLocalDate();
    }

    public LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), TEHRAN_TIME_ZONE);
    }

    public LocalDateTime convertToLocalDateTime(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public Date mines(Date date, int value, TemporalUnit unit) {
        return Date.from(((LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault())).minus(value, unit)).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date plus(Date date, int value, TemporalUnit unit) {
        return Date.from(((LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault())).plus(value, unit)).atZone(ZoneId.systemDefault()).toInstant());
    }
}
