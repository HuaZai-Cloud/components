package cloud.huazai.tool.java.date;

import lombok.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DateTimeUtils
 *
 * @author devon
 * @since 2024/12/26
 */

public class DateUtils {

    // ------------------------------------------------- Date ----------------------------------------------------------

    @Deprecated
    public static Date now() {
        return new Date();
    }


    @Deprecated
    public static <T extends Temporal> Date toDate(@NonNull T dateTime) {
        if (dateTime instanceof LocalDate) {
            LocalDate localDate = (LocalDate) dateTime;
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }
        if (dateTime instanceof LocalTime) {
            LocalTime localTime = (LocalTime) dateTime;
            return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        }
        if (dateTime instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) dateTime;
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        if (dateTime instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) dateTime;
            return Date.from(zonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
        }

        throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName()
                + ". Supported types are LocalDate, LocalTime, LocalDateTime, and ZonedDateTime.");

    }

    @Deprecated
    public static Date toDate(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return toDate(toLocalDateTime(year, month, dayOfMonth, hour, minute, second));
    }

    @Deprecated
    public static Date toDate(int year, int month, int dayOfMonth) {
        return toDate(year, month, dayOfMonth, 0, 0, 0);
    }

    // ---------------------------------------------- LocalDate -------------------------------------------------------

    public static <T extends Temporal> LocalDate toLocalDate(@NonNull T dateTime) {
        if (dateTime instanceof LocalDate) {
            return (LocalDate) dateTime;
        }

        if (dateTime instanceof LocalDateTime || dateTime instanceof ZonedDateTime) {
            return LocalDate.from(dateTime);
        }

        throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName()
                + ". Supported types are LocalDate, LocalDateTime, and ZonedDateTime.");

    }

    public static LocalDate toLocalDate(@NonNull Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static LocalDate toLocalDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }


    // ---------------------------------------------- LocalTime -------------------------------------------------------


    public static LocalTime toLocalTime(@NonNull Date date) {
        return toLocalDateTime(date).toLocalTime();
    }


    public static LocalTime toLocalTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);
    }

    public static LocalTime toLocalTime(int hour, int minute, int second, int millis) {
        return LocalTime.of(hour, minute, second, millis);
    }


    // -------------------------------------------- LocalDateTime -----------------------------------------------------


    public static LocalDateTime toLocalDateTime(@NonNull Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static <T extends Temporal> LocalDateTime toLocalDateTime(@NonNull T dateTime) {
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atStartOfDay();
        }

        if (dateTime instanceof LocalTime) {
            return ((LocalTime) dateTime).atDate(LocalDate.now());
        }

        if (dateTime instanceof LocalDateTime) {
            return (LocalDateTime) dateTime;
        }

        if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).toLocalDateTime();
        }

        throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName()
                + ". Supported types are LocalDate, LocalTime, LocalDateTime, and ZonedDateTime.");
    }

    public static LocalDateTime toLocalDateTime(@NonNull LocalDate date, @NonNull LocalTime time) {
        return date.atTime(time);
    }


    public static LocalDateTime toLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return toLocalDate(year, month, dayOfMonth).atTime(toLocalTime(hour, minute, second));
    }

    public static LocalDateTime toLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second, int millis) {
        return toLocalDate(year, month, dayOfMonth).atTime(toLocalTime(hour, minute, second, millis));
    }


    public static <T extends Temporal> LocalDateTime atStartOfDay(@NonNull T dateTime) {
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atStartOfDay();
        }

        if (dateTime instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) dateTime;
            return localDateTime.toLocalDate().atStartOfDay();
        }

        if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).toLocalDate().atStartOfDay();
        }

        throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName()
                + ". Supported types are LocalDate, LocalDateTime, and ZonedDateTime.");
    }


    public static LocalDateTime atStartOfDay(@NonNull Date date) {
        return atStartOfDay(toLocalDateTime(date));
    }


    public static <T extends Temporal> LocalDateTime atEndOfDay(@NonNull T dateTime) {
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.MAX);
        }

        if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).with(LocalTime.MAX);
        }

        if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).with(LocalTime.MAX).toLocalDateTime();
        }

        throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName()
                + ". Supported types are LocalDate, LocalDateTime, and ZonedDateTime.");
    }

    public static LocalDateTime atEndOfDay(Date date) {
        return atEndOfDay(toLocalDateTime(date));
    }


    public static <T extends Temporal> LocalDateTime atEndOfDayToSecond(@NonNull T dateTime) {
        LocalTime endOfDay = toLocalTime(23, 59, 59);

        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(endOfDay);
        }

        if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).with(endOfDay);
        }

        if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).with(endOfDay).toLocalDateTime();
        }

        throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName()
                + ". Supported types are LocalDate, LocalDateTime, and ZonedDateTime.");
    }

    public static LocalDateTime atEndOfDayToSecond(Date date) {
        return atEndOfDayToSecond(toLocalDateTime(date));
    }

    public static <T extends Temporal> LocalDateTime atStartOfWeek(@NonNull T dateTime, @NonNull DayOfWeek dayOfWeekStart) {
        return atStartOfDay(toLocalDate(dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
    }


    public static <T extends Temporal> LocalDateTime atEndOfWeek(@NonNull T dateTime,@NonNull DayOfWeek dayOfWeekEnd) {
        return atEndOfDay(toLocalDate(dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
    }

    public static <T extends Temporal> LocalDateTime atEndOfWeekToSecond(@NonNull T dateTime,@NonNull DayOfWeek dayOfWeekEnd) {
        return atEndOfDayToSecond(toLocalDate(dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
    }

    public static <T extends Temporal> LocalDateTime atStartOfMonth(@NonNull T dateTime) {
        return atStartOfDay(toLocalDate(dateTime).with(TemporalAdjusters.firstDayOfMonth()));
    }


    public static <T extends Temporal> LocalDateTime atEndOfMonth(@NonNull T dateTime) {
        return atEndOfDay(toLocalDate(dateTime).with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static <T extends Temporal> LocalDateTime atEndOfMonthToSecond(@NonNull T dateTime) {
        return atEndOfDayToSecond(toLocalDate(dateTime).with(TemporalAdjusters.lastDayOfMonth()));
    }


    public static <T extends Temporal> LocalDateTime atStartOfYear(@NonNull T dateTime) {
        return atStartOfDay(toLocalDate(dateTime).with(TemporalAdjusters.firstDayOfYear()));
    }


    public static <T extends Temporal> LocalDateTime atEndOfYear(@NonNull T dateTime) {
        return atEndOfDay(toLocalDate(dateTime).with(TemporalAdjusters.lastDayOfYear()));
    }

    public static <T extends Temporal> LocalDateTime atEndOfYearToSecond(@NonNull T dateTime) {
        return atEndOfDayToSecond(toLocalDate(dateTime).with(TemporalAdjusters.lastDayOfYear()));
    }

    public static <T extends Temporal> LocalDateTime offsetSecond(@NonNull T dateTime, long secondsToOffset) {
        return toLocalDateTime(dateTime).plusSeconds(secondsToOffset);
    }


    public static <T extends Temporal> LocalDateTime offsetMinute(@NonNull T dateTime, long minutesToOffset) {
        return toLocalDateTime(dateTime).plusMinutes(minutesToOffset);
    }

    public static <T extends Temporal> LocalDateTime offsetHour(@NonNull T dateTime, long hoursToOffset) {
        return toLocalDateTime(dateTime).plusHours(hoursToOffset);
    }

    public static <T extends Temporal> LocalDateTime offsetDay(@NonNull T dateTime, long daysToOffset) {
        return toLocalDateTime(dateTime).plusDays(daysToOffset);
    }


    public static <T extends Temporal> LocalDateTime offsetWeek(@NonNull T dateTime, long weeksToOffset) {
        return toLocalDateTime(dateTime).plusWeeks(weeksToOffset);
    }


    public static <T extends Temporal> LocalDateTime offsetMonth(@NonNull T dateTime, long monthsToOffset) {
        return toLocalDateTime(dateTime).plusMonths(monthsToOffset);
    }


    public static <T extends Temporal> LocalDateTime offsetYear(@NonNull T dateTime, long yearsToOffset) {
        return toLocalDateTime(dateTime).plusYears(yearsToOffset);
    }


    // -------------------------------------------- ZonedDateTime -----------------------------------------------------


    public static ZonedDateTime now(@NonNull String timeZone) {
        return now(TimeZone.getTimeZone(timeZone));
    }

    public static ZonedDateTime now(@NonNull TimeZone timeZone) {
        ZoneId zoneId = timeZone.toZoneId();
        return ZonedDateTime.now(zoneId);
    }


    public static ZonedDateTime now(@NonNull TimeZoneID timeZoneID, int offsetHour) {
        return ZonedDateTime.now(ZoneId.ofOffset(timeZoneID.name(), ZoneOffset.ofHours(offsetHour)));
    }


    public static ZonedDateTime toZonedDateTime(@NonNull LocalDateTime localDateTime, @NonNull ZoneId zone) {
        return ZonedDateTime.of(localDateTime, zone);
    }

    public static ZonedDateTime toZonedDateTime(@NonNull LocalDate localDate, @NonNull LocalTime localTime, @NonNull ZoneId zone) {
        return ZonedDateTime.of(localDate, localTime, zone);
    }

    public static ZonedDateTime toZonedDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second, ZoneId zone) {
        return ZonedDateTime.of(toLocalDateTime(year, month, dayOfMonth, hour, minute, second), zone);
    }

    // -------------------------------------------- between -----------------------------------------------------


    public static <T extends Temporal> long betweenYear(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.YEARS);
    }

    public static <T extends Temporal> long betweenMonth(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.MONTHS);
    }

    public static <T extends Temporal> long betweenDay(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.DAYS);
    }

    public static <T extends Temporal> long betweenWeek(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.WEEKS);
    }

    public static <T extends Temporal> long betweenHour(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.HOURS);
    }

    public static <T extends Temporal> long betweenMinute(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.MINUTES);
    }

    public static <T extends Temporal> long betweenSecond(@NonNull T startDateTime, @NonNull T endDateTime) {

        return startDateTime.until(endDateTime, ChronoUnit.SECONDS);
    }


    public static <T extends Temporal> long betweenMillis(@NonNull T startDateTime, @NonNull T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.MILLIS);
    }


    public static <T extends Temporal> TemporalPeriod toTemporalPeriod(@NonNull T startDateTime, @NonNull T endDateTime) {

        LocalDateTime startDateTimeTemp = toLocalDateTime(startDateTime);
        LocalDateTime endDateTimeTemp = toLocalDateTime(endDateTime);

        Period period = Period.between(startDateTimeTemp.toLocalDate(), endDateTimeTemp.toLocalDate());
        long hours = startDateTimeTemp.toLocalTime().until(endDateTimeTemp.toLocalTime(), ChronoUnit.HOURS);
        long minutes = startDateTimeTemp.toLocalTime()
                .until(endDateTimeTemp.toLocalTime(), ChronoUnit.MINUTES) % TemporalPeriod.MINUTES_PER_HOUR;
        long seconds = startDateTimeTemp.toLocalTime()
                .until(endDateTimeTemp.toLocalTime(), ChronoUnit.SECONDS) % TemporalPeriod.SECONDS_PER_MINUTE;
        long millis = startDateTimeTemp.toLocalTime()
                .until(endDateTimeTemp.toLocalTime(), ChronoUnit.MILLIS) % TemporalPeriod.MILLIS_PER_SECOND;

        return TemporalPeriod.create(period.getYears(), period.getMonths(), period.getDays(), hours, minutes, seconds, millis);

    }

    public static String format(@NonNull Date dateTime, @NonNull String format) {
        return format(toLocalDateTime(dateTime), format);
    }

    public static <T extends Temporal> String format(@NonNull T dateTime, @NonNull String format) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return format(dateTime, formatter);
    }

    public static <T extends Temporal> String format(@NonNull T dateTime, @NonNull String format, @NonNull Locale locale) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);
        return format(dateTime, formatter);
    }

    public static <T extends Temporal> String format(@NonNull T dateTime, @NonNull DateTimeFormatter formatter) {
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).format(formatter);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).format(formatter);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).format(formatter);
        } else if (dateTime instanceof LocalTime) {
            return ((LocalTime) dateTime).format(formatter);
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }

    private static LocalDateTime parse(@NonNull String dateTime, @NonNull String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static boolean isValidDateTime(@NonNull String dateTimeStr, @NonNull String format) {
        return isValidDateTime(dateTimeStr, format, false);
    }
    public static boolean isValidDateTime(@NonNull String dateTimeStr, @NonNull String format , boolean strict) {
        DateTimeFormatter formatter ;
        if (strict) {
            formatter = DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.STRICT);
        }else{
            formatter = DateTimeFormatter.ofPattern(format);
        }

        return canParse(dateTimeStr, formatter, LocalDate.class)
                || canParse(dateTimeStr, formatter, LocalTime.class)
                || canParse(dateTimeStr, formatter, LocalDateTime.class);
    }

    private static <T> boolean canParse(String dateTimeStr, DateTimeFormatter formatter, Class<T> targetClass) {
        try {
            if (targetClass == LocalDate.class) {
                LocalDate.parse(dateTimeStr, formatter);
            } else if (targetClass == LocalTime.class) {
                LocalTime.parse(dateTimeStr, formatter);
            } else if (targetClass == LocalDateTime.class) {
                LocalDateTime.parse(dateTimeStr, formatter);
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
