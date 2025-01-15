package cloud.huazai.tool.java.date;

import cloud.huazai.tool.java.constant.StringConstant;
import lombok.Getter;


/**
 * TemporalFormat
 *
 * @author devon
 * @since 2025/1/15
 */

@Getter
public class TemporalFormat {

   public static final String YEAR = "yyyy";

   public static final String MONTH = "MM";

   public static final String DAY = "dd";

   private static final String HOUR = "HH";

   private static final String MINUTE = "mm";

   private static final String SECOND = "ss";

   // yyyy-MM
   public static final String YEAR_DASHED_MONTH = YEAR + StringConstant.DASHED + MONTH;
   // MM-dd
   public static final String MONTH_DASHED_DAY = MONTH + StringConstant.DASHED + DAY;
   // yyyy-MM-dd
   public static final String YEAR_DASHED_MONTH_DASHED_DAY = YEAR + StringConstant.DASHED + MONTH + StringConstant.DASHED + DAY;
   // HH:mm
   public static final String HOUR_COLON_MINUTE = HOUR + StringConstant.COLON + MINUTE;
   // HH:mm:ss
   public static final String HOUR_COLON_MINUTE_COLON_SECOND = HOUR + StringConstant.COLON + MINUTE + StringConstant.COLON + SECOND;
   // yyyy-MM-dd HH:mm
   public static final String YEAR_DASHED_MONTH_DASHED_DAY_SPACE_HOUR_COLON_MINUTE = YEAR_DASHED_MONTH_DASHED_DAY + StringConstant.SPACE + HOUR_COLON_MINUTE;
   // yyyy-MM-dd HH:mm:ss
   public static final String YEAR_DASHED_MONTH_DASHED_DAY_SPACE_HOUR_COLON_MINUTE_COLON_SECOND = YEAR_DASHED_MONTH_DASHED_DAY + StringConstant.SPACE + HOUR_COLON_MINUTE_COLON_SECOND;




}
