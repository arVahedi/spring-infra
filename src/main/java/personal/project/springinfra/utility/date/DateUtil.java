package personal.project.springinfra.utility.date;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {

    private static final ULocale JALALI_LOCALE = new ULocale("fa_IR@calendar=persian");
    private static final SimpleDateFormat JALALI_DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss", JALALI_LOCALE);

    public static String getCurrentJalaliDateTime() {
        return JALALI_DATE_TIME_FORMATTER.format(Calendar.getInstance(JALALI_LOCALE).getTime());
    }
}
