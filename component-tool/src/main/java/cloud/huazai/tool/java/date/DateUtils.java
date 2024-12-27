package cloud.huazai.tool.java.date;

import java.time.*;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateTimeUtils
 *
 * @author devon
 * @since 2024/12/26
 */

public class DateUtils {

    // ------------------------------------------------- Date ----------------------------------------------------------

    public static Date now() {
        return new Date();
    }

    public static Date toDate(Temporal temporal) {

        if (temporal instanceof ZonedDateTime) {
            return toDate(((ZonedDateTime) temporal).toLocalDateTime());

        } else if (temporal instanceof LocalDateTime) {
            return toDate((LocalDateTime) temporal);

        } else if (temporal instanceof LocalDate) {
            return toDate(((LocalDate) temporal).atStartOfDay());
        }

        throw new IllegalArgumentException("temporal should be ZonedDateTime, LocalDateTime or LocalDate, is: " + temporal);
    }

    public static Date toDate(ZonedDateTime zonedDateTime) {
        return toDate(zonedDateTime.toLocalDateTime());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }


    public static Date toDate(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return toDate(toLocalDateTime(year, month, dayOfMonth, hour, minute, second));
    }

    public static Date toDate(int year, int month, int dayOfMonth) {
        return toDate(year, month, dayOfMonth,0,0,0);
    }

    // ---------------------------------------------- LocalDate -------------------------------------------------------


    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static LocalDate toLocalDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }


    // ---------------------------------------------- LocalTime -------------------------------------------------------

    public static LocalTime toLocalTime(Date date) {
        return toLocalDateTime(date).toLocalTime();
    }


    public static LocalTime toLocalTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);

    }


    // -------------------------------------------- LocalDateTime -----------------------------------------------------


    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate, LocalTime localTime) {
        return localDate.atTime(localTime);
    }

    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return toLocalDate(year, month, dayOfMonth).atTime(toLocalTime(hour, minute, second));
    }

    public static LocalDateTime atStartOfDay(LocalDateTime localDateTime){
        return localDateTime.toLocalDate().atTime(LocalTime.MIN);
    }

    public static LocalDateTime atEndOfDay(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    public static LocalDateTime atEndOfDayToSecond(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(toLocalTime(23, 59, 59));
    }

    public static LocalDateTime atStartOfWeek(LocalDateTime dateTime, DayOfWeek dayOfWeekStart) {
        return atStartOfDay(dateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
    }

    public static LocalDateTime atEndOfWeek(LocalDateTime dateTime,DayOfWeek dayOfWeekEnd) {
        return atEndOfDay(dateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
    }

    public static LocalDateTime atStartOfMonth(LocalDateTime dateTime) {
       return atStartOfDay(dateTime.with(TemporalAdjusters.firstDayOfMonth()));
    }

    public static LocalDateTime atEndOfMonth(LocalDateTime dateTime) {
        return atEndOfDay(dateTime.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static LocalDateTime atStartOfYear(LocalDateTime dateTime) {
        return atStartOfDay(dateTime.with(TemporalAdjusters.firstDayOfYear()));
    }

    public static LocalDateTime atEndOfYear(LocalDateTime dateTime) {
        return atEndOfDay(dateTime.with(TemporalAdjusters.lastDayOfYear()));
    }

    public static LocalDateTime offsetSecond(LocalDateTime dateTime, long secondsToOffset) {
        return dateTime.plusSeconds(secondsToOffset);
    }

    public static LocalDateTime offsetMinute(LocalDateTime dateTime, long minutesToOffset) {
        return dateTime.plusMinutes(minutesToOffset);
    }

    public static LocalDateTime offsetHour(LocalDateTime dateTime, long hoursToOffset) {
        return dateTime.plusHours(hoursToOffset);
    }

    public static LocalDateTime offsetDay(LocalDateTime dateTime, long daysToOffset) {
        return dateTime.plusDays(daysToOffset);
    }

    public static LocalDateTime offsetWeek(LocalDateTime dateTime, long weeksToOffset) {
        return dateTime.plusWeeks(weeksToOffset);
    }

    public static LocalDateTime offsetMonth(LocalDateTime dateTime, long monthsToOffset) {
        return dateTime.plusMonths(monthsToOffset);
    }

    public static LocalDateTime offsetYear(LocalDateTime dateTime, long yearsToOffset) {
        return dateTime.plusYears(yearsToOffset);
    }

    public static LocalDateTime demo() {
       return LocalDateTime.now();
    }




    // -------------------------------------------- ZonedDateTime -----------------------------------------------------


    public static ZonedDateTime now(String timeZone) {
        return now(TimeZone.getTimeZone(timeZone));
    }

    public static ZonedDateTime now(TimeZone timeZone) {
        ZoneId zoneId = timeZone.toZoneId();
        return ZonedDateTime.now(zoneId);
    }


    public static ZonedDateTime now(TimeZoneID timeZoneID, int offsetHour) {
        return ZonedDateTime.now(ZoneId.ofOffset(timeZoneID.name(), ZoneOffset.ofHours(offsetHour)));
    }


    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, ZoneId zone) {
        return ZonedDateTime.of(localDateTime, zone);
    }

    public static ZonedDateTime toZonedDateTime(LocalDate localDate, LocalTime localTime, ZoneId zone) {
        return ZonedDateTime.of(localDate, localTime, zone);
    }

    public static ZonedDateTime toZonedDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second, ZoneId zone) {
        return ZonedDateTime.of(toLocalDateTime(year, month, dayOfMonth, hour, minute, second), zone);
    }


}
