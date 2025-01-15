package cloud.huazai.tool.java.date;

import java.time.*;
import java.time.temporal.ChronoUnit;
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

    @Deprecated
    public static Date now() {
        return new Date();
    }


    @Deprecated
    public static <T extends Temporal> Date toDate(T temporal) {

        return switch (temporal) {
            case LocalDate localDate -> Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            case LocalTime localTime ->
                    Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
            case LocalDateTime localDateTime -> Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            case ZonedDateTime zonedDateTime ->
                    Date.from(zonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
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

    public static <T extends Temporal> LocalDate toLocalDate(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> localDate;
            case LocalDateTime localDateTime -> localDateTime.toLocalDate();
            case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

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

    public static LocalTime toLocalTime(int hour, int minute, int second,int millis) {
        return LocalTime.of(hour, minute, second,millis);
    }


    // -------------------------------------------- LocalDateTime -----------------------------------------------------


    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static <T extends Temporal> LocalDateTime toLocalDateTime(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atStartOfDay();
            case LocalTime localTime -> localTime.atDate(LocalDate.now());
            case LocalDateTime localDateTime -> localDateTime;
            case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDateTime();
            case null, default -> LocalDateTime.now();
        };
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate, LocalTime localTime) {
        return localDate.atTime(localTime);
    }


    public static LocalDateTime toLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return toLocalDate(year, month, dayOfMonth).atTime(toLocalTime(hour, minute, second));
    }

    public static LocalDateTime toLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second,int millis) {
        return toLocalDate(year, month, dayOfMonth).atTime(toLocalTime(hour, minute, second,millis));
    }


    public static <T extends Temporal> LocalDateTime atStartOfDay(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atStartOfDay();
            case LocalDateTime localDateTime -> localDateTime.toLocalDate().atStartOfDay();
            case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate().atStartOfDay();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static LocalDateTime atStartOfDay(Date date) {
        return atStartOfDay(toLocalDateTime(date));
    }


    public static <T extends Temporal> LocalDateTime atEndOfDay(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.MAX);
            case LocalDateTime localDateTime -> localDateTime.toLocalDate().atTime(LocalTime.MAX);
            case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate().atTime(LocalTime.MAX);
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

    public static LocalDateTime atEndOfDay(Date date) {
        return atEndOfDay(toLocalDateTime(date));
    }


    public static <T extends Temporal> LocalDateTime atEndOfDayToSecond(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(toLocalTime(23, 59, 59));
            case LocalDateTime localDateTime -> localDateTime.toLocalDate().atTime(toLocalTime(23, 59, 59));
            case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate().atTime(toLocalTime(23, 59, 59));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

    public static LocalDateTime atEndOfDayToSecond(Date date) {
        return atEndOfDayToSecond(toLocalDateTime(date));
    }

    public static <T extends Temporal> LocalDateTime atStartOfWeek(T temporal, DayOfWeek dayOfWeekStart) {
        return switch (temporal) {
            case LocalDate localDate -> atStartOfDay(localDate.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
            case LocalDateTime localDateTime ->
                    atStartOfDay(localDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
            case ZonedDateTime zonedDateTime ->
                    atStartOfDay(zonedDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime atEndOfWeek(T temporal, DayOfWeek dayOfWeekEnd) {
        return switch (temporal) {
            case LocalDate localDate -> atEndOfDay(localDate.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
            case LocalDateTime localDateTime ->
                    atEndOfDay(localDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
            case ZonedDateTime zonedDateTime ->
                    atEndOfDay(zonedDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

    public static <T extends Temporal> LocalDateTime atStartOfMonth(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> atStartOfDay(localDate.with(TemporalAdjusters.firstDayOfMonth()));
            case LocalDateTime localDateTime -> atStartOfDay(localDateTime.with(TemporalAdjusters.firstDayOfMonth()));
            case ZonedDateTime zonedDateTime -> atStartOfDay(zonedDateTime.with(TemporalAdjusters.firstDayOfMonth()));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime atEndOfMonth(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> atEndOfDay(localDate.with(TemporalAdjusters.lastDayOfMonth()));
            case LocalDateTime localDateTime -> atEndOfDay(localDateTime.with(TemporalAdjusters.lastDayOfMonth()));
            case ZonedDateTime zonedDateTime -> atEndOfDay(zonedDateTime.with(TemporalAdjusters.lastDayOfMonth()));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime atStartOfYear(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> atStartOfDay(localDate.with(TemporalAdjusters.firstDayOfYear()));
            case LocalDateTime localDateTime -> atStartOfDay(localDateTime.with(TemporalAdjusters.firstDayOfYear()));
            case ZonedDateTime zonedDateTime -> atStartOfDay(zonedDateTime.with(TemporalAdjusters.firstDayOfYear()));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime atEndOfYear(T temporal) {
        return switch (temporal) {
            case LocalDate localDate -> atEndOfDay(localDate.with(TemporalAdjusters.lastDayOfYear()));
            case LocalDateTime localDateTime -> atEndOfDay(localDateTime.with(TemporalAdjusters.lastDayOfYear()));
            case ZonedDateTime zonedDateTime -> atEndOfDay(zonedDateTime.with(TemporalAdjusters.lastDayOfYear()));
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

    public static <T extends Temporal> LocalDateTime offsetSecond(T temporal, long secondsToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusSeconds(secondsToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusSeconds(secondsToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusSeconds(secondsToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime offsetMinute(T temporal, long minutesToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusMinutes(minutesToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusMinutes(minutesToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusMinutes(minutesToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

    public static <T extends Temporal> LocalDateTime offsetHour(T temporal, long hoursToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusHours(hoursToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusHours(hoursToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusHours(hoursToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }

    public static <T extends Temporal> LocalDateTime offsetDay(T temporal, long daysToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusDays(daysToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusDays(daysToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusDays(daysToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime offsetWeek(T temporal, long weeksToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusWeeks(weeksToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusWeeks(weeksToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusWeeks(weeksToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime offsetMonth(T temporal, long monthsToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusMonths(monthsToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusMonths(monthsToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusMonths(monthsToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
    }


    public static <T extends Temporal> LocalDateTime offsetYear(T temporal, long yearsToOffset) {
        return switch (temporal) {
            case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusYears(yearsToOffset);
            case LocalDateTime localDateTime -> localDateTime.plusYears(yearsToOffset);
            case ZonedDateTime zonedDateTime -> zonedDateTime.plusYears(yearsToOffset).toLocalDateTime();
            case null, default ->
                    throw new IllegalArgumentException("Unsupported Temporal type: " + (temporal != null ? temporal.getClass().getName() : ""));
        };
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

    // -------------------------------------------- between -----------------------------------------------------


    public  static <T extends Temporal> long betweenYear(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.YEARS);
    }

    public static <T extends Temporal> long betweenMonth(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.MONTHS);
    }

    public static <T extends Temporal> long betweenDay(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.DAYS);
    }

    public static <T extends Temporal> long betweenWeek(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.WEEKS);
    }

    public static <T extends Temporal> long betweenHour(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.HOURS);
    }

    public static <T extends Temporal> long betweenMinute(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.MINUTES);
    }

    public static <T extends Temporal> long betweenSecond(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.SECONDS);
    }


    public static <T extends Temporal> long betweenMillis(T startDateTime, T endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.MILLIS);
    }


    public static <T extends Temporal> TemporalPeriod toTemporalPeriod(T startDateTime, T endDateTime) {

        LocalDateTime startDateTimeTemp = toLocalDateTime(startDateTime);
        LocalDateTime endDateTimeTemp = toLocalDateTime(endDateTime);

        Period period = Period.between(startDateTimeTemp.toLocalDate(), endDateTimeTemp.toLocalDate());
        long hours = startDateTimeTemp.toLocalTime().until(endDateTimeTemp.toLocalTime(), ChronoUnit.HOURS);
        long minutes = startDateTimeTemp.toLocalTime().until(endDateTimeTemp.toLocalTime(), ChronoUnit.MINUTES) % TemporalPeriod.MINUTES_PER_HOUR;
        long seconds = startDateTimeTemp.toLocalTime().until(endDateTimeTemp.toLocalTime(), ChronoUnit.SECONDS) % TemporalPeriod.SECONDS_PER_MINUTE;
        long millis = startDateTimeTemp.toLocalTime().until(endDateTimeTemp.toLocalTime(), ChronoUnit.MILLIS) % TemporalPeriod.MILLIS_PER_SECOND;

        return TemporalPeriod.create(period.getYears(),period.getMonths(),period.getDays(),hours,minutes,seconds,millis);

    }


}
