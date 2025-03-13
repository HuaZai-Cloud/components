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

        // return switch (dateTime) {
        //     case LocalDate localDate ->
        //             Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        //
        //     case LocalTime localTime ->
        //             Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        //
        //     case LocalDateTime localDateTime ->
        //             Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        //
        //     case ZonedDateTime zonedDateTime ->
        //             Date.from(zonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
        //
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };

        if (dateTime instanceof LocalDate) {
            LocalDate localDate = (LocalDate) dateTime;
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else if (dateTime instanceof LocalTime) {
            LocalTime localTime = (LocalTime) dateTime;
            return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        } else if (dateTime instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) dateTime;
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } else if (dateTime instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) dateTime;
            return Date.from(zonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName());
        }
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
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate;
        //     case LocalDateTime localDateTime -> localDateTime.toLocalDate();
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };

        if (dateTime instanceof LocalDate) {
            return (LocalDate) dateTime;
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).toLocalDate();
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).toLocalDate();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName());
        }
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
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atStartOfDay();
        //     case LocalTime localTime -> localTime.atDate(LocalDate.now());
        //     case LocalDateTime localDateTime -> localDateTime;
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDateTime();
        //     default -> LocalDateTime.now();
        //
        // };

        if (dateTime instanceof LocalDate) {
            LocalDate localDate = (LocalDate) dateTime;
            return localDate.atStartOfDay(); // 将 LocalDate 转换为当天的起始时间
        } else if (dateTime instanceof LocalTime) {
            LocalTime localTime = (LocalTime) dateTime;
            return localTime.atDate(LocalDate.now()); // 将 LocalTime 与当前日期结合
        } else if (dateTime instanceof LocalDateTime) {
            return (LocalDateTime) dateTime; // 直接返回 LocalDateTime
        } else if (dateTime instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) dateTime;
            return zonedDateTime.toLocalDateTime(); // 将 ZonedDateTime 转换为 LocalDateTime
        } else {
            return LocalDateTime.now(); // 默认返回当前时间
        }
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
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atStartOfDay();
        //     case LocalDateTime localDateTime -> localDateTime.toLocalDate().atStartOfDay();
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate().atStartOfDay();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName() );
        // };
        if (dateTime instanceof LocalDate) {
            LocalDate localDate = (LocalDate) dateTime;
            return localDate.atStartOfDay(); // 将 LocalDate 转换为当天的起始时间
        } else if (dateTime instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) dateTime;
            return localDateTime.toLocalDate().atStartOfDay(); // 将 LocalDateTime 转换为当天的起始时间
        } else if (dateTime instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) dateTime;
            return zonedDateTime.toLocalDate().atStartOfDay(); // 将 ZonedDateTime 转换为当天的起始时间
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName());
        }
    }


    public static LocalDateTime atStartOfDay(@NonNull Date date) {
        return atStartOfDay(toLocalDateTime(date));
    }


    public static <T extends Temporal> LocalDateTime atEndOfDay(@NonNull T dateTime) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.MAX);
        //     case LocalDateTime localDateTime -> localDateTime.toLocalDate().atTime(LocalTime.MAX);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate().atTime(LocalTime.MAX);
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.MAX);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).toLocalDate().atTime(LocalTime.MAX);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).toLocalDate().atTime(LocalTime.MAX);
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : "null"));
        }
    }

    public static LocalDateTime atEndOfDay(Date date) {
        return atEndOfDay(toLocalDateTime(date));
    }


    public static <T extends Temporal> LocalDateTime atEndOfDayToSecond(@NonNull T dateTime) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(toLocalTime(23, 59, 59));
        //     case LocalDateTime localDateTime -> localDateTime.toLocalDate().atTime(toLocalTime(23, 59, 59));
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.toLocalDate().atTime(toLocalTime(23, 59, 59));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };

        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(toLocalTime(23, 59, 59));
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).toLocalDate().atTime(toLocalTime(23, 59, 59));
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).toLocalDate().atTime(toLocalTime(23, 59, 59));
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : "null"));
        }
    }

    public static LocalDateTime atEndOfDayToSecond(Date date) {
        return atEndOfDayToSecond(toLocalDateTime(date));
    }

    public static <T extends Temporal> LocalDateTime atStartOfWeek(@NonNull T dateTime, @NonNull DayOfWeek dayOfWeekStart) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> atStartOfDay(localDate.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
        //     case LocalDateTime localDateTime ->
        //             atStartOfDay(localDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
        //     case ZonedDateTime zonedDateTime ->
        //             atStartOfDay(zonedDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            return atStartOfDay(((LocalDate) dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekStart)));
        } else if (dateTime instanceof LocalDateTime) {
            return atStartOfDay(((LocalDateTime) dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekStart)).toLocalDate());
        } else if (dateTime instanceof ZonedDateTime) {
            return atStartOfDay(((ZonedDateTime) dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekStart)).toLocalDate());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : "null"));
        }
    }


    public static <T extends Temporal> LocalDateTime atEndOfWeek(@NonNull T dateTime, DayOfWeek dayOfWeekEnd) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> atEndOfDay(localDate.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
        //     case LocalDateTime localDateTime ->
        //             atEndOfDay(localDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
        //     case ZonedDateTime zonedDateTime ->
        //             atEndOfDay(zonedDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeekEnd)));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            LocalDate adjustedDate = ((LocalDate) dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekEnd));
            return atEndOfDay(adjustedDate);
        } else if (dateTime instanceof LocalDateTime) {
            LocalDateTime adjustedDateTime = ((LocalDateTime) dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekEnd));
            return atEndOfDay(adjustedDateTime.toLocalDate());
        } else if (dateTime instanceof ZonedDateTime) {
            ZonedDateTime adjustedZonedDateTime = ((ZonedDateTime) dateTime).with(TemporalAdjusters.previousOrSame(dayOfWeekEnd));
            return atEndOfDay(adjustedZonedDateTime.toLocalDate());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : "null"));
        }
    }

    public static <T extends Temporal> LocalDateTime atStartOfMonth(@NonNull T dateTime) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> atStartOfDay(localDate.with(TemporalAdjusters.firstDayOfMonth()));
        //     case LocalDateTime localDateTime -> atStartOfDay(localDateTime.with(TemporalAdjusters.firstDayOfMonth()));
        //     case ZonedDateTime zonedDateTime -> atStartOfDay(zonedDateTime.with(TemporalAdjusters.firstDayOfMonth()));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            return atStartOfDay(((LocalDate) dateTime).with(TemporalAdjusters.firstDayOfMonth()));
        } else if (dateTime instanceof LocalDateTime) {
            return atStartOfDay(((LocalDateTime) dateTime).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate());
        } else if (dateTime instanceof ZonedDateTime) {
            return atStartOfDay(((ZonedDateTime) dateTime).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : "null"));
        }
    }


    public static <T extends Temporal> LocalDateTime atEndOfMonth(@NonNull T dateTime) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> atEndOfDay(localDate.with(TemporalAdjusters.lastDayOfMonth()));
        //     case LocalDateTime localDateTime -> atEndOfDay(localDateTime.with(TemporalAdjusters.lastDayOfMonth()));
        //     case ZonedDateTime zonedDateTime -> atEndOfDay(zonedDateTime.with(TemporalAdjusters.lastDayOfMonth()));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName() );
        // };
        if (dateTime instanceof LocalDate) {
            return atEndOfDay(((LocalDate) dateTime).with(TemporalAdjusters.lastDayOfMonth()));
        } else if (dateTime instanceof LocalDateTime) {
            return atEndOfDay(((LocalDateTime) dateTime).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate());
        } else if (dateTime instanceof ZonedDateTime) {
            return atEndOfDay(((ZonedDateTime) dateTime).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : "null"));
        }
    }


    public static <T extends Temporal> LocalDateTime atStartOfYear(@NonNull T dateTime) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> atStartOfDay(localDate.with(TemporalAdjusters.firstDayOfYear()));
        //     case LocalDateTime localDateTime -> atStartOfDay(localDateTime.with(TemporalAdjusters.firstDayOfYear()));
        //     case ZonedDateTime zonedDateTime -> atStartOfDay(zonedDateTime.with(TemporalAdjusters.firstDayOfYear()));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName() );
        // };
        if (dateTime instanceof LocalDate) {
            return atStartOfDay(((LocalDate) dateTime).with(TemporalAdjusters.firstDayOfYear()));
        } else if (dateTime instanceof LocalDateTime) {
            return atStartOfDay(((LocalDateTime) dateTime).with(TemporalAdjusters.firstDayOfYear()).toLocalDate());
        } else if (dateTime instanceof ZonedDateTime) {
            return atStartOfDay(((ZonedDateTime) dateTime).with(TemporalAdjusters.firstDayOfYear()).toLocalDate());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }


    public static <T extends Temporal> LocalDateTime atEndOfYear(@NonNull T dateTime) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> atEndOfDay(localDate.with(TemporalAdjusters.lastDayOfYear()));
        //     case LocalDateTime localDateTime -> atEndOfDay(localDateTime.with(TemporalAdjusters.lastDayOfYear()));
        //     case ZonedDateTime zonedDateTime -> atEndOfDay(zonedDateTime.with(TemporalAdjusters.lastDayOfYear()));
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            return atEndOfDay(((LocalDate) dateTime).with(TemporalAdjusters.lastDayOfYear()));
        } else if (dateTime instanceof LocalDateTime) {
            return atEndOfDay(((LocalDateTime) dateTime).with(TemporalAdjusters.lastDayOfYear()).toLocalDate());
        } else if (dateTime instanceof ZonedDateTime) {
            return atEndOfDay(((ZonedDateTime) dateTime).with(TemporalAdjusters.lastDayOfYear()).toLocalDate());
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + dateTime.getClass().getName());
        }
    }

    public static <T extends Temporal> LocalDateTime offsetSecond(@NonNull T dateTime, long secondsToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusSeconds(secondsToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusSeconds(secondsToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusSeconds(secondsToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusSeconds(secondsToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusSeconds(secondsToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusSeconds(secondsToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }


    public static <T extends Temporal> LocalDateTime offsetMinute(@NonNull T dateTime, long minutesToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusMinutes(minutesToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusMinutes(minutesToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusMinutes(minutesToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusMinutes(minutesToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusMinutes(minutesToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusMinutes(minutesToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }

    public static <T extends Temporal> LocalDateTime offsetHour(@NonNull T dateTime, long hoursToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusHours(hoursToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusHours(hoursToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusHours(hoursToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : ""));
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusHours(hoursToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusHours(hoursToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusHours(hoursToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }

    public static <T extends Temporal> LocalDateTime offsetDay(@NonNull T dateTime, long daysToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusDays(daysToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusDays(daysToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusDays(daysToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime != null ? dateTime.getClass().getName() : ""));
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusDays(daysToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusDays(daysToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusDays(daysToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }


    public static <T extends Temporal> LocalDateTime offsetWeek(@NonNull T dateTime, long weeksToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusWeeks(weeksToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusWeeks(weeksToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusWeeks(weeksToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName() );
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusWeeks(weeksToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusWeeks(weeksToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusWeeks(weeksToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }


    public static <T extends Temporal> LocalDateTime offsetMonth(@NonNull T dateTime, long monthsToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusMonths(monthsToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusMonths(monthsToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusMonths(monthsToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName() );
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusMonths(monthsToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusMonths(monthsToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusMonths(monthsToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
    }


    public static <T extends Temporal> LocalDateTime offsetYear(@NonNull T dateTime, long yearsToOffset) {
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.atTime(LocalTime.now()).plusYears(yearsToOffset);
        //     case LocalDateTime localDateTime -> localDateTime.plusYears(yearsToOffset);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.plusYears(yearsToOffset).toLocalDateTime();
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName() );
        // };
        if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atTime(LocalTime.now()).plusYears(yearsToOffset);
        } else if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).plusYears(yearsToOffset);
        } else if (dateTime instanceof ZonedDateTime) {
            return ((ZonedDateTime) dateTime).plusYears(yearsToOffset).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported Temporal type: " + (dateTime.getClass().getName()));
        }
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
        // return switch (dateTime) {
        //     case LocalDate localDate -> localDate.format(formatter);
        //     case LocalDateTime localDateTime -> localDateTime.format(formatter);
        //     case ZonedDateTime zonedDateTime -> zonedDateTime.format(formatter);
        //     case LocalTime localTime -> localTime.format(formatter);
        //     case null, default ->
        //             throw new IllegalArgumentException("Unsupported Temporal type: " +  dateTime.getClass().getName());
        // };
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
