package cloud.huazai.tool.java.date;

import java.time.*;
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

    public static Date nowDate(){
        return new Date();
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
        return toDate(toLocalDate(year, month, dayOfMonth));
    }

    // ---------------------------------------------- LocalDate -------------------------------------------------------

    public static LocalDate nowLocalDate() {
        return LocalDate.now();
    }

    public static LocalDate toLocalDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDate();
    }

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static LocalDate toLocalDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }



    // ---------------------------------------------- LocalTime -------------------------------------------------------
    public static LocalTime nowLocalTime() {
        return LocalTime.now();
    }

    public static LocalTime toLocalTime(Date date) {
        return toLocalDateTime(date).toLocalTime();
    }

    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime();
    }

    public static LocalTime toLocalTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalTime();
    }

    public static LocalTime toLocalTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);

    }


    // -------------------------------------------- LocalDateTime -----------------------------------------------------
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate,LocalTime localTime) {
        return localDate.atTime(localTime);
    }

    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return  toLocalDate(year, month, dayOfMonth).atTime(toLocalTime(hour, minute, second));
    }


    // -------------------------------------------- ZonedDateTime -----------------------------------------------------
    public static ZonedDateTime nowZonedDateTime() {
        return ZonedDateTime.now();
    }

    public static ZonedDateTime nowZoneDateTime(String zoneId) {
        return nowZoneDateTime(ZoneId.of(zoneId));
    }

    public static ZonedDateTime nowZoneDateTime(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    public static ZonedDateTime nowZonedDateTime(String timeZone) {
        return nowZonedDateTime(TimeZone.getTimeZone(timeZone));
    }

    public static ZonedDateTime nowZonedDateTime(TimeZone timeZone) {
        ZoneId zoneId = timeZone.toZoneId();
        return ZonedDateTime.now(zoneId);
    }


    public static ZonedDateTime nowZonedDateTime(TimeZoneID timeZoneID, int offsetHour) {
        return nowZoneDateTime(ZoneId.ofOffset(timeZoneID.name(), ZoneOffset.ofHours(offsetHour)));
    }


    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, ZoneId zone) {
        return ZonedDateTime.of(localDateTime, zone);
    }

    public static ZonedDateTime toZonedDateTime(LocalDate localDate, LocalTime localTime, ZoneId zone) {
        return ZonedDateTime.of(localDate, localTime, zone);
    }

    public static ZonedDateTime toZonedDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second,  ZoneId zone) {
        return ZonedDateTime.of(toLocalDateTime(year, month, dayOfMonth, hour, minute, second), zone);
    }























}
