package cloud.huazai.tool.java.date;

import lombok.Getter;
import lombok.Setter;

/**
 * TemporalPeriod
 *
 * @author devon
 * @since 2025/1/10
 */



@Setter
@Getter
public class DateTimePeriod {

    static final int HOURS_PER_DAY = 24;

    static final int MINUTES_PER_HOUR = 60;

    static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

    static final int SECONDS_PER_MINUTE = 60;

    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;

    static final long MILLIS_PER_SECOND = 1000L;

    private static final DateTimePeriod ZERO = new DateTimePeriod(0, 0, 0,0,0,0,0);

    private long year;

    private long month;

    private long day;

    private long hour;

    private long minute;

    private long second;

    private long millis;

    private DateTimePeriod(long year, long month, long day, long hour, long minute, long second, long millis) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millis = millis;
    }

    public static DateTimePeriod create(long year, long month, long day, long hour, long minute, long second, long millis) {
        return new DateTimePeriod(year, month, day, hour, minute, second, millis);
    }
}
