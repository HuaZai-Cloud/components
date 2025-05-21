package cloud.huazai.tool.java.date;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void isValidDateTime() {

        System.out.println("DateUtils.isValidDateTime(\"2025-02-30\", TemporalFormat.YEAR_DASHED_MONTH_DASHED_DAY) = " + DateUtils.isValidDateTime("2025-02-30", DateTimeFormat.YEAR_DASHED_MONTH_DASHED_DAY));
        boolean validDateTime = DateUtils.isValidDateTime("2025-02-30", DateTimeFormat.YEAR_DASHED_MONTH_DASHED_DAY, true);
        System.out.println("validDateTime = " + validDateTime);
    }
}