package personal.project.springinfra.utility.date;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class DateUtility {

    private final ULocale US_LOCALE = new ULocale("en_US@calender=gregorian");
    private final ULocale JALALI_LOCALE = new ULocale("fa_IR@calendar=persian");
    private final SimpleDateFormat JALALI_DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss", JALALI_LOCALE);
    private final SimpleDateFormat JALALI_DATE_FORMATTER = new SimpleDateFormat("yyyy/MM/dd", JALALI_LOCALE);
    private final ZoneId TEHRAN_TIME_ZONE = ZoneId.of("Asia/Tehran");

    public String getCurrentJalaliDateTime() {
        return JALALI_DATE_TIME_FORMATTER.format(Calendar.getInstance(JALALI_LOCALE).getTime());
    }

    public String convertToJalaliDate(Date date) {
        return JALALI_DATE_FORMATTER.format(date);
    }

    public String convertToJalaliDateTime(Date date) {
        return JALALI_DATE_TIME_FORMATTER.format(date);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", US_LOCALE);
        String gregorianDate = dateFormat.format(calendar.getTime());
        return convertToLocalDate(gregorianDate, "yyyy/MM/dd");
    }

    public LocalDateTime convertToGregorian(LocalDateTime jalaliDateTime) {
        Calendar calendar = Calendar.getInstance(JALALI_LOCALE);
        calendar.set(jalaliDateTime.getYear(), jalaliDateTime.getMonthValue() - 1, jalaliDateTime.getDayOfMonth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", US_LOCALE);
        String gregorianDate = dateFormat.format(calendar.getTime());
        return convertToLocalDateTime(gregorianDate, "yyyy/MM/dd HH:mm:ss");
    }

    public LocalDate convertToLocalDate(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDate convertToLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), TEHRAN_TIME_ZONE).toLocalDate();
    }

    public LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), TEHRAN_TIME_ZONE);
    }

    public LocalDateTime convertToLocalDateTime(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
